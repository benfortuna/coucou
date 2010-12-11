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
package org.mnode.coucou;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.JTable;

import ca.odell.glazedlists.TreeList;

import com.ocpsoft.pretty.time.PrettyTime;

/**
 * @author Ben
 *
 */
public class DateCellRenderer extends DefaultNodeTableCellRenderer {

    private static final long serialVersionUID = 1L;
    
    private final PrettyTime df = new PrettyTime();
    
    public DateCellRenderer(TreeList<Map<String, ?>> items) {
        super(items, new ArrayList<String>());
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Date) {
            setText(df.format((Date) value));
        }
        return this;
    }
}
