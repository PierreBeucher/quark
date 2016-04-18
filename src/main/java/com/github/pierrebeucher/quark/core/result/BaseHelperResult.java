package com.github.pierrebeucher.quark.core.result;

/**
 * A typed helper result, defining the type of the actual and expected
 * values.
 * @author Pierre Beucher
 *
 */
public class BaseHelperResult<A> implements HelperResult<A> {

	private boolean success;
	
	private A actual;
	
	private Throwable cause;
	
	protected String message;
	
	public BaseHelperResult(boolean success, A actual) {
		this(success, actual, null);
	}
	
	public BaseHelperResult(boolean success, A actual, String message) {
		this(success, actual, message, null);
	}
	
	public BaseHelperResult(boolean success, A actual, String message, Throwable cause) {
		this.success = success;
		this.actual = actual;
		this.message = message;
		this.cause = cause;
	}

	public boolean isSuccess() {
		return success;
	}

	public A getActual() {
		return actual;
	}

	@Override
	public void assertSuccess() {
		if(success) return;
		
		throw buildAssertionError();
	}
	
	@Override
	public void assertFailure() {
		if(!success) return;
		
		throw buildAssertionError();
	}

	
	/**
	 * Build the AssertionError used by assertSuccess().
	 * @return a new AssertionError representing this Helper result success or failure
	 */
	protected AssertionError buildAssertionError(){
		StringBuilder builder = new StringBuilder("Expected ")
			.append(FAILURE)
			.append(" for: ")
			.append(success ? SUCCESS : FAILURE)
			.append(getMessage());
		
		if(getCause() != null){
			return new AssertionError(builder.toString(), getCause());
		} else {
			return new AssertionError(builder.toString());
		}
	}

	@Override
	public String getMessage() {
		return "[actual:" + getActual() + "|action:" + message + "]";
	}
	
	@Override
	public String getShortMessage() {
		return getActual() + "|" + message;
	}

	@Override
	public Throwable getCause() {
		return this.cause;
	}

	/**
	 * Return the value returned by the getMessage() method.
	 */
	@Override
	public String toString() {
		return getMessage();
	}
}
