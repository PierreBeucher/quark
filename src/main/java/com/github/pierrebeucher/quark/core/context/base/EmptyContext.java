package com.github.pierrebeucher.quark.core.context.base;

/**
 * The empty context, not defining any attribute. This singleton may be used 
 * when describing contexts not requiring any parameter or attribute.
 * @author Pierre Beucher
 *
 */
public final class EmptyContext implements HelperContext{
	
	private static final EmptyContext ctx = new EmptyContext();

	/**
	 * 
	 * @return the unique EmptyContext instance
	 */
	public static EmptyContext context() {
		return ctx;
	}
}
