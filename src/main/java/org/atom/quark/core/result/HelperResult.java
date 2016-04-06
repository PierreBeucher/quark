package org.atom.quark.core.result;

/**
 * <p>A HelperResult represent an action result performed by a Helper, either failure or a success
 * An expected and actual value being compared to define the success or failure</p>
 * <p>The expected value may be a boolean, a String, or any complex type. This value
 * is used to decide if the actual output is correct or not. </p>
 * <p>The actual output for this action. Depending on the action,
 * it may be a boolean, a String, a set of object or any complex type. It was matched
 * against the expected value to define success or failure. If success,
 * the data represent an object or set of objects matching
 * our expected value. If failure, it represent an object or set of object
 * not matching our expected value (such as an empty set or a null value)</p>
 * 
 * 
 * @author Pierre Beucher
 *
 */
public interface HelperResult {
	
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
	boolean isSuccess();
	
	/**
	 * Get the obtained output for this HelperResult. The obtained result
	 * does not necessarily match the Expected output type, as it may represent
	 * an incorrect result, or a set of data in which the expected result
	 * is supposed to be found.
	 * @return the obtained output
	 */
	Object getActual();

}
