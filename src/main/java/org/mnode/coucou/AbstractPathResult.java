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


public abstract class AbstractPathResult<T, R> implements PathResult<T, R> {

	private final T element;
	
	private final String name;
	
	private final PathResultContext context;
	
	public AbstractPathResult(T element, String name) {
		this(element, name, null);
	}
	
	public AbstractPathResult(T element, String name, PathResultContext context) {
		this.element = element;
		this.name = name;
		this.context = context;
	}
	
	@Override
	public String getName() throws PathResultException {
		return name;
	}

	@Override
	public T getElement() {
		return element;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	public final PathResultContext getContext() {
		return context;
	}
}
