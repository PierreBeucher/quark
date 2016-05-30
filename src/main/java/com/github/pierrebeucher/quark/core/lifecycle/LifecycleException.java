package com.github.pierrebeucher.quark.core.lifecycle;

public class LifecycleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1517092455685508427L;

	public LifecycleException() {
		super();
	}

	public LifecycleException(String message, Throwable cause) {
		super(message, cause);
	}

	public LifecycleException(String message) {
		super(message);
	}

	public LifecycleException(Throwable cause) {
		super(cause);
	}

	protected LifecycleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
