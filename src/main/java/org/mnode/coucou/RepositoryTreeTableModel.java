/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
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
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class RepositoryTreeTableModel extends AbstractNodeTreeTableModel {

    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(RepositoryTreeTableModel.class));
    
    /**
     * @param node
     */
    public RepositoryTreeTableModel(Node node) {
        super(node, new String[] {"Name", "Type", "State"});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(Object treeNode, int column) {
        String value = null;
        Node node = (Node) treeNode;
        try {
            switch(column) {
                case 0:
                    value = node.getName();
                    break;
                case 1:
                    value = node.getPrimaryNodeType().getName();
                    break;
                case 2:
                    value = node.isNew() ? "N" : node.isModified() ? "M" : null;
                    break;
            }
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return value;
    }

}
