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

import java.util.Enumeration;
import java.util.Vector;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class ExplorerTreeTableNode extends AbstractRepositoryTreeTableNode {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(ExplorerTreeTableNode.class));

    /**
     * @param node
     */
    public ExplorerTreeTableNode(Node node) {
        super(node);
    }

    /**
     * @param node
     * @param parent
     */
    public ExplorerTreeTableNode(Node node, TreeTableNode parent) {
        super(node, parent);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<? extends TreeTableNode> children() {
        Vector<ExplorerTreeTableNode> children = new Vector<ExplorerTreeTableNode>();
        final Node node = (Node) getUserObject();
        try {
            final NodeIterator nodes = node.getNodes();
            while (nodes.hasNext()) {
                children.add(new ExplorerTreeTableNode(nodes.nextNode(), this));
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return children.elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeTableNode getChildAt(int index) {
        TreeTableNode childNode = null;
        final Node node = (Node) getUserObject();
        try {
            final NodeIterator nodes = node.getNodes();
            nodes.skip(index);
            childNode = new ExplorerTreeTableNode(nodes.nextNode(), this);
        } catch (RepositoryException e) {
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
        Node node = (Node) getUserObject();
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
