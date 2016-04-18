package com.github.pierrebeucher.quark.file.helper;

import java.io.File;
import java.nio.charset.Charset;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.file.context.FileContext;
import com.github.pierrebeucher.quark.file.context.FileContextTest;
import com.github.pierrebeucher.quark.file.helper.FileHelper;

public class FileHelperTest {
	
	private static final File FILE = new File("/tmp");
	
	private static final Charset CHARSET = FileContextTest.getNonDefaultCharset();

	@Test
	public void FileHelper() {
		FileHelper helper = new FileHelper();
		Assert.assertNotNull(helper.getContext());
	}

	@Test
	public void FileHelperFileContext() {
		FileHelper helper = new FileHelper(new FileContext(FILE));
		Assert.assertEquals(helper.getContext().getFile(), FILE);
	}

	@Test
	public void charset() {
		FileHelper helper = new FileHelper();
		helper.charset(CHARSET);
		
		Assert.assertEquals(helper.getContext().getCharset(), CHARSET);
	}

	@Test
	public void file() {
		FileHelper helper = new FileHelper();
		helper.file(FILE);
		
		Assert.assertEquals(helper.getContext().getFile(), FILE);
	}

	@Test
	public void isReadyPositive() {
		FileContext ctx = new FileContext(FILE, CHARSET);
		FileHelper helper = new FileHelper(ctx);
		
		Assert.assertEquals(helper.isReady(), true);
	}
	
	@Test
	public void isReadyNegative() {
		FileContext ctx = new FileContext();
		FileHelper helper = new FileHelper(ctx);
		
		Assert.assertEquals(helper.isReady(), false);
	}
}
