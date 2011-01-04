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
public class PlannerTreeTableNode extends AbstractRepositoryTreeTableNode<TreeTableNode> {
    
    private static final LogAdapter LOG = new JclAdapter(LogFactory.getLog(PlannerTreeTableNode.class));

    /**
     * @param node
     */
    public PlannerTreeTableNode(Node node) {
        super(node);
    }

    /**
     * @param node
     * @param parent
     */
    public PlannerTreeTableNode(Node node, TreeTableNode parent) {
        super(node, parent);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    protected TreeTableNode createChildNode(Node node) {
        TreeTableNode childNode = null;
        try {
            if (node.hasNode("query")) {
                childNode = new PlannerQueryTreeTableNode(node, this);
            }
            else {
                childNode = new PlannerTreeTableNode(node, this);
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
        Node node = (Node) getUserObject();
        try {
            switch(column) {
                case 0:
                    if (node.hasProperty("summary")) {
                        value = node.getProperty("summary").getString();
                    }
                    else {
                        value = node.getName();
                    }
                    break;
                case 1:
                    if (node.hasProperty("organiser")) {
                        value = node.getProperty("organiser").getString();
                    }
                    break;
                case 2:
                    if (node.hasProperty("categories")) {
                        value = node.getProperty("categories").getDate().getTime();
                    }
                    break;
                case 3:
                    if (node.hasProperty("due")) {
                        value = node.getProperty("due").getDate().getTime();
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
