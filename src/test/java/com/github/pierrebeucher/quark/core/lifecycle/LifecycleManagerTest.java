package com.github.pierrebeucher.quark.core.lifecycle;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.base.EmptyContext;
import com.github.pierrebeucher.quark.core.context.base.HelperContext;
import com.github.pierrebeucher.quark.core.helper.Helper;

public class LifecycleManagerTest {
	
	/**
	 * Dummy implementation of Helper for this test
	 * @author pierreb
	 *
	 */
	private class LifecycleTestHelper implements Helper{

		@Override
		public HelperContext getContext() {
			return EmptyContext.context();
		}

		@Override
		public boolean isReady() {
			return true;
		}
	}

	@Test
	public void LifecycleManager() {
		Helper h = new LifecycleTestHelper();
		LifecycleManager manager = new LifecycleManager(h);
		Assert.assertEquals(manager.getState(), LifecycleState.NONE);
		Assert.assertEquals(manager.getHelper(), h);
	}

	@Test
	public void LifecycleManagerLifecycleState() {
		Helper h = new LifecycleTestHelper();
		LifecycleManager manager = new LifecycleManager(h, LifecycleState.DISPOSED);
		Assert.assertEquals(manager.getState(), LifecycleState.DISPOSED);
		Assert.assertEquals(manager.getHelper(), h);
	}

	@Test
	public void dispose() {
		LifecycleManager manager = new LifecycleManager(new LifecycleTestHelper());
		manager.dispose();
		Assert.assertEquals(manager.getState(), LifecycleState.DISPOSED);
	}

	@Test
	public void initialise() {
		LifecycleManager manager = new LifecycleManager(new LifecycleTestHelper());
		manager.initialise();
		Assert.assertEquals(manager.getState(), LifecycleState.INITILISASED);
	}

	@Test
	public void isCurrentState() {
		LifecycleManager manager = new LifecycleManager(new LifecycleTestHelper(), LifecycleState.INITILISASED);
		Assert.assertTrue(manager.isCurrentState(LifecycleState.INITILISASED),
				"Current state should be " + LifecycleState.INITILISASED);
	}
}
