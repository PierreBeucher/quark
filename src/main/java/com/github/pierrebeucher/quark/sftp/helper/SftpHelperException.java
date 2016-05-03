package com.github.pierrebeucher.quark.sftp.helper;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public class SftpHelperException extends HelperException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 43674526809658421L;

	protected SftpHelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SftpHelperException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SftpHelperException(String arg0) {
		super(arg0);
	}

	public SftpHelperException(Throwable arg0) {
		super(arg0);
	}

}
