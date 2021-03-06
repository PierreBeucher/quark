package com.github.pierrebeucher.quark.cmis.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;

public class AtomPubBindingContextTest {

	private String user = "user";
	private String password = "password";
	private String atomPubUrl = "http://localhost/atom";

	@Test
	public void AtomPubBindingContextStringStringURLString() {
		AtomPubBindingContext ctx = new AtomPubBindingContext();
		Assert.assertNull(ctx.getBindingType());
		Assert.assertNull(ctx.getPassword());
		Assert.assertNull(ctx.getUser());
		Assert.assertNull(ctx.getAtomPubUrl());
	}

	@Test
	public void AtomPubBindingContext() throws MalformedURLException {
		AtomPubBindingContext ctx = new AtomPubBindingContext(user, password, new URL(atomPubUrl));
		Assert.assertEquals(ctx.getBindingType(), CMISBindingContext.BINDING_ATOMPUB);
		Assert.assertEquals(ctx.getPassword(), password);
		Assert.assertEquals(ctx.getUser(), user);
		Assert.assertEquals(ctx.getAtomPubUrl(), new URL(atomPubUrl));
	}
	
	@Test
	public void AtomPubBindingContextCopy() throws MalformedURLException {
		AtomPubBindingContext baseContext = new AtomPubBindingContext(user, password, new URL(atomPubUrl));
		AtomPubBindingContext copyContext =  new AtomPubBindingContext(baseContext);
		Assert.assertEquals(copyContext.getBindingType(), CMISBindingContext.BINDING_ATOMPUB);
		Assert.assertEquals(copyContext.getPassword(), password);
		Assert.assertEquals(copyContext.getUser(), user);
		Assert.assertEquals(copyContext.getAtomPubUrl(), new URL(atomPubUrl));
	}
}
