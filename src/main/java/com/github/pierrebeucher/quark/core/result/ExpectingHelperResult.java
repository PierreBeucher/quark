package com.github.pierrebeucher.quark.core.result;

/**
 * HelperResult interface providing access to the expected data.
 * @author Pierre Beucher
 *
 */
public interface ExpectingHelperResult<A, E> extends HelperResult<A> {

	/**
	 * 
	 * @return The expected output for this result.
	 */
	Object getExpected();

}
