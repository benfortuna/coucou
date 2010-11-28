/**
 * 
 */
package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;

import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.QueryNodePathResult;

/**
 * @author fortuna
 *
 */
public class MailboxNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public MailboxNodePathResult(Node node) {
		super(node);
	}

	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		final List<PathResult<?, Node>> children = new ArrayList<PathResult<?, Node>>();
		try {
			// add search queries..
			final NodeIterator childNodes = getElement().getNodes();
			while (childNodes.hasNext()) {
				final Node node = childNodes.nextNode();
//				if (node.isNodeType(NodeType.NT_UNSTRUCTURED) && !"folders".equals(node.getName())) {
//					children.add(new NodePathResult(node));
//				}
				if (node.hasNode("query")) {
					children.add(new QueryNodePathResult(node));
				}
			}
			
			// add folders..
			final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
			while (folderNodes.hasNext()) {
				final Node node = folderNodes.nextNode();
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
		return new FolderNodePathResult(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
			while (folderNodes.hasNext()) {
				final Node node = folderNodes.nextNode();
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
