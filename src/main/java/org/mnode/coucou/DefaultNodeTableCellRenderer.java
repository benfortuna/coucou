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
package org.mnode.coucou;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import ca.odell.glazedlists.TreeList;

/**
 * @author Ben
 *
 */
public class DefaultNodeTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6498301743998728522L;
    
    private final Font defaultFont;
    private final Font unreadFont;
    
    private final Color defaultForeground;
    private final Color nonItemForeground;
    private final Color flaggedBackground;
    private final Color defaultBackground;
    
//    private final Node parent;
    private final TreeList<Map<String, ?>> items;
    
    private final List<String> groupNames;
    
    public DefaultNodeTableCellRenderer(TreeList<Map<String, ?>> items, List<String> groupNames) {
        defaultFont = getFont();
        unreadFont = getFont().deriveFont(Font.BOLD);
        defaultForeground = Color.BLACK;
        nonItemForeground = Color.LIGHT_GRAY;
        flaggedBackground = new Color(255, 255, 0, 32);
        defaultBackground = Color.WHITE;
//        this.parent = parent;
        this.items = items;
        this.groupNames = groupNames;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

		setForeground(defaultForeground);
    	setBackground(defaultBackground);
		setFont(defaultFont);
    	
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try {
//        	if (Arrays.asList(null, "", "Today", "Older Items").contains(value)) {
//        	if (Arrays.asList("Today", "Yesterday", "Older Items").contains(value)) {
        	if (column == 0 && groupNames.contains(value)) {
        		setForeground(nonItemForeground);
        	}
        	else {
//              Node node = ((AbstractNodeTableModel) table.getModel()).getNodeAt(table.convertRowIndexToModel(row));
//                NodeIterator nodes = parent.getNodes();
//                nodes.skip(table.convertRowIndexToModel(row));
//                Node node = nodes.nextNode();
        		Node node = (Node) items.get(table.convertRowIndexToModel(row)).get("node");
                if (node.hasProperty("seen") && !node.getProperty("seen").getBoolean()) {
                    setFont(unreadFont);
                }
                
                if (!isSelected && node.hasProperty("flagged") && node.getProperty("flagged").getBoolean()) {
                	setBackground(flaggedBackground);
                }
        	}
        }
        catch (Exception e) {
            setFont(defaultFont);
    		setForeground(defaultForeground);
        }
        return this;
    }
}
