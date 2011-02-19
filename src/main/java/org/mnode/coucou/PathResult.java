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
/**
 * 
 */
package org.mnode.coucou;

import java.util.List;

/**
 * @author fortuna
 *
 */
public interface PathResult<T, R> {

	/**
	 * @return the path result name
	 * @throws PathResultException
	 */
	String getName() throws PathResultException;
	
	/**
	 * @return the path result element
	 */
	T getElement();
	
	/**
	 * @return false if the result may have children, otherwise true
	 */
	boolean isLeaf();
	
	/**
	 * @return the child elements
	 * @throws PathResultException
	 */
	List<PathResult<?, R>> getChildren() throws PathResultException;
	
	/**
	 * @param result a result element
	 * @return the corresponding path for the specified result
	 * @throws PathResultException
	 */
	PathResult<?, R> getChild(R result) throws PathResultException;
	
	/**
	 * @return the list of results for the path
	 */
	List<R> getResults() throws PathResultException;
}
