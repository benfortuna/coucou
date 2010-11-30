/**
 * 
 */
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.swing.Icon;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;
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

	private Icon searchPathIcon;
	
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
						if (result instanceof SearchPathResult) {
							pathChoice.set("icon", searchPathIcon);
						}
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
	public Icon getSearchPathIcon() {
		return searchPathIcon;
	}

	/**
	 * @param searchPathIcon the searchPathIcon to set
	 */
	public void setSearchPathIcon(Icon searchPathIcon) {
		this.searchPathIcon = searchPathIcon;
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
