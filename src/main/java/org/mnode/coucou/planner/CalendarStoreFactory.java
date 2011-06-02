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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.fortuna.ical4j.connector.CalendarStore;
import net.fortuna.ical4j.connector.dav.CalDavCalendarCollection;
import net.fortuna.ical4j.connector.dav.CalDavCalendarStore;
import net.fortuna.ical4j.connector.dav.PathResolver;

public class CalendarStoreFactory {

	private static final Map<String, PathResolver> pathResolvers = new HashMap<String, PathResolver>();
	
	static {
		pathResolvers.put("chandler", PathResolver.CHANDLER);
		pathResolvers.put("gcal", PathResolver.GCAL);
	}
	
	private final String prodId;
	
	public CalendarStoreFactory(String prodId) {
		this.prodId = prodId;
	}
	
	public CalendarStore<CalDavCalendarCollection> newInstance(URL url, String storeType) {
		final PathResolver pathResolver = pathResolvers.get(storeType);
		
		return new CalDavCalendarStore(prodId, url, pathResolver);
	}
}
