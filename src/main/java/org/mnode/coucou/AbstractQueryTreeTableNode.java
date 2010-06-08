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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @param <T> the child node type
 * 
 * @author Ben
 *
 */
public abstract class AbstractQueryTreeTableNode<T extends TreeTableNode> implements TreeTableNode {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(AbstractQueryTreeTableNode.class));
    
    private final Node node;
    
    private final Query query;
    
    private final TreeTableNode parent;
    
    private List<Node> result;
    
    private Map<String, Value> bindVariables = new HashMap<String, Value>();
    
    /**
     * @param query
     */
    public AbstractQueryTreeTableNode(Node node) {
        this(node, null);
    }
    
    /**
     * @param query
     * @param parent
     */
    public AbstractQueryTreeTableNode(Node node, TreeTableNode parent) {
        this.node = node;
        this.query = createQuery(node);
        this.parent = parent;
    }

    private Query createQuery(Node node) {
        Query query = null;
        try {
            if (node.hasNode("query")) {
                query = node.getSession().getWorkspace().getQueryManager().getQuery(node.getNode("query"));
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return query;
    }

    protected abstract T createChildNode(Node node);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final Enumeration<? extends TreeTableNode> children() {
        Vector<T> children = new Vector<T>();
        for (Node node : getNodes()) {
            children.add(createChildNode(node));
        }
        return children.elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreeTableNode getChildAt(int index) {
        return createChildNode(getNodes().get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getColumnCount() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getValueAt(int column) {
        Object value = null;
        try {
            value = node.getName();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return value;
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
                for (String varName : bindVariables.keySet()) {
                    query.bindValue(varName, bindVariables.get(varName));
                }
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

    /**
     * @return the bindVariables
     */
    public Map<String, Value> getBindVariables() {
        return bindVariables;
    }

    /**
     * @param bindVariables the bindVariables to set
     */
    public void setBindVariables(Map<String, Value> bindVariables) {
        this.bindVariables = bindVariables;
    }
}
