/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
 *
 * Coucou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Coucou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Coucou.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.coucou.feed

import groovyx.gpars.GParsPool

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import javax.jcr.Repository
import javax.jcr.query.Query

import org.apache.jackrabbit.util.Text
import org.mnode.base.log.FormattedLogEntry
import org.mnode.base.log.LogAdapter
import org.mnode.base.log.LogEntry
import org.mnode.base.log.LogEntry.Level
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.AbstractNodeManager
import org.mnode.ousia.layer.ProgressLayerUI
import org.mnode.ousia.layer.ProgressLayerUI.ProgressMonitor;
import org.slf4j.LoggerFactory

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndLink
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.io.XmlReader

/**
 * @author fortuna
 *
 */
class Aggregator extends AbstractNodeManager {
	
	private static LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Aggregator))
	private static LogEntry updating_feed = new FormattedLogEntry(Level.Info, 'Updating feed: %s')
	private static LogEntry found_feeds = new FormattedLogEntry(Level.Info, 'Found %s feeds: %s')

	def updateThread
	
	Query allFeedsQuery
	
	ProgressLayerUI progressMonitor;
		
	Aggregator(Repository repository, String nodeName) {
		super(repository, 'feeds', nodeName)
		
		updateThread = Executors.newSingleThreadScheduledExecutor()
		
		allFeedsQuery = baseNode.session.workspace.queryManager.createQuery(
			"SELECT * FROM [nt:unstructured] AS all_nodes WHERE ISDESCENDANTNODE(all_nodes, [${baseNode.path}]) AND all_nodes.url IS NOT NULL",
			Query.JCR_JQOM)
	}
	
	void start() {
	   updateThread.scheduleAtFixedRate({
		   GParsPool.withPool {
			   def asyncUpdateFeed = updateFeed.async()
//			   def asyncUpdateFeed = { url, parent -> parent }.async()
			   def allFeeds = allFeedsQuery.execute().nodes
//			   progressMonitor?.maximum = allFeeds.size
//			   progressMonitor?.progress = 0
			   ProgressMonitor monitor = ['Updating Feeds', 0, allFeeds.size as int] 
			   progressMonitor?.addMonitor(monitor)
			   def futures = []
			   for (node in allFeeds) {
					log.log updating_feed, node.title.string
					futures << asyncUpdateFeed(node.url.string, node.parent)
			   }
			   
//			   ParallelEnhancer.enhanceInstance(futures)
			   futures.each {
					try {
						def node = it.get()
						log.info "Completed: $node.title.string"
					}
					catch (Exception e) {
						log.log unexpected_error, e
					}
					finally {
						// technically as this is async we haven't completed the update here, but the effect is good..
//						if (progressMonitor) {
//							progressMonitor.addProgress(1)
//						}
						monitor.addDelta(1)
					}
				}
		   }
	   }, 0, 30, TimeUnit.MINUTES)
	}
/*	
	void update(def entries) {
		def now = new Date()
		urls.each {
			def feed = new SyndFeedInput().build(new XmlReader(new URL(it)))
			feed.entries.each {
				def entry = [:]
				entry['title'] = it.title
				entry['source'] = feed.title
				if (it.publishedDate) {
					entry['date'] = it.publishedDate
				}
				else {
					entry['date'] = now
				}
				
				entries << entry
			}
		}
	}
*/
	
	def loadOpml = { file ->
		def opml = new XmlSlurper().parse(file)
		GParsPool.withPool(50) {
			def feedUrls = [:]
			opml.body.outline.each {
				if (it.@xmlUrl.text()) {
//					updateFeed.callAsync(it.@xmlUrl.text())
					feedUrls[it.@xmlUrl.text()] = null
				}
				else {
//					def folder = getNode(baseNode, Text.escapeIllegalJcrChars(it.@title.text()))
//					save baseNode
					def folder = it.@title.text()
					it.outline.each {
//						updateFeed.callAsync(it.@xmlUrl.text(), folder)
						feedUrls[it.@xmlUrl.text()] = folder
					}
				}
			}
			ProgressMonitor monitor = ['Importing Feeds', 0, feedUrls.size()]
			progressMonitor?.addMonitor(monitor)

			def futures = []
			def asyncUpdateFeed = updateFeed.async()
			feedUrls.keySet().each {
				if (feedUrls[it]) {
					def folder = getNode(baseNode, Text.escapeIllegalJcrChars(feedUrls[it]))
					futures << asyncUpdateFeed(it, folder)
				}
				else {
					futures << asyncUpdateFeed(it)
				}
			}
//			ParallelEnhancer.enhanceInstance(futures)
		   futures.each {
				try {
					def node = it.get()
					log.info "Completed: $node.title.string"
				}
				catch (Exception e) {
					log.log unexpected_error, e
				}
				finally {
					monitor.addDelta(1)
				}
			}
		}
/*		
		def feeds = opml.body.outline.outline.collect { it.@xmlUrl.text() }
		if (feeds.isEmpty()) {
			feeds = opml.body.outline.collect { it.@xmlUrl.text() }
		}
		println "Feeds: ${feeds}"
		def errorMap = [:]
		GParsExecutorsPool.withPool(10) {
			for (feed in feeds) {
				try {
					def future = aggregator.updateFeed.callAsync(feed)
//	                            future.get()
//	                            doLater {
//	                                feedList.model.fireTableDataChanged()
//	                            }
				}
				catch (Exception ex) {
					log.log unexpected_error, ex
					errorMap.put(feed, ex)
				}
			}
		}
*/
	}
	
	def addFeed = { url, parent = baseNode ->
		 def feedNode = null
		 def feedUrl
		 try {
			 feedUrl = new URL(url)
		 }
		 catch (MalformedURLException e) {
			 feedUrl = new URL("http://${url}")
		 }
		 
		 try {
			 feedNode = updateFeed(url, parent)
		 }
		 catch (Exception e) {
			  def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(feedUrl.content)
			  def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
			  log.log found_feeds, feeds.size(), feeds.collect { it.@href.text() }
			  if (!feeds.isEmpty()) {
				  feedNode = updateFeed(new URL(feedUrl, feeds[0].@href.text()).toString(), parent)
			  }
			  else {
//				  doLater {
//					  JOptionPane.showMessageDialog(frame, "No feeds found for site: ${url}")
//				  }
				  throw new IllegalArgumentException("No feeds found for site: ${url}")
			  }
		 }
	}
	
	def updateFeed = { url, parent = baseNode ->
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = Aggregator.classLoader
		
//		def feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
		FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.instance
		FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
		SyndFeed feed = feedFetcher.retrieveFeed(new URL(url))
		
		javax.jcr.Node feedNode = getNode(parent, Text.escapeIllegalJcrChars(feed.title), true)
		updateProperty(feedNode, 'url', url)
		updateProperty(feedNode, 'title', feed?.title)

		if (feed?.link) {
			updateProperty(feedNode, 'source', feed.link)
		}
		else if (!feed.links?.isEmpty()) {
			if (feed.links[0] instanceof SyndLink) {
				updateProperty(feedNode, 'source', feed.links[0].href)
			}
			else {
				updateProperty(feedNode, 'source', feed.links[0])
			}
		}

		def now = Calendar.instance
		
		for (entry in feed.entries) {
			def entryNode
			if (entry.uri) {
				entryNode = getNode(parent, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.uri)}")
			}
			else if (entry.link) {
				entryNode = getNode(parent, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.link)}")
			}
			else {
				entryNode = getNode(parent, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.title)}")
			}
			updateProperty(entryNode, 'title', entry?.title)
			if (entry?.description) {
				updateProperty(entryNode, 'description', entry.description.value)
			}
			else if (entry.contents && !entry.contents.isEmpty()) {
				updateProperty(entryNode, 'description', entry.contents[0].value)
			}
			
			// entry link..
			if (entry?.uri) {
				try {
					new URL(entry.uri)
					updateProperty(entryNode, 'link', entry.uri)
				}
				catch (Exception e) {
					// not a valid url..
				}
			}
			if (entry?.link) {
				try {
					new URL(entry.link)
					updateProperty(entryNode, 'link', entry.link)
				}
				catch (Exception e) {
					// not a valid url..
				}
			}
			
			// XXX: properties below not being updated..
			if (entryNode.isNew()) {
				if (!feedNode['date'] || feedNode['date'].date.time.before(now.time)) {
					updateProperty(feedNode, 'date', now)
				}
				updateProperty(entryNode, 'seen', false)
				updateProperty(entryNode, 'source', feedNode)
			}
			
			// XXX: future published dates are ignored..
			if (entry.publishedDate?.before(now.time)
				 && (!entryNode['date'] || entryNode['date'].date.time.after(entry.publishedDate))) {
				 
				def publishedDate = Calendar.instance
				publishedDate.time = entry.publishedDate
				updateProperty(entryNode, 'date', publishedDate)
			}
			else if (entryNode.isNew()) {
				updateProperty(entryNode, 'date', now)
			}
		}
		save feedNode

		return feedNode
	}

	def markNodeRead = { node ->
		node.session.save {
			node.setProperty('seen', true)
		}
	}
	
	def delete = { node ->
		node.session.save {
			def parent = node.parent
			node.remove()
		}
	}
}
