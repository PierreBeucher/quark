package com.github.pierrebeucher.quark.sftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.helper.HelperBuilderTestBase;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.JSchSftpHelperBuilder;

public class JSchSftpHelperBuilderTest extends HelperBuilderTestBase {

	@Test
	public void JSchSftpHelperBuilder() {
		SftpContext ctx = new SftpContext();
		JSchSftpHelperBuilder builder = new JSchSftpHelperBuilder(ctx);
		Assert.assertEquals(builder.getBaseContext(), ctx);
	}

	@Test
	public void buildBaseHelper() {
		SftpContext ctx = new SftpContext();
		JSchSftpHelperBuilder builder = new JSchSftpHelperBuilder(ctx);
		testNoReuseBaseContext(builder);
	}
}
