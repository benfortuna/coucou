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
        swing.panel(this) {
        	borderLayout()
			name = rs('Repository Explorer')
			border = emptyBorder(10)
        
			actions {
		        action id: 'deleteNodeAction', name: rs('Delete'), closure: {
                    def selectedPath = explorerTree.getPathForRow(explorerTree.selectedRow)
					if (selectedPath 
						&& JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(parentWindow, "${rs('Delete node')}: ${selectedPath.lastPathComponent.userObject.name}?", rs('Confirm delete'), JOptionPane.OK_CANCEL_OPTION)) {
						
//                            selectedPath.lastPathComponent.userObject.remove()
//							selectedPath.lastPathComponent.userObject.session.save()
                            swing.edt {
								explorerTree.clearSelection()
//								def model = explorerTree.treeTableModel
//                                model.fireTreeNodesRemoved model, model.getPathToRoot(selectedPath.parentPath),
//									 [] as int[], [selectedPath.lastPathComponent.userObject] as Object[]  //selectedPath.parentPath.lastPathComponent)
								explorerTree.treeTableModel.removeNodeFromParent selectedPath.lastPathComponent
                            }
                    }
		        }
        	}
			
        	splitPane(orientation: JSplitPane.VERTICAL_SPLIT, dividerLocation: 200, continuousLayout: true) {
				scrollPane(constraints: 'left') {
					treeTable(id: 'explorerTree') {
						keyStrokeAction(explorerTree, actionKey: 'deleteNode', keyStroke: 'DELETE', action: deleteNodeAction)
		                explorerTree.treeTableModel = new DefaultTreeTableModel(new ExplorerTreeTableNode(rootNode), [rs('Name'), rs('Type'), rs('State')])
		                explorerTree.selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
		                explorerTree.selectionModel.valueChanged = {
							def selectedPath = explorerTree.getPathForRow(explorerTree.selectedRow)
		                    if (selectedPath) {
		                        swing.edt {
		                            propertyTable.model = new PropertiesTableModel(selectedPath.lastPathComponent.userObject)
		                        }
		                    }
		                    else {
		                        swing.edt {
		                            propertyTable.model = EMPTY_TABLE_MODEL
		                        }
		                    }
						}
						explorerTree.packAll()
					}
                }
	            scrollPane(constraints: 'right') {
	                table(showHorizontalLines: false, id: 'propertyTable')
	//                propertyTable.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY))
	            }
            }
        }
    }
    
}
