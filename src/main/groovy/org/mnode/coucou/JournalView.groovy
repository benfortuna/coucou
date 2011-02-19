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
package org.mnode.coucou

import org.jdesktop.swingx.JXPanel;
import org.mnode.ousia.OusiaBuilder;


/**
 * @author Ben
 *
 */
public class JournalView extends JXPanel {

    def editJournalEntry
    
    JournalView(def node) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = node.getProperty('subject').string
        border = swing.emptyBorder(10)

        add swing.scrollPane() {
            editorPane(editable: false, contentType: 'text/plain', opaque: true, border: null, id: 'contentView')
            contentView.text = node.getProperty('content').string
        }
        add swing.hbox(border: swing.emptyBorder([10, 0, 0, 0])) {
            button(text: 'Revisions..')
            hglue()
            button(text: 'Edit', actionPerformed: {editJournalEntry(node.parent, node.name)})
        }, BorderLayout.SOUTH

    }
    
}
