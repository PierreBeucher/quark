package com.github.pierrebeucher.quark.core.lifecycle;

import com.github.pierrebeucher.quark.core.helper.HelperException;

public interface Initialisable {
	
	/**
	 * Initialize this helper, allocating required resources. Calling
	 * this method on already initialised Helper ({@link #isInitialised()} return true)
	 * does nothing. 
	 * @throws HelperException if an an error prevent proper initialization
	 */
	void initialise() throws InitialisationException;
	
	/**
	 * 
	 * @return true if this Helper is initialized (a susccessful call to {@link #initialise()} has been made), false otherwise
	 */
	boolean isInitialised();

}
