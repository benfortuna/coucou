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
import org.jdesktop.swingx.treetable.DefaultTreeTableModelimport javax.swing.JSplitPaneimport javax.swing.ListSelectionModelimport javax.swing.JOptionPaneimport org.jdesktop.swingx.decorator.HighlighterFactoryimport org.mnode.ousia.OusiaBuilder;

import javax.swing.table.DefaultTableModel

/**
 * @author Ben
 *
 */
public class ExplorerView extends JXPanel{
    
    private static final def EMPTY_TABLE_MODEL = new DefaultTableModel()

    ExplorerView(def rootNode, def parentWindow, def editContext) {
        def swing = new OusiaBuilder()
        
        layout = swing.borderLayout()
        name = 'Repository Explorer'
        border = swing.emptyBorder(10)
        
        add swing.splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true) {
            scrollPane(constraints: 'left') {
                treeTable(id: 'explorerTree')
//                explorerTree.treeTableModel = new RepositoryTreeTableModel(node)
                explorerTree.treeTableModel = new DefaultTreeTableModel(new ExplorerTreeTableNode(rootNode), ['Name', 'Type', 'State'])
                explorerTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
                explorerTree.selectionModel.valueChanged = {
                    def selectedPath = explorerTree.getPathForRow(explorerTree.selectedRow)
                    if (selectedPath) {
                        swing.edt {
                            propertyTable.model = new PropertiesTableModel(selectedPath.lastPathComponent.userObject)
                            editContext.delete = {
                                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parentWindow, "Delete node: ${selectedPath.lastPathComponent.userObject.name}?", 'Confirm delete', JOptionPane.OK_CANCEL_OPTION)) {
                                    explorerTree.clearSelection()
//                                    def removedIndices = [explorerTree.treeTableModel.getIndexOfChild(selectedPath.lastPathComponent.parent, selectedPath.lastPathComponent)]
                                    removeNode selectedPath.lastPathComponent.userObject
//                                    println removedIndices
                                    swing.edt {
//                                                explorerTree.treeTableModel.fireTreeNodesRemoved(explorerTree, selectedPath.parentPath.path, removedIndices as int[], [selectedPath.lastPathComponent] as Object[])
                                        explorerTree.treeTableModel.reload() //selectedPath.parentPath.lastPathComponent)
                                    }
                                }
                            }
                        }
//                        editContext.enabled = true
                    }
                    else {
                        swing.edt {
                            propertyTable.model = EMPTY_TABLE_MODEL
//                        editContext.enabled = false
                            editContext.delete = null
                        }
                    }
                }
                explorerTree.packAll()
            }
            scrollPane(constraints: 'right') {
                table(showHorizontalLines: false, id: 'propertyTable')
//                propertyTable.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY))
            }
        }
    }
    
}
