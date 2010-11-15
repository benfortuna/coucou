/**
 * 
 */
package org.mnode.coucou.activity;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

/**
 * @author fortuna
 *
 */
public class NodeActivityItem implements ActivityItem {

	private final Node node;
	
	private final String[] properties;
	
	public NodeActivityItem(Node node, String...properties) {
		this.node = node;
		this.properties = properties;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(int column) {
		Property value = null;
		try {
			value = node.getProperty(properties[column]);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return value;
	}

}
