package org.atom.quark.sftp.context;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SftpContextTest {

	private static final String HOST = "localhost";
	private static final int PORT = 2222;
	private static final File FILE = new File("/tmp");
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	@Test
	public void SftpContext() {
		SftpContext ctx = new SftpContext();
		Assert.assertNotNull(ctx.getAuthContext());
		Assert.assertNotNull(ctx.getOptions());
		Assert.assertNull(ctx.getHost());
		Assert.assertEquals(ctx.getPort(), 0);
	}

	@Test
	public void SftpContextStringintFileSftpAuthContextProperties() {
		SftpAuthContext authCtx = new SftpAuthContext(LOGIN, PASSWORD);
		Properties opts = new Properties();
		SftpContext ctx = new SftpContext(HOST, PORT, FILE, 
				authCtx, opts);
		Assert.assertEquals(ctx.getAuthContext(), authCtx);
		Assert.assertEquals(ctx.getHost(), HOST);
		Assert.assertEquals(ctx.getPort(), PORT);
		Assert.assertEquals(ctx.getFile(), FILE);
		Assert.assertEquals(ctx.getOptions(), opts);
	}

	@Test
	public void SftpContextStringintFileSftpAuthContext() {
		SftpAuthContext authCtx = new SftpAuthContext(LOGIN, PASSWORD);
		SftpContext ctx = new SftpContext(HOST, PORT, FILE, authCtx);
		Assert.assertEquals(ctx.getAuthContext(), authCtx);
		Assert.assertEquals(ctx.getHost(), HOST);
		Assert.assertEquals(ctx.getPort(), PORT);
		Assert.assertEquals(ctx.getFile(), FILE);
		Assert.assertNotNull(ctx.getOptions());
	}

	@Test
	public void authContext() {
		SftpAuthContext authCtx = new SftpAuthContext();
		SftpContext ctx = new SftpContext();
		ctx.setAuthContext(authCtx);
		Assert.assertEquals(ctx.getAuthContext(), authCtx);
	}

	@Test
	public void file() {
		SftpContext ctx = new SftpContext();
		ctx.setFile(FILE);
		Assert.assertEquals(ctx.getFile(), FILE);
	}

	@Test
	public void options() {
		SftpContext ctx = new SftpContext();
		Properties opts = new Properties();
		ctx.setOptions(opts);
		Assert.assertEquals(ctx.getOptions(), opts);
	}

	@Test
	public void toUri() throws URISyntaxException {
		SftpAuthContext authCtx = new SftpAuthContext(LOGIN, PASSWORD);
		SftpContext ctx = new SftpContext(HOST, PORT, FILE, authCtx);
		Assert.assertEquals(ctx.toUri(), new URI("sftp://" + LOGIN + "@" + HOST + ":" + PORT));
	}
}
