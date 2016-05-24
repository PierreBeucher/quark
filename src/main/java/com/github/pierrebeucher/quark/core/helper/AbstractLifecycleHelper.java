package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * <p>Base class for LifecycleHelper classes. Implements most of the lifecycle
 * contract, requiring extending classes to only implement  {@link #doInitialise()}
 * and {@link #doDispose()}</p>
 * <p>The current implementation maintain the current state of the Helper (initialised 
 * or disposed), and log a warning if the Helper has been initialised and not disposed
 * when {@link #finalize()} is called. (to avoid allocated leaving out allocated resources) </p>
 * @author pierreb
 *
 * @param <E>
 */
public abstract class AbstractLifecycleHelper<E extends HelperContext> extends AbstractHelper<E>
		implements Lifecycle{

	private boolean initialised;
	
	private boolean disposed;
	
	public AbstractLifecycleHelper(E context) {
		super(context);
		this.initialised = false;
		this.disposed = false;
	}
	
	/**
	 * Change the state of this Helper to initialised. 
	 * @param initialised
	 */
	protected void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}

	/**
	 * Change the state of this Helper to disposed.
	 * @param disposed
	 */
	protected void setDisposed(boolean disposed) {
		this.disposed = disposed;
	}
	
	/**
	 * This method is called by {@link #initialise()} immediately before setting the Helper as initialised.
	 * Perform resources allocations for this helper. 
	 */
	protected abstract void doInitialise() throws InitializationException;
	
	/**
	 * This method is called by {@link #dispose()} immediately after setting the helper as disposed.
	 * It should not thrown any RuntimeException as it would prevent other resources to be
	 * freed, but log encountered errors instead. 
	 * Perform freeing-up of resources for this Helper. 
	 */
	protected abstract void doDispose();

	@Override
	public void initialise() throws InitializationException {
		if(isInitialised()) return;
		if(isDisposed()) throw new InitializationException("Cannot initialise a disposed Helper");
		
		doInitialise();
		setInitialised(true);
	}
	
	@Override
	public void dispose() {
		if(isDisposed()) return;
		
		doDispose();
		setDisposed(true);
		setInitialised(false);
	}
	
	@Override
	public boolean isInitialised() {
		return initialised && !disposed;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	protected void finalize() throws Throwable {
		if(!isFinaliseReady()){
			logger.warn("finalise() called for initialised but non disposed helper: {} ({})", this, hashCode());			
		}
		super.finalize();
	}
	
	/**
	 * The Helper is ready to be finalised if it has either not been initialised, or it has been initialised
	 * and disposed. 
	 * @return true if this Helper is ready to be finalised.
	 */
	public boolean isFinaliseReady(){
		return !isInitialised() || isDisposed();
	}

}
