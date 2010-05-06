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
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public abstract class AbstractNodeTableModel extends AbstractTableModel implements EventListener {

    private static final long serialVersionUID = -6466346860232504839L;

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractNodeTableModel.class));
    
    private final Node node;
    
    private final String[] columnNames;
    
    private final Class<?>[] columnClasses;

    public AbstractNodeTableModel(Node node, String[] columns) {
        this(node, columns, null);
    }

    public AbstractNodeTableModel(Node node, String[] columns, Class<?>[] classes) {
        this.node = node;
        this.columnNames = columns;
        this.columnClasses = classes;
        try {
            node.getSession().getWorkspace().getObservationManager().addEventListener(this,
                    Event.NODE_ADDED | Event.NODE_REMOVED | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED,
                    node.getPath(), true, null, null, false);
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
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
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnClasses != null && columnIndex < columnClasses.length) {
            return columnClasses[columnIndex];
        }
        return super.getColumnClass(columnIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        try {
            return (int) node.getNodes().getSize();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
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
    
    @Override
    public void onEvent(EventIterator events) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        });
    }
}
