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
package org.mnode.coucou;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathResultComparator implements Comparator<PathResult<?, ?>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PathResultComparator.class);
	
	@Override
	public int compare(PathResult<?, ?> o1, PathResult<?, ?> o2) {
		int retVal = 0;
		if (o1.isLeaf() && !o2.isLeaf()) {
			retVal = Integer.MAX_VALUE;
		}
		else if (o2.isLeaf() && !o1.isLeaf()) {
			retVal = Integer.MIN_VALUE;
		}
		else {
			try {
				retVal = o1.getName().compareToIgnoreCase(o2.getName());
			}
			catch (PathResultException pre) {
				LOGGER.error(String.format("Error comparing o1: %s, and o2: %s", o1, o2), pre);			
			}
		}
		return retVal;
	}
}
