package com.github.pierrebeucher.quark.core.context.authentication;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.authentication.PasswordAuthContext;

public class PasswordAuthContextTest {

	private static final String LOGIN = "login";
	private static final String PASS = "pass";
	@Test
	public void PasswordAuthContext() {
		PasswordAuthContext ctx = new PasswordAuthContext();
		Assert.assertNull(ctx.getLogin());
		Assert.assertNull(ctx.getPassword());
	}

	@Test
	public void PasswordAuthContextString() {
		PasswordAuthContext ctx = new PasswordAuthContext(LOGIN);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertNull(ctx.getPassword());
	}

	@Test
	public void PasswordAuthContextStringString() {
		PasswordAuthContext ctx = new PasswordAuthContext(LOGIN, PASS);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertEquals(ctx.getPassword(), PASS);
	}
	
	@Test
	public void PasswordAuthContextCopy() {
		PasswordAuthContext base = new PasswordAuthContext(LOGIN, PASS);
		PasswordAuthContext ctx = new PasswordAuthContext(base);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertEquals(ctx.getPassword(), PASS);
	}

	@Test
	public void password() {
		PasswordAuthContext ctx = new PasswordAuthContext();
		ctx.setPassword(PASS);
		Assert.assertEquals(ctx.getPassword(), PASS);
	}
}
