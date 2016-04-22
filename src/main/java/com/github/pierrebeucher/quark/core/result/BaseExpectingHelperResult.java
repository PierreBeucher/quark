package com.github.pierrebeucher.quark.core.result;

public class BaseExpectingHelperResult<A, E> extends BaseHelperResult<A>
		implements ExpectingHelperResult<A, E>{

	private E expected;
	
	public BaseExpectingHelperResult(boolean success, A actual, E expected, String message,
			Throwable cause) {
		super(success, actual, message, cause);
		this.expected = expected;
	}
	
	public BaseExpectingHelperResult(boolean success, A actual, E expected, String message) {
		super(success, actual, message);
		this.expected = expected;
	}
	
	public BaseExpectingHelperResult(boolean success, A actual, E expected) {
		super(success, actual);
		this.expected = expected;
	}
	
	@Override
	public Object getExpected() {
		return expected;
	}
	
	public ExpectingHelperResult<A, E> expected(E expected){
		this.expected = expected;
		return this;
	}

}
