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

import javax.mail.Flags

import org.mnode.coucou.DateCellRenderer


class FolderResultLoader {

	def reloadResults = { ousia, actionContext, activities, ttsupport ->
		ousia.doOutside {
			def items = breadcrumb.model.items[-1].data.results
	
			doLater {
				// install new renderer..
				MessageTableCellRenderer defaultRenderer = [activityTree, ['Today', 'Yesterday', 'Older Items']]
				defaultRenderer.background = Color.WHITE
				
				DateCellRenderer dateRenderer = [defaultRenderer]
				dateRenderer.background = Color.WHITE
				
				ttsupport.delegateRenderer = defaultRenderer
				activityTable.columnModel.getColumn(1).cellRenderer = defaultRenderer
				
				activities.withWriteLock {
					clear()
				}
			}
	
			 items.reverseEach {
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
		}
		
		ousia.doLater {
			activityTable.scrollRectToVisible(activityTable.getCellRect(0, 0, true))
		}
	}
}
