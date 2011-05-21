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
/**
 * 
 */
package org.mnode.coucou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.apache.jackrabbit.util.Text;

/**
 * @author fortuna
 *
 */
public class NodePathResult extends AbstractPathResult<Node, Node> {

	private static final Comparator<PathResult<?, ?>> COMPARATOR = new PathResultComparator();
	
	public NodePathResult(Node node) {
		this(node, null);
	}
	
	public NodePathResult(Node node, PathResultContext context) {
		super(node, null, context);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() throws PathResultException {
		try {
			return Text.unescapeIllegalJcrChars(getElement().getName());
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		try {
			final NodeIterator childNodes = getElement().getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					children.add(getChild(node));
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		
		Collections.sort(children, COMPARATOR);
		
		return children;
	}
	
	@Override
	public PathResult<?, ?> getChild(Node result) throws PathResultException {
		return new NodePathResult(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator childNodes = getElement().getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					results.add(node);
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}
}
