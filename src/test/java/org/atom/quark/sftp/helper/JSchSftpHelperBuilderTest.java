package org.atom.quark.sftp.helper;

import org.atom.quark.core.helper.HelperBuilderTestBase;
import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.Test;

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
