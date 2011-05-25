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

import java.awt.Color
import java.awt.Cursor

import javax.mail.FetchProfile
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.Store
import javax.mail.URLName
import javax.swing.JFileChooser
import javax.swing.JOptionPane

import org.mnode.coucou.DateCellRenderer
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority

class MailModule {
	
	def mailbox
	
	def initUI = { ousia ->
		ousia.edt {
			
			resizableIcon('/mail.svg', size: [16, 16], id: 'mailIcon')
			resizableIcon('/mail.svg', size: [12, 12], id: 'mailIconSmall')
		
			actions {
				
				// import email..
				action id: 'importMailAction', name: rs('Email'), closure: {
					if (dirChooser.showOpenDialog() == JFileChooser.APPROVE_OPTION) {
						doOutside {
							// load email..
							Session importSession = Session.getInstance(new Properties())
							Store importStore = importSession.getStore(new URLName("mstor:${dirChooser.selectedFile.absolutePath}"))
							importStore.connect()
					
							mailbox.importMail importStore
						}
					}
				}
		
				action id: 'addMailAccountAction', name: rs('Add Account'), SmallIcon: newIcon, closure: {
					def emailAddress = JOptionPane.showInputDialog(frame, rs('Email Address'))
					if (emailAddress) {
						def accountNode = mailbox.addAccount(emailAddress)
						doOutside {
							mailbox.updateAccount accountNode
						}
					}
				}
		
			}
			
			ribbonApplicationMenuEntrySecondary(id: 'importMail', icon: mailIcon, text: rs('Email'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: importMailAction)
			
			ribbonTask(rs('Action'), id: 'mailRibbonTask', bands: [
	
				ribbonBand(rs('Configure'), id: 'mailConfigurationBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(addMailAccountAction),
						priority: RibbonElementPriority.TOP
					])
				},
			
				ribbonBand(rs('Respond'), icon: forwardIcon, id: 'respondBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(replyIcon, text: rs('Reply')),
						priority: RibbonElementPriority.TOP
					])
					ribbonComponent([
						component: commandButton(replyAllIcon, text: rs('Reply To All')),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(forwardIcon, text: rs('Forward')),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(chatIcon, text: rs('Chat')),
						priority: RibbonElementPriority.MEDIUM
					])
				},
			
				ribbonBand(rs('Organise'), icon: forwardIcon, id: 'organiseBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(rs('Flag')),
						priority: RibbonElementPriority.TOP
					])
					ribbonComponent([
						component: commandButton(rs('Tag')),
						priority: RibbonElementPriority.MEDIUM
					])
		//			ribbonComponent([
		//				component: commandButton(rs('Move To')),
		//				priority: RibbonElementPriority.MEDIUM
		//			])
					ribbonComponent([
						component: commandButton(archiveAction),
						priority: RibbonElementPriority.MEDIUM
					])
					ribbonComponent([
						component: commandButton(rs('Delete'), icon: deleteMailIcon, actionPerformed: deleteAction),
						priority: RibbonElementPriority.MEDIUM
					])
				},
	
				ribbonBand(rs('Extras'), icon: forwardIcon, id: 'actionExtrasBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(copyIcon, text: rs('Copy')),
						priority: RibbonElementPriority.TOP
					])
					ribbonComponent([
						component: commandButton(eventIcon, text: rs('Add To Planner')),
						priority: RibbonElementPriority.MEDIUM
					])
				},
			])
			
			frame.ribbon.addContextualTaskGroup new RibbonContextualTaskGroup(rs('Mail'), Color.PINK, mailRibbonTask)
			
		}
	}
		
	def loadResults = { ousia, activities, ttsupport, pathResult ->
		ousia.doOutside {
			doLater {
				// install new renderer..
				MessageTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
				defaultRenderer.background = Color.WHITE
				
				DateCellRenderer dateRenderer = [defaultRenderer]
				dateRenderer.background = Color.WHITE
				
				ttsupport.delegateRenderer = defaultRenderer
				activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
				activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
				
				activities.withWriteLock {
					clear()
				}
			}

			if (!pathResult.element.isOpen()) {
				pathResult.element.open(Folder.READ_ONLY);
			}
			if ((pathResult.element.getType() & Folder.HOLDS_MESSAGES) > 0) {
				final Message[] messages = pathResult.element.getMessages();
				
				FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				fp.add(FetchProfile.Item.FLAGS);
	
				pathResult.element.fetch(messages, fp);
/*				
				List<Message> messageList = new ArrayList<Message>(Arrays.asList(messages));
				for (Iterator<Message> iter = messageList.iterator(); iter.hasNext();) {
					if (iter.next().isExpunged()) {
						iter.remove();
					}
				}
*/
				messages.reverseEach {
					 def item = [:]
					 item['title'] = it.subject
					 item['source'] = it.from.collect { it.personal ? it.personal : it.address }.join(', ')
					 item['date'] = it.receivedDate
					 item['entry'] = it
					 item.seen = { msg ->
						 msg.flags.contains(Flags.Flag.SEEN)
					 }.curry(it)
					 item.flagged = { msg ->
						 msg.flags.contains(Flags.Flag.FLAGGED)
					 }.curry(it)
					 
					 doLater {
						 activities.withWriteLock {
							 add(item)
						 }
					 }
				}
				
				doLater {
					frame.contentPane.cursor = Cursor.defaultCursor
				}
   
//				return new ArrayList<Object>(messageList);
			}
//			else {
//				return Arrays.asList(getElement().list());
//			}
	
		}

	}
}
