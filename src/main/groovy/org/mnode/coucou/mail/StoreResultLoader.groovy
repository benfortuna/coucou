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

import org.mnode.coucou.DateCellRenderer
import org.mnode.coucou.DefaultNodeTableCellRenderer


class StoreResultLoader {

	def reloadResults = { ousia, actionContext, activities, ttsupport ->
		def items = breadcrumb.model.items[-1].data.results

		doLater {
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

		 items.reverseEach {
			 def item = [:]
			 item['title'] = it.name
			 item['entry'] = it

			 doLater {
				 activities.withWriteLock {
					 add(item)
				 }
			 }
		}
	}
}
