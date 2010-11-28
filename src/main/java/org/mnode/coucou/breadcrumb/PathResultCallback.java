/**
 * 
 */
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.RootNodePathResult;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarCallBack;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarException;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;
import org.pushingpixels.flamingo.api.common.StringValuePair;

/**
 * @author fortuna
 *
 */
public class PathResultCallback extends BreadcrumbBarCallBack<PathResult<?, Node>> {

	private final PathResult<Node, Node> root;

	public PathResultCallback(Node root) {
		this.root = new RootNodePathResult(root);
	}
	
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
						pathChoices.add(new StringValuePair<PathResult<?, Node>>(result.getName(), result));
					}
				}
			}
		}
		catch (PathResultException pre) {
			throw new BreadcrumbBarException(pre);
		}
		
		return pathChoices;
	}
}
