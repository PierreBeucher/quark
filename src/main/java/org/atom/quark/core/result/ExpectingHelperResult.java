package org.atom.quark.core.result;

/**
 * HelperResult interface providing access to the final expected data.
 * @author Pierre Beucher
 *
 */
public interface ExpectingHelperResult extends HelperResult {

	/**
	 * 
	 * @return The expected output for this result.
	 */
	Object getExpected();

}
