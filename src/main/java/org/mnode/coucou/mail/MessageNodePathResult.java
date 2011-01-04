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
package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;

import org.mnode.coucou.LeafNodePathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class MessageNodePathResult extends LeafNodePathResult {

	private Query relatedQuery;

	/**
	 * @param node
	 */
	public MessageNodePathResult(Node node) {
		super(node);
		try {
			relatedQuery = node.getSession().getWorkspace().getQueryManager().createQuery(
					String.format("SELECT * FROM [nt:unstructured] AS headers WHERE ISDESCENDANTNODE(headers, [/Mail]) AND NAME(headers) = 'headers' AND (headers.[In-Reply-To] = $messageId OR headers.References = $messageId)"), Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() throws PathResultException {
		try {
			if (getElement().hasNode("headers") && getElement().getNode("headers").hasProperty("Subject")) {
				return getElement().getNode("headers").getProperty("Subject").getString();
			}
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
		return super.getName();
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		results.add(getElement());
		// show related messages..
		if (relatedQuery != null) {
			try {
				findRelated(results, getElement(), "Message-ID");
//				findRelated(results, getElement(), "In-Reply-To");
//				findRelated(results, getElement(), "References");
			}
			catch (RepositoryException re) {
				throw new PathResultException(re);
			}
		}
		return results;
	}
	
	private void findRelated(List<Node> results, Node node, String header) throws RepositoryException {
		if (node.getNode("headers").hasProperty(header)) {
			final Value messageId = node.getSession().getValueFactory().createValue(
					node.getNode("headers").getProperty(header).getString());
			
			relatedQuery.bindValue("messageId", messageId);
			final NodeIterator resultNodes = relatedQuery.execute().getNodes();
			while (resultNodes.hasNext()) {
				final Node related = resultNodes.nextNode().getParent();
				results.add(related);
				findRelated(results, related, "Message-ID");
//				findRelated(results, related, "In-Reply-To");
//				findRelated(results, related, "References");
			}
		}
	}
}
