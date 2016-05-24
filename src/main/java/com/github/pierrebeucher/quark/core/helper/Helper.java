package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * Test Helper interface. All test Helpers inherit this class.
 * 
 * A Test Helper is used to perform test actions in a defined environment.
 * TODO should not be parameterized with generic, and use Object instead 
 * @author Pierre Beucher
 *
 */
public interface Helper {
	
	/**
	 * 
	 * @return The Descriptor representing this Helper context
	 */
	HelperContext getContext();
	
	/**
	 * Check whether or not this Helper is ready for work.
	 * To be ready for work, a Helper should have a properly
	 * configured Context. If some parameters or elements are missing
	 * in its Context, the Helper is not ready.
	 * @return true if this Helper is ready, false otherwise
	 */
	boolean isReady();
}
