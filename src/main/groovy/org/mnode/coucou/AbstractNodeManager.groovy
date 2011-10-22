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
//@Invariant({ session && baseNode })
abstract class AbstractNodeManager {
	
	private Lock lock = new ReentrantLock()
	
	protected static LogEntry adding_path = new FormattedLogEntry(Level.Info, 'Adding path: %s')
	protected static LogEntry saving_node = new FormattedLogEntry(Level.Info, 'Saving node: %s')
	protected static LogEntry unexpected_error = new FormattedLogEntry(Level.Error, 'An unexpected error has occurred')

//	Session session
	
	javax.jcr.Node baseNode
	
	protected AbstractNodeManager(Repository repository, String user, String nodeName) {
		Session session = repository.login(new SimpleCredentials(user, ''.toCharArray()))
		session.save {
			baseNode = getNode(rootNode, nodeName)
		}
	}

	javax.jcr.Node getNode(javax.jcr.Node baseNode, String path, boolean referenceable = false) {
		if (!baseNode.hasNode(path)) {
			log.log adding_path, path
			
			// lock to avoid concurrent modification..
			baseNode.session.withLock(lock) {
				def node = baseNode.addNode(path)
				if (referenceable) {
					node.addMixin('mix:referenceable')
				}
			}
		}
		baseNode."$path"
	}

	void updateProperty(javax.jcr.Node aNode, String propertyName, Object value) {
		// XXX: value may be a boolean value itself..
		if (value != null) {
			// lock to avoid concurrent modification..
			aNode.session.withLock(lock) {
				aNode."$propertyName" = value
			}
		}
	}

	// save a node hierarchy..
	javax.jcr.Node save(javax.jcr.Node node) {
		log.log saving_node, node.path
		def parent = node
		while (parent.isNew()) {
			parent = parent.parent
		}
		// lock to avoid concurrent modification..
		node.session.withLock(lock) {
			save()
		}
		node
	}
}
