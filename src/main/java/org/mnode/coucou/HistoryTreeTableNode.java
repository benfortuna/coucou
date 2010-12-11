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
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class HistoryTreeTableNode extends AbstractRepositoryTreeTableNode<TreeTableNode> {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(HistoryTreeTableNode.class));

    /**
     * @param node
     */
    public HistoryTreeTableNode(Node node) {
        super(node);
    }

    /**
     * @param node
     * @param parent
     */
    public HistoryTreeTableNode(Node node, TreeTableNode parent) {
        super(node, parent);
    }

    @Override
    public int getColumnCount() {
        return 5;
    }
    
    @Override
    protected TreeTableNode createChildNode(Node node) {
        TreeTableNode childNode = null;
        try {
            if (node.hasNode("query")) {
                childNode = new HistoryQueryTreeTableNode(node, this);
            }
            else {
                childNode = new HistoryTreeTableNode(node, this);
            }
        }
        catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return childNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int column) {
        Object value = null;
        final Node node = (Node) getUserObject();
        try {
            final Node headers = node.hasNode("headers") ? node.getNode("headers") : null;
            switch(column) {
                case 0:
                    if (node.hasProperty("flags")) {
                        value = node.getProperty("flags").getString();
                    }
                    else {
                        value = node.getName();
                    }
                    break;
                case 1:
                    if (headers != null && headers.hasProperty("Subject")) {
                        value = headers.getProperty("Subject").getString();
                    }
                    break;
                case 2:
                    if (headers != null && headers.hasProperty("From")) {
                        value = node.getProperty("From").getString();
                    }
                    break;
                case 3:
                    value = node.getNodes().getSize();
                    break;
                case 4:
                    if (headers != null && headers.hasProperty("Date")) {
                        value = node.getProperty("Date").getString();
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
