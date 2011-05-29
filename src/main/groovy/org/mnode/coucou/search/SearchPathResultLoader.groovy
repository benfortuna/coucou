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
package org.mnode.coucou.search

import java.awt.Color;

import javax.jcr.PropertyType;

import org.mnode.coucou.DateCellRenderer;
import org.mnode.coucou.DefaultNodeTableCellRenderer;
import org.mnode.coucou.util.HtmlCodes;

class SearchPathResultLoader {
	
	def loadResults = { ousia, activities, ttsupport, pathResult ->
		ousia.doLater {
			
			for (i in 0..frame.ribbon.contextualTaskGroupCount - 1) {
				if (frame.ribbon.getContextualTaskGroup(i).title == 'Feeds') {
					frame.ribbon.setVisible frame.ribbon.getContextualTaskGroup(i), true
					
					if (breadcrumb.model.items[-1].data.name == 'Feeds') {
						frame.ribbon.selectedTask = feedRibbonTask
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
			activityTable.columnModel.getColumn(2).cellRenderer = dateRenderer
			
			activities.withWriteLock {
				clear()
			}
		}
	
		pathResult.results.reverseEach {
//		for (it in pathResult.element.nodes) {
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
//						 item['source'] = HtmlCodes.unescape(it.source.node.title.string).intern()
					 item['source'] = HtmlCodes.unescape(it.getProperty('source').getNode().getProperty('title').string).intern()
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
			 item.seen = { it.seen?.boolean == true }
			 item.flagged = { it.flagged?.boolean == true }
			 
			 ousia.doLater {
				 activities.withWriteLock {
					 add(item)
				}
			}
		}
	}

}
