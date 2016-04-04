package org.atom.quark.file.helper;

import java.io.File;

import org.atom.quark.core.helper.HelperBuilderTestBase;
import org.atom.quark.file.context.FileContext;
import org.testng.Assert;
import org.testng.annotations.Test;

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
