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

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class NoteTableModel extends AbstractNodeTableModel {

    private static final long serialVersionUID = 1L;
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(NoteTableModel.class));

    /**
     * @param node
     */
    public NoteTableModel(Node node) {
        super(node, new String[] {"Title", "Tags", "Last Modified"},
                new Class[] {String.class, String.class, Date.class});
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
                    if (node.hasProperty("title")) {
                        value = node.getProperty("title").getString();
                    }
                    else {
                        value = node.getName();
                    }
                    break;
                case 1:
                    if (node.hasProperty("tags")) {
                        value = node.getProperty("tags").getString();
                    }
                    break;
                case 2:
                    if (node.hasProperty("lastModified")) {
                        value = node.getProperty("lastModified").getDate().getTime();
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
