/**
 * 
 */
package org.mnode.coucou.breadcrumb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
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
	
//	private final Query relatedMessages;
	
	public NodeCallback(Node root) {
		this.root = root;
//		this.relatedMessages = relatedMessages;
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
				// if last node is a message, don't add children..
				if (!"messages".equals(lastPathNode.getParent().getName())) {
					final NodeIterator nodes = lastPathNode.getNodes();
					while (nodes.hasNext()) {
						final Node node = nodes.nextNode();
						// folder nodes..
						if ("folders".equals(node.getName()) && "Mail".equals(node.getAncestor(1).getName())) {
							final NodeIterator folderNodes = node.getNodes();
							while (folderNodes.hasNext()) {
								final Node folderNode = folderNodes.nextNode();
								if (folderNode.isNodeType(NodeType.NT_UNSTRUCTURED)) {
//										pathChoices.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
									pathChoices.add(new StringValuePair<Node>(folderNode.getProperty("folderName").getString(), folderNode));
								}
							}
						}
						// message nodes..
						else if ("messages".equals(node.getName()) && "Mail".equals(node.getAncestor(1).getName())) {
							final NodeIterator messageNodes = node.getNodes();
							while (messageNodes.hasNext()) {
								final Node messageNode = messageNodes.nextNode();
								if (messageNode.isNodeType(NodeType.NT_UNSTRUCTURED) && messageNode.hasNode("headers") && messageNode.getNode("headers").hasProperty("Subject")) {
									pathChoices.add(new StringValuePair<Node>(messageNode.getNode("headers").getProperty("Subject").getString(), messageNode));
								}
							}
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
				// query nodes..
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
				// folder nodes..
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
				// message nodes..
				else if ("messages".equals(lastPathNode.getParent().getName())) {
	                leafs = new ArrayList<StringValuePair<Node>>();
	                /*
					if (relatedMessages != null) {
						relatedMessages.bindValue("messageId", lastPathNode.getSession().getValueFactory().createValue(lastPathNode.getPath()));
						final NodeIterator nodes = relatedMessages.execute().getNodes();
						while (nodes.hasNext()) {
							final Node node = nodes.nextNode();
							if (node.isNodeType(NodeType.NT_UNSTRUCTURED)) {
								leafs.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(node.getName()), node));
							}
						}
					}
					else {
						leafs.add(new StringValuePair<Node>(Text.unescapeIllegalJcrChars(lastPathNode.getName()), lastPathNode));
					}
					*/
//	                Node messageNode = lastPathNode;
//	                while (messageNode.hasProperty("inReplyTo")) {
//	                	messageNode = root.getSession().getNode(messageNode.getProperty("inReplyTo").getString());
//						leafs.add(0, new StringValuePair<Node>(messageNode.getName(), messageNode));
//						if (messageNode.hasProperty("references")) {
//							final PropertyIterator referenceProps = messageNode.getProperties("references");
//							while (referenceProps.hasNext()) {
//								
//							}
//						}
//	                }
//					leafs.add(new StringValuePair<Node>(lastPathNode.getName(), lastPathNode));
	                final Map<String, Node> messages = new HashMap<String, Node>();
	                addMessageNode(messages, lastPathNode);
	                
	                for (Node messageNode : messages.values()) {
		        		leafs.add(new StringValuePair<Node>(messageNode.getName(), messageNode));
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
	
	private void addMessageNode(Map<String, Node> nodes, Node messageNode) throws RepositoryException {
        if (messageNode.hasProperty("inReplyTo")) {
        	final Node inReplyToNode = root.getSession().getNode(messageNode.getProperty("inReplyTo").getString());
        	if (!nodes.keySet().contains(inReplyToNode.getPath())) {
            	addMessageNode(nodes, inReplyToNode);
        	}
        }
		nodes.put(messageNode.getPath(), messageNode);
		if (messageNode.hasProperty("references")) {
			final Value[] references = messageNode.getProperty("references").getValues();
			for (Value reference : references) {
				final Node referenceNode = root.getSession().getNode(reference.getString());
	        	if (!nodes.keySet().contains(referenceNode.getPath())) {
		        	addMessageNode(nodes, referenceNode);
	        	}
			}
		}
	}
}
