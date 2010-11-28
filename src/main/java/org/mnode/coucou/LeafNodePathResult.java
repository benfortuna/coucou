/**
 * 
 */
package org.mnode.coucou;

import javax.jcr.Node;


/**
 * @author fortuna
 *
 */
public class LeafNodePathResult extends NodePathResult {

	/**
	 * @param node
	 */
	public LeafNodePathResult(Node node) {
		super(node);
	}

	@Override
	public final boolean isLeaf() {
		return true;
	}
}
