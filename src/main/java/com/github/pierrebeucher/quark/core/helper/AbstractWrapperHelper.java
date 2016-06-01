package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * Abstract implementation of the <code>WrapperHelper</code>, sharing the same
 * context as the wrapped helper.
 * @author pierreb
 *
 * @param <H> Helper type
 * @param <C> HelperContext type
 */
public class AbstractWrapperHelper<C extends HelperContext, H extends Helper> extends AbstractHelper<C> implements WrapperHelper{

	protected H helper;
	
	public AbstractWrapperHelper(C context, H h) {
		super(context);
		this.helper = h;
	}

	/**
	 * The <code>WrapperHelper</code> is ready if its underlying Helper is ready.
	 */
	@Override
	public boolean isReady() {
		return helper.isReady();
	}

	@Override
	public H getWrappedHelper() {
		return helper;
	}

	/**
	 * Same as calling underlying helper toString() method.
	 */
	@Override
	public String toString() {
		return helper.toString();
	}

}
