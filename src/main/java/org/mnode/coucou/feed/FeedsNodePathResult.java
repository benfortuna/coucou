/**
 * 
 */
package org.mnode.coucou.feed;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

import org.apache.jackrabbit.util.Text;
import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

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
	            final Query query = result.getSession().getWorkspace().getQueryManager().getQuery(
	            		result.getNode("query"));
				return new SearchPathResult(query, Text.unescapeIllegalJcrChars(result.getName()));
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return new FeedNodePathResult(result);
	}
}
