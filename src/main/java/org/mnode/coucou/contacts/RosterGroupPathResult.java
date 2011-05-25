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

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

public class RosterGroupPathResult extends AbstractXmppPathResult<RosterGroup> {

	public RosterGroupPathResult(RosterGroup group, Node conversationsNode) {
		super(group, null, conversationsNode);
	}
	
	@Override
	public String getName() throws PathResultException {
		return getElement().getName();
	}
	
	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		
		for (RosterEntry entry : getElement().getEntries()) {
			children.add(getChild(entry));
		}
		return children;
	}

	@Override
	public List<RosterEntry> getResults() throws PathResultException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
