package org.atom.quark.core.result;

/**
 * <p>A HelperResult represent an action output performed by a Helper. Such action
 * either failed or succeed, with a certain output.</p>
 * 
 * <p>Success or failure can be retrieved using isSuccess().</p>
 * 
 * <p>The action output is the data resulting from this test action. It may be any Object, such as SQL
 * result set, XML text, a list of files, etc.; or null. </p>
 * 
 * @author Pierre Beucher
 *
 * @param <E>
 */
public interface HelperResult<E> {
	
	/**
	 * String representing a Success, used for result description
	 */
	public static final String SUCCESS = "Success";
	
	/**
	 * String representing a Failure, used for result description
	 */
	public static final String FAILURE = "Failure";
	
	/**
	 * Whether this result failed of succeed 
	 * @return true on success, false on failure
	 */
	Boolean isSuccess();
	
	/**
	 * 
	 * @return Data output obtained from the test action represented by this result. May be null.
	 */
	E getActionOutput();
	
	/**
	 * The result description is a short string representing in a human readable way
	 * this result. It gives a short overview of this result output.
	 * @return this result short description
	 */
	String getResultDescription();

}
