/**
 * 
 */
package org.mnode.coucou.mail;

import java.util.Arrays;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.mnode.coucou.LeafNodePathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class MessageNodePathResult extends LeafNodePathResult {

	/**
	 * @param node
	 */
	public MessageNodePathResult(Node node) {
		super(node);
	}

	@Override
	public String getName() throws PathResultException {
		try {
			if (getElement().hasNode("headers") && getElement().getNode("headers").hasProperty("Subject")) {
				return getElement().getNode("headers").getProperty("Subject").getString();
			}
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
		return super.getName();
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		// show related messages..
		return Arrays.asList(getElement());
	}
}
