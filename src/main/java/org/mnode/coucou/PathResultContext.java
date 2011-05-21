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

import java.util.HashMap;
import java.util.Map;

public class PathResultContext {

	private final Map<String, Object> sharedValues;
	
	public PathResultContext() {
		sharedValues = new HashMap<String, Object>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSharedValue(String key) {
		return (T) sharedValues.get(key);
	}
	
	public void putSharedValue(String key, Object value) {
		sharedValues.put(key, value);
	}
}
