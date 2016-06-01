package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;
import com.github.pierrebeucher.quark.core.lifecycle.LifecycleManager;
import com.github.pierrebeucher.quark.core.lifecycle.LifecycleState;

/**
 * Abstract <code>LifecycleHelper</code> implementation providing a {@link com.github.pierrebeucher.quark.core.lifecycle.LifecycleManager}
 *
 * @param <E>
 */
public abstract class AbstractLifecycleHelper<E extends HelperContext> extends AbstractHelper<E>
		implements LifecycleHelper{

	protected LifecycleManager lifecycleManager;
	
	public AbstractLifecycleHelper(E context) {
		super(context);
		this.lifecycleManager = new LifecycleManager(this);
	}

	@Override
	public LifecycleState getState() {
		return lifecycleManager.getState();
	}

	public LifecycleManager getLifecycleManager() {
		return lifecycleManager;
	}

	@Override
	protected void finalize() throws Throwable {
		if(!isFinaliseReady()){
			logger.warn("finalise() called for initialised but non disposed helper: {} ({})", this, hashCode());			
		}
		super.finalize();
	}
	
	/**
	 * <p>Check whether this <code>Helper</code> is ready to be finalised or not.
	 * A <code>Helper</code> is ready to be finalised if all resources allocated
	 * has been freed properly.</p>
	 * <p>Current implementation behavior is as follow:
	 * <ul>
	 * <li>Always returns false if the {@link com.github.pierrebeucher.quark.core.lifecycle.Disposable} is not implemented</li>
	 * <li>If the {@link com.github.pierrebeucher.quark.core.lifecycle.Disposable} is implemented, then:</li>
	 * 	<ul>
	 * 	<li>Returns true if the current state is {@link com.github.pierrebeucher.quark.core.lifecycle.LifecycleState#DISPOSED}</li>
	 * 	<li>Returns true if the <code>Helper</code> is in its default state (e.g. its lifecycle never began)</li>
	 * 	<li>Returns false if the <code>Helper</code> is not in its default state (e.g. its lifecycle began, meaning resources
	 * 	may has been allocated)</li>
	 * 	</ul>
	 * </ul>Current implementation will check if the {@link com.github.pierrebeucher.quark.core.lifecycle.Disposable}
	 * interface is implemented. f it is, then the <code>Helper</code> should either be disposed or being in its initial state.  
	 * @return true if this Helper is ready to be finalised.
	 */
	public boolean isFinaliseReady(){
		//if does not implement Disposable, always finalise ready
		//otherwise, should be disposed
		return !lifecycleManager.isDisposable() ||
				(lifecycleManager.isDisposable() &&
						( lifecycleManager.isDisposed() || !lifecycleManager.isInitialised()));
	}

}
