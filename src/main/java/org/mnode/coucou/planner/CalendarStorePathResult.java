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

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fortuna.ical4j.connector.CalendarCollection;
import net.fortuna.ical4j.connector.CalendarStore;
import net.fortuna.ical4j.connector.ObjectNotFoundException;
import net.fortuna.ical4j.connector.ObjectStoreException;

import org.mnode.coucou.AbstractPathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

public class CalendarStorePathResult<T extends CalendarCollection> extends AbstractPathResult<CalendarStore<T>, CalendarCollection> {

	public CalendarStorePathResult(CalendarStore<T> store, String name) {
		super(store, name);
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		try {
			final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
			if (!getElement().isConnected()) {
				final PasswordAuthentication pa = Authenticator.requestPasswordAuthentication(null, -1, null, "Password", null);
				getElement().connect(pa.getUserName(), pa.getPassword());
			}
			for (T collection : getElement().getCollections()) {
				children.add(getChild(collection));
			}
			return children;
		}
		catch (ObjectNotFoundException onfe) {
			throw new PathResultException(onfe);
		}
		catch (ObjectStoreException ose) {
			throw new PathResultException(ose);
		}
	}

	@Override
	public PathResult<CalendarCollection, ?> getChild(CalendarCollection result) throws PathResultException {
		return new CalendarCollectionPathResult(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarCollection> getResults() throws PathResultException {
		return Collections.EMPTY_LIST;
	}

	
}
