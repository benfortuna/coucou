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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public abstract class AbstractQueryTreeTableNode implements TreeTableNode {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractQueryTreeTableNode.class));
    
    private final Query query;
    
    private final TreeTableNode parent;
    
    private List<Node> result;
    
    /**
     * @param query
     */
    public AbstractQueryTreeTableNode(Query query) {
        this(query, null);
    }
    
    /**
     * @param query
     * @param parent
     */
    public AbstractQueryTreeTableNode(Query query, TreeTableNode parent) {
        this.query = query;
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeTableNode getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getUserObject() {
        return query;
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
        return getNodes().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(TreeNode treeNode) {
        int index = -1;
        List<Node> nodes = getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            try {
                if (node.isSame((Node) ((TreeTableNode) treeNode).getUserObject())) {
                    index = i;
                    break;
                }
            } catch (RepositoryException e) {
                LOG.log(LogEntries.NODE_ERROR, e, node);
            }
        }
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf() {
        return getNodes().isEmpty();
    }

    protected final List<Node> getNodes() {
        if (result == null) {
            try {
                final NodeIterator nodes = query.execute().getNodes();
                result = new CopyOnWriteArrayList<Node>();
                while (nodes.hasNext()) {
                    Node nextNode = nodes.nextNode();
                    result.add(nextNode);
                }
            } catch (RepositoryException e) {
                LOG.log(LogEntries.QUERY_ERROR, e, query);
            }
        }
        return result;
    }
    
    protected final void reset() {
        result = null;
    }
}
