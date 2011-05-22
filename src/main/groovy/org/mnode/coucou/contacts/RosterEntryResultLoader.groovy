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

import org.mnode.coucou.DateCellRenderer;
import org.mnode.coucou.DefaultNodeTableCellRenderer;

class RosterEntryResultLoader {

	def reloadResults = { ousia, actionContext, activities, ttsupport ->
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
				
				 try {
					 // lock for list modification..
					 activities.readWriteLock.writeLock().lock()
					 activities.clear()
				 }
				 finally {
					 // unlock post-list modification..
					 activities.readWriteLock.writeLock().unlock()
				 }
			}
	
			 items.reverseEach {
				 def item = [:]
				 item['title'] = it.name ? it.name : it.user
				 item['entry'] = it
	
				 doLater {
					 try {
						 // lock for list modification..
						 activities.readWriteLock.writeLock().lock()
						 activities.add(item)
					 }
					 finally {
						 // unlock post-list modification..
						 activities.readWriteLock.writeLock().unlock()
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
