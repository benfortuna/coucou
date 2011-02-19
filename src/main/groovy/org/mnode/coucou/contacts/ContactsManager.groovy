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
package org.mnode.coucou.contacts

import javax.jcr.Repository

import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.AbstractNodeManager
import org.mnode.coucou.mail.Mailbox
import org.slf4j.LoggerFactory

/**
 * @author fortuna
 *
 */
class ContactsManager extends AbstractNodeManager {
	
	private static LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(ContactsManager))
	
//	javax.jcr.Node rootNode
	
	ContactsManager(Repository repository, String nodeName) {
//		if (!session.rootNode.hasNode(nodeName)) {
//			rootNode = session.rootNode.addNode(nodeName)
//			session.rootNode.save()
//		}
//		else {
//			rootNode = session.rootNode.getNode(nodeName)
//		}		
		super(repository, 'contacts', nodeName)
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
