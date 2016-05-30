package com.github.pierrebeucher.quark.core.lifecycle;

public interface Disposable {
	
	/**
	 * Dispose of this helper, freeing up any resources. If an error occur, this method should
	 * not throw any exception, but log the error instead. 
	 * Calling this method on already disposed Helper ({@link #isDisposed()} returns true) does nothing.  
	 */
	void dispose() throws DisposeException;
	
	/**
	 * 
	 * @return true if this Helper has been disposed of (a successful call to {@link #dispose()}), false otherwise.
	 */
	boolean isDisposed();

}
