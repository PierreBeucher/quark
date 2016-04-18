package com.github.pierrebeucher.quark.core.result;

public class StringHelperResult extends BaseHelperResult<String> implements HelperResult<String>{

	public StringHelperResult(boolean success, String actual, String message, Throwable cause) {
		super(success, actual, message, cause);
	}

	public StringHelperResult(boolean success, String actual, String message) {
		super(success, actual, message);
	}

	public StringHelperResult(boolean success, String actual) {
		super(success, actual);
	}

}
