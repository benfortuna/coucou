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

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;
import org.jdesktop.swingx.JXPanel;
import org.mnode.ousia.HyperlinkBrowser;
import org.mnode.ousia.OusiaBuilder;


/**
 * @author Ben
 *
 */
public class NoteView extends JXPanel{

    def editNote
    
    NoteView(def node, def defaultEditorKit) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = node.getProperty('title').string
        border = swing.emptyBorder(10)

        add swing.scrollPane() {
            if ('text/html' == node.getProperty('contentType').string) { 
                editorPane(editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null, id: 'contentView')
                if (node.hasProperty('markupLanguage')) {
                    if ('MediaWiki' == node.getProperty('markupLanguage').string) {
                        def parser = new MarkupParser(new MediaWikiLanguage())
                        def content = parser.parseToHtml(node.getProperty('content').string).replaceAll(/(?i)\<\?.*\?\>|\<meta.*\/\>/, '')
                        println "Parsed mediawiki: ${content}"
                        contentView.text = content
                    }
                    else if ('Confluence' == node.getProperty('markupLanguage').string) {
                        def parser = new MarkupParser(new ConfluenceLanguage())
                        def content = parser.parseToHtml(node.getProperty('content').string).replaceAll(/(?i)\<\?.*\?\>|\<meta.*\/\>/, '')
                        println "Parsed confluence: ${content}"
                        contentView.text = content
                    }
                    else if ('Textile' == node.getProperty('markupLanguage').string) {
                        def parser = new MarkupParser(new TextileLanguage())
                        def content = parser.parseToHtml(node.getProperty('content').string).replaceAll(/(?i)\<\?.*\?\>|\<meta.*\/\>/, '')
                        println "Parsed confluence: ${content}"
                        contentView.text = content
                    }
                }
                else {
                    contentView.text = node.getProperty('content').string
                }
                contentView.addHyperlinkListener(new HyperlinkBrowser())
                contentView.caretPosition = 0
            }
            else {
                editorPane(editable: false, contentType: node.getProperty('contentType').string, opaque: true, border: null, id: 'contentView')
                contentView.text = node.getProperty('content').string
            }
        }
        
        add swing.hbox(border: swing.emptyBorder([10, 0, 0, 0])) {
            button(text: 'Revisions..')
            hglue()
            button(text: 'Edit', actionPerformed: {editNote(node.parent, node.name)})
        }, BorderLayout.SOUTH
    }
    
}
