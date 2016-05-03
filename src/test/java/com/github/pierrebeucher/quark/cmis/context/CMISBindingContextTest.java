package com.github.pierrebeucher.quark.cmis.context;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;

public class CMISBindingContextTest {

	private String user = "user";
	private String password = "password";
	private String bindingType = CMISBindingContext.BINDING_BROWSER;
	
	@Test
	public void CMISBindingContext() {
		CMISBindingContext ctx = new CMISBindingContext(){};
		Assert.assertNull(ctx.getBindingType());
		Assert.assertNull(ctx.getUser());
		Assert.assertNull(ctx.getPassword());
	}

	@Test
	public void CMISBindingContextStringStringStringString() {
		CMISBindingContext ctx = new CMISBindingContext(bindingType, user, password) {};
		Assert.assertEquals(ctx.getBindingType(), bindingType);
		Assert.assertEquals(ctx.getUser(), user);
		Assert.assertEquals(ctx.getPassword(), password);
	}
	
	@Test
	public void CMISBindingContextCopy() {
		CMISBindingContext base = new CMISBindingContext(bindingType, user, password) {};
		CMISBindingContext ctx = new CMISBindingContext(base) {};
		Assert.assertEquals(ctx.getBindingType(), bindingType);
		Assert.assertEquals(ctx.getUser(), user);
		Assert.assertEquals(ctx.getPassword(), password);
	}
}
