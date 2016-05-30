package com.github.pierrebeucher.quark.core.lifecycle;

public class DisposeException extends LifecycleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5777481038259222419L;

	public DisposeException() {
		super();
	}

	public DisposeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DisposeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DisposeException(String message) {
		super(message);
	}

	public DisposeException(Throwable cause) {
		super(cause);
	}

}
