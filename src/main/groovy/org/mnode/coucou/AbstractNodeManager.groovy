/**
 * 
 */
package org.mnode.coucou

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.poi.poifs.property.Parent;

/**
 * @author fortuna
 *
 */
abstract class AbstractNodeManager {
	
	static ReadWriteLock lock = new ReentrantReadWriteLock()
	
	Session session
	
	javax.jcr.Node rootNode
	
	AbstractNodeManager(Repository repository, String user, String nodeName) {
		session = repository.login(new SimpleCredentials(user, ''.toCharArray()))
		rootNode = getNode(session.rootNode, nodeName)
		save rootNode
	}
	
	def lock() {
		lock.writeLock().lock()
//		println '*** Save lock obtained'
	}

	def unlock() {
		lock.writeLock().unlock()
//		println '*** Save lock released'
	}

	def getNode = { rootNode, path, referenceable = false ->
		if (!rootNode.hasNode(path)) {
//			log.log init_node, path
			println "Adding path: ${path}"
			// lock to avoid concurrent modification..
			try {
				lock()
				def node = rootNode.addNode(path)
				if (referenceable) {
					node.addMixin('mix:referenceable')
				}
			}
			finally {
				unlock()
			}
		}
		return rootNode.getNode(path)
	}

	def updateProperty = { aNode, propertyName, value ->
		if (value != null) { // && (!aNode.hasProperty(propertyName)) || aNode.getProperty(propertyName).string != value)) {
			// lock to avoid concurrent modification..
			try {
				lock()
				aNode.setProperty(propertyName, value)
			}
			finally {
				unlock()
			}
		}
	}

	// save a node hierarchy..
	def save = { node ->
		println "Saving node: ${node.path}"
		def parent = node
		while (parent.isNew()) {
			parent = parent.parent
		}
		// lock to avoid concurrent modification..
//		parent.save()
		try {
//			parent.session.workspace.lockManager.lock(parent.path, true, true, 5, getClass().name)
			lock()
			parent.save()
		}
		finally {
//			parent.session.workspace.lockManager.unlock(parent.path)
			unlock()
		}
	}
}
