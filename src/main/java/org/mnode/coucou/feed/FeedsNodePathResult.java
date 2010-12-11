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
package org.mnode.coucou.feed;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
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
public class FeedsNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public FeedsNodePathResult(Node node) {
		super(node);
	}
	
	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		try {
			if (result.hasNode("query")) {
	            final Query query = result.getSession().getWorkspace().getQueryManager().getQuery(
	            		result.getNode("query"));
				return new SearchPathResult(query, Text.unescapeIllegalJcrChars(result.getName()));
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return new FeedNodePathResult(result);
	}
}
