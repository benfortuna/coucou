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

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JOptionPane;

import org.mnode.coucou.DateCellRenderer;
import org.mnode.coucou.DefaultNodeTableCellRenderer;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;

class ContactsModule {
	
	def contactsManager
	
	def initUI = { ousia ->
		ousia.edt {
			
			actions {
				action id: 'addXmppAccountAction', name: rs('Add Account'), SmallIcon: newIcon, closure: {
					def user = JOptionPane.showInputDialog(frame, rs('User'))
					if (user) {
						def accountNode = contactsManager.addAccount(user)
						doOutside {
							contactsManager.connect accountNode
						}
					}
				}
		
			}
		
			ribbonTask(rs('Action'), id: 'contactsRibbonTask', bands: [
				
				ribbonBand(rs('Configure'), id: 'xmppConfigurationBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(addXmppAccountAction),
						priority: RibbonElementPriority.TOP
					])
				},
	
			])
			
			frame.ribbon.addContextualTaskGroup new RibbonContextualTaskGroup(rs('Contacts'), Color.YELLOW, contactsRibbonTask)
		}
	}
	
	def loadResults = { ousia, activities, ttsupport, pathResult ->
	
		ousia.doLater {
			
			for (i in 0..frame.ribbon.contextualTaskGroupCount - 1) {
				if (frame.ribbon.getContextualTaskGroup(i).title == 'Contacts') {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), true
					
					if (breadcrumb.model.items[-1].data.name == 'Contacts') {
						frame.ribbon.selectedTask = contactsRibbonTask
					}
				}
				else {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), false
				}
			}
			
			// install new renderer..
			DefaultNodeTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
			defaultRenderer.background = Color.WHITE
			
			DateCellRenderer dateRenderer = [defaultRenderer]
			dateRenderer.background = Color.WHITE
			
			ttsupport.delegateRenderer = defaultRenderer
			activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
			
			activities.withWriteLock {
				clear()
			}
		}

		if (pathResult.class == ContactsNodePathResult) {
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
		}
		else {
			pathResult.element.entries.each {
				 def item = [:]
				 item['title'] = it.name ? it.name : it.user
				 item['entry'] = it
	
				 ousia.doLater {
					 activities.withWriteLock {
						 add(item)
					 }
				 }
			}
		}
	}
}
