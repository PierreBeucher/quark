package org.atom.quark.core.helper;

/**
 * Exception used when a context or any helper misuse is detected.
 * @author Pierre Beucher
 *
 */
public class HelperException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8360811008792406358L;

	public HelperException() {
		super();
	}

	public HelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public HelperException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HelperException(String arg0) {
		super(arg0);
	}

	public HelperException(Throwable arg0) {
		super(arg0);
	}
	
	

}
