package com.github.pierrebeucher.quark.core.lifecycle;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LifecycleManagerTest {

	@Test
	public void LifecycleManager() {
		LifecycleManager manager = new LifecycleManager();
		Assert.assertEquals(manager.getState(), LifecycleState.NONE);
	}

	@Test
	public void LifecycleManagerLifecycleState() {
		LifecycleManager manager = new LifecycleManager(LifecycleState.DISPOSED);
		Assert.assertEquals(manager.getState(), LifecycleState.DISPOSED);
	}

	@Test
	public void dispose() {
		LifecycleManager manager = new LifecycleManager();
		manager.dispose();
		Assert.assertEquals(manager.getState(), LifecycleState.DISPOSED);
	}

	@Test
	public void initialise() {
		LifecycleManager manager = new LifecycleManager();
		manager.initialise();
		Assert.assertEquals(manager.getState(), LifecycleState.INITILISASED);
	}

	@Test
	public void isCurrentState() {
		LifecycleManager manager = new LifecycleManager(LifecycleState.INITILISASED);
		Assert.assertTrue(manager.isCurrentState(LifecycleState.INITILISASED),
				"Current state should be " + LifecycleState.INITILISASED);
	}
}
