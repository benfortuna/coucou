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

import org.mnode.coucou.feed.FeedsNodePathResult;
import org.mnode.coucou.mail.MailboxNodePathResult;
import org.mnode.coucou.planner.PlannerNodePathResult;

/**
 * @author fortuna
 *
 */
public class RootNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public RootNodePathResult(Node node) {
		super(node);
	}
	
	@Override
	public PathResult<?, ?> getChild(Node result) throws PathResultException {
		try {
			if ("/Mail".equals(result.getPath())) {
				return new MailboxNodePathResult(result);
			}
			else if ("/Feeds".equals(result.getPath())) {
				return new FeedsNodePathResult(result);
			}
			else if ("/Planner".equals(result.getPath())) {
				return new PlannerNodePathResult(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return super.getChild(result);
	}
}
