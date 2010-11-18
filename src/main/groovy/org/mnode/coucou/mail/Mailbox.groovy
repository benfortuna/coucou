/**
 * 
 */
package org.mnode.coucou.mail

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

import org.apache.commons.lang.time.DurationFormatUtils;
import org.mnode.juicer.query.QueryBuilder;

/**
 * @author fortuna
 *
 */
class Mailbox {
	
//	def rootNode

	Session mailSession
	
	def updateThread
		
	Mailbox(javax.jcr.Session session, String nodeName) {
		def mailNode
		if (!session.rootNode.hasNode(nodeName)) {
			mailNode = session.rootNode.addNode(nodeName)
			session.rootNode.save()
		}
		else {
			mailNode = session.rootNode.getNode(nodeName)
		}
		
		if (!mailNode.hasNode('folders')) {
			mailNode.addNode('folders')
			mailNode.save()
		}
		
		//if (mailNode.hasNode('Attachments')) {
		//	mailNode.getNode('Attachments').remove()
		//}
		
		if (!mailNode.hasNode('Attachments')) {
			def attachments = new QueryBuilder(session.workspace.queryManager).with {
				query(
					source: selector(nodeType: 'nt:file', name: 'files'),
					constraint: and(
						constraint1: descendantNode(selectorName: 'files', path: "/${nodeName}"),
						constraint2: and(
							constraint1: not(comparison(
								operand1: nodeName(selectorName: 'files'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: literal(session.valueFactory.createValue('part')))),
							constraint2: not(comparison(
								operand1: nodeName(selectorName: 'files'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: literal(session.valueFactory.createValue('data'))))))
				)
			}
			def attachmentsNode = mailNode.addNode('Attachments')
			attachments.storeAsNode("${attachmentsNode.path}/query")
			mailNode.save()
		}
		
		// email init..
		def mailSessionProps = new Properties()
		mailSessionProps.setProperty('mstor.repository.name', 'coucou')
		mailSessionProps.setProperty('mstor.repository.path', nodeName)
		mailSessionProps.setProperty('mstor.repository.create', 'true')
		mailSessionProps.setProperty('mail.store.protocol', 'mstor')
		
		mailSession = Session.getInstance(mailSessionProps, {new PasswordAuthentication('mail', '')} as Authenticator)

		updateThread = Executors.newSingleThreadScheduledExecutor()
	}

	void start() {
	   updateThread.scheduleAtFixedRate({
		   Session importSession = Session.getInstance(new Properties())
		   Store importStore = importSession.getStore('pop3')
		   importStore.connect('mail.internode.on.net', 'username', 'password')
		   
		   importMail importStore

	   }, 0, 30, TimeUnit.MINUTES)
	}
	
	def importMail = { importStore ->
		
		def now = Calendar.instance
		
		for (folder in importStore.defaultFolder.list()) {
			folder.open(Folder.READ_ONLY)
			try {
//				def messages = folder.messages
//				for (message in messages) {
//					println message.subject
//				}
				def localStore = mailSession.store
				localStore.connect()
				def inbox = localStore.defaultFolder.getFolder('Inbox')
				if (!inbox.exists()) {
					inbox.create(Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES)
				}
				inbox.open(Folder.READ_WRITE)
				
				int interval = 500;
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
}
