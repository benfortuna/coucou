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
abstract class AbstractManager {
	
	static ReadWriteLock lock = new ReentrantReadWriteLock()
	
	Session session
	
	javax.jcr.Node rootNode
	
	AbstractManager(Repository repository, String user, String nodeName) {
		session = repository.login(new SimpleCredentials(user, ''.toCharArray()))
		rootNode = getNode(session.rootNode, nodeName)
	}
	
	def getNode = { rootNode, path, referenceable = false ->
		if (!rootNode.hasNode(path)) {
//			log.log init_node, path
			println "Adding path: ${path}"
			// lock to avoid concurrent modification..
			try {
				lock.writeLock().lock()
				def node = rootNode.addNode(path)
				if (referenceable) {
					node.addMixin('mix:referenceable')
				}
			}
			finally {
				lock.writeLock().unlock()
			}
		}
		return rootNode.getNode(path)
	}

	def updateProperty = { aNode, propertyName, value ->
		if (value != null && (!aNode.hasProperty(propertyName) || aNode.getProperty(propertyName).string != value)) {
			// lock to avoid concurrent modification..
			try {
				lock.writeLock().lock()
				aNode.setProperty(propertyName, value)
			}
			finally {
				lock.writeLock().unlock()
			}
		}
	}

	// save a node hierarchy..
	def save = { node ->
		def parent = node
		while (parent.isNew()) {
			parent = parent.parent
		}
		// lock to avoid concurrent modification..
//		parent.save()
		try {
//			parent.session.workspace.lockManager.lock(parent.path, true, true, 5, getClass().name)
			lock.writeLock().lock()
			parent.save()
		}
		finally {
//			parent.session.workspace.lockManager.unlock(parent.path)
			lock.writeLock().unlock()
		}
	}

}
