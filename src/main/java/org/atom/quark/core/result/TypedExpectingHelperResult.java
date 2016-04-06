package org.atom.quark.core.result;

public class TypedExpectingHelperResult<A, E> extends TypedHelperResult<A>
		implements ExpectingHelperResult{

	private E expected;
	
	public TypedExpectingHelperResult(boolean success, A actual, E expected) {
		super(success, actual);
		this.expected = expected;
	}

	@Override
	public Object getExpected() {
		return expected;
	}

}
