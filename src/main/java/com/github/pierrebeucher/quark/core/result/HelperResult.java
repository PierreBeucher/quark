package com.github.pierrebeucher.quark.core.result;

/**
 * <p>A HelperResult represent an action result performed by a Helper, either failure or a success
 * with the obtained result. It can also define the expected result, the context under which the
 * action was run, and the eventual throwable cause for this result. Also, a String description 
 * can be generated with the available data in a human-readable way.</p>
 * <p>Typically, the HelperResult can be used to:
 * <ul>
 * <li>Assert this result success or failure with {@link #isSuccess()}</li>
 * <li>Obtain the output data of the represented test action using {@link #getActual()}</li>
 * <li>Generate a description with {@link #getDescription()}</li>
 * <li>The optional Throwable cause for this result using {@link #getCause()}</li>
 * <li>The optional context under which the test action ran with {@link #getContext()}</li>
 * </ul>
 * </p>
 * 
 * <p><b>Use an obtained HelperResult</b><br>
 * Considering an Helper performing various test actions, the HelperResult provide facility methods
 * to assert the test success or failure, obtain resulting and expected test output, and print, log or describe
 * in a human readable-way the result:
 * <pre>
 * // perform your test action
 * HelperResult<Map<String, Object>> result = myHelper.performSomeTestActionWithMapOutput();
 * 
 * // you may want to log the result
 * logger.info("Some test with map output: {}", result.getDescription());
 * 
 * // assert the result with a human-readable error message on failure
 * Assert.assertTrue(result.isSuccess(), "Some test action failed: " + result.getDescription());
 * </pre> 
 * </p>
 * 
 * <p><b>Generate HelperResult for your Helper</b><br>
 * If you want your Helper implementation to return HelperResult, building an instance is easy using
 * inline setters and the HelperBuilder:
 * <pre>
 * 	//some test action...
 *	ResultBuilder.instance(output) //or helper.actual(outpout)
 * 		.success(actual != null && actual.get("somekey").equals("expected"))
 *  		.context(new URL("http://localhost:8080/mywebservice?testParam=value"));
 * </pre>
 * </p>
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
	 * Get the obtained output for this HelperResult. The obtained result
	 * does not necessarily match the Expected output type, as it may represent
	 * an incorrect result, or a set of data in which the expected result
	 * is supposed to be found.
	 * @return the obtained output
	 */
	Object getActual();
	
	/**
	 * A short description of this result, resuming the available elements: success, actual, expected, 
	 * cause and context; if available.
	 * @return A short description of this result
	 */
	String getDescription();
	
	/**
	 * <p>The most atomic context under which the action for this result was run. The returned Context
	 * contain, at least, all the element involved in the test action providing this result.</p>
	 * <p>For example, for an action run using a JMS server on a given queue, the context will at least
	 * describe the involved JMS server and queue.</p>
	 * @return most atomic context use by the action providing this result
	 */
	Object getContext();
	
	/**
	 * Get the Throwabl cause for this Result. Most of the time, a Throwable will cause this
	 * Result to fail, but it may be used to explain a success cause as well. The returned value
	 * may be null it there is no Throwable cause for this Result.
	 * @return any existing Throwable cause, or null
	 */
	Throwable getCause();
	
	/**
	 * Set the success (true) or failure (false) for this Helper.
	 * @param success true for success, false for failure
	 * @return self
	 */
	HelperResult<A> success(boolean success);
	
	/**
	 * Set the actual output obtained for this Result
	 * @param actual actual output
	 * @return self
	 */
	HelperResult<A> actual(A actual);
	
	/**
	 * Set the context under which the action for this Result was run
	 * @param context context under which the action for this Result was run
	 * @return self
	 */
	HelperResult<A> context(Object context);
	
	/**
	 * Set the eventual cause for success or failure of this Helper
	 * @param cause result cause
	 * @return self
	 */
	HelperResult<A> cause(Throwable cause);


}
