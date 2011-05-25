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

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;

import org.jivesoftware.smack.RosterEntry;
import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.DataException;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

public abstract class AbstractXmppPathResult<T> extends AbstractPathResult<T, RosterEntry> {
	
	private final Node conversationsNode;
	private final Query conversationsQuery;

	public AbstractXmppPathResult(T element, String name, Node conversationsNode) {
		super(element, name);
		this.conversationsNode = conversationsNode;
		try {
			conversationsQuery = conversationsNode.getSession().getWorkspace().getQueryManager().createQuery(
					"SELECT conversations.* FROM [nt:unstructured] AS conversations WHERE conversations.[participant] = $participant",
					Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			throw new DataException(e);
		}
	}

	@Override
	public final PathResult<?, Node> getChild(RosterEntry result) throws PathResultException {
		try {
			final Map<String, Value> bindValues = new HashMap<String, Value>();
			bindValues.put("participant", conversationsNode.getSession().getValueFactory().createValue(result.getUser()));
			final String name = result.getName() != null ? result.getName() : result.getUser();
			return new SearchPathResult(conversationsQuery, name, bindValues);
		}
		catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
	
	public Node getConversationsNode() {
		return conversationsNode;
	}
}
