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

/**
 * @author fortuna
 *
 */
class Mailbox {
	
//	def rootNode

	Session mailSession
	
	def updateThread
		
	Mailbox() {

		// email init..
		def mailSessionProps = new Properties()
		mailSessionProps.setProperty('mstor.repository.name', 'coucou')
		mailSessionProps.setProperty('mstor.repository.path', 'Mail')
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
