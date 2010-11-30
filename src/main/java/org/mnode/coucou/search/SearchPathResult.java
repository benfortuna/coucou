/**
 * 
 */
package org.mnode.coucou.search;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class SearchPathResult implements PathResult<Query, Node> {

	private final Query query;
	
	private final String name;
	
	public SearchPathResult(Query query, String name) {
		this.query = query;
		this.name = name;
	}
	
	@Override
	public String getName() throws PathResultException {
		return name;
	}

	@Override
	public Query getElement() {
		return query;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		return null;
	}

	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		return null;
	}

	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
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