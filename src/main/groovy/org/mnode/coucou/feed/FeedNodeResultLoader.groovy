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
package org.mnode.coucou.feed

import java.awt.Color;
import java.awt.Cursor;

import javax.jcr.PropertyType;
import javax.mail.internet.MailDateFormat;

import org.apache.jackrabbit.util.Text;
import org.mnode.coucou.DateCellRenderer;
import org.mnode.coucou.DefaultNodeTableCellRenderer;
import org.mnode.coucou.util.HtmlCodes;

class FeedNodeResultLoader {
	
	def mailDateFormat = new MailDateFormat()
	
	def reloadResults = { ousia, actionContext, activities, ttsupport ->
		ousia.edt {
			filterTextField.text = null
			frame.title = breadcrumb.model.items.collect({ it.data.name }).join(' | ') << ' - Coucou'
			frame.contentPane.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
			
			// enable/disable ribbon tasks..
			quickSearchField.text = null
			quickSearchField.enabled = !breadcrumb.model.items[-1].data.leaf
			quickSearchButton.enabled = !breadcrumb.model.items[-1].data.leaf
			
			if (breadcrumb.model.items[0].data.element.path == '/Mail') {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(0), true
				if (breadcrumb.model.items[-1].data.name == 'Mail') {
					frame.ribbon.selectedTask = mailRibbonTask
				}
			}
			else {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(0), false
			}
			
			if (breadcrumb.model.items[0].data.element.path == '/Feeds') {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(1), true
				if (breadcrumb.model.items[-1].data.name == 'Feeds') {
					frame.ribbon.selectedTask = feedRibbonTask
				}
			}
			else {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(1), false
			}
			
			if (breadcrumb.model.items[0].data.element.path == '/Contacts') {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(2), true
				if (breadcrumb.model.items[-1].data.name == 'Contacts') {
					frame.ribbon.selectedTask = contactsRibbonTask
				}
			}
			else {
				frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(2), false
			}
	
			actionContext.addFolder = { folderName ->
				breadcrumb.model.items[-1].data.element.addNode(Text.escapeIllegalJcrChars(folderName))
				breadcrumb.model.items[-1].data.element.save()
			}
		}
	
		ousia.doOutside {
			def items = breadcrumb.model.items[-1].data.results
	
			doLater {
				// install new renderer..
				DefaultNodeTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
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
	
			 items.reverseEach {
				 def item = [:]
				 // feeds / items..
				 if (it.hasProperty('title')) {
					 item['title'] = HtmlCodes.unescape(it.title.string)
				 }
				 else {
					 item['title'] = HtmlCodes.unescape(it.name)
				 }
				 
				 if (it.hasProperty('source')) {
					 if (it.source.type == PropertyType.REFERENCE) {
						 item['source'] = HtmlCodes.unescape(it.source.node.title.string).intern()
					 }
					 else {
						 item['source'] = HtmlCodes.unescape(it.source.string).intern()
					 }
				 }
				 else {
					 item['source'] = it.parent.name
				 }
	
				 if (it.hasProperty('date')) {
					 item['date'] = it.date.date.time
				 }
				 else if (it.hasProperty('received')) {
					 item['date'] = it.received.date.time
				 }
	
				 item['node'] = it
				 item.seen = { node ->
					 node.seen?.boolean == true
				 }.curry(it)
				 item.flagged = { node ->
					 node.flagged?.boolean == true
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
		}
		
		ousia.doLater {
			activityTable.scrollRectToVisible(activityTable.getCellRect(0, 0, true))
		}
	}
	
}
