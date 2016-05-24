package com.github.pierrebeucher.quark.core.helper;

/**
 * Thrown if an error is encountered when initialising Helper, enclosing
 * the Exception causing the error. 
 * @author pierreb
 *
 */
public class InitializationException extends HelperException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8459692674513421108L;

	protected InitializationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InitializationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InitializationException(String arg0) {
		super(arg0);
	}

	public InitializationException(Throwable arg0) {
		super(arg0);
	}
}
