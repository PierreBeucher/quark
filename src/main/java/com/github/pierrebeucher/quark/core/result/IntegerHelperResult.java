package com.github.pierrebeucher.quark.core.result;

public class IntegerHelperResult extends BaseHelperResult<Integer>
		implements HelperResult<Integer> {

	/**
	 * @param success
	 * @param actual
	 * @param message
	 * @param cause
	 */
	public IntegerHelperResult(boolean success, Integer actual, String message, Throwable cause) {
		super(success, actual, message, cause);
	}

	/**
	 * @param success
	 * @param actual
	 * @param message
	 */
	public IntegerHelperResult(boolean success, Integer actual, String message) {
		super(success, actual, message);
	}

	/**
	 * @param success
	 * @param actual
	 */
	public IntegerHelperResult(boolean success, Integer actual) {
		super(success, actual);
	}

}
