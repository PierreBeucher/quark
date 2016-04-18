package com.github.pierrebeucher.quark.sftp.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.core.context.server.ServerContext;

/**
 * Descriptor a SFTP context. The SFTP context is represented by
 * its server hostname, port and authentication context.
 * @author Pierre Beucher
 *
 */
public class SftpContext extends ServerContext {

	//private File file;
	
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
	public SftpContext(String host, int port, SftpAuthContext authContext, Properties options) {
		super(host, port);
		this.authContext = authContext;
		this.options = options;
	}
	
//	public SftpContext(String host, int port, File file, SftpAuthContext authContext, Properties options) {
//		super(host, port);
//		this.file = file;
//		this.authContext = authContext;
//		this.options = options;
//	}

	/**
	 * Constructor with an empty set of options
	 * @param host
	 * @param port
	 * @param file
	 * @param authContext
	 */
	public SftpContext(String host, int port, SftpAuthContext authContext) {
		this(host, port, authContext, new Properties());
	}
//	public SftpContext(String host, int port, File file, SftpAuthContext authContext) {
//		this(host, port, file, authContext, new Properties());
//	}
//	public File getFile() {
//		return file;
//	}
//
//	public void setFile(File file) {
//		this.file = file;
//	}

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

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		try {
			buf.append(toUri().toString());
		} catch (URISyntaxException e) {
			//TODO try to build manually with warning?
			e.printStackTrace();
			buf.append("[URISyntaxException]");
		}
		
		buf.append(" (");
		if(StringUtils.isEmpty(getAuthContext().getPassword())
				&& StringUtils.isEmpty(getAuthContext().getPrivateKey())){
			buf.append("anonymous");
		} else {
			buf.append("password:");
			buf.append(StringUtils.isEmpty(getAuthContext().getPassword()) ? "no" : "yes");
			buf.append(",key:");
			if(!StringUtils.isEmpty(getAuthContext().getPrivateKey())){
				buf.append("yes");
				if(StringUtils.isEmpty(getAuthContext().getPrivateKeyPassword())){
					buf.append("(no passphrase)");
				}
			} else {
				buf.append("no");
			}
			
		}
		buf.append(")");
		
//		.append(StringUtils.isNotEmpty(getAuthContext().getPassword()))
//		.append(",key:")
//		.append(getAuthContext().getPrivateKey())
//		.append(",keyPassphrase:")
//		.append(StringUtils.isNotEmpty(getAuthContext().getPrivateKeyPassword()))
//		.append(")");
		
		return buf.toString();
	}

	@Override
	public URI toUri() throws URISyntaxException {
		return new URI("sftp", 
				getAuthContext().getLogin(),
				getHost(),
				getPort(),
				null,
				null,
				null);
	}

}
