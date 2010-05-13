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
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public abstract class AbstractRepositoryTreeTableNode implements TreeTableNode {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractRepositoryTreeTableNode.class));

    private final Node node;
    
    private final TreeTableNode parent;
    
    /**
     * @param node
     */
    public AbstractRepositoryTreeTableNode(Node node) {
        this(node, null);
    }
    
    /**
     * @param node
     * @param parent
     */
    public AbstractRepositoryTreeTableNode(Node node, TreeTableNode parent) {
        this.node = node;
        this.parent = parent;
    }

    @Override
    public TreeTableNode getParent() {
        return parent;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUserObject() {
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditable(int column) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserObject(Object arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAt(Object arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount() {
        int size = 0;
        try {
            size = (int) node.getNodes().getSize();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(TreeNode treeNode) {
        int index = -1;
        try {
            final NodeIterator nodes = node.getNodes();
            while (nodes.hasNext()) {
                Node nextNode = nodes.nextNode();
                if (nextNode.isSame((Node) ((TreeTableNode) treeNode).getUserObject())) {
                    index = (int) nodes.getPosition();
                    break;
                }
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf() {
        boolean leaf = false;
        try {
            leaf = !node.hasNodes();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return leaf;
    }

}
