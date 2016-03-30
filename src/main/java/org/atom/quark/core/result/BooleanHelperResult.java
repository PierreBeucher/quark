package org.atom.quark.core.result;

/**
 * <p>The BooleanHelperResult represent results for an atomic operation simply
 * resulting in either success or failure. It always expects <i>true</i>, and may
 * have <i>true</i> or <i>false</i> as obtained data. 
 * @author Pierre Beucher
 *
 */
public class BooleanHelperResult implements HelperResult {
	
	/**
	 * String used for success description
	 */
	public static final String SUCCESS = "Success";
	
	/**
	 * String used for failure description
	 */
	public static final String FAILURE = "Failure";

	private boolean success;
	
	public BooleanHelperResult(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getExpected() {
		return true;
	}

	public Object getActual() {
		return success;
	}

}
