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

import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * @author Ben
 *
 */
public class HistoryQueryTreeTableNode extends AbstractQueryTreeTableNode<HistoryTreeTableNode> {

    /**
     * @param node
     */
    public HistoryQueryTreeTableNode(Node node) {
        super(node);
    }

    /**
     * @param node
     * @param parent
     */
    public HistoryQueryTreeTableNode(Node node, TreeTableNode parent) {
        super(node, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected HistoryTreeTableNode createChildNode(Node node) {
        return new HistoryTreeTableNode(node, this);
    }

}
