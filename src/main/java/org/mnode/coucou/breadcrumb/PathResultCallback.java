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
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
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
public class PathResultCallback extends BreadcrumbBarCallBack<PathResult<?, Node>> {

	private PathResult<Node, Node> root;

	private Map<Class<? extends PathResult<?, ?>>, Icon> pathIcons;
	
//	public PathResultCallback(Node root) {
//		this.root = new RootNodePathResult(root);
//	}
	
	@Override
	public List<StringValuePair<PathResult<?, Node>>> getPathChoices(List<BreadcrumbItem<PathResult<?, Node>>> path)
			throws BreadcrumbBarException {
		
		final List<StringValuePair<PathResult<?, Node>>> pathChoices = new ArrayList<StringValuePair<PathResult<?, Node>>>();
		try {
			if (path == null) {
				for (PathResult<?, Node> result : root.getChildren()) {
					pathChoices.add(new StringValuePair<PathResult<?, Node>>(result.getName(), result));
				}
			}
			else if (path.isEmpty()) {
				return null;
			}
			else {
				final PathResult<?, Node> lastPathResult = path.get(path.size() - 1).getData();
				if (!lastPathResult.isLeaf()) {
					for (PathResult<?, Node> result : lastPathResult.getChildren()) {
						final StringValuePair<PathResult<?, Node>> pathChoice = new StringValuePair<PathResult<?, Node>>(result.getName(), result);
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
	public PathResult<Node, Node> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(PathResult<Node, Node> root) {
		this.root = root;
	}
}
