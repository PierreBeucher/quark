package com.github.pierrebeucher.quark.core.helper;

/**
 * A <code>WrapperHelper</code> is a <code>Helper</code> wrapping another 
 * to provide additional functionality on top of it.
 * @author pierreb
 *
 */
public interface WrapperHelper extends Helper {

	/**
	 * 
	 * @return the underlying wrapper <code>Helper</code> instance
	 */
	public Helper getWrappedHelper();
}
