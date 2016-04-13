package org.atom.quark.cmis.context;

import org.testng.Assert;
import org.testng.annotations.Test;

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
}
