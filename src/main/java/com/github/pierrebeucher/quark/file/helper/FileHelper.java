package com.github.pierrebeucher.quark.file.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.ResultBuilder;
import com.github.pierrebeucher.quark.file.context.FileContext;

/**
 * <p>The FileHelper contains facility functions related to file testing.
 * Its context manages a single file on which test actions can be performed, either
 * to test an existing file, or to transform a file prior to testing</p>
 * <p>TODO work in progress. More functions and a little refactoring is needed.</p>
 * @author Pierre Beucher
 *
 */
public class FileHelper extends AbstractHelper<FileContext> implements Helper {
	
	/**
	 * Facility function creating an instance of FileHelper for the given file
	 * using the default charset
	 * @param file file to manage
	 * @return
	 */
	public static FileHelper helper(File file){
		return new FileHelper(new FileContext(file));
	}
	
	/**
	 * Facility function creating an instance of FileHelper for the given file
	 * and charset.
	 * @param file file to manage
	 * @param charset charset to use
	 * @return
	 */
	public static FileHelper helper(File file, Charset charset){
		return new FileHelper(new FileContext(file, charset));
	}

	/**
	 * Create a FileHelper with a null file and the default charset.
	 */
	public FileHelper() {
		super(new FileContext());
	}

	/**
	 * Create a FileHelper using the given context
	 * @param context
	 */
	public FileHelper(FileContext context) {
		super(context);
	}
	
	public FileHelper file(File file){
		getContext().setFile(file);
		return this;
	}
	
	public FileHelper charset(Charset charset){
		getContext().setCharset(charset);
		return this;
	}

	/**
	 * A FileHelper is ready if its File context defines a non-null file.
	 */
	public boolean isReady() {
		return context != null && context.getFile() != null;
	}
	
	/**
	 * Return a Stream for the managed file.
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream getStream() throws FileNotFoundException{
		return new BufferedInputStream(new FileInputStream(getContext().getFile()));
	}
	
	/**
	 * Create a new BufferedReader for the managed file using the defined encoding
	 * @return
	 * @throws FileNotFoundException
	 */
	private BufferedReader createBufferedReader() throws FileNotFoundException{
		return new BufferedReader(
				new InputStreamReader(
						new FileInputStream(getContext().getFile()),
						getContext().getCharset()
						)
				);
	}
	
	/**
	 * Check whether the managed file contains the given String. 
	 * @param str string to find
	 * @return true if the string is found at least once, false otherwise
	 * @throws IOException 
	 */
	public boolean contains(String str) throws IOException{
		BufferedReader reader = createBufferedReader();
		
		String line = null;
		while((line = reader.readLine()) != null){
			if(line.contains(str)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether the managed file contains a certain number of occurence
	 * of the given string
	 * @param str string to find
	 * @param count number of occurence the data are to be found
	 * @return result as number of occurrence found
	 * @throws IOException 
	 */
	public BaseHelperResult<Integer> contains(String str, int count) throws IOException{
		int result = countMatches(str, count);
		return ResultBuilder.expectingResult(count == result, result, count,
				"Counting occurences of '" + str + "' in " + context.getFile().getName());
	}
	
	/**
	 * Count the number of occurence of the given String in the managed file
	 * @param str string to count
	 * @return number of occurence found
	 * @throws IOException 
	 */
	public int countMatches(String str) throws IOException{
		return countMatches(str, 0);
	}
	
	/**
	 * Count the number of occurence of the given String in the managed file
	 * until a certain count is reached.
	 * @param str string to count
	 * @return number of occurence found
	 * @throws IOException 
	 */
	public int countMatches(String str, int max) throws IOException{		
		BufferedReader reader = createBufferedReader();

		int current = 0;
		String line = null;
		while((line = reader.readLine()) != null){
			current += StringUtils.countMatches(line, str);
			if(max > 0 && current >= max){
				return max;
			}
		}
		
		return current;
	}
	
	//TODO replace using generic function
	public String getMD5HexChecksum() throws IOException{
		return DigestUtils.md5Hex(getStream());
	}
	
	/**
	 * Check whether the file managed by this Helper is identical to the given file. Both files
	 * are read using this Helper encoding. Two files with an identical content but encoded
	 * differently will be found to be different.
	 * @param file file to compare
	 * @return true if both files matches, false otherwise
	 * @throws IOException 
	 */
	public boolean match(File file) throws IOException{
		return FileUtils.contentEquals(getContext().getFile(), file);
	}

}
