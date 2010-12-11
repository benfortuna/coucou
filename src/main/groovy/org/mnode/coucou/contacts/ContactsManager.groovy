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
package org.mnode.coucou.contacts

import javax.jcr.Repository;

import org.mnode.coucou.AbstractNodeManager;

/**
 * @author fortuna
 *
 */
class ContactsManager extends AbstractNodeManager {

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
