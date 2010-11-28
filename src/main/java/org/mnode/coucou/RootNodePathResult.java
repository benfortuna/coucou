/**
 * 
 */
package org.mnode.coucou;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.feed.FeedsNodePathResult;
import org.mnode.coucou.mail.MailboxNodePathResult;

/**
 * @author fortuna
 *
 */
public class RootNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public RootNodePathResult(Node node) {
		super(node);
	}
	
	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		try {
			if ("/Mail".equals(result.getPath())) {
				return new MailboxNodePathResult(result);
			}
			else if ("/Feeds".equals(result.getPath())) {
				return new FeedsNodePathResult(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return super.getChild(result);
	}
}
