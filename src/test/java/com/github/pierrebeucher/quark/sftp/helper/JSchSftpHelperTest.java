package com.github.pierrebeucher.quark.sftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.JSchSftpHelper;

public class JSchSftpHelperTest {

	@Test
	public void JSchSftpHelper() {
		JSchSftpHelper helper = new JSchSftpHelper();
		Assert.assertNotNull(helper.getContext());
	}

	@Test
	public void JSchSftpHelperSftpContext() {
		SftpContext ctx = new SftpContext();
		JSchSftpHelper helper = new JSchSftpHelper(ctx);
		Assert.assertEquals(helper.getContext(), ctx);
	}
}
