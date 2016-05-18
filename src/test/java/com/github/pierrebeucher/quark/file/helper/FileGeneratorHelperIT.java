package com.github.pierrebeucher.quark.file.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.file.context.FileContext;

public class FileGeneratorHelperIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String templateContent;
	
	private String xmlTemplateContent;
	private String xmlToXPathReplace;
	
	private File template;
	private File xmlTemplate;
	private Charset charset;
	
	@BeforeClass
	public void beforeClass() throws IOException{
		//create a dummy template file to perform our tests
		charset = Charset.forName("UTF-8");
		templateContent = "abcydge aaTOKEN1 cdhddesuc TOKEN2יb\n cjechfue -\"egATOKENd42ez יחא&&&";
		
		//build xml template
		xmlToXPathReplace = "ToReplace";
		xmlTemplateContent = "<root><parent><child>" + xmlToXPathReplace + "</child></parent></root>";
		
		template = createTemplateFile(templateContent);
		xmlTemplate = createTemplateFile(xmlTemplateContent);
	}
	
	private File createTemplateFile(String content) throws IOException{
		BufferedWriter writer = null;
		try{
			File f = File.createTempFile("quarkit", null);
			writer = Files.newBufferedWriter(f.toPath(), charset);
			writer.write(content);
			return f;
		} finally {
			if(writer != null){
				writer.close();
			}
		}
	}

	/**
	 * Create a FileGeneratorHelper using this test template and charset
	 * @param template
	 * @param charset
	 * @return
	 */
	private FileGeneratorHelper createGeneratorHelper(File file){
		FileContext ctx = new FileContext(file);
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
		
		FileGeneratorHelper helper = createGeneratorHelper(template);
		helper.generate(tokenMap, dest);
		
		List<String> lines = Files.readAllLines(dest.toPath(), charset);
		
		logger .info("Generated file is: {}", lines);
		
		Assert.assertEquals(lines.size(), 2, "Destination file after generation should contain 2 lines");
		Assert.assertEquals(lines.get(0).contains(replacement1), true, "First line '" + lines.get(0) + "' does not contain '" + replacement1 + "'");
		Assert.assertEquals(lines.get(0).contains(replacement2), true, "First line '" + lines.get(0) + "' does not contain '" + replacement2 + "'");
		Assert.assertEquals(lines.get(1).contains(replacement3), true, "Second line '" + lines.get(0) + "' does not contain '" + replacement3 + "'");
	}
	
	@Test
	public void testXPathReplace() throws XPathExpressionException, IOException{
		FileGeneratorHelper helper = createGeneratorHelper(xmlTemplate);
		XPathExpression exp =  XPathFactory.newInstance().newXPath().compile("//parent/child/text()");
		String replacement = "MyReplacement";
		File generated = helper.xPathReplace(exp, replacement);
		
		logger.info("XPath generated: {}", generated); 
		
		String generatedContent = FileUtils.readFileToString(generated);
		Assert.assertEquals(generatedContent.contains(replacement), true);
		Assert.assertEquals(generatedContent.contains(xmlToXPathReplace), false);
	}
}
