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
package org.mnode.coucou.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.RowIterator;

import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class SearchPathResult extends AbstractPathResult<Query, Node> {
	
	private final String selector;
	
	private final Map<String, Value> bindValues;
	
	@SuppressWarnings("unchecked")
	public SearchPathResult(Query query, String name) {
		this(query, name, null, Collections.EMPTY_MAP);
	}
	
	public SearchPathResult(Query query, String name, Map<String, Value> bindValues) {
		this(query, name, null, bindValues);
	}
	
	@SuppressWarnings("unchecked")
	public SearchPathResult(Query query, String name, String selector) {
		this(query, name, selector, Collections.EMPTY_MAP);
	}
	
	public SearchPathResult(Query query, String name, String selector, Map<String, Value> bindValues) {
		super(query, name);
		this.selector = selector;
		this.bindValues = bindValues;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		return null;
	}

	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		return null;
	}

	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			for (String key : bindValues.keySet()) {
				getElement().bindValue(key, bindValues.get(key));
			}
			if (selector != null) {
				final RowIterator rows = getElement().execute().getRows();
				while (rows.hasNext()) {
					final Node node = rows.nextRow().getNode(selector);
					results.add(node);
				}
			}
			else {
				final NodeIterator resultNodes = getElement().execute().getNodes();
				while (resultNodes.hasNext()) {
					final Node node = resultNodes.nextNode();
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
