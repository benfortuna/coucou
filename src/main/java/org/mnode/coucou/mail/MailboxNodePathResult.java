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
package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;

import org.apache.jackrabbit.util.Text;
import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

/**
 * @author fortuna
 *
 */
public class MailboxNodePathResult extends NodePathResult {

	private Query receivedItemsQuery;
//	private Query sentItemsQuery;
//	private Query archivedItemsQuery;
//	private Query deletedItemsQuery;

	/**
	 * @param node
	 */
	public MailboxNodePathResult(Node node) {
		super(node);
		try {
			receivedItemsQuery = node.getSession().getWorkspace().getQueryManager().createQuery(
				String.format("SELECT messages.* FROM [nt:unstructured] AS messages INNER JOIN [nt:unstructured] AS headers ON ISCHILDNODE(headers, messages) WHERE ISDESCENDANTNODE(messages, ['%s']) AND NAME(headers) = 'headers' AND headers.Received IS NOT NULL", node.getPath()),
				Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		final List<PathResult<?, Node>> children = new ArrayList<PathResult<?, Node>>();
		
		if (receivedItemsQuery != null) {
			children.add(new SearchPathResult(receivedItemsQuery, "Received Items", "messages"));
		}
		
		try {
			// add search queries..
			final NodeIterator childNodes = getElement().getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
//				if (node.isNodeType(NodeType.NT_UNSTRUCTURED) && !"folders".equals(node.getName())) {
//					children.add(new NodePathResult(node));
//				}
				if (node.hasNode("query")) {
		            final Query query = node.getSession().getWorkspace().getQueryManager().getQuery(
		            		node.getNode("query"));
					children.add(new SearchPathResult(query, Text.unescapeIllegalJcrChars(node.getName())));
				}
			}
			
			// add folders..
			final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
			while (folderNodes.hasNext()) {
				final Node node = folderNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					children.add(getChild(node));
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return children;
	}
	
	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		return new FolderNodePathResult(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
			while (folderNodes.hasNext()) {
				final Node node = folderNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					results.add(node);
				}
			}
			
			final NodeIterator accountNodes = getElement().getNode("accounts").getNodes();
			while (accountNodes.hasNext()) {
				final Node node = accountNodes.nextNode();
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
