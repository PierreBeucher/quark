package com.github.pierrebeucher.quark.jdbc.helper;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public class JdbcHelperException extends HelperException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9221162433551636022L;

	protected JdbcHelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public JdbcHelperException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JdbcHelperException(String arg0) {
		super(arg0);
	}

	public JdbcHelperException(Throwable arg0) {
		super(arg0);
	}

}
