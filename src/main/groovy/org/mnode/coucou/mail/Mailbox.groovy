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
package org.mnode.coucou.mail

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import javax.jcr.Repository
import javax.jcr.query.qom.QueryObjectModelConstants
import javax.mail.Authenticator
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Store

import org.mnode.base.log.LogAdapter
import org.mnode.base.log.adapter.Slf4jAdapter
import org.mnode.coucou.AbstractNodeManager
import org.mnode.juicer.query.QueryBuilder
import org.slf4j.LoggerFactory

/**
 * @author fortuna
 *
 */
class Mailbox extends AbstractNodeManager {
	
	private static LogAdapter log = new Slf4jAdapter(LoggerFactory.getLogger(Mailbox))

	javax.mail.Session mailSession
	
	Authenticator passwordPrompt
	
	def updateThread
	
	Mailbox(Repository repository, String nodeName) {
		super(repository, 'mail', nodeName)
		
		if (!rootNode.hasNode('folders')) {
			rootNode.addNode('folders')
		}
		if (!rootNode.hasNode('accounts')) {
			rootNode.addNode('accounts')
		}

		save rootNode
		
		// email init..
		def mailSessionProps = new Properties()
		mailSessionProps.setProperty('mstor.repository.name', 'coucou')
		mailSessionProps.setProperty('mstor.repository.path', nodeName)
		mailSessionProps.setProperty('mstor.repository.create', 'true')
		mailSessionProps.setProperty('mail.store.protocol', 'mstor')
		
		mailSession = javax.mail.Session.getInstance(mailSessionProps, {new PasswordAuthentication('mail', '')} as Authenticator)

		updateThread = Executors.newSingleThreadScheduledExecutor()
/*
		relatedMessages = new QueryBuilder(session.workspace.queryManager).with {
			query(
				source: selector(nodeType: 'nt:unstructured', name: 'messages'),
				constraint: and(
					constraint1: descendantNode(selectorName: 'messages', path: "/${nodeName}"),
//						constraint2: or(
						constraint2: comparison(
							operand1: propertyValue(selectorName: 'messages', propertyName: 'inReplyTo'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: bindVariable(name: 'messageId'))),
//							constraint2: not(comparison(
//								operand1: nodeNamex(selectorName: 'files'),
//								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
//								operand2: bindVariable(name: 'messageId')))))
			)
		}
*/
	}

	void start() {
	   updateThread.scheduleAtFixedRate({
		   for (account in rootNode.accounts.nodes) {
			   try {
				   updateAccount account
			   }
			   catch (Exception e) {
				  log.log unexpected_error, e
			   }
		   }

	   }, 0, 30, TimeUnit.MINUTES)
	}
	
	def addAccount = { emailAddress ->
		def accountNode = getNode(rootNode.accounts, emailAddress, true)
		accountNode.address = emailAddress
		if (emailAddress =~ /^.*@gmail\.com$/) {
			accountNode.protocol = 'imaps'
			accountNode.server = 'imap.gmail.com'
		}
		save accountNode
	}
	
	def updateAccount = { account ->
		Properties props = new Properties()
		props.setProperty("mail.store.protocol", account.protocol.string)
		props.setProperty("mail.host", account.server.string)
		props.setProperty("mail.user", account.address.string)
		Session session = Session.getInstance(props, passwordPrompt)
		Store store = session.getStore(account.protocol.string)
		store.connect()
		importMail store
	}
	
	def importMail = { importStore ->
		
		def now = Calendar.instance
		
		for (folder in importStore.defaultFolder.list()) {
//			if (folder.name != 'Sent') {
//				continue
//			}
			folder.open(Folder.READ_ONLY)
			try {
//				def messages = folder.messages
//				for (message in messages) {
//					println message.subject
//				}
				def localStore = mailSession.store
				localStore.connect()
				def inbox = localStore.defaultFolder.getFolder(folder.name)
				if (!inbox.exists()) {
					inbox.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES)
				}
				inbox.open(Folder.READ_WRITE)
				
				int interval = 100;
				long start = -1, init = System.currentTimeMillis();
				for (int i = 1; i <= folder.getMessageCount();) {
					int end = Math.min(interval + i - 1, folder.getMessageCount());
//					LOG.info("Appending messages: " + i + " - " + (end));
					start = System.currentTimeMillis();
					inbox.appendMessages(folder.getMessages(i, end));
//					LOG.info((1f / ((System.currentTimeMillis() - start) / (1000 * interval)))
//							+ " message(s)/s. Est. completion: "
//							+ DurationFormatUtils.formatDurationHMS((((System.currentTimeMillis() - init) / end)
//									* (initFolder.getMessageCount() - end))));
					i += interval;
				}

//				inbox.appendMessages(messages)
				inbox.close(false)
				localStore.close()
			} catch (MessagingException e) {
				e.printStackTrace()
			}
		}
	}
	
	def delete = { node ->
		addFlags(node, new Flags(Flag.DELETED))
	}
	
	def archive = { node ->
		addFlags(node, new Flags('archived'))
	}
	
	private void addFlags(javax.jcr.Node node, Flags flags) {
		def localStore = mailSession.store
		
		Folder folder = null
		try {
			localStore.connect()
			folder = localStore.defaultFolder.getFolder(node.parent.parent.name)
			folder.open(Folder.READ_WRITE)
			int msgNum = node.messageNumber.long as Integer
			folder.setFlags(msgNum, msgNum, flags, true)
		}
		catch (Exception e) {
			e.printStackTrace()
		}
		finally {
			folder?.close(false)
			localStore.close()
		}
	}
}
