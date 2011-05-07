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
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarCallBack;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarException;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;
import org.pushingpixels.flamingo.api.common.StringValuePair;

/**
 * @author fortuna
 *
 */
public class PathResultCallback extends BreadcrumbBarCallBack<PathResult<?, ?>> {

	private PathResult<?, ?> root;

	private Map<Class<? extends PathResult<?, ?>>, Icon> pathIcons;
	
//	public PathResultCallback(Node root) {
//		this.root = new RootNodePathResult(root);
//	}
	
	@Override
	public List<StringValuePair<PathResult<?, ?>>> getPathChoices(List<BreadcrumbItem<PathResult<?, ?>>> path)
			throws BreadcrumbBarException {
		
		final List<StringValuePair<PathResult<?, ?>>> pathChoices = new ArrayList<StringValuePair<PathResult<?, ?>>>();
		try {
			if (path == null) {
				for (PathResult<?, ?> result : root.getChildren()) {
					pathChoices.add(new StringValuePair<PathResult<?, ?>>(result.getName(), result));
				}
			}
			else if (path.isEmpty()) {
				return null;
			}
			else {
				final PathResult<?, ?> lastPathResult = path.get(path.size() - 1).getData();
				if (!lastPathResult.isLeaf()) {
					for (PathResult<?, ?> result : lastPathResult.getChildren()) {
						final StringValuePair<PathResult<?, ?>> pathChoice = new StringValuePair<PathResult<?, ?>>(result.getName(), result);
						pathChoice.set("icon", pathIcons.get(result.getClass()));
						pathChoices.add(pathChoice);
					}
				}
			}
		}
		catch (PathResultException pre) {
			if (throwsExceptions) {
				throw new BreadcrumbBarException(pre);
			}
			else {
				pre.printStackTrace();
			}
		}
		
		return pathChoices;
	}

	/**
	 * @return the searchPathIcon
	 */
	public Map<Class<? extends PathResult<?, ?>>, Icon> getPathIcons() {
		return pathIcons;
	}

	/**
	 * @param searchPathIcon the searchPathIcon to set
	 */
	public void setPathIcons(Map<Class<? extends PathResult<?, ?>>, Icon> pathIcons) {
		this.pathIcons = pathIcons;
	}

	/**
	 * @return the root
	 */
	public PathResult<?, ?> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(PathResult<?, ?> root) {
		this.root = root;
	}
}
