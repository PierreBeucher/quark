package com.github.pierrebeucher.quark.file.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.file.context.FileContext;

/**
 * <p>The FileGeneratorHelper can generated random and unique content.
 * It generates files by replacing pre-defined tokens from a template
 * file and generating a new file with the generated content. </p>
 * @author pierreb
 *
 */
public class FileGeneratorHelper extends AbstractHelper<FileContext> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Empty constructor with null context.
	 */
	public FileGeneratorHelper() {
		this(null);
	}
	
	public FileGeneratorHelper(FileContext context) {
		super(context);
	}

	/**
	 * Evaluate the given XPath expression for the file managed by this helper, and replace all matching
	 * node values by the replacement string. The returned file is written in the system
	 * temp directory.
	 * @param xpath
	 * @param replacement
	 * @return
	 * @throws IOException 
	 */
	public File xPathReplace(XPathExpression expression, String replacement) throws IOException{
		try{
			//replace
			Document doc = generateDomDocument(context.getFile());
			NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			if(nodeList != null && nodeList.getLength() > 0){
				for(int i=0; i<nodeList.getLength(); i++){
					nodeList.item(i).setNodeValue(replacement);
				}
			}
			
			//write out
			File out = File.createTempFile(context.getFile().getName(), null);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Source source = new DOMSource(doc);
			Result target = new StreamResult(new FileOutputStream(out));
			transformer.transform(source, target);
			
			return out;
		} catch (SAXException | IOException | ParserConfigurationException |
				XPathExpressionException | TransformerFactoryConfigurationError | TransformerException e){
			throw new IOException(e.getMessage(), e);
		}
	}
	
	private Document generateDomDocument(File file) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		return builder.parse(file);
	}
	
	/**
	 * <p>Generate a file using the given template and token map. A new file
	 * will be created by replacing any found token (map keys) in the template
	 * file managed by this helper by its corresponding values (map values). The
	 * newly generated file is generated as a temporary file, using {@link java.io.File#createTempFile(String, String)}.</p>
	 * <p>TODO if the tokenMap contains a value matching another token, this value may be identified as a token and replaced
	 * twice.</p>
	 * 
	 * @param tokenMap map of tolen / values to be replaced in template. Values are converted to String using toString().
	 * @return
	 * @throws IOException 
	 */
	public File generate(Map<String, Object> tokenMap, File dest) throws IOException{
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		logger.debug("With {} generating to {} from {}", tokenMap, dest, context);
		try{
			reader = getTemplateReader();
			writer = createWriter(dest, context.getCharset());
			String line = null;
			
			//for each line, write to new file using the original charset
			while((line=reader.readLine()) != null){
				String newLine = line;
				for(Entry<String, Object> entry : tokenMap.entrySet()){
					newLine = newLine.replace(entry.getKey(), entry.getValue().toString());
				}
				writer.write(newLine);
				writer.newLine();
			}
		} finally {
			if(reader != null){
				reader.close();
			}
			if(writer != null){
				writer.close();
			}
		}
		return dest;
	}
	
	private BufferedReader getTemplateReader() throws FileNotFoundException{
		return new BufferedReader(new InputStreamReader(
						new FileInputStream(getContext().getFile()),
							getContext().getCharset()
					));
	}
	
	private BufferedWriter createWriter(File f, Charset c) throws IOException{
		return Files.newBufferedWriter(f.toPath(), c);
	}

	@Override
	public boolean isReady() {
		return context.getFile() != null &&
				context.getFile().isFile() &&
				context.getCharset() != null;
	}
}
