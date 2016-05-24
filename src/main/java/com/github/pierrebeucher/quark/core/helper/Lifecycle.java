package com.github.pierrebeucher.quark.core.helper;

/**
 * <code>Lifecycle<code> provide an object allocating and disposing resources as it is used
 * via {@link #initialise()} and {@link #dispose()} methods.
 * @author pierreb
 *
 */
public interface Lifecycle {

	/**
	 * Initialize this helper, allocating required resources. Calling
	 * this method on already initialised Helper ({@link #isInitialised()} return true)
	 * does nothing. 
	 * @throws HelperException if an an error prevent proper initialization
	 */
	void initialise() throws InitializationException;
	
	/**
	 * 
	 * @return true if this Helper is initialized (a susccessful call to {@link #initialise()} has been made), false otherwise
	 */
	boolean isInitialised();
	
	/**
	 * Dispose of this helper, freeing up any resources. If an error occur, this method should
	 * not throw any exception, but log the error instead. 
	 * Calling this method on already disposed Helper ({@link #isDisposed()} returns true) does nothing.  
	 */
	void dispose();
	
	/**
	 * 
	 * @return true if this Helper has been disposed of (a successful call to {@link #dispose()}), false otherwise.
	 */
	boolean isDisposed();
}
