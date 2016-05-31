package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * <code>Helper</code> is the base interface for all Helpers. It provides
 * basic functions defining the <code>Helper</code> principle: a <code>HelperContext</code>
 * under which perform test actions. 
 *  
 * @author Pierre Beucher
 *
 */
public interface Helper {
	
	/**
	 * 
	 * @return The <code>HelperContext</code> managed by this <code>Helper</code>
	 */
	HelperContext getContext();
	
	/**
	 * <p>Check whether or not this <code>Helper</code> is ready for work.
	 * Depending on the implementation, various conditions may
	 * be required for the <code>Helper</code> to be ready.</p>
	 * <p>A <code>Helper</code> is ready for work if its test functions 
	 * can be used without further configuration or initialization.
	 * @return true if this Helper is ready for work, false otherwise
	 */
	boolean isReady();
}
