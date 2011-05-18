package org.mnode.coucou.contacts;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;

import org.mnode.coucou.DataException;
import org.mnode.coucou.NodePathResult;
import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;
import org.mnode.coucou.search.SearchPathResult;

public class ContactsNodePathResult extends NodePathResult {

	private Query allContactsQuery;

	public ContactsNodePathResult(Node node) {
		super(node);
		try {
			allContactsQuery = node.getSession().getWorkspace().getQueryManager().createQuery(
				String.format("SELECT * FROM [nt:unstructured] AS contacts WHERE ISDESCENDANTNODE(contacts, ['%s']) AND contacts.personal IS NOT NULL", node.getPath()),
				Query.JCR_JQOM);
		}
		catch (RepositoryException e) {
			throw new DataException(e);
		}
	}
	
	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		
		if (allContactsQuery != null) {
			children.add(new SearchPathResult(allContactsQuery, "All Contacts"));
		}
//		children.addAll(super.getChildren());
		return children;
	}
	
	@Override
	public List<Node> getResults() throws PathResultException {
		final List<Node> results = new ArrayList<Node>();
		try {
			final NodeIterator accountNodes = getElement().getNode("accounts").getNodes();
			while (accountNodes.hasNext()) {
				final Node node = accountNodes.nextNode();
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
