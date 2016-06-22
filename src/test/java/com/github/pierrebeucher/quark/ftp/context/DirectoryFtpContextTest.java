package com.github.pierrebeucher.quark.ftp.context;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DirectoryFtpContextTest {

	private String host = "host";
	private int port = 21;
	private String login = "login";
	private String password = "password";
	private String dir = "/some/dir";
	
	@Test
	public void DirectoryFtpContext() {
		DirectoryFtpContext ctx = new DirectoryFtpContext();
		Assert.assertNull(ctx.getDirectory());
		Assert.assertNull(ctx.getLogin());
		Assert.assertNull(ctx.getPassword());
		Assert.assertEquals(ctx.getPort(), -1);
	}

	@Test
	public void DirectoryFtpContextDirectoryFtpContext() {
		DirectoryFtpContext base = new DirectoryFtpContext(host, port, login, password, dir);
		DirectoryFtpContext ctx = new DirectoryFtpContext(base);
		Assert.assertEquals(ctx.getDirectory(), base.getDirectory());
		Assert.assertEquals(ctx.getLogin(), base.getLogin());
		Assert.assertEquals(ctx.getHost(), base.getHost());
		Assert.assertEquals(ctx.getPort(), base.getPort());
		Assert.assertEquals(ctx.getPassword(), base.getPassword());
	}

	@Test
	public void DirectoryFtpContextStringintStringStringString() {
		DirectoryFtpContext ctx = new DirectoryFtpContext(host, port, login, password, dir);
		Assert.assertEquals(ctx.getDirectory(), dir);
		Assert.assertEquals(ctx.getLogin(), login);
		Assert.assertEquals(ctx.getHost(), host);
		Assert.assertEquals(ctx.getPort(), port);
		Assert.assertEquals(ctx.getPassword(), password);
	}

	@Test
	public void getDirectory() {
		DirectoryFtpContext ctx = new DirectoryFtpContext(host, port, login, password, dir);
		Assert.assertEquals(ctx.getDirectory(), dir);
	}

	@Test
	public void toUri() throws URISyntaxException {
		DirectoryFtpContext ctx = new DirectoryFtpContext(host, port, login, password, dir);
		Assert.assertEquals(ctx.toUri(), new URI("ftp://" + login + "@" + host + ":" + port + dir));
	}
}
