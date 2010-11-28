/**
 * 
 */
package org.mnode.coucou;

import java.util.List;

/**
 * @author fortuna
 *
 */
public interface PathResult<T, R> {

	/**
	 * @return the path result name
	 * @throws PathResultException
	 */
	String getName() throws PathResultException;
	
	/**
	 * @return the path result element
	 */
	T getElement();
	
	/**
	 * @return false if the result may have children, otherwise true
	 */
	boolean isLeaf();
	
	/**
	 * @return the child elements
	 * @throws PathResultException
	 */
	List<PathResult<?, R>> getChildren() throws PathResultException;
	
	/**
	 * @param result a result element
	 * @return the corresponding path for the specified result
	 * @throws PathResultException
	 */
	PathResult<?, R> getChild(R result) throws PathResultException;
	
	/**
	 * @return the list of results for the path
	 */
	List<R> getResults() throws PathResultException;
}
