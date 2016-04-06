package org.atom.quark.core.result;

/**
 * A typed helper result, defining the type of the actual and expected
 * values.
 * @author Pierre Beucher
 *
 * @param <E>
 * @param <A>
 */
public class TypedHelperResult<A> implements HelperResult {

	private boolean success;
	
	private A actual;
	
	
	public TypedHelperResult(boolean success, A actual) {
		this.success = success;
		this.actual = actual;
	}

	public boolean isSuccess() {
		return success;
	}

	public A getActual() {
		return actual;
	}

	public String getResultDescription() {
		
		StringBuffer buf = new StringBuffer(isSuccess() ? SUCCESS : FAILURE)
				.append(":")
				.append(getActual());
		return buf.toString();
	}

}
