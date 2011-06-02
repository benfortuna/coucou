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

import org.jdesktop.swingx.treetable.MutableTreeTableNode;

/**
 * @param <T> the child node type
 * 
 * @author Ben
 *
 */
public abstract class AbstractMutableRepositoryTreeTableNode<T extends MutableTreeTableNode>
	extends AbstractRepositoryTreeTableNode<T> implements MutableTreeTableNode {
    
    /**
     * @param node
     */
    public AbstractMutableRepositoryTreeTableNode(Node node) {
        super(node);
    }
    
    /**
     * @param node
     * @param parent
     */
    public AbstractMutableRepositoryTreeTableNode(Node node, T parent) {
    	super(node, parent);
    }

    protected abstract T createChildNode(Node node);
    
    @Override
    public void insert(MutableTreeTableNode paramMutableTreeTableNode, int paramInt) {
    	throw new UnsupportedOperationException("Method not supported");
    }
    
    @Override
    public void remove(int paramInt) {
    	remove((MutableTreeTableNode) getChildAt(paramInt));
    }
    
    @Override
    public void remove(MutableTreeTableNode paramMutableTreeTableNode) {
    	try {
    		final Node node = (Node) paramMutableTreeTableNode.getUserObject();
			node.remove();
			node.getSession().save();
		} catch (RepositoryException e) {
			throw new DataException(e);
		}
    }
    
    @Override
    public void removeFromParent() {
    	getParent().remove(this);
    }
    
    @Override
    public void setParent(MutableTreeTableNode paramMutableTreeTableNode) {
    	throw new UnsupportedOperationException("Method not supported");
    }
    
    
}
