package org.atom.quark.sftp.helper;

import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.Test;

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
