package org.atom.quark.file.helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.atom.quark.core.result.HelperResult;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FileHelperIT {
	
	/*
	 * String repeated in test files
	 */
	private static final String REPEAT_STRING = "Repeat";
	
	/*
	 * Number of times the REPEAT_STRING is repeated in test file
	 */
	private static final int REPEAT_STRING_COUNT = 3;
	
	/*
	 * File used to perform basic testing
	 */
	private File baseFile;
	
	/*
	 * File used to perform comparison with base file
	 */
	private File compareFile;
	
	/*
	 * Charset used for testing
	 */
	private Charset charset;	
	
	public FileHelperIT(File baseFile, File compareFile, Charset charset) {
		super();
		this.baseFile = baseFile;
		this.compareFile = compareFile;
		this.charset = charset;
	}

	@Factory
	@Parameters({"file-utf-8", "file-iso-8859-1"})
	public static Object[] createInstances(String utf_8_file, String iso_8859_1_file){
	
		File utf8 = new File(utf_8_file);
		File iso88591 = new File(iso_8859_1_file);
		return new Object[]{
				new FileHelperIT(utf8, iso88591, Charset.forName("UTF-8")),
				new FileHelperIT(iso88591, utf8, Charset.forName("ISO-8859-1"))
			};
	}

	private FileHelper createHelper(){
		return FileHelper.helper(baseFile, charset);
	}

	@Test
	public void containsString() throws IOException {
		FileHelper helper = createHelper();
		boolean result = helper.contains("Banana");
		Assert.assertEquals(result, true, "File should contain 'Banana'");
	}
	
	@Test
	public void containsStringNegative() throws IOException {
		FileHelper helper = createHelper();
		boolean result = helper.contains("DoNotExist");
		Assert.assertEquals(result, false, "File does not contain 'DoNotExist', but helper return success for this assertion");
	}

	@Test
	public void containsStringintPositive() throws IOException {
		FileHelper helper = createHelper();
		HelperResult<?> result = helper.contains(REPEAT_STRING, 3);
		Assert.assertEquals(result.isSuccess(), true, "File contains 'Repeat' " + REPEAT_STRING_COUNT + " times"
				+ " but helper returns failure for this assertion");
	}
	
	@Test
	public void containsStringintNegative() throws IOException {
		FileHelper helper = createHelper();
		HelperResult<?> result = helper.contains(REPEAT_STRING, 78);
		Assert.assertEquals(result.isSuccess(), false, "File contains 'Repeat' " + REPEAT_STRING_COUNT + " times"
				+ " but helper returns success when asserting a different count");
	}

	@Test
	public void countMatchesString() throws IOException {
		FileHelper helper = createHelper();
		int result = helper.countMatches(REPEAT_STRING);
		Assert.assertEquals(result, 4, "Repeated string should be matched 3 times");
	}
	
	@Test
	public void countMatchesStringint() throws IOException {
		FileHelper helper = createHelper();
		int result = helper.countMatches(REPEAT_STRING, 2);
		Assert.assertEquals(result, 2, "Repeated string with maximum occurence defined should be matched 2 times");
	}

	@Test
	public void matchPositive() throws IOException {
		FileHelper helper = createHelper();
		
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
		FileHelper helper = createHelper();
		boolean result = helper.match(compareFile);
		Assert.assertEquals(result, false, helper.getContext().getFile().getName() + " should not match " 
				+ compareFile.getName());
	}

}
