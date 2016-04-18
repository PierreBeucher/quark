package com.github.pierrebeucher.quark.file.context;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.file.context.FileContext;

public class FileContextTest {
	
	/**
	 * Return a non-default Charset. If only one Charset is available,
	 * or if no non-default charset is found, return null.
	 * @return first non-default Charset found, or null
	 */
	public static Charset getNonDefaultCharset(){
		if(Charset.availableCharsets().size() == 1){
			return null;
		}
		
		for(Entry<String, Charset> e : Charset.availableCharsets().entrySet()){
			if(e.getValue() != Charset.defaultCharset()){
				return e.getValue();
			}
		}
		
		return null;
	}

	private static final File testFile = new File("tmpdir/tmpfile");
	
	/*
	 * pick a random charset which is not default charset
	 * this is to prevent collision with default constructor using
	 * default charset: we want to test that the defined charset is set
	 * and not the default one
	 */
	private static final Charset testCharset = getNonDefaultCharset();
	
	@Test
	public void FileContext() {
		FileContext ctx = new FileContext();
		Assert.assertEquals(ctx.getCharset(), Charset.defaultCharset());
		Assert.assertEquals(ctx.getFile(), null);
	}

	@Test
	public void FileContextFile() {
		FileContext ctx = new FileContext(testFile);
		Assert.assertEquals(ctx.getCharset(), Charset.defaultCharset());
		Assert.assertEquals(ctx.getFile(), testFile);
	}

	
	@Test
	public void FileContextFileCharset() {
		FileContext ctx = new FileContext(testFile, testCharset);
	    Assert.assertEquals(ctx.getCharset(), testCharset);
	    Assert.assertEquals(ctx.getFile(), testFile);
	}

	@Test
	public void charset() {
		FileContext ctx = new FileContext();
		ctx.setCharset(testCharset);
	    Assert.assertEquals(ctx.getCharset(), testCharset);
	}

	@Test
	public void file() {
		FileContext ctx = new FileContext();
		ctx.setFile(testFile);
	    Assert.assertEquals(ctx.getFile(), testFile);
	}
}
