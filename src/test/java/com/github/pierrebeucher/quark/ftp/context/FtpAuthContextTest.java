package com.github.pierrebeucher.quark.ftp.context;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FtpAuthContextTest {
	
	private String login = "login";
	private String password = "password";

	@Test
	public void FtpAuthContext() {
		FtpAuthContext ctx = new FtpAuthContext();
		Assert.assertNull(ctx.getLogin());
		Assert.assertNull(ctx.getPassword());
	}

	@Test
	public void FtpAuthContextFtpAuthContext() {
		FtpAuthContext base = new FtpAuthContext(login, password);
		FtpAuthContext ctx = new FtpAuthContext(base);
		Assert.assertEquals(ctx.getLogin(), base.getLogin());
		Assert.assertEquals(ctx.getPassword(), base.getPassword());
	}

	@Test
	public void FtpAuthContextString() {
		FtpAuthContext ctx = new FtpAuthContext(login);
		Assert.assertEquals(ctx.getLogin(),login);
		Assert.assertEquals(ctx.getPassword(), "");
	}

	@Test
	public void FtpAuthContextStringString() {
		FtpAuthContext ctx = new FtpAuthContext(login, password);
		Assert.assertEquals(ctx.getLogin(),login);
		Assert.assertEquals(ctx.getPassword(), password);
	}
}
