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

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
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

    private final Color defaultBackground;
    
    public ActivityListCellRenderer() {
        setIconTextGap(10);
        setVerticalAlignment(SwingConstants.CENTER);
        //alignmentY = 0.5
        setVerticalTextPosition(SwingConstants.TOP);
//        border = BorderFactory.createEmptyBorder(2, 5, 2, 0)
        int r = getBackground().getRed();
        int g = getBackground().getGreen();
        int b = getBackground().getBlue();
        defaultBackground = new Color(r, g, b, 128);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (!isSelected) {
            setBackground(defaultBackground);
        }
        
        Node node = (Node) value;
        try {
            ActivityStringBuilder b = new ActivityStringBuilder();
            if (node.hasProperty("source")) {
                Property source = node.getProperty("source");
                if (source.getType() == PropertyType.REFERENCE || source.getType() == PropertyType.WEAKREFERENCE) {
                    b.author(source.getNode().getProperty("title").getString());
                }
                else {
                    b.author(source.getString());
                }
            }
            else {
                b.author("");
            }
            
            if (node.hasProperty("subject")) {
                b.subject(node.getProperty("subject").getString());
            }
            else if (node.hasProperty("title")) {
                b.subject(node.getProperty("title").getString());
            }
            else {
                b.subject("");
            }
            
            if (node.hasProperty("date")) {
                b.time(node.getProperty("date").getDate().getTime());
            }
            else {
                b.time(new Date());
            }
            setText(b.build());
            
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
