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

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class PropertiesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 3908266956269667969L;

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(PropertiesTableModel.class));

    private static final String[] COLUMN_NAMES = {"Property Name", "Type", "Value"};
    
    private final Node node;
    
    /**
     * 
     */
    public PropertiesTableModel(Node node) {
        this.node = node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        int rowCount = 0;
        try {
            rowCount = (int) node.getProperties().getSize();
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return rowCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        try {
            PropertyIterator props = node.getProperties();
            props.skip(rowIndex);
            Property prop = props.nextProperty();
            switch(columnIndex) {
                case 0:
                    value = prop.getName();
                    break;
                case 1:
                    if (prop.isMultiple()) {
                        // XXX: concat prop types..
                    }
                    else {
                        value = PropertyType.nameFromValue(prop.getValue().getType());
                    }
                    break;
                case 2:
                    if (prop.isMultiple()) {
                        // XXX: concat prop values..
                    }
                    else {
                        value = prop.getString();
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
