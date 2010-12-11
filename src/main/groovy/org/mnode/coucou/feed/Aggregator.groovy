/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
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

import groovyx.gpars.GParsExecutorsPool;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.jcr.Repository;

import org.apache.jackrabbit.util.Text;
import org.mnode.coucou.AbstractNodeManager;
import org.mnode.juicer.query.QueryBuilder;

import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author fortuna
 *
 */
class Aggregator extends AbstractNodeManager {

//	def rootNode

	def updateThread
		
	Aggregator(Repository repository, String nodeName) {
//		if (!session.rootNode.hasNode(nodeName)) {
//			rootNode = session.rootNode.addNode(nodeName)
//		}
//		else {
//			rootNode = session.rootNode.getNode(nodeName)
//		}
		super(repository, 'feeds', nodeName)
		
		//if (feedsNode.hasNode('All Items')) {
		//	feedsNode.getNode('All Items').remove()
		//}
		
		if (!rootNode.hasNode('All Items')) {
			def allItems = new QueryBuilder(session.workspace.queryManager).with {
				query(
					source: selector(nodeType: 'nt:unstructured', name: 'all_nodes'),
					constraint: and(
						constraint1: descendantNode(selectorName: 'all_nodes', path: "/${nodeName}"),
						constraint2: not(childNode(selectorName: 'all_nodes', path: "/${nodeName}")))
				)
			}
			def allItemsNode = rootNode.addNode('All Items')
			allItems.storeAsNode("${allItemsNode.path}/query")
//			rootNode.save()
		}
		
		save rootNode
		
		updateThread = Executors.newSingleThreadScheduledExecutor()
	}
	
	void start() {
	   updateThread.scheduleAtFixedRate({
		   for (node in rootNode.nodes) {
			   if (node.hasProperty('url')) {
					println "Updating feed: ${node.getProperty('title').value.string}"
				
					GParsExecutorsPool.withPool(10) {
						try {
							def future = updateFeed.callAsync(node.getProperty('url').value.string)
						}
						catch (Exception e) {
	//						log.log unexpected_error, e
							e.printStackTrace()
						}
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
	
	def addFeed = { url ->
		 def feedNode = null
		 def feedUrl
		 try {
			 feedUrl = new URL(url)
		 }
		 catch (MalformedURLException e) {
			 feedUrl = new URL("http://${url}")
		 }
		 
		 try {
			 feedNode = updateFeed(url)
		 }
		 catch (Exception e) {
			  def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(feedUrl.content)
			  def feeds = html.head.link.findAll { it.@type == 'application/rss+xml' || it.@type == 'application/atom+xml' }
			  println "Found ${feeds.size()} feeds: ${feeds.collect { it.@href.text() } }"
			  if (!feeds.isEmpty()) {
				  feedNode = updateFeed(new URL(feedUrl, feeds[0].@href.text()).toString())
			  }
			  else {
//				  doLater {
//					  JOptionPane.showMessageDialog(frame, "No feeds found for site: ${url}")
//				  }
				  throw new IllegalArgumentException("No feeds found for site: ${url}")
			  }
		 }
	}
	
	def updateFeed = { url ->
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = Aggregator.classLoader
		
		def feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
		def feedNode = getNode(rootNode, Text.escapeIllegalJcrChars(feed.title), true)
		updateProperty(feedNode, 'url', url)
		updateProperty(feedNode, 'title', feed?.title)

		if (feed?.link) {
			updateProperty(feedNode, 'source', feed.link)
		}
		else if (!feed.links?.isEmpty()) {
			updateProperty(feedNode, 'source', feed.links[0])
		}

		def now = Calendar.instance
		
		for (entry in feed.entries) {
			def entryNode
			if (entry.uri) {
				entryNode = getNode(rootNode, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.uri)}")
			}
			else if (entry.link) {
				entryNode = getNode(rootNode, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.link)}")
			}
			else {
				entryNode = getNode(rootNode, "${feedNode.name}/${Text.escapeIllegalJcrChars(entry.title)}")
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
				if (!feedNode.hasProperty('date') || feedNode.getProperty('date')?.date.time.before(now.time)) {
					updateProperty(feedNode, 'date', now)
				}
				updateProperty(entryNode, 'seen', false)
				updateProperty(entryNode, 'source', feedNode)
			}
			
			// XXX: future published dates are ignored..
			if (entry.publishedDate && (!entryNode.hasProperty('date') || entryNode.getProperty('date').date.time.after(entry.publishedDate))) {
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
        node.setProperty('seen', true)
        save node
	}
	
	def delete = { node ->
		def parent = node.parent
		node.remove()
		save parent
	}
}
