package org.atom.quark.core.result;

/**
 * A typed helper result, defining the type of the actual and expected
 * values.
 * @author Pierre Beucher
 *
 * @param <E>
 * @param <A>
 */
public class TypedHelperResult<E, A> implements HelperResult {

	private boolean success;
	
	private A actual;
	
	private E expected;
	
	public TypedHelperResult(boolean success, E expected, A actual) {
		this.success = success;
		this.expected = expected;
		this.actual = actual;
	}

	public boolean isSuccess() {
		return success;
	}

	public E getExpected() {
		return expected;
	}

	public A getActual() {
		return actual;
	}

	public String getResultDescription() {
		
		StringBuffer buf = new StringBuffer(isSuccess() ? SUCCESS : FAILURE)
				.append(":{")
				.append(getExpected())
				.append("}-{")
				.append(getActual())
				.append("}");
		return buf.toString();
	}

}
