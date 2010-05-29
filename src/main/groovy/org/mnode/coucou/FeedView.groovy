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

import org.jdesktop.swingx.JXPanel
import groovy.swing.SwingXBuilderimport javax.swing.JSplitPaneimport org.jdesktop.swingx.decorator.HighlighterFactoryimport ca.odell.glazedlists.swing.EventTableModelimport javax.swing.SortOrderimport java.awt.event.MouseEventimport org.mnode.base.desktop.HyperlinkListenerImplimport java.awt.Desktop

/**
 * @author Ben
 *
 */
public class FeedView extends JXPanel {

    def markNodeRead
    
    FeedView(def node, def resultList, def editContext, def defaultEditorKit) {
        def swing = new SwingXBuilder()
        
        layout = swing.borderLayout()
        name = node.getProperty('title').string
        border = swing.emptyBorder(10)

        def entryListModel = new EventTableModel(resultList, new FeedEntryTableFormat())
        
        add swing.splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true) {
            def contentView
            scrollPane(constraints: 'left') {
//                def entryList = list()
//                entryList.model = new RepositoryListModel(node)
//                entryList.cellRenderer = new FeedViewListCellRenderer()
                table(showHorizontalLines: false, id: 'entryList')
                entryList.addHighlighter(simpleStripingHighlighter(stripeBackground: HighlighterFactory.GENERIC_GRAY))
                entryList.setDefaultRenderer(String, new DefaultNodeTableCellRenderer(node))
                entryList.setDefaultRenderer(Date, new DateCellRenderer(node))
//                entryList.model = new FeedEntryTableModel(node)
                entryList.model = entryListModel
//                EventListJXTableSorting.install(entryList, sortedList)
                entryList.setSortOrder(3, SortOrder.DESCENDING)
                entryList.sortsOnUpdates = true
                
                // XXX: need to remove from filterableLists on tab close..
//                filterableLists << entryList
                entryList.selectionModel.valueChanged = { e ->
                    if (!e.valueIsAdjusting) {
//                    if (entryList.selectedValue && entryList.selectedValue.hasProperty('description')) {
//                        contentView.text = entryList.selectedValue.getProperty('description').value.string
                        if (entryList.selectedRow >= 0) {
                            def entries = node.nodes
                            entries.skip(entryList.convertRowIndexToModel(entryList.selectedRow))
                            def entry = entries.nextNode()
                            swing.edt {
                                if (entry.hasProperty('description')) {
//                                        println "Entry selected: ${entryList.model[entryList.selectedRow]}"
                                    def content = entry.getProperty('description').string //.replaceAll(/(http:\/\/)?([a-zA-Z0-9\-.]+\.[a-zA-Z0-9\-]{2,}([\/]([a-zA-Z0-9_\/\-.?&%=+])*)*)(\s+|$)/, '<a href="http://$2">$2</a> ')
                                    contentView.text = content
                                    contentView.caretPosition = 0
                                }
                                else {
                                    contentView.text = null
                                }
                                editContext.markAsRead = {
                                    markNodeRead(entry, true)
                                    if (entryList.selectedRow < entryList.rowCount - 1) {
                                        entryList.setRowSelectionInterval(entryList.selectedRow + 1, entryList.selectedRow + 1)
                                    }
                                }
                                editContext.markAsUnread = {
                                    markNodeRead(entry, false)
                                    if (entryList.selectedRow < entryList.rowCount - 1) {
                                        entryList.setRowSelectionInterval(entryList.selectedRow + 1, entryList.selectedRow + 1)
                                    }
                                }
                                editContext.markAllRead = {
                                    markNodeRead(node, true)
//                                            swing.edt {
//                                                entryList.model.fireTableDataChanged()
//                                            }
                                }
                                editContext.flag = {
                                    def flag = true
                                    if (entry.hasProperty('flag')) {
                                        flag = !entry.getProperty('flag').boolean
                                    }
                                    entry.setProperty('flag', flag)
                                    entry.save()
                                }
                            }
//                            editContext.markAsReadEnabled = true
                        }
                        else {
                            swing.edt {
                                contentView.text = null
//                                        editContext.markAsReadEnabled = false
                                editContext.markAsRead = null
                                editContext.markAsUnread = null
                                editContext.markAllRead = null
                                editContext.flag = null
                            }
                        }
                    }
                }
                entryList.mouseClicked = { e ->
                    if (e.button == MouseEvent.BUTTON1 && e.clickCount >= 2 && entryList.selectedRow >= 0) {
//                        if (entryList.selectedRow >= 0 && entryList.model[entryList.selectedRow].hasProperty('link')) {
//                            Desktop.desktop.browse(URI.create(entryList.model[entryList.selectedRow].getProperty('link').value.string))
//                        }
                        def entries = node.nodes
                        entries.skip(entryList.convertRowIndexToModel(entryList.selectedRow))
                        def entry = entries.nextNode()
                        if (entry.hasProperty('link')) {
                            Desktop.desktop.browse(URI.create(entry.getProperty('link').value.string))
                            entry.setProperty('seen', true)
                            entry.save()
                        }
                    }
                }
                entryList.focusLost = { e ->
                    if (e.oppositeComponent != contentView) {
                        entryList.clearSelection()
                    }
                }
                
                // XXX: temporary..
                putClientProperty('entryList', entryList)
            }

            scrollPane(constraints: 'right') {
                contentView = editorPane(editorKit: defaultEditorKit, editable: false, contentType: 'text/html', opaque: true, border: null)
            }
            contentView.addHyperlinkListener(new HyperlinkListenerImpl())
            contentView.focusLost = { e ->
                if (e.oppositeComponent != entryList) {
                    entryList.clearSelection()
                }
            }
            contentView.mouseClicked = { e ->
                if (e.popupTrigger) {
                    def popupLocation = contentView.getPopupLocation(e)
                    contentView.componentPopupMenu.show(contentView, popupLocation.x, popupLocation.y)
                }
            }
            
            /*
            contentView.componentPopupMenu = popupMenu() {
                menuItem(internetSearchAction)
                separator()
                menuItem(text: 'View Source')//, action: { frame(title: title, size: [430, 400], show: true, locationRelativeTo: coucouFrame) } as Action)
            }
            */
        }
        
    }
    
}
