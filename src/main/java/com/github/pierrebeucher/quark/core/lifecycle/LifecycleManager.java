package com.github.pierrebeucher.quark.core.lifecycle;

/**
 * <p>The <code>LifecycleManager</code> managed lifecycle states for an object</p>
 * @author pierreb
 *
 */
public class LifecycleManager implements Initialisable, Disposable{

	private LifecycleState state;
	
	public LifecycleManager(){
		this(LifecycleState.NONE);
	}

	public LifecycleManager(LifecycleState initialState){
		this.state = initialState;
	}
	
	/**
	 * Set the new state. If the current state is already the given state,
	 * does nothing and return.
	 * @param state new state 
	 * @throws LifecycleException 
	 */
	public synchronized void setState(LifecycleState state) {
		if(isCurrentState(state)) return;
		this.state = state;
	}
	
	public LifecycleState getState() {
		return state;
	}

	public boolean isCurrentState(LifecycleState state){
		return this.state == state;
	}

	@Override
	public void dispose() {
		setState(LifecycleState.DISPOSED);
	}

	@Override
	public boolean isDisposed() {
		return isCurrentState(LifecycleState.DISPOSED);
	}

	@Override
	public void initialise() {
		setState(LifecycleState.INITILISASED);
	}

	@Override
	public boolean isInitialised() {
		return isCurrentState(LifecycleState.INITILISASED);
	}
}
