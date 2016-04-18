package com.github.pierrebeucher.quark.file.helper;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.helper.HelperBuilderTestBase;
import com.github.pierrebeucher.quark.file.context.FileContext;
import com.github.pierrebeucher.quark.file.helper.FileHelperBuilder;

public class FileHelperBuilderTest extends HelperBuilderTestBase {

	private static final FileContext CONTEXT = new FileContext(new File("/tmp"));
	@Test
	public void FileHelperBuilder() {
		FileHelperBuilder builder = new FileHelperBuilder(CONTEXT);
		Assert.assertEquals(builder.getBaseContext(), CONTEXT);
	}

	@Test
	public void buildBaseHelper() {
		FileHelperBuilder builder = new FileHelperBuilder(CONTEXT);
		testNoReuseBaseContext(builder);
	}
}
