/**
 * 
 */
package org.mnode.coucou.feed

import groovyx.gpars.Asynchronizer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.jackrabbit.util.Text;

import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author fortuna
 *
 */
class Aggregator {

	def rootNode

	def updateThread
		
	Aggregator() {
	   updateThread = Executors.newSingleThreadScheduledExecutor()
	}
	
	void start() {
	   updateThread.scheduleAtFixedRate({
		   for (node in rootNode.nodes) {
			   if (node.hasProperty('url')) {
					try {
						println "Updating feed: ${node.getProperty('title').value.string}"
					
						Asynchronizer.doParallel(5) {
							def future = updateFeed.callAsync(node.getProperty('url').value.string)
						}
					}
					catch (Exception e) {
//						log.log unexpected_error, e
						e.printStackTrace()
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
	
	def getNode = { path, referenceable = false ->
		if (!rootNode.hasNode(path)) {
//			log.log init_node, path
			println "Adding path: ${path}"
			def node = rootNode.addNode(path)
			if (referenceable) {
				node.addMixin('mix:referenceable')
			}
		}
		return rootNode.getNode(path)
	}
	
	def updateProperty = { aNode, propertyName, value ->
		if (value && (!aNode.hasProperty(propertyName) || aNode.getProperty(propertyName).string != value)) {
			aNode.setProperty(propertyName, value)
		}
	}

	// save a node hierarchy..
	def saveNode = { node ->
		def parent = node.parent
		while (parent.isNew()) {
			parent = parent.parent
		}
		parent.save()
	}

	def updateFeed = { url ->
		// rome uses Thread.contextClassLoader..
		Thread.currentThread().contextClassLoader = Aggregator.classLoader
		
		def feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
		def feedNode = getNode(Text.escapeIllegalJcrChars(feed.title), true)
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
				entryNode = getNode("${feedNode.name}/${Text.escapeIllegalJcrChars(entry.uri)}")
			}
			else if (entry.link) {
				entryNode = getNode("${feedNode.name}/${Text.escapeIllegalJcrChars(entry.link)}")
			}
			else {
				entryNode = getNode("${feedNode.name}/${Text.escapeIllegalJcrChars(entry.title)}")
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
					feedNode.setProperty('date', now)
				}
				entryNode.setProperty('seen', false)
				entryNode.setProperty('source', feedNode)
			}
			
			// XXX: future published dates are ignored..
			if (entry.publishedDate && (!entryNode.hasProperty('date') || entryNode.getProperty('date')?.date.time.after(entry.publishedDate.time))) {
				def publishedDate = Calendar.instance
				publishedDate.time = entry.publishedDate
				entryNode.setProperty('date', publishedDate)
			}
			else if (entryNode.isNew()) {
				entryNode.setProperty('date', now)
			}
		}
		saveNode feedNode
		return feedNode
	}

}
