package org.atom.quark.core.helper;

import org.atom.quark.core.context.base.HelperContext;

public abstract class AbstractHelper<E extends HelperContext> implements Helper<E> {

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

	public void setContext(E context) {
		this.context = context;
	}
}
