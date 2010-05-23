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

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.JclAdapter;

/**
 * @author Ben
 *
 */
public class JournalQueryTreeTableNode extends AbstractQueryTreeTableNode {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(JournalQueryTreeTableNode.class));

    private final Node node;
    
    /**
     * @param query
     */
    public JournalQueryTreeTableNode(Node node) {
        super(createQuery(node));
        this.node = node;
        try {
            if ("Today".equals(node.getName())) {
                Calendar todayCal = Calendar.getInstance();
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.clear(Calendar.MINUTE);
                todayCal.clear(Calendar.SECOND);
                todayCal.clear(Calendar.MILLISECOND);
                getBindVariables().put("minDate", node.getSession().getValueFactory().createValue(todayCal));
            }
            else if ("Yesterday".equals(node.getName())) {
                Calendar yesterdayCal = Calendar.getInstance();
                yesterdayCal.add(Calendar.DAY_OF_MONTH, -1);
                yesterdayCal.set(Calendar.HOUR_OF_DAY, 0);
                yesterdayCal.clear(Calendar.MINUTE);
                yesterdayCal.clear(Calendar.SECOND);
                yesterdayCal.clear(Calendar.MILLISECOND);
                getBindVariables().put("minDate", node.getSession().getValueFactory().createValue(yesterdayCal));
            }
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
    }

    /**
     * @param query
     * @param parent
     */
    public JournalQueryTreeTableNode(Node node, TreeTableNode parent) {
        super(createQuery(node), parent);
        this.node = node;
    }

    private static Query createQuery(Node node) {
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<? extends TreeTableNode> children() {
        Vector<JournalTreeTableNode> children = new Vector<JournalTreeTableNode>();
        for (Node node : getNodes()) {
            children.add(new JournalTreeTableNode(node, this));
        }
        return children.elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeTableNode getChildAt(int index) {
        return new JournalTreeTableNode(getNodes().get(index), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int column) {
        Object value = null;
        try {
            value = node.getName();
        } catch (RepositoryException e) {
            LOG.log(LogEntries.NODE_ERROR, e, node);
        }
        return value;
    }

}
