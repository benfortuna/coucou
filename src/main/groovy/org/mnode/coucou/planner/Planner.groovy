/**
 * 
 */
package org.mnode.coucou.planner

import javax.jcr.Repository;

import org.mnode.coucou.AbstractManager;

/**
 * @author fortuna
 *
 */
class Planner extends AbstractManager {

	Planner(Repository repository, String nodeName) {
		super(repository, 'planner', nodeName)
	}
}
