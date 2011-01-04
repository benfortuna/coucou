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
public class FeedEntryTableModel extends AbstractNodeTableModel {

    private static final long serialVersionUID = 1L;
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(FeedEntryTableModel.class));

    /**
     * @param node
     * @param columns
     * @param classes
     */
    public FeedEntryTableModel(Node node) {
        super(node, new String[] {"Flags", "Title", "Source", "Last Updated"},
                new Class[] {Object.class, String.class, String.class, Date.class},
                Event.NODE_ADDED | Event.NODE_REMOVED);
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
                    if (node.hasProperty("flag")) {
                        if (node.getProperty("flag").getBoolean()) {
                            value = "*";
                        }
                    }
                    break;
                case 1:
                    value = node.getProperty("title").getString();
                    break;
                case 2:
                    if (node.hasProperty("source")) {
                        value = node.getProperty("source").getNode().getProperty("title").getString();
                    }
                    break;
                case 3:
                    if (node.hasProperty("date")) {
                        value = node.getProperty("date").getDate().getTime();
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
