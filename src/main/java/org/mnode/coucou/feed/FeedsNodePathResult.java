/**
 * 
 */
package org.mnode.coucou.feed;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.QueryNodePathResult;

/**
 * @author fortuna
 *
 */
public class FeedsNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public FeedsNodePathResult(Node node) {
		super(node);
	}
	
	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		try {
			if (result.hasNode("query")) {
				return new QueryNodePathResult(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return new FeedNodePathResult(result);
	}
}
