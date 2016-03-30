package org.atom.quark.core.result;

/**
 * The SimpleHelperResult is a HelperResult with an expected and actual
 * value using the same type. 
 * 
 * @author Pierre Beucher
 *
 * @param <E>
 */
public class SimpleHelperResult<E> extends TypedHelperResult<E, E>{

	public SimpleHelperResult(boolean success, E expected, E actual) {
		super(success, expected, actual);
	}
}
