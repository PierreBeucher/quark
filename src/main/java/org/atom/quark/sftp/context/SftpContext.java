package org.atom.quark.sftp.context;

import java.io.File;
import java.util.Properties;

import org.atom.quark.core.context.server.ServerContext;

/**
 * Descriptor a SFTP context. The SFTP context is represented by
 * its server hostname, port and authentication context.
 * @author Pierre Beucher
 *
 */
public class SftpContext extends ServerContext {

	private File file;
	
	private SftpAuthContext authContext;
	
	private Properties options;
	
	public SftpContext() {
		super();
		this.authContext = new SftpAuthContext();
		this.options = new Properties();
	}

	/**
	 * 
	 * @param host sftp hort
	 * @param port sftp port
	 * @param file file managed by this helper
	 * @param authContext sftp authentication context
	 * @param options sftp client options
	 */
	public SftpContext(String host, int port, File file, SftpAuthContext authContext, Properties options) {
		super(host, port);
		this.file = file;
		this.authContext = authContext;
		this.options = options;
	}

	/**
	 * Constructor with an empty set of options
	 * @param host
	 * @param port
	 * @param file
	 * @param authContext
	 */
	public SftpContext(String host, int port, File file, SftpAuthContext authContext) {
		this(host, port, file, authContext, new Properties());
	}
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public SftpAuthContext getAuthContext() {
		return authContext;
	}

	public void setAuthContext(SftpAuthContext authContext) {
		this.authContext = authContext;
	}
	
	public Properties getOptions() {
		return options;
	}

	public void setOptions(Properties options) {
		this.options = options;
	}
	
	public void addOption(String key, Object value){
		this.options.put(key, value);
	}
	

}
