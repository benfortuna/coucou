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

import javax.swing.JOptionPane;

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
}
