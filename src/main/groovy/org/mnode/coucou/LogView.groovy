package org.mnode.coucou

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.jdesktop.swingx.JXPanel;
import org.mnode.ousia.OusiaBuilder;

class LogView extends JXPanel {

	LogView(File logFile) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = swing.rs('Repository Explorer')
        border = swing.emptyBorder(10)
		
		add swing.scrollPane {
			textArea(editable: false, id: 'logField')
		
			Tailer.create logFile, { line ->
				logField.text = logField.text + '\n' + line
				logField.caretPosition = logField.document.length
			} as TailerListener
		}
	}
}
