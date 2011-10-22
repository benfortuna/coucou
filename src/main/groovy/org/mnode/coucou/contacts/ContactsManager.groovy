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

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.util.StringUtils
import org.jivesoftware.smackx.ChatStateManager
import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.AbstractNodeManager
import org.slf4j.LoggerFactory

/**
 * @author fortuna
 *
 */
class ContactsManager extends AbstractNodeManager {
	
	private static LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(ContactsManager))
	
//	javax.jcr.Node baseNode
	
	List<XMPPConnection> xmppConnections = []
	
	def activeChats = [:]
	
	def passwordPrompt
	
	ContactsManager(Repository repository, String nodeName) {
//		if (!session.baseNode.hasNode(nodeName)) {
//			baseNode = session.rootNode.addNode(nodeName)
//			session.rootNode.save()
//		}
//		else {
//			baseNode = session.rootNode.getNode(nodeName)
//		}		
		super(repository, 'contacts', nodeName)
		if (!baseNode.hasNode('accounts')) {
			baseNode.addNode('accounts')
		}
		if (!baseNode.hasNode('conversations')) {
			baseNode.addNode('conversations')
		}

		save baseNode
	}
	
	void connect() {
		for (account in baseNode.accounts.nodes) {
			connect account
		}
	}
	
	def connect = { account ->
		ConnectionConfiguration config = [account.host.string, account.port.long as Integer, account.serviceName.string]
		XMPPConnection xmpp = [config]
		xmpp.connect()
		
		def password = passwordPrompt("Password for $account.user.string:")
		if (password) {
			xmpp.login(account.user.string, password)
		}
		
		// NOTE: getInstance(org.jivesoftware.smack.XMPPConnection) needs to be
		// called in order for the listeners to be registered appropriately with
		// the connection. If this does not occur you will not receive the
		// update notifications.
		ChatStateManager.getInstance(xmpp)
		
		xmppConnections << xmpp
	}
	
	javax.jcr.Node add(def address) {
		def node
		if (baseNode.hasNode(address.address)) {
			node = baseNode.getNode(address.address)
		}
		else {
			node = baseNode.addNode(address.address)
		}
		node.setProperty('email', address.address)
		node.setProperty('personal', address.personal)
		save node
		node
	}
	
	def addAccount = { user ->
		def accountNode = getNode(baseNode.accounts, user, true)
		accountNode.user = user
		if (user =~ /^.*@gmail\.com$/) {
			accountNode.host = 'talk.google.com'
			accountNode.port = 5222
			accountNode.serviceName = 'gmail.com'
		}
		save accountNode
	}

	def getXmppName = { message, xmpp ->
		def participant = xmpp.roster.getEntry(StringUtils.parseBareAddress(message.from))
		if (participant?.name) {
			return participant.name
		}
		else if (participant?.user) {
			return participant.user
		}
		StringUtils.parseName(message.from)
	}
}
