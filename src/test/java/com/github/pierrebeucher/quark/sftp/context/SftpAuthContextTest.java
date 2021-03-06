package com.github.pierrebeucher.quark.sftp.context;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;

public class SftpAuthContextTest {
	
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String KEY = "/tmp/key";
	private static final String KEYPASS = "passphrase";

	@Test
	public void SftpAuthContext() {
		SftpAuthContext ctx = new SftpAuthContext();
		Assert.assertNull(ctx.getLogin());
		Assert.assertNull(ctx.getPassword());
		Assert.assertNull(ctx.getPrivateKey());
		Assert.assertNull(ctx.getPrivateKeyPassword());
	}

	@Test
	public void SftpAuthContextStringString() {
		SftpAuthContext ctx = new SftpAuthContext(LOGIN, PASSWORD);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertEquals(ctx.getPassword(), PASSWORD);
	}

	@Test
	public void SftpAuthContextStringStringString() {
		SftpAuthContext ctx = new SftpAuthContext(LOGIN, KEY, KEYPASS);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertEquals(ctx.getPrivateKey(), KEY);
		Assert.assertEquals(ctx.getPrivateKeyPassword(), KEYPASS);
	}
	
	@Test
	public void SftpAuthContextCopy() {
		SftpAuthContext base = new SftpAuthContext(LOGIN, PASSWORD, KEY, KEYPASS);
		SftpAuthContext ctx = new SftpAuthContext(base);
		Assert.assertEquals(ctx.getLogin(), LOGIN);
		Assert.assertEquals(ctx.getPassword(), PASSWORD);
		Assert.assertEquals(ctx.getPrivateKey(), KEY);
		Assert.assertEquals(ctx.getPrivateKeyPassword(), KEYPASS);
	}
}
