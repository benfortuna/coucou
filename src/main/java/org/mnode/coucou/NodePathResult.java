/**
 * 
 */
package org.mnode.coucou;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.apache.jackrabbit.util.Text;

/**
 * @author fortuna
 *
 */
public class NodePathResult implements PathResult<Node, Node> {

	private final Node node;
	
	public NodePathResult(Node node) {
		this.node = node;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() throws PathResultException {
		try {
			return Text.unescapeIllegalJcrChars(node.getName());
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}

	@Override
	public Node getElement() {
		return node;
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		final List<PathResult<?, Node>> children = new ArrayList<PathResult<?, Node>>();
		try {
			final NodeIterator childNodes = node.getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					children.add(getChild(node));
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return children;
	}
	
	@Override
	public PathResult<?, Node> getChild(Node result) throws PathResultException {
		return new NodePathResult(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator childNodes = node.getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
				if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
					results.add(node);
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}
}
