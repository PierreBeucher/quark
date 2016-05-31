package com.github.pierrebeucher.quark.core.lifecycle;

/**
 * <code>Disposable</code> represent a component which can dispose of allocated resources on demand. 
 * @author pierreb
 *
 */
public interface Disposable {
	
	/**
	 * Dispose of this component, freeing up any resources. If an error occur, this method should
	 * not throw any exception, but log the error instead. 
	 * Calling this method on already disposed component ({@link #isDisposed()} returns true) does nothing.  
	 */
	void dispose() throws DisposeException;
	
	/**
	 * 
	 * @return true if this component has been disposed of (a successful call to {@link #dispose()}), false otherwise. If resources
	 * has never been allocated, this method returns false.
	 */
	boolean isDisposed();

}
