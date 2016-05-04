package com.github.pierrebeucher.quark.file.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

//	/**
//	 * Write a new file
//	 * @param newContent content of the file
//	 * @param newFileDirectory directory where to write, or null to use default temp directory
//	 * @return resulting File
//	 * @throws IOException
//	 */
//	private File writeNewFile(byte[] newContent, File newFileDirectory) throws IOException{
//		String fileExt = FilenameUtils.getExtension(originalFile.getPath());
//		String baseFilename = FilenameUtils.getBaseName(originalFile.getPath());
//		File newFile = File.createTempFile(baseFilename, "." + fileExt, newFileDirectory);
//		Files.write(Paths.get(newFile.getPath()), newContent, StandardOpenOption.APPEND);
//		
//		this.generatedFile.add(newFile);
//		
//		if(this.deleteOnExit){
//			newFile.deleteOnExit();
//		}
//		
//		return newFile;
//	}
//	
//	/**
//	 * Read the file managed by this helper as byte array
//	 * @return
//	 * @throws IOException
//	 */
//	private byte[] readAllBytes() throws IOException{
//		return Files.readAllBytes(Paths.get(originalFile.getPath()));
//	}
//	
//	/**
//	 * Read the file managed by this helper as byte array
//	 * and transform it into a String using this helper Charset
//	 * @return
//	 * @throws IOException
//	 */
//	private String readAllBytesAsString() throws IOException{
//		return new String(readAllBytes(), getCharset());
//	}
//	
//	/**
//	 * <p>Generate a unique file by replacing the given token from the
//	 * original file by a randomly unique generated value. A new file 
//	 * is written in the given folder with random name. </p>
//	 * @param replacementToken token to be replaced to generate unique file. Can be REGEX value.
//	 * @param newFiledirectory directory where the new file is written
//	 * @return a new unique file
//	 * @throws IOException 
//	 */
//	private File doGenerateUniqueFile(String replacementToken, File newFiledirectory) throws IOException{
//		
//		String uniqueValue = generateUniqueValue().toString();
//		String newContent  = readAllBytesAsString().replaceAll(replacementToken, uniqueValue);
//		
//		return writeNewFile(newContent.getBytes(), newFiledirectory);
//	}
//	
//	/**
//	 * <p>Generate a unique file by replacing the given token from the
//	 * original file by a randomly unique generated value. A new file 
//	 * is written in the given folder with random name. </p>
//	 * @param replacementToken token to be replaced to generate unique file. Can be REGEX value.
//	 * @param newFiledirectory directory where the new file is written
//	 * @return a new unique file
//	 * @throws IOException 
//	 */
//	public File generateUniqueFile(String replacementToken, File newFiledirectory) throws IOException{
//		return doGenerateUniqueFile(replacementToken, newFiledirectory);
//	}
//	
//	/**
//	 * <p>Generate a unique file by replacing the given token from the
//	 * original file by a randomly unique generated value.</p>
//	 * A new file is written in the given folder with random name. 
//	 * @param replacementToken token to be replaced to generate unique file. Can be REGEX value.
//	 * @return a new unique file
//	 * @throws IOException 
//	 */
//	public File generateUniqueFile(String replacementToken) throws IOException{
//		return generateUniqueFile(replacementToken, null);
//	}
//	
//	/**
//	 * Generate a unique value. This implementation
//	 * uses UUID.
//	 * @return an object which will return a unique value using toString()
//	 */
//	protected Object generateUniqueValue(){
//		return UUID.randomUUID();
//	}
//	
//	/**
//	 * Generate a transformed file, by replacing all the tokens (key)
//	 * by its replacement (values) using the given map. Tokens can 
//	 * be REGEX patterns. 
//	 * @param replacementMap map containing token (keys) and their replacement values (values)
//	 * @param directory where to write the resulting file
//	 * @return the resulting File pointer
//	 * @throws IOException 
//	 */
//	public File generateTransformedFile(Map<String, String> replacementMap, File newFileDirectory) throws IOException{
//		String fileStr = readAllBytesAsString();
//		
//		String newStr = fileStr;
//		for(Entry<String, String> e : replacementMap.entrySet()){
//			newStr = newStr.replaceAll(e.getKey(), e.getValue());
//			
//			//debug
//			//System.out.println("Replacing " + e.getKey() + " by " + e.getValue());
//		}
//		
//		return this.writeNewFile(newStr.getBytes(), newFileDirectory);
//	}
//	/**
//	 * Generate a transformed file, by replacing all the tokens (key)
//	 * by its replacement (values) using the given map. Tokens can 
//	 * be REGEX patterns. 
//	 * @param replacementMap map containing token (keys) and their replacement values (values)
//	 * @return the transformed File pointer
//	 * @throws IOException 
//	 */
//	public File generateTransformedFile(Map<String, String> replacementMap) throws IOException{
//		//write to default tmp dir
//		return generateTransformedFile(replacementMap, null);
//	}

}
