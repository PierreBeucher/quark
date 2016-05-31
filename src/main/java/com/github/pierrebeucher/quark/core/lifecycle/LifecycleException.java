package com.github.pierrebeucher.quark.core.lifecycle;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public class LifecycleException extends HelperException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1517092455685508427L;

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
