package com.github.pierrebeucher.quark.ftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpHelperTest {

	private String host = "host";
	private String login = "login";
	private String password = "pass";
	private int port = 21;
	
	@Test
	public void FtpHelper() {
		FtpHelper helper = new FtpHelper();
		Assert.assertNotNull(helper.getContext());
	}

	@Test
	public void FtpHelperFtpContext() {
		FtpContext ctx = new FtpContext(host, port, login, password);
		FtpHelper helper = new FtpHelper(ctx);
		Assert.assertEquals(helper.getContext(), ctx);
	}

	@Test
	public void inlineSetterContext() {
		FtpContext ctx = new FtpContext(host, port, login, password);
		FtpHelper helper = new FtpHelper();
		helper.context(ctx);
		Assert.assertEquals(helper.getContext(), ctx);
	}

	@Test
	public void inlineSetters() {
		FtpHelper helper = new FtpHelper()
				.host(host)
				.port(port)
				.login(login)
				.password(password);
		Assert.assertEquals(helper.getContext().getHost(), host);
		Assert.assertEquals(helper.getContext().getLogin(), login);
		Assert.assertEquals(helper.getContext().getPassword(), password);
		Assert.assertEquals(helper.getContext().getPort(), port);
	}

	@Test
	public void isReadyPositive() {
		FtpContext ctx = new FtpContext(host, port, login);
		FtpHelper helper = new FtpHelper(ctx);
		Assert.assertTrue(helper.isReady());
	}
	
	@Test
	public void isReadyNegative() {
		FtpContext ctx = new FtpContext(host, port, null);
		FtpHelper helper = new FtpHelper(ctx);
		Assert.assertFalse(helper.isReady());
	}
}
