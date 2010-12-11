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
package org.mnode.coucou.activity;

import ca.odell.glazedlists.TreeList.ExpansionModel;

/**
 * @author fortuna
 *
 */
public class DateExpansionModel implements ExpansionModel<Object> {

	public boolean isExpanded(Object arg0, java.util.List<Object> arg1) {
		return !"Older Items".equals(arg0);
	};
	
	public void setExpanded(Object arg0, java.util.List<Object> arg1, boolean arg2) {};
}
