/**
 * 
 */
package org.mnode.coucou;

/**
 * @author fortuna
 *
 */
public class PathResultException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 */
	public PathResultException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PathResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
