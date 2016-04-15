package org.atom.quark.core.result;

/**
 * Factory class for HelperResult.
 * @author Pierre Beucher
 *
 */
public class ResultBuilder {
	
	public static <A> BaseHelperResult<A> result(boolean success, A actual) {
		return new BaseHelperResult<A>(success, actual);
	}
	
	public static <A> BaseHelperResult<A> result(boolean success, A actual, String message){
		return new BaseHelperResult<A> (success, actual, message);
	}
	
	public static <A> BaseHelperResult<A> result(boolean success, A actual, String message, Throwable cause){
		return new BaseHelperResult<A> (success, actual, message, cause);
	}
	
	/**
	 * Create a failure with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return failure result
	 */
	public static <A> BaseHelperResult<A> failure(A actual){
		return result(false, actual);
	}
	
	public static <A> BaseHelperResult<A> failure(A actual, String message){
		return result(false, actual, message);
	}
	
	public static <A> BaseHelperResult<A> failure(A actual, Throwable cause){
		return result(false, actual, null, cause);
	}
	
	public static <A> BaseHelperResult<A> failure(A actual, String message, Throwable cause){
		return result(false, actual, message, cause);
	}
	
	/**
	 * Create a success with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return success result
	 */
	public static <A> BaseHelperResult<A> success(A actual){
		return result(true, actual);
	}
	
	public static <A> BaseHelperResult<A> success(A actual, String message){
		return result(true, actual, message);
	}
	
	public static <A> BaseHelperResult<A> success(A actual, String message, Throwable cause){
		return result(true, actual, message, cause);
	}
	
	public static <A, E> BaseExpectingHelperResult<A, E> expectingResult(boolean success, A actual, E expected) {
		return new BaseExpectingHelperResult<A, E>(success, actual, expected);
	}
	
	public static <A, E> BaseExpectingHelperResult<A, E> expectingResult(boolean success, A actual, E expected,
			String message) {
		return new BaseExpectingHelperResult<A, E>(success, actual, expected, message);
	}
	
	public static <A, E> BaseExpectingHelperResult<A, E> expectingResult(boolean success, A actual, E expected,
			String message, Throwable cause) {
		return new BaseExpectingHelperResult<A, E>(success, actual, expected, message, cause);
	}
	
	public static <A, E> BaseExpectingHelperResult<A, E> expectingSuccess(A actual, E expected){
		return expectingResult(true, actual, expected);
	}
	
	public static <A, E> BaseExpectingHelperResult<A, E> expectingRailure(A actual, E expected){
		return expectingResult(false, actual, expected);
	}
}
