package com.github.pierrebeucher.quark.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * Base class for any Helper. Provide a HelperContext and a Logger for extending class.
 * 
 * @author pierreb
 *
 * @param <E>
 */
public abstract class AbstractHelper<E extends HelperContext> implements Helper {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Context managed by this helper
	 */
	protected E context;

	public AbstractHelper(E context) {
		this.context = context;
	}

	public E getContext() {
		return context;
	}

	public void setContext(E context){
		this.context = context;
	}
}
