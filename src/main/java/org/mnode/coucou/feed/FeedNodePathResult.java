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
package org.mnode.coucou.feed;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.LeafNodePathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.util.HtmlCodes;

/**
 * @author fortuna
 *
 */
public class FeedNodePathResult extends LeafNodePathResult {

	/**
	 * @param node
	 */
	public FeedNodePathResult(Node node) {
		super(node);
	}

	@Override
	public String getName() throws PathResultException {
		try {
			return HtmlCodes.unescape(getElement().getProperty("title").getString());
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
}
