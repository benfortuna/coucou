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

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public abstract class AbstractNodeTreeTableModel extends AbstractTreeTableModel {

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractNodeTableModel.class));
    
    private final Node node;
    
    private final String[] columnNames;
    
    public AbstractNodeTreeTableModel(Node node, String[] columns) {
        super(node);
        this.node = node;
        this.columnNames = columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getChild(Object parent, int index) {
        try {
            NodeIterator nodes = ((Node) parent).getNodes();
            nodes.skip(index);
            return nodes.nextNode();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, node, e.getMessage());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount(Object parent) {
        try {
            return (int) ((Node) parent).getNodes().getSize();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, node, e.getMessage());
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        try {
            NodeIterator nodes = ((Node) parent).getNodes();
            while (nodes.hasNext()) {
                if (nodes.nextNode().equals(child)) {
                    return (int) nodes.getPosition();
                }
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, node, e.getMessage());
        }
        return 1;
    }

    @Override
    public boolean isLeaf(Object node) {
        try {
            return !((Node) node).hasNodes();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, node, e.getMessage());
        }
        return true;
    }
}
