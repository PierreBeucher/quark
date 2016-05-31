package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.lifecycle.LifecycleState;

/**
 * The <code>LifecycleHelper</code> define a <code>Helper</code> lifecycle to
 * describe its readiness. A <code>LifecycleState</code> describe the current Helper state,
 * directly impacting the behavior of {@link #isReady()} method. 
 * @author pierreb
 *
 */
public interface LifecycleHelper extends Helper {

	/**
	 * The current <code>Helper</code> state. Depending
	 * of its state, it may be ready or not.
	 * @return the current <code>Helper</code> state.
	 */
	public LifecycleState getState();
}
