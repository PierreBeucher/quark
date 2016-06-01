package com.github.pierrebeucher.quark.core.lifecycle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.pierrebeucher.quark.core.helper.Helper;

/**
 * <p>The <code>LifecycleManager</code> managed lifecycle states for an object</p>
 * @author pierreb
 *
 */
public class LifecycleManager implements Initialisable, Disposable{

	/**
	 * State initially defined when no LifecycleState is specified in constructor
	 */
	public static LifecycleState DEFAULT_STATE = LifecycleState.NONE;
	
	/*
	 * Helper for which the lifecycle is managed
	 */
	private Helper helper;
	
	/*
	 * current state
	 */
	private LifecycleState state;
	
	/*
	 * Set of available states for this Helper
	 */
	private Set<LifecycleState> availableState;
	
	public LifecycleManager(Helper helper){
		this(helper, LifecycleState.NONE);
	}

	public LifecycleManager(Helper helper, LifecycleState initialState){
		this.helper = helper;
		this.state = initialState;
		this.availableState = new HashSet<LifecycleState>();
		registerAvailableLifecycleState();
	}
	
	/**
	 * Register all available LifecycleState for the managed Helper
	 */
	private void registerAvailableLifecycleState(){
		availableState.add(LifecycleState.NONE);
		if(helper instanceof Initialisable){
			availableState.add(LifecycleState.INITILISASED);
		}
		if(helper instanceof Disposable){
			availableState.add(LifecycleState.DISPOSED);
		}
	}
	
	/**
	 * 
	 * @return an unmodifiable Set composed of available States
	 */
	public Set<LifecycleState> getAvailableStates(){
		return Collections.unmodifiableSet(availableState);
	}
	
	/**
	 * Check whether a LifecycleState is available.
	 * @param state state for which to check availability
	 * @return true if state available, false otherwise
	 */
	public boolean isStateAvailable(LifecycleState state){
		return availableState.contains(state);
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

	public Helper getHelper() {
		return helper;
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
	
	/**
	 * 
	 * @return true if the DISPOSED state is available.
	 */
	public boolean isDisposable(){
		return isStateAvailable(LifecycleState.DISPOSED);
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
