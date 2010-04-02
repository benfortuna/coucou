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

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SwingConstants;

/**
 * @author Ben
 *
 */
public class ActivityListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    public ActivityListCellRenderer() {
        setIconTextGap(10);
        setVerticalAlignment(SwingConstants.CENTER);
        //alignmentY = 0.5
        setVerticalTextPosition(SwingConstants.TOP);
//        border = BorderFactory.createEmptyBorder(2, 5, 2, 0)
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        Node node = (Node) value;
        try {
            setText(new ActivityStringBuilder().author(node.getProperty("source").getString())
                    .subject(node.getProperty("subject").getString())
                    .time(node.getProperty("date").getDate().getTime()).build());
        } catch (ValueFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PathNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return this;
    }
}
