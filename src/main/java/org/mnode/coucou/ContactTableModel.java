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

import java.util.Date;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class ContactTableModel extends AbstractNodeTableModel {

    private static final long serialVersionUID = 1L;
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(ContactTableModel.class));

    /**
     * @param node
     */
    public ContactTableModel(Node node) {
        super(node, new String[] {"Name", "Organisation", "Telephone", "Mobile", "Email"},
                new Class[] {String.class, String.class, Date.class},
                Event.NODE_ADDED | Event.NODE_REMOVED | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        Node node = null;
        try {
            node = getNodeAt(rowIndex);
            switch(columnIndex) {
                case 0:
                    if (node.hasProperty("fullName")) {
                        value = node.getProperty("fullName").getString();
                    }
                    else {
                        value = node.getName();
                    }
                    break;
                case 1:
                    if (node.hasProperty("org")) {
                        value = node.getProperty("org").getString();
                    }
                    break;
                case 2:
                    if (node.hasProperty("mainPhone")) {
                        value = node.getProperty("mainPhone").getString();
                    }
                    break;
                case 3:
                    if (node.hasProperty("mobile")) {
                        value = node.getProperty("mobile").getString();
                    }
                    break;
                case 4:
                    if (node.hasProperty("email")) {
                        value = node.getProperty("email").getString();
                    }
                    break;
            }
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return value;
    }

}
