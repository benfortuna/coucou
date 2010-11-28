/**
 * 
 */
package org.mnode.coucou.feed;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.LeafNodePathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class FeedNodePathResult extends LeafNodePathResult {

	/**
	 * @param node
	 */
	public FeedNodePathResult(Node node) {
		super(node);
	}

	@Override
	public String getName() throws PathResultException {
		try {
			return getElement().getProperty("title").getString();
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
}
