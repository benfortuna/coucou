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
package org.mnode.coucou.planner;

import java.util.ArrayList;
import java.util.List;

import net.fortuna.ical4j.connector.CalendarCollection;
import net.fortuna.ical4j.connector.ObjectStoreException;
import net.fortuna.ical4j.model.Calendar;

import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

public class CalendarCollectionPathResult extends AbstractPathResult<CalendarCollection, Calendar> {

	public CalendarCollectionPathResult(CalendarCollection collection) {
		super(collection, null);
	}
	
	@Override
	public String getName() throws PathResultException {
		return getElement().getDisplayName();
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		try {
			final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
			for (Calendar calendar : getElement().getComponents()) {
				children.add(getChild(calendar));
			}
			return children;
		}
		catch (ObjectStoreException ose) {
			throw new PathResultException(ose);
		}
	}

	@Override
	public PathResult<Calendar, ?> getChild(Calendar result) throws PathResultException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Calendar> getResults() throws PathResultException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
