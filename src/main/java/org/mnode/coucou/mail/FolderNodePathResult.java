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
import javax.mail.Folder;

import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

/**
 * @author fortuna
 *
 */
public class FolderNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public FolderNodePathResult(Node node) {
		super(node);
	}

	@Override
	public String getName() throws PathResultException {
		try {
			return getElement().getProperty("folderName").getString();
		} catch (RepositoryException e) {
			throw new PathResultException(e);
		}
	}
	
	@Override
	public List<PathResult<?, Node>> getChildren() throws PathResultException {
		final List<PathResult<?, Node>> children = new ArrayList<PathResult<?, Node>>();
		try {
			if ((Folder.HOLDS_FOLDERS & getElement().getProperty("type").getLong()) > 0) {
				// add folders..
				final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
				while (folderNodes.hasNext()) {
					final Node node = folderNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						children.add(new FolderNodePathResult(node));
					}
				}
			}
			else if ((Folder.HOLDS_MESSAGES & getElement().getProperty("type").getLong()) > 0) {
				// add messages..
				final NodeIterator messageNodes = getElement().getNode("messages").getNodes();
				while (messageNodes.hasNext()) {
					final Node node = messageNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						children.add(getChild(node));
					}
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
		try {
			if ("messages".equals(result.getParent().getName())) {
				return new MessageNodePathResult(result);
			}
			else if ("folders".equals(result.getParent().getName())) {
				return new FolderNodePathResult(result);
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return super.getChild(result);
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			if ((Folder.HOLDS_MESSAGES & getElement().getProperty("type").getLong()) > 0) {
				// add messages..
				final NodeIterator messageNodes = getElement().getNode("messages").getNodes();
				while (messageNodes.hasNext()) {
					final Node node = messageNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						results.add(node);
					}
				}
			}
			else if ((Folder.HOLDS_FOLDERS & getElement().getProperty("type").getLong()) > 0) {
				// add folders..
				final NodeIterator folderNodes = getElement().getNode("folders").getNodes();
				while (folderNodes.hasNext()) {
					final Node node = folderNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						results.add(node);
					}
				}
			}
		}
		catch (RepositoryException re) {
			throw new PathResultException(re);
		}
		return results;
	}
}
