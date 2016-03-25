package org.atom.quark.core.result;

/**
 * Factory class for HelperResult.
 * @author Pierre Beucher
 *
 */
public class ResultBuilder {

	public static <E> HelperResult<E> success(E result){
		return new SimpleHelperResult<E>(true, result);
	}
	
	public static HelperResult<Boolean> success(){
		return new BooleanHelperResult(true);
	}
	
	public static HelperResult<Boolean> failure(){
		return new BooleanHelperResult(false);
	}
	
	/**
	 * Create a failure Result using the given String as action output
	 * @param desc failure action output
	 * @return failure result with a String action output
	 */
	public static <E> HelperResult<E> failure(E actionOutput){
		return new SimpleHelperResult<E>(false, actionOutput);
	}
}
