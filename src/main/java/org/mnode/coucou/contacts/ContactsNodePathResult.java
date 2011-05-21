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
package org.mnode.coucou.contacts;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.mnode.coucou.DataException;
import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultContext;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

public class ContactsNodePathResult extends NodePathResult {

	private final Query allContactsQuery;
	
	public ContactsNodePathResult(Node node, PathResultContext context) {
		super(node, context);
		try {
			allContactsQuery = node.getSession().getWorkspace().getQueryManager().createQuery(
				String.format("SELECT * FROM [nt:unstructured] AS contacts WHERE ISDESCENDANTNODE(contacts, ['%s']) AND contacts.personal IS NOT NULL", node.getPath()),
				Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			throw new DataException(e);
		}
	}
	
	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		
		if (allContactsQuery != null) {
			children.add(new SearchPathResult(allContactsQuery, "All Contacts"));
		}
		
		try {
			final NodeIterator accountNodes = getElement().getNode("accounts").getNodes();
			while (accountNodes.hasNext()) {
				final Node accountNode = accountNodes.nextNode();
				children.add(getChild(accountNode));
			}
			return children;
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
	}
	
	@Override
	public PathResult<?, ?> getChild(Node result) throws PathResultException {
		try {
			if (result.getParent().getName().equals("accounts")) {
				final List<XMPPConnection> xmppConnections = getContext().getSharedValue("xmppConnections");
//				final Roster roster = getContext().getSharedValue(String.format("roster.%s", result.getName()));
				final Roster roster = xmppConnections.get(0).getRoster();
				return new RosterPathResult(roster, result.getName(), getElement().getNode("conversations"));
			}
			else {
				return super.getChild(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
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
