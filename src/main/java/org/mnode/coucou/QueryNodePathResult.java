/**
 * 
 */
package org.mnode.coucou;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

/**
 * @author fortuna
 *
 */
public class QueryNodePathResult extends LeafNodePathResult {

	/**
	 * @param node
	 */
	public QueryNodePathResult(Node node) {
		super(node);
		try {
			if (!node.hasNode("query")) {
				throw new IllegalArgumentException("Query node not found");
			}
		}
		catch (RepositoryException re) {
			re.printStackTrace();
		}
	}

	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
            final Query query = getElement().getSession().getWorkspace().getQueryManager().getQuery(
            		getElement().getNode("query"));
            final NodeIterator resultNodes = query.execute().getNodes();
			while (resultNodes.hasNext()) {
				final Node node = resultNodes.nextNode();
				results.add(node);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}
}
