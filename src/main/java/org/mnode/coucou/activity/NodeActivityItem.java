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
package org.mnode.coucou.activity;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

/**
 * @author fortuna
 *
 */
public class NodeActivityItem implements ActivityItem {

	private final Node node;
	
	private final String[] properties;
	
	public NodeActivityItem(Node node, String...properties) {
		this.node = node;
		this.properties = properties;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(int column) {
		Property value = null;
		try {
			value = node.getProperty(properties[column]);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return value;
	}

}
