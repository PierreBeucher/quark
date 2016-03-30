package org.atom.quark.file.context;

import java.io.File;
import java.nio.charset.Charset;

import org.atom.quark.core.context.base.HelperContext;

/**
 * <p>Defines a context under which a File is tested, with a Charset. The only required parameters
 * is the file itself.</p>
 * 
 * @author Pierre Beucher
 *
 */
public class FileContext implements HelperContext {

	private File file;
	
	private Charset charset;

	/**
	 * Empty constructor. Requires setters to be called for setup.
	 */
	public FileContext() {
		super();
	}
	
	/**
	 * Create a file context for the given file. The charset is defined
	 * using the platform default charset. 
	 * @param file
	 */
	public FileContext(File file) {
		this(file, Charset.defaultCharset());
	}

	/**
	 * Create a file context using the given file and charset.
	 * @param file
	 * @param charset
	 */
	public FileContext(File file, Charset charset) {
		super();
		this.file = file;
		this.charset = charset;
	}

	/**
	 * The file managed by this context.
	 * @return
	 */
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * The Charset associated with the file managed by this context
	 * @return
	 */
	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
