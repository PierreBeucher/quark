package com.github.pierrebeucher.quark.ftp.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpCleanerTest {

	@Test
	public void FtpCleanerFtpContext() {
		FtpContext ctx = new FtpContext();
		FtpCleaner cleaner = new FtpCleaner(ctx);
		Assert.assertEquals(cleaner.getContext(), ctx);
		Assert.assertEquals(cleaner.getWrappedHelper().getContext(), ctx);
	}

	@Test
	public void FtpCleanerFtpHelper() {
		FtpContext ctx = new FtpContext();
		FtpHelper helper = new FtpHelper(ctx);
		FtpCleaner cleaner = new FtpCleaner(helper);
		Assert.assertEquals(cleaner.getWrappedHelper(), helper);
		Assert.assertEquals(cleaner.getContext(), ctx);
		Assert.assertEquals(cleaner.getWrappedHelper().getContext(), ctx);
	}
}
