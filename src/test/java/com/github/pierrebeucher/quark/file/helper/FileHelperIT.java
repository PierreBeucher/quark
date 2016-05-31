package com.github.pierrebeucher.quark.file.helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.HelperResult;
import com.github.pierrebeucher.quark.file.helper.FileHelper;

public class FileHelperIT extends BaseFileHelperIT<FileHelper>{
	
	/*
	 * String repeated in test files
	 */
	private static final String REPEAT_STRING = "Repeat";
	
	/*
	 * Number of times the REPEAT_STRING is repeated in test file
	 */
	private static final int REPEAT_STRING_COUNT = 3;
	
	@Factory
	@Parameters({"file-utf-8", "file-iso-8859-1"})
	public static Object[] createInstances(String utf8Filepath, String iso88591Filepath){
	
		File utf8File = new File(utf8Filepath);
		File iso88591File = new File(iso88591Filepath);
		return new Object[]{
				new FileHelperIT(createHelper(utf8File, Charset.forName("UTF-8")), iso88591File),
				new FileHelperIT(createHelper(iso88591File, Charset.forName("ISO-8859-1")), utf8File)
			};
	}

	private static FileHelper createHelper(File file, Charset charset){
		return FileHelper.helper(file, charset);
	}
	
	/*
	 * File used to perform comparison with base file. This file have
	 * the same content but different encoding than the tested file,
	 * and should be detected as different
	 */
	private File compareFile;	
	
	public FileHelperIT(FileHelper helper, File compareFile) {
		super(helper);
		this.compareFile = compareFile;
	}

	@Test
	public void containsString() throws IOException {
		boolean result = helper.contains("Banana");
		Assert.assertEquals(result, true, "File should contain 'Banana'");
	}
	
	@Test
	public void containsStringNegative() throws IOException {
		boolean result = helper.contains("DoNotExist");
		Assert.assertEquals(result, false, "File does not contain 'DoNotExist', but helper return success for this assertion");
	}

	@Test
	public void containsStringintPositive() throws IOException {
		HelperResult<?> result = helper.contains(REPEAT_STRING, 3);
		Assert.assertEquals(result.isSuccess(), true, "File contains 'Repeat' " + REPEAT_STRING_COUNT + " times"
				+ " but helper returns failure for this assertion");
	}
	
	@Test
	public void containsStringintNegative() throws IOException {
		HelperResult<?> result = helper.contains(REPEAT_STRING, 78);
		Assert.assertEquals(result.isSuccess(), false, "File contains 'Repeat' " + REPEAT_STRING_COUNT + " times"
				+ " but helper returns success when asserting a different count");
	}

	@Test
	public void countMatchesString() throws IOException {
		int result = helper.countMatches(REPEAT_STRING);
		Assert.assertEquals(result, 4, "Repeated string should be matched 3 times");
	}
	
	@Test
	public void countMatchesStringint() throws IOException {
		int result = helper.countMatches(REPEAT_STRING, 2);
		Assert.assertEquals(result, 2, "Repeated string with maximum occurence defined should be matched 2 times");
	}

	@Test
	public void matchPositive() throws IOException {
		//create tmp file deleted on exit cloning our managed file
		File tmpFile = File.createTempFile("quarktest", null);
		tmpFile.deleteOnExit();
		FileUtils.copyFile(helper.getContext().getFile(), tmpFile);
		
		//both files should match
		boolean result = helper.match(tmpFile);
		Assert.assertEquals(result, true, helper.getContext().getFile().getName() + " should match its copy.");
		
	}
	
	@Test
	public void matchNegative() throws IOException {
		boolean result = helper.match(compareFile);
		Assert.assertEquals(result, false, helper.getContext().getFile().getName() + " should not match " 
				+ compareFile.getName());
	}

}
