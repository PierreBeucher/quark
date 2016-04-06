package org.atom.quark.core.result;

/**
 * Factory class for HelperResult.
 * @author Pierre Beucher
 *
 */
public class ResultBuilder {
	
	/**
	 * Create a failure with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return failure result
	 */
	public static <A> TypedHelperResult<A> failure(A actual){
		return result(false, actual);
	}
	
	/**
	 * Create a success with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return success result
	 */
	public static <A> TypedHelperResult<A> success(A actual){
		return result(true, actual);
	}
	
	public static <A> TypedHelperResult<A> result(boolean success, A actual) {
		return new TypedHelperResult<A>(success, actual);
	}
	
	public static <A, E> TypedExpectingHelperResult<A, E> result(boolean success, A actual, E expected){
		return new TypedExpectingHelperResult<A, E>(success, actual, expected);
	}
	
	public static <A, E> TypedExpectingHelperResult<A, E> success(A actual, E expected){
		return result(true, actual, expected);
	}
	
	public static <A, E> TypedExpectingHelperResult<A, E> failure(A actual, E expected){
		return result(false, actual, expected);
	}
}
