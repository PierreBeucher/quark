package com.github.pierrebeucher.quark.mantisbt.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;

public class MantisBTCleanerTest {

	@Test
	public void MantisBTCleanerMantisBTContext() {
		MantisBTContext ctx = new MantisBTContext();
		MantisBTCleaner cleaner = new MantisBTCleaner(ctx);
		Assert.assertEquals(cleaner.getContext(), ctx);
		Assert.assertEquals(cleaner.getWrappedHelper().getContext(), ctx);
	}

	@Test
	public void MantisBTCleanerMantisBTHelper() {
		MantisBTContext ctx = new MantisBTContext();
		MantisBTHelper helper = new MantisBTHelper(ctx);
		MantisBTCleaner cleaner = new MantisBTCleaner(helper);
		Assert.assertEquals(cleaner.getWrappedHelper(), helper);
		Assert.assertEquals(cleaner.getContext(), ctx);
		Assert.assertEquals(cleaner.getWrappedHelper().getContext(), ctx);
	}
}
