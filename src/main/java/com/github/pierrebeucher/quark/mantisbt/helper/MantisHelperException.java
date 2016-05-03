package com.github.pierrebeucher.quark.mantisbt.helper;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public class MantisHelperException extends HelperException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4485658387420637564L;

	protected MantisHelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MantisHelperException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MantisHelperException(String arg0) {
		super(arg0);
	}

	public MantisHelperException(Throwable arg0) {
		super(arg0);
	}

}
