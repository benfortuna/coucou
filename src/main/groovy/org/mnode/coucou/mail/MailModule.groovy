
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
import static org.mnode.juicer.JuicerUtils.hasPropertyValue

import java.awt.Color

import javax.jcr.nodetype.NodeType
import javax.mail.FetchProfile
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.Store
import javax.mail.URLName
import javax.mail.internet.MailDateFormat;
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.text.html.StyleSheet;

import org.mnode.coucou.DateCellRenderer
import org.mnode.coucou.layer.StatusLayerUI;
import org.mnode.juicer.JuicerUtils
import org.mnode.ousia.HTMLEditorKitExt;
import org.mnode.ousia.HyperlinkBrowser;
import org.mnode.ousia.HyperlinkBrowser.HyperlinkFeedback;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority

class MailModule {
	
	def mailDateFormat = new MailDateFormat()
	
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
			
			// add result list for mail folders and messages..
			
			// add preview pane for mail folders and messages..
			panel(id: 'mailMessageView') {
				borderLayout()
				
				def statusLayer = new StatusLayerUI()
				layer(statusLayer) {
					scrollPane {
						def styleSheet = new StyleSheet()
						styleSheet.addRule("body {background-color:#ffffff; color:#444b56; font-family:verdana,sans-serif; margin:8px; }")
				//        styleSheet.addRule("a {text-decoration:underline; color:blue; }")
				//                            styleSheet.addRule("a:hover {text-decoration:underline; }")
				//        styleSheet.addRule("img {border-width:0; }")
						
						defaultEditorKit = new HTMLEditorKitExt(styleSheet: styleSheet)
				
						editorPane(id: 'mailMessageContent', editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
						mailMessageContent.addHyperlinkListener(new HyperlinkBrowser(feedback: [
								show: { uri -> statusLayer.showStatusMessage uri.toString() },
								hide: { statusLayer.hideStatusMessage() }
							] as HyperlinkFeedback))
					}
				}
			}

		}
	}
		
	def loadResults = { ousia, activities, ttsupport, pathResult ->
		
		ousia.doLater {
			
			for (i in 0..frame.ribbon.contextualTaskGroupCount - 1) {
				if (frame.ribbon.getContextualTaskGroup(i).title == 'Mail') {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), true
					
					if (breadcrumb.model.items[-1].data.name == 'Mail') {
						frame.ribbon.selectedTask = mailRibbonTask
					}
				}
				else {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), false
				}
			}

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

		if (pathResult.class == MailboxNodePathResult) {
			for (accountNode in pathResult.element.accounts.nodes) {
				def item = [:]
				item['title'] = accountNode.name
				item['node'] = accountNode
	
				 ousia.doLater {
					 activities.withWriteLock {
						 add(item)
					 }
				 }
			}
			
			for (folderNode in pathResult.element.folders.nodes) {
				def item = [:]
				item['title'] = folderNode.name
				item['node'] = folderNode
	
				 ousia.doLater {
					 activities.withWriteLock {
						 add(item)
					 }
				 }
			}
		}
		else if (pathResult.class == StorePathResult) {
			if (!pathResult.element.connected) {
				pathResult.element.connect()
			}
			
			pathResult.element.defaultFolder.list().each {
				 def item = [:]
				 item['title'] = it.name
				 item['entry'] = it
	
				 ousia.doLater {
					 activities.withWriteLock {
						 add(item)
					 }
				 }
			}
		}
		else if (pathResult.class == FolderNodePathResult) {
			if ((Folder.HOLDS_MESSAGES & pathResult.element.getProperty("type").long) > 0) {
				// add messages..
				for (messageNode in pathResult.element.messages.nodes) {
					if (messageNode.isNodeType(NodeType.NT_UNSTRUCTURED)
							&& !JuicerUtils.hasPropertyValue(messageNode.flags.values, "deleted")
							&& !JuicerUtils.hasPropertyValue(messageNode.flags.values, "archived")) {
						
						def item = [:]
						if (messageNode.headers.hasProperty('Subject')) {
							item['title'] = messageNode.headers.Subject.string
						}
						else {
							item['title'] = '<No Subject>'
						}
						
						if (messageNode.parent.parent.name == 'Sent' && messageNode.headers.hasProperty('To')) {
							item['source'] = messageNode.headers.getProperty('To').string.intern()
						}
						else if (messageNode.headers.hasProperty('From')) {
							item['source'] = messageNode.headers.getProperty('From').string.intern()
						}
						else {
							item['source'] = '<Unknown Sender>'
						}
						
						if (messageNode.headers.hasProperty('Date')) {
							item['date'] = mailDateFormat.parse(messageNode.headers.getProperty('Date').string)
						}
						else if (messageNode.hasProperty('received')) {
							item['date'] = messageNode.getProperty('received').date.time
						}
						item.seen = { node ->
							node.seen?.boolean == true
						}.curry(messageNode)
						
						item.flagged = { node ->
							node.flagged?.boolean == true
						}.curry(messageNode)
						item['node'] = messageNode
						
						ousia.doLater {
							activities.withWriteLock {
								add(item)
							}
						}
					}
				}
			}
			else if ((Folder.HOLDS_FOLDERS & pathResult.element.getProperty("type").long) > 0) {
				// add folders..
				for (folderNode in pathResult.element.folders.nodes) {
					if (folderNode.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						def item = [:]
						item['title'] = (folderNode.folderName) ? folderNode.folderName : folderNode.name
						item['source'] = folderNode.parent.name
						item['node'] = folderNode
						
						ousia.doLater {
							activities.withWriteLock {
								add(item)
							}
						}
					}
				}
			}
		}
		else {
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
						 
						 ousia.doLater {
							 activities.withWriteLock {
								 add(item)
							 }
						 }
					}
			}
	//			else {
	//				return Arrays.asList(getElement().list());
	//			}
		}
	

	}
	
	def loadPreview = { ousia, entry ->
		ousia.edt {
			if (entry) {
				contentTitle.text = "<html><strong>${entry['title']}</strong><br/>${entry['source']} <em>${entry['date']}</em></html>"
				
				mailMessageContent.editorKit = defaultEditorKit
				
				if (entry['node'].hasNode('body')) {
					def content = entry['node'].getNode('body').getNode('part').getNode('jcr:content')
					if (content.getProperty('jcr:mimeType').string =~ /(?i)^text\/plain.*$/) {
						contentView.text = "<html><pre>${content.getProperty('jcr:data').string}"
					}
					else {
						contentView.text = content.getProperty('jcr:data').string
					}
					contentView.caretPosition = 0
				}
				else if (entry['node'].hasNode('content')) {
					def content = entry['node'].getNode('content').getNode('data').getNode('jcr:content').getProperty('jcr:data').string
					contentView.text = content
					contentView.caretPosition = 0
				}
				else {
					mailMessageContent.text = null
				}
			}
			else {
				contentTitle.text = ''
				mailMessageContent.text = null
			}
			
			previewPane.layout.show(previewPane, 'mailMessageView')
		}
	}
}
