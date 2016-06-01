package com.github.pierrebeucher.quark.sftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.helper.HelperBuilderTestBase;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelperBuilder;

public class SftpHelperBuilderTest extends HelperBuilderTestBase {
	
	private static final String HOST = "host";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String KEY = "key";
	private static final String KEYPASS = "keypass";
	private static final int PORT = 2222;
	
	private SftpHelperBuilder createEmptyTestBuilder(){
		return new SftpHelperBuilder(null){
			@Override
			protected SftpHelper buildBaseHelper() {
				return new SftpHelper(new SftpContext());
			}
		};
	}
	
	@Test
	public void JSchSftpHelperBuilder() {
		SftpContext ctx = new SftpContext();
		SftpHelperBuilder builder = new SftpHelperBuilder(ctx);
		Assert.assertEquals(builder.getBaseContext(), ctx);
	}

	@Test
	public void buildBaseHelper() {
		SftpContext ctx = new SftpContext();
		SftpHelperBuilder builder = new SftpHelperBuilder(ctx);
		testNoReuseBaseContext(builder);
	}

	@Test
	public void build() {
		SftpHelperBuilder builder = createEmptyTestBuilder();
		SftpHelper helper = builder.build();
		Assert.assertNotNull(helper.getContext());
		Assert.assertNull(helper.getContext().getHost());
		Assert.assertEquals(helper.getContext().getPort(), -1);
	}

	@Test
	public void buildString() {
		SftpHelperBuilder builder = createEmptyTestBuilder();
		SftpHelper helper = builder.build(HOST);
		Assert.assertEquals(helper.getContext().getHost(), HOST);
	}

	@Test
	public void buildStringint() {
		SftpHelperBuilder builder = createEmptyTestBuilder();
		SftpHelper helper = builder.build(HOST, PORT);
		Assert.assertEquals(helper.getContext().getHost(), HOST);
		Assert.assertEquals(helper.getContext().getPort(), PORT);
	}

	@Test
	public void buildStringString() {
		SftpHelperBuilder builder = createEmptyTestBuilder();
		SftpHelper helper = builder.build(LOGIN, PASSWORD);
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), LOGIN);
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), PASSWORD);
	}

	@Test
	public void buildStringStringString() {
		SftpHelperBuilder builder = createEmptyTestBuilder();
		SftpHelper helper = builder.build(LOGIN, KEY, KEYPASS);
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), LOGIN);
		Assert.assertEquals(helper.getContext().getAuthContext().getPrivateKey(), KEY);
		Assert.assertEquals(helper.getContext().getAuthContext().getPrivateKeyPassword(), KEYPASS);
	}

	@Test
	public void getBaseContext() {
		SftpContext ctx = new SftpContext();
		SftpHelperBuilder builder = new SftpHelperBuilder(ctx){
			@Override
			protected SftpHelper buildBaseHelper() {
				return new SftpHelper(new SftpContext());
			}
		};
		Assert.assertEquals(builder.getBaseContext(), ctx);
	}
}
