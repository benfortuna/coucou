/**
 * 
 */
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;
import javax.mail.Folder;

import org.apache.jackrabbit.util.Text;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarCallBack;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarException;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;
import org.pushingpixels.flamingo.api.common.StringValuePair;

/**
 * @author fortuna
 *
 */
public class NodeCallback extends BreadcrumbBarCallBack<Node> {

	private final Node root;
	
	public NodeCallback(Node root) {
		this.root = root;
	}
	
	@Override
	public List<StringValuePair<Node>> getPathChoices(
			List<BreadcrumbItem<Node>> path) throws BreadcrumbBarException {

		final List<StringValuePair<Node>> pathChoices = new ArrayList<StringValuePair<Node>>();
		
		try {
			if (path == null) {
				final NodeIterator rootNodes = root.getNodes();
				while (rootNodes.hasNext()) {
					final Node node = rootNodes.nextNode();
					if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						pathChoices.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
					}
				}
			}
			else if (path.isEmpty()) {
				return null;
			}
			else {
				final Node lastPathNode = path.get(path.size() - 1).getData();
				final NodeIterator nodes = lastPathNode.getNodes();
				while (nodes.hasNext()) {
					final Node node = nodes.nextNode();
					// folder nodes..
					if ("folders".equals(node.getName()) && "Mail".equals(node.getAncestor(1).getName())) {
						final NodeIterator folderNodes = node.getNodes();
						while (folderNodes.hasNext()) {
							final Node folderNode = folderNodes.nextNode();
							if (folderNode.isNodeType(NodeType.NT_UNSTRUCTURED)) {
//									pathChoices.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
								pathChoices.add(new StringValuePair<Node>(folderNode.getProperty("folderName").getString(), folderNode));
							}
						}
					}
					else if ("messages".equals(node.getName()) && "Mail".equals(node.getAncestor(1).getName())) {
						// do nothing for now..
					}
					else if (node.isNodeType(NodeType.NT_UNSTRUCTURED) && node.hasProperty("title")) {
						pathChoices.add(new StringValuePair<Node>(node.getProperty("title").getString(), node));
					}
					else if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
						pathChoices.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
					}
				}
			}
		}
		catch (RepositoryException re) {
			throw new BreadcrumbBarException(re);
		}
		return pathChoices;
	}
	
	@Override
	public List<StringValuePair<Node>> getLeafs(List<BreadcrumbItem<Node>> path)
			throws BreadcrumbBarException {

		List<StringValuePair<Node>> leafs = null;
		
		try {
			if (path != null && !path.isEmpty()) {
				final Node lastPathNode = path.get(path.size() - 1).getData();
				if (lastPathNode.hasNode("query")) {
	                final Query query = lastPathNode.getSession().getWorkspace().getQueryManager().getQuery(
	                		lastPathNode.getNode("query"));
	                final NodeIterator nodes = query.execute().getNodes();
	                
	                leafs = new ArrayList<StringValuePair<Node>>();
					while (nodes.hasNext()) {
						final Node node = nodes.nextNode();
						if (node.isNodeType(NodeType.NT_UNSTRUCTURED) || node.isNodeType(NodeType.NT_FILE)) {
							leafs.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
						}
					}
				}
				else if (lastPathNode.hasProperty("type")) {
					final int folderType = (int) lastPathNode.getProperty("type").getLong();
					if ((folderType & Folder.HOLDS_MESSAGES) > 0) {
		                final NodeIterator nodes = lastPathNode.getNode("messages").getNodes();
		                
		                leafs = new ArrayList<StringValuePair<Node>>();
						while (nodes.hasNext()) {
							final Node node = nodes.nextNode();
							if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
								leafs.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
							}
						}
					}
				}
			}
			
			if (leafs == null) {
				leafs = getPathChoices(path);
			}
		}
		catch (RepositoryException re) {
			re.printStackTrace();
		}
		
		for (Iterator<StringValuePair<Node>> it = leafs.iterator(); it.hasNext();) {
			try {
				// don't include query nodes in leafs..
				if (it.next().getValue().hasNode("query")) {
					it.remove();
				}
			}
			catch (RepositoryException re) {
				re.printStackTrace();
			}
		}
		
		return leafs;
	}
}
