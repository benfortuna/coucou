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

import java.util.ArrayList;
import java.util.List;

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

    public AbstractNodeTableModel(Node node, String[] columns, int eventMask) {
        this(node, columns, null, eventMask);
    }

    public AbstractNodeTableModel(Node node, String[] columns, Class<?>[] classes, int eventMask) {
        this.node = node;
        this.columnNames = columns;
        this.columnClasses = classes;
        try {
            node.getSession().getWorkspace().getObservationManager().addEventListener(this,
                    eventMask, node.getPath(), true, null, null, false);
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
    
    protected final int getIndex(Event event, boolean isProperty) {
        int index = -1;
        try {
            Node childNode = null;
            if (isProperty) {
                childNode = node.getSession().getProperty(event.getPath()).getParent();
            }
            else {
                childNode = node.getSession().getNode(event.getPath());
            }
            // only return index of direct child node..
            if (childNode.getParent().isSame(node)) {
                // XXX: need to iterate through all child nodes to find index.. currently will always return zero.
                index = childNode.getIndex() - 1;
            }
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.EVENT_PATH_ERROR, e, event);
        }
        return index;
    }
    
    @Override
    public void onEvent(final EventIterator events) {
        final List<Integer> addedIndicies = new ArrayList<Integer>();
        final List<Integer> removedIndicies = new ArrayList<Integer>();
        final List<Integer> changedIndicies = new ArrayList<Integer>();
        while (events.hasNext()) {
            Event e = events.nextEvent();
            if (e.getType() == Event.NODE_ADDED) {
                int index = getIndex(e, false);
                if (index >= 0) {
                    addedIndicies.add(index);
                }
            }
            else if (e.getType() == Event.NODE_REMOVED) {
                int index = getIndex(e, false);
                if (index >= 0) {
                    removedIndicies.add(index);
                }
            }
            else if (e.getType() == Event.PROPERTY_ADDED || e.getType() == Event.PROPERTY_CHANGED) {
                int index = getIndex(e, true);
                if (index >= 0) {
                    changedIndicies.add(index);
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!addedIndicies.isEmpty()) {
//                    Collections.sort(addedIndicies);
//                    fireTableRowsInserted(addedIndicies.get(0), addedIndicies.get(addedIndicies.size() - 1));
                    fireTableDataChanged();
                }
                if (!removedIndicies.isEmpty()) {
//                    Collections.sort(removedIndicies);
//                    fireTableRowsDeleted(removedIndicies.get(0), removedIndicies.get(removedIndicies.size() - 1));
                    fireTableDataChanged();
                }
                if (!changedIndicies.isEmpty()) {
//                    Collections.sort(changedIndicies);
//                    fireTableRowsUpdated(changedIndicies.get(0), changedIndicies.get(changedIndicies.size() - 1));
                    fireTableDataChanged();
                }
            }
        });
    }
}
