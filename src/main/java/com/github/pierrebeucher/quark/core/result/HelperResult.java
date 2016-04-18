package com.github.pierrebeucher.quark.core.result;

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
 * @param <A> Type of the actual output wrapped by this HelperResult
 *
 */
public interface HelperResult<A> {
	
	/**
	 * String representing a Success, used for result description
	 */
	public static final String SUCCESS = "Success";
	
	/**
	 * String representing a Failure, used for result description
	 */
	public static final String FAILURE = "Failure";
	
	/**
	 * Whether this Result is a failure or a success. 
	 * @return true on success, false on failure
	 */
	boolean isSuccess();
	
	/**
	 * Utility method to easily assert this Result success. If the Result
	 * is not success (i.e isSucess() does not return true), a <code>java.lang.AssertionError</code>
	 * is thrown.
	 * @throws java.lang.AssertionError if this Result represents a failure. 
	 */
	void assertSuccess();
	
	/**
	 * Utility method to easily assert this Result failure. If the Result
	 * is a success (i.e isSucess() returns true), a <code>java.lang.AssertionError</code>
	 * is thrown.
	 * @throws java.lang.AssertionError if this Result represents a failure. 
	 */
	void assertFailure();
	
	/**
	 * Get the obtained output for this HelperResult. The obtained result
	 * does not necessarily match the Expected output type, as it may represent
	 * an incorrect result, or a set of data in which the expected result
	 * is supposed to be found.
	 * @return the obtained output
	 */
	Object getActual();
	
	/**
	 * A message explaining this Result success or failure. If this Result represents a Failure,
	 * this message represents the reason of the failure. If this Result represents a Success,
	 * this message represents the data found and why it gives as successful result.
	 * @return message explaining the success or failure for this Result
	 */
	String getMessage();
	
	/**
	 * A short message explaining this Result success or failure. 
	 */
	String getShortMessage();
	
	
	/**
	 * Get the Throwabl cause for this Result. Most of the time, a Throwable will cause this
	 * Result to fail, but it may be used to explain a success cause as well. The returned value
	 * may be null it there is no Throwable cause for this Result.
	 * @return any existing Throwable cause, or null
	 */
	Throwable getCause();


}
