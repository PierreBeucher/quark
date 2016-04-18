package com.github.pierrebeucher.quark.sftp.helper;

import java.io.InputStream;
import java.util.Vector;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.AbstractSftpHelper;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelper;
import com.jcraft.jsch.SftpException;

public class AbstractSftpHelperTest {
	
	private AbstractSftpHelper buildTestHelper(){
		return new AbstractSftpHelper(){
			@Override
			public boolean connect() throws Exception {
				return false;
			}
			@Override
			public boolean disconnect() throws Exception {
				return false;
			}
			@Override
			public boolean upload(InputStream stream, String dest, int mode) throws SftpException {
				return false;
			}
			@Override
			public Vector<LsEntry> list(String dir) throws SftpException {
				return null;
			}
			@Override
			public InputStream getInputStream(String dest) throws SftpException {
				return null;
			}
		};
	}

	@Test
	public void addOption() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.addOption("option", "value");
		Assert.assertEquals(helper.getContext().getOptions().get("option"), "value");
	}

	@Test
	public void context() {
		AbstractSftpHelper helper = buildTestHelper();
		SftpContext ctx = new SftpContext();
		helper.context(ctx);
		
		Assert.assertEquals(helper.getContext(), ctx);
	}

	@Test
	public void host() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.host("localhost");
		
		Assert.assertEquals(helper.getContext().getHost(), "localhost");
	}

	@Test
	public void isReadyNegative() {
		SftpHelper helper = buildTestHelper().login("login").password("password");
		Assert.assertEquals(helper.isReady(), false);
	}
	
	@Test
	public void isReadyPositive() {
		SftpHelper helper = buildTestHelper().login("login").host("localhost");
		Assert.assertEquals(helper.isReady(), true);
	}

	@Test
	public void login() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.login("login");
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), "login");
	}

	@Test
	public void password() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.password("password");
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), "password");
	}

	@Test
	public void port() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.port(2222);
		Assert.assertEquals(helper.getContext().getPort(), 2222);
	}

	@Test
	public void privateKey() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.privateKey("/privkey");
		Assert.assertEquals(helper.getContext().getAuthContext().getPrivateKey(), "/privkey");
	}

	@Test
	public void privateKeyPassword() {
		AbstractSftpHelper helper = buildTestHelper();
		helper.privateKeyPassword("passphrase");
		Assert.assertEquals(helper.getContext().getAuthContext().getPrivateKeyPassword(), "passphrase");
	}
}
