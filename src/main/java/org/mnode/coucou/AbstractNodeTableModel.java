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
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public abstract class AbstractNodeTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -6466346860232504839L;

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractNodeTableModel.class));
    
    private final Node node;
    
    private final String[] columnNames;
    
    public AbstractNodeTableModel(Node node, String[] columns) {
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
    public int getRowCount() {
        try {
            return (int) node.getNodes().getSize();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, node, e.getMessage());
        }
        return 0;
    }

    protected final Node getNodeAt(int index) throws RepositoryException {
        NodeIterator nodes = node.getNodes();
        nodes.skip(index);
        return nodes.nextNode();
    }

    protected final Property getPropertyAt(int index) throws RepositoryException {
        PropertyIterator properties = node.getProperties();
        properties.skip(index);
        return properties.nextProperty();
    }
}
