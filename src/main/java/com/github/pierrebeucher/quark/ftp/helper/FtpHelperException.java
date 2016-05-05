package com.github.pierrebeucher.quark.ftp.helper;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public class FtpHelperException extends HelperException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3919668384087266127L;

	protected FtpHelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public FtpHelperException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FtpHelperException(String arg0) {
		super(arg0);
	}

	public FtpHelperException(Throwable arg0) {
		super(arg0);
	}

}
