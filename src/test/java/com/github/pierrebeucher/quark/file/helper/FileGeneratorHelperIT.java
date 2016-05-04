package com.github.pierrebeucher.quark.file.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.file.context.FileContext;

public class FileGeneratorHelperIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String templateContent;
	private File template;
	private Charset charset;
	
	@BeforeClass
	public void beforeClass() throws IOException{
		//create a dummy template file to perform our tests
		charset = Charset.forName("UTF-8");
		templateContent = "abcydge aaTOKEN1 cdhddesuc TOKEN2יb\n cjechfue -\"egATOKENd42ez יחא&&&";
		template = File.createTempFile("quarkit", null);
		
		BufferedWriter writer = Files.newBufferedWriter(template.toPath(), charset);
		writer.write(templateContent);
		writer.close();	
	}

	/**
	 * Create a FileGeneratorHelper using this test template and charset
	 * @param template
	 * @param charset
	 * @return
	 */
	private FileGeneratorHelper createGeneratorHelper(){
		FileContext ctx = new FileContext(template);
		return new FileGeneratorHelper(ctx);
	}
	
	@Test
	public void testGenerate() throws IOException{
		String replacement1 = "replacement1";
		String replacement2 = "replacement2";
		String replacement3 = "another replacement";
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		tokenMap.put("TOKEN1", replacement1);
		tokenMap.put("TOKEN2", replacement2);
		tokenMap.put("ATOKEN", replacement3);
		
		File dest = File.createTempFile("quarkit_replacedest", null);
		dest.deleteOnExit();
		
		FileGeneratorHelper helper = createGeneratorHelper();
		helper.generate(tokenMap, dest);
		
		List<String> lines = Files.readAllLines(dest.toPath(), charset);
		
		logger .info("Generated file is: {}", lines);
		
		Assert.assertEquals(lines.size(), 2, "Destination file after generation should contain 2 lines");
		Assert.assertEquals(lines.get(0).contains(replacement1), true, "First line '" + lines.get(0) + "' does not contain '" + replacement1 + "'");
		Assert.assertEquals(lines.get(0).contains(replacement2), true, "First line '" + lines.get(0) + "' does not contain '" + replacement2 + "'");
		Assert.assertEquals(lines.get(1).contains(replacement3), true, "Second line '" + lines.get(0) + "' does not contain '" + replacement3 + "'");
	}
}
