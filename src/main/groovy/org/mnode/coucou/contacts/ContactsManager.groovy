/**
 * 
 */
package org.mnode.coucou.contacts

import javax.jcr.Session;

import org.mnode.coucou.AbstractManager;

/**
 * @author fortuna
 *
 */
class ContactsManager extends AbstractManager {

	javax.jcr.Node rootNode
	
	ContactsManager(Session session, String nodeName) {
		if (!session.rootNode.hasNode(nodeName)) {
			rootNode = session.rootNode.addNode(nodeName)
			session.rootNode.save()
		}
		else {
			rootNode = session.rootNode.getNode(nodeName)
		}		
	}
	
	javax.jcr.Node add(def address) {
		def node
		if (rootNode.hasNode(address.address)) {
			node = rootNode.getNode(address.address)
		}
		else {
			node = rootNode.addNode(address.address)
		}
		node.setProperty('email', address.address)
		node.setProperty('personal', address.personal)
		save node
		return node
	}
}
