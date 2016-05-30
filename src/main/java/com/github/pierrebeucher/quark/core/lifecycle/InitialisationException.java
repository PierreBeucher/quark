package com.github.pierrebeucher.quark.core.lifecycle;

public class InitialisationException extends LifecycleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4265706147116487506L;

	public InitialisationException() {
		super();
	}

	public InitialisationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitialisationException(String message) {
		super(message);
	}

	public InitialisationException(Throwable cause) {
		super(cause);
	}

}
