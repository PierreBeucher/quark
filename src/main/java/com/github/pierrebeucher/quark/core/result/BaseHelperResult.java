package com.github.pierrebeucher.quark.core.result;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

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
	
	private Object context;
	
	public BaseHelperResult() {
		super();
	}

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
	public String getDescription() {
		StringBuilder builder = new StringBuilder(success ? SUCCESS : FAILURE)
				.append(": ")
				.append(actual);
		
		if(cause != null){
			builder.append(", cause: ").append(cause);
		}
		
		if(context != null){
			builder.append(" w/ ").append(context);
		}
		
		return builder.toString();
	}

	@Override
	public Throwable getCause() {
		return this.cause;
	}

	/**
	 * Same as calling {@link #getDescription()}
	 */
	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public Object getContext() {
		return this.context;
	}

	public void setContext(HelperContext context) {
		this.context = context;
	}
	
	@Override
	public BaseHelperResult<A> success(boolean success){
		this.success = success;
		return this;
	}
	
	@Override
	public BaseHelperResult<A> actual(A actual){
		this.actual = actual;
		return this;
	}
	
	@Override
	public BaseHelperResult<A> context(Object context){
		this.context = context;
		return this;
	}
	
	@Override
	public BaseHelperResult<A> cause(Throwable cause){
		this.cause = cause;
		return this;
	}
}
