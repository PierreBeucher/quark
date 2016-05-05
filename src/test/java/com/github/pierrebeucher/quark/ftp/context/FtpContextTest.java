package com.github.pierrebeucher.quark.ftp.context;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FtpContextTest {
	
	private String host = "host";
	private String login = "login";
	private String password = "pass";
	private int port = 21;

	@Test
	public void FtpContext() {
		FtpContext ctx = new FtpContext();
		Assert.assertNull(ctx.getHost());
		Assert.assertNull(ctx.getLogin());
		Assert.assertNull(ctx.getPassword());
		Assert.assertFalse(ctx.getPort() > 0);
	}

	@Test
	public void FtpContextFtpContext() {
		FtpContext base = new FtpContext(host, port, login, password);
		FtpContext ctx = new FtpContext(base);
		Assert.assertEquals(base.getHost(), ctx.getHost());
		Assert.assertEquals(base.getPort(), ctx.getPort());
		Assert.assertEquals(base.getPassword(), ctx.getPassword());
		Assert.assertEquals(base.getLogin(), ctx.getLogin());
	}

	@Test
	public void FtpContextStringintString() {
		FtpContext ctx = new FtpContext(host, port, login);
		Assert.assertEquals(ctx.getHost(), host);
		Assert.assertEquals(ctx.getPort(), port);
		Assert.assertEquals(ctx.getLogin(), login);
		Assert.assertEquals(ctx.getPassword(), "");
	}

	@Test
	public void FtpContextStringintStringString() {
		FtpContext ctx = new FtpContext(host, port, login, password);
		Assert.assertEquals(ctx.getHost(), host);
		Assert.assertEquals(ctx.getPort(), port);
		Assert.assertEquals(ctx.getLogin(), login);
		Assert.assertEquals(ctx.getPassword(), password);
	}

	@Test
	public void toUri() throws URISyntaxException{
		FtpContext ctx = new FtpContext(host, port, login, password);
		URI compare = new URI("ftp://" + login + "@" + host + ":" + port);
		URI uri = ctx.toUri();
		Assert.assertEquals(compare, uri);
	}
}
