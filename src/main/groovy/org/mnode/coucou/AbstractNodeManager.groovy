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
package org.mnode.coucou

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.jcr.Repository
import javax.jcr.Session
import javax.jcr.SimpleCredentials

import org.mnode.base.log.FormattedLogEntry
import org.mnode.base.log.LogEntry
import org.mnode.base.log.LogEntry.Level

/**
 * @author fortuna
 *
 */
abstract class AbstractNodeManager {
	
	static Lock lock = new ReentrantLock()
	
	protected static LogEntry adding_path = new FormattedLogEntry(Level.Info, 'Adding path: %s')
	protected static LogEntry saving_node = new FormattedLogEntry(Level.Info, 'Saving node: %s')
	protected static LogEntry unexpected_error = new FormattedLogEntry(Level.Error, 'An unexpected error has occurred')

	Session session
	
	javax.jcr.Node rootNode
	
	AbstractNodeManager(Repository repository, String user, String nodeName) {
		session = repository.login(new SimpleCredentials(user, ''.toCharArray()))
		rootNode = getNode(session.rootNode, nodeName)
		save rootNode
	}

	def getNode = { rootNode, path, referenceable = false ->
		if (!rootNode.hasNode(path)) {
			log.log adding_path, path
			
			// lock to avoid concurrent modification..
			session.withLock(lock) {
				def node = rootNode.addNode(path)
				if (referenceable) {
					node.addMixin('mix:referenceable')
				}
			}
		}
		return rootNode.getNode(path)
	}

	def updateProperty = { aNode, propertyName, value ->
		if (value != null) { // && (!aNode.hasProperty(propertyName)) || aNode.getProperty(propertyName).string != value)) {
			// lock to avoid concurrent modification..
			session.withLock(lock) {
				aNode.setProperty(propertyName, value)
			}
		}
	}

	// save a node hierarchy..
	def save = { node ->
		log.log saving_node, node.path
		def parent = node
		while (parent.isNew()) {
			parent = parent.parent
		}
		// lock to avoid concurrent modification..
		session.withLock(lock) {
			parent.save()
		}
	}
}
