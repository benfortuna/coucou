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
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.ocpsoft.pretty.time.PrettyTime;

/**
 * @author Ben
 *
 */
public class DateCellRenderer implements TableCellRenderer {

    private static final long serialVersionUID = 1L;
    
    private final PrettyTime df = new PrettyTime();
    
    private final DefaultTableCellRenderer parent;
    
    public DateCellRenderer(DefaultTableCellRenderer parent) {
        this.parent = parent;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        final Component renderer = parent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Date) {
            ((DefaultTableCellRenderer) renderer).setText(df.format((Date) value));
        }
        return renderer;
    }
    
    public void setBackground(Color background) {
    	parent.setBackground(background);
    }
}
