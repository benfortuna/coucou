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
