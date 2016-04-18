package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

public abstract class AbstractCleaningHelper<E extends HelperContext> extends AbstractHelper<E> implements Helper, CleaningHelper {

	public AbstractCleaningHelper(E context) {
		super(context);
	}

	@Override
	public void clean() throws Exception {
		clean(CleaningHelper.DEFAULT_CLEAN_METHOD);
	}

	@Override
	public void clean(CleaningMethod cleaningMethod) throws Exception {
		switch(cleaningMethod){
		case SAFE:
			cleanSafe();
			break;
		case HARD:
			cleanHard();
			break;
		case NONE:
			cleanNone();
			break;
		default:
			throw new RuntimeException("Unknown cleaning method:" + cleaningMethod);
		}
	}
	
	/**
	 * Clean safely the context managed by this helper.
	 * @throws Exception
	 */
	protected abstract void cleanSafe() throws Exception;
	
	/**
	 * Clean hardly the context managed by this helper.
	 * @throws Exception
	 */
	protected abstract void cleanHard() throws Exception;
	
	/**
	 * Does nothing unless overriden. 
	 */
	protected void cleanNone(){
		
	}

}
