/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2010, Ben Fortuna [fortuna@micronode.com]
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

import org.jdesktop.swingx.JXPanel
import org.mnode.ousia.OusiaBuilder

class LogView extends JXPanel {

	LogView(File logFile) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = swing.rs('Repository Explorer')
        border = swing.emptyBorder(10)
		
		add swing.scrollPane {
			textArea(editable: false, id: 'logField')
		
//			Tailer.create logFile, { line ->
//				logField.text = logField.text + '\n' + line
//				logField.caretPosition = logField.document.length
//			} as TailerListener
		}
	}
}
