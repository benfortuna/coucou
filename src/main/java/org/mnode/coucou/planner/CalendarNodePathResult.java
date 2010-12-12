/**
 * 
 */
package org.mnode.coucou.planner;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.LeafNodePathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class CalendarNodePathResult extends LeafNodePathResult {

	/**
	 * @param node
	 */
	public CalendarNodePathResult(Node node) {
		super(node);
	}

	@Override
	public String getName() throws PathResultException {
		try {
			return getElement().getProperty("summary").getString();
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}

}
