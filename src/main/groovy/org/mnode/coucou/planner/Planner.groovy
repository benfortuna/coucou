/**
 * 
 */
package org.mnode.coucou.planner

import javax.jcr.Repository;

import org.mnode.coucou.AbstractNodeManager;

/**
 * @author fortuna
 *
 */
class Planner extends AbstractNodeManager {

	Planner(Repository repository, String nodeName) {
		super(repository, 'planner', nodeName)
	}
}
