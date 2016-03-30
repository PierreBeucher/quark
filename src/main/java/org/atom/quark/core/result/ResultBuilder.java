package org.atom.quark.core.result;

/**
 * Factory class for HelperResult.
 * @author Pierre Beucher
 *
 */
public class ResultBuilder {

	/**
	 * Build a success result, without specific output data
	 * @return
	 */
	public static BooleanHelperResult success(){
		return new BooleanHelperResult(true);
	}
	
	/**
	 * Build a failure result, without specific output data
	 * @return
	 */
	public static BooleanHelperResult failure(){
		return new BooleanHelperResult(false);
	}
	
	/**
	 * Build a new result, the expected data being identical to the obtained one.
	 * @param output the obtained and expected data 
	 * @return
	 */
	public static <E> TypedHelperResult<E, E> success(E output){
		return new SimpleHelperResult<E>(true, output, output);
	}
	
	/**
	 * Create a failure with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return failure result
	 */
	public static <E, O> TypedHelperResult<E, O> failure(E expected, O obtained){
		return new TypedHelperResult<E, O>(false, expected, obtained);
	}
	
	/**
	 * Create a success with the expected and obtained output.
	 * @param expected the expected output
	 * @param obtained the obtained output
	 * @return success result
	 */
	public static <E, O> TypedHelperResult<E, O> success(E expected, O obtained){
		return new TypedHelperResult<E, O>(true, expected, obtained);
	}
	
	public static <E> SimpleHelperResult<E> successSimple(E expected, E obtained){
		return resultSimple(true, expected, obtained);
	}
	
	public static <E> SimpleHelperResult<E> failureSimple(E expected, E obtained){
		return resultSimple(false, expected, obtained);
	}
	
	public static <E> SimpleHelperResult<E> resultSimple(boolean success, E expected, E obtained){
		return new SimpleHelperResult<E>(success, expected, obtained);
	}
	
	public static <E, O> TypedHelperResult<E, O> result(boolean success, E expected, O obtained) {
		return new TypedHelperResult<E, O>(success, expected, obtained);
	}
}
