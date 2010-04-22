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

package org.mnode.coucou;

import java.awt.Component;
import java.awt.Font;

import javax.jcr.Node;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Ben
 *
 */
public class DefaultNodeTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6498301743998728522L;
    
    private final Font defaultFont;
    private final Font unreadFont;
    
    public DefaultNodeTableCellRenderer() {
        defaultFont = getFont();
        unreadFont = getFont().deriveFont(Font.BOLD);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        try {
            Node node = ((AbstractNodeTableModel) table.getModel()).getNodeAt(table.convertRowIndexToModel(row));
            if (node.hasProperty("seen") && !node.getProperty("seen").getBoolean()) {
                setFont(unreadFont);
            }
            else {
                setFont(defaultFont);
            }
        }
        catch (Exception e) {
            setFont(defaultFont);
        }
        return this;
    }
}
