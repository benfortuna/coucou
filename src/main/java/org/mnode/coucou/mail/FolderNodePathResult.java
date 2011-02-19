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
import javax.mail.Folder;

import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

/**
 * @author fortuna
 *
 */
public class FolderNodePathResult extends NodePathResult {

	private Query attachmentsQuery;
	
	/**
	 * @param node
	 */
	public FolderNodePathResult(Node node) {
		super(node);
		try {
			attachmentsQuery = node.getSession().getWorkspace().getQueryManager().createQuery(
					String.format("SELECT * FROM [nt:file] AS files WHERE ISDESCENDANTNODE(files, [%s]) AND (NOT NAME(files) = 'part') AND (NOT NAME(files) = 'data')", node.getPath()), Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() throws PathResultException {
		try {
			return getElement().getProperty("folderName").getString();
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
	
	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		final List<PathResult<?, Node>> children = new ArrayList<PathResult<?, Node>>();
		
		if (attachmentsQuery != null) {
			children.add(new SearchPathResult(attachmentsQuery, "Attachments"));
		}
		
		try {
			if ((Folder.HOLDS_FOLDERS & getElement().getProperty("type").getLong()) > 0) {
				// add folders..
				final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
				while (folderNodes.hasNext()) {
					final Node node = folderNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						children.add(new FolderNodePathResult(node));
					}
				}
			}
			else if ((Folder.HOLDS_MESSAGES & getElement().getProperty("type").getLong()) > 0) {
				// add messages..
				final NodeIterator messageNodes = getElement().getNode("messages").getNodes();
				while (messageNodes.hasNext()) {
					final Node node = messageNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						children.add(getChild(node));
					}
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
		try {
			if ("messages".equals(result.getParent().getName())) {
				return new MessageNodePathResult(result);
			}
			else if ("folders".equals(result.getParent().getName())) {
				return new FolderNodePathResult(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return super.getChild(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			if ((Folder.HOLDS_MESSAGES & getElement().getProperty("type").getLong()) > 0) {
				// add messages..
				final NodeIterator messageNodes = getElement().getNode("messages").getNodes();
				while (messageNodes.hasNext()) {
					final Node node = messageNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						results.add(node);
					}
				}
			}
			else if ((Folder.HOLDS_FOLDERS & getElement().getProperty("type").getLong()) > 0) {
				// add folders..
				final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
				while (folderNodes.hasNext()) {
					final Node node = folderNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						results.add(node);
					}
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}
}
