/**
 * 
 */
package org.mnode.coucou.mail

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.jcr.Repository;
import javax.jcr.query.qom.QueryObjectModelConstants;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

import org.mnode.coucou.AbstractNodeManager;
import org.mnode.juicer.query.QueryBuilder;

/**
 * @author fortuna
 *
 */
class Mailbox extends AbstractNodeManager {
	
//	def rootNode

	javax.mail.Session mailSession
	
	def updateThread
		
//	Query relatedMessages
	
	Mailbox(Repository repository, String nodeName) {
//		def mailNode
//		if (!session.rootNode.hasNode(nodeName)) {
//			mailNode = session.rootNode.addNode(nodeName)
//		}
//		else {
//			mailNode = session.rootNode.getNode(nodeName)
//		}
		super(repository, 'mail', nodeName)
		
		if (!rootNode.hasNode('folders')) {
			rootNode.addNode('folders')
		}
		
		//if (mailNode.hasNode('Attachments')) {
		//	mailNode.getNode('Attachments').remove()
		//}
		
		if (!rootNode.hasNode('Attachments')) {
			def attachments = new QueryBuilder(session.workspace.queryManager).with {
				query(
					source: selector(nodeType: 'nt:file', name: 'files'),
					constraint: and(
						constraint1: descendantNode(selectorName: 'files', path: "/${nodeName}"),
						constraint2: and(
							constraint1: not(comparison(
								operand1: nodeNamex(selectorName: 'files'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: literal(session.valueFactory.createValue('part')))),
							constraint2: not(comparison(
								operand1: nodeNamex(selectorName: 'files'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: literal(session.valueFactory.createValue('data'))))))
				)
			}
			def attachmentsNode = rootNode.addNode('Attachments')
			attachments.storeAsNode("${attachmentsNode.path}/query")
//			mailNode.save()
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
		   Session importSession = Session.getInstance(new Properties())
		   Store importStore = importSession.getStore('pop3')
		   importStore.connect('mail.internode.on.net', 'username', 'password')
		   
		   importMail importStore

	   }, 0, 30, TimeUnit.MINUTES)
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
}
