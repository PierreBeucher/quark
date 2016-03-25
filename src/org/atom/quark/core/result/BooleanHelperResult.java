package org.atom.quark.core.result;

/**
 * <p>A Boolean Helper result, representing simple actions
 * with no other output than their success in itself.</p>
 * <p>The action output for this Result will always be 
 * its success or failure</p>
 * @author Pierre Beucher
 *
 */
public class BooleanHelperResult implements HelperResult<Boolean> {
	
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
		super();
		this.success = success;
	}

	public Boolean isSuccess() {
		return success;
	}

	public Boolean getActionOutput() {
		return success;
	}

	public String getResultDescription() {
		return success ? SUCCESS : FAILURE;
	}

}
