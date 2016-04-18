package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

public abstract class AbstractHelper<E extends HelperContext> implements Helper {

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

	/**
	 * Set the context to be used by this Helper. As changing the context
	 * may cause come helper to re-initialize some component, an Exception
	 * may be thrown if said context is not properly configured.
	 * @param context
	 * @throws Exception
	 */
	public void setContext(E context) throws Exception {
		this.context = context;
	}
}
