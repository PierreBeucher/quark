package com.github.pierrebeucher.quark.core.helper;

public interface HelperBuilder {

	/**
	 * Return the base context used by this builder. Helper instances will
	 * be created based on a copy of this base context and its configuration,
	 * optionally defining over attributes.
	 * @return the base context instance
	 */
	Object getBaseContext();
	
}
