/**
 * This file is part of Base Modules.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
 *
 * Base Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Base Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Base Modules.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.coucou

import org.jdesktop.swingx.JXPanel;
import org.mnode.ousia.OusiaBuilder;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;


/**
 * @author Ben
 *
 */
public class SearchView extends JXPanel {

    def showItem
    
    SearchView(def title, def resultList, def comparator) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = title
        border = swing.emptyBorder(10)

        def searchModel = new EventListModel(new SortedList(resultList, comparator as Comparator))
        
        add swing.scrollPane(horizontalScrollBarPolicy: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, border: null) {
            list(opaque: false, id: 'searchList', cellRenderer: new ActivityListCellRenderer())
            searchList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
            searchList.mouseClicked = { e ->
                if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && searchList.selectedValue && showItem) {
                    showItem(searchList.selectedValue)
                }
            }
            searchList.focusLost = {
                searchList.clearSelection()
            }
            searchList.model = searchModel
        }

    }
    
}
