package com.github.pierrebeucher.quark.sftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpContext;

public class SftpHelperTest {

	@Test
	public void JSchSftpHelper() {
		SftpHelper helper = new SftpHelper();
		Assert.assertNotNull(helper.getContext());
	}

	@Test
	public void JSchSftpHelperSftpContext() {
		SftpContext ctx = new SftpContext();
		SftpHelper helper = new SftpHelper(ctx);
		Assert.assertEquals(helper.getContext(), ctx);
	}
}
