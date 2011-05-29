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
package org.mnode.coucou.planner

import java.awt.Color;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.mnode.coucou.DateCellRenderer;
import org.mnode.coucou.DefaultNodeTableCellRenderer;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;

class PlannerModule {
	
	def planner
	
	def initUI = { ousia ->
		ousia.edt {
			
			actions {
				
				action id: 'addCalendarAction', name: rs('Calendar'), closure: {
					def url = JOptionPane.showInputDialog(frame, rs('URL'))
					if (url) {
						doOutside {
							try {
								planner.addCalendar(url)
							}
							catch (MalformedURLException e) {
								doLater {
									JOptionPane.showMessageDialog(frame, "Invalid URL: ${url}")
								}
							}
							catch (Exception e2) {
								doLater {
									ErrorInfo error = ['Error', "${e2.message}",
										"<html><body>Error adding feed: ${e2}</body></html>", null, null, null, null]
									JXErrorPane.showDialog(frame, error);
								}
							}
						}
					}
				}
		
				action id: 'addCalendarAccountAction', name: rs('Add Account'), SmallIcon: newIcon, closure: {
					def url = JOptionPane.showInputDialog(frame, rs('Calendar URL'))
					if (url) {
						def accountNode = planner.addAccount(url)
					}
				}
			}
			
			ribbonTask(rs('Action'), id: 'plannerRibbonTask', bands: [
				
				ribbonBand(rs('Configure'), id: 'plannerConfigurationBand', resizePolicies: ['mirror']) {
					ribbonComponent([
						component: commandButton(addCalendarAccountAction),
						priority: RibbonElementPriority.TOP
					])
				},
	
			])
			frame.ribbon.addContextualTaskGroup new RibbonContextualTaskGroup(rs('Planner'), Color.CYAN, plannerRibbonTask)
			
			ribbonApplicationMenuEntrySecondary(id: 'newCalendar', icon: eventIcon, text: rs('Calendar'), kind: CommandButtonKind.ACTION_ONLY, actionPerformed: addCalendarAction)
		}
	}
	
	def loadResults = { ousia, activities, ttsupport, pathResult ->
	
		ousia.doLater {
			
			for (i in 0..frame.ribbon.contextualTaskGroupCount - 1) {
				if (frame.ribbon.getContextualTaskGroup(i).title == 'Planner') {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), true
					
					if (breadcrumb.model.items[-1].data.name == 'Planner') {
						frame.ribbon.selectedTask = plannerRibbonTask
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

		if (pathResult.class == PlannerNodePathResult) {
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
		else if (pathResult.class == CalendarStorePathResult) {
			if (!pathResult.element.connected) {
				final PasswordAuthentication pa = Authenticator.requestPasswordAuthentication(null, -1, null, "Password", null);
				pathResult.element.connect(pa.userName, pa.password)
			}
			pathResult.element.collections.each {
				def item = [:]
				item['title'] = it.displayName
				item['collection'] = it
	
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
				 item['title'] = it.getProperty('X-WR-CALNAME').value
				 item['calendar'] = it
	
				 ousia.doLater {
					 activities.withWriteLock {
						 add(item)
					 }
				 }
			}
		}
	}
}
