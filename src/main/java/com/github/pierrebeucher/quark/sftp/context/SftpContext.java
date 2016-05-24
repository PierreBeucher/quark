package com.github.pierrebeucher.quark.sftp.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

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
	
	public SftpContext(SftpContext ctx) {
		this(ctx.getHost(), ctx.getPort(), new SftpAuthContext(ctx.getAuthContext()));
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
	
	public void setLogin(String login){
		this.authContext.setLogin(login);
	}
	
	public String getLogin(){
		return this.authContext.getLogin();
	}
	
	public void setPassword(String password){
		this.authContext.setPassword(password);
	}
	
	public String getPassword(){
		return this.authContext.getPassword();
	}
	
	public void setPrivateKey(String keyPath){
		this.authContext.setPrivateKey(keyPath);
	}
	
	public String getPrivateKey(){
		return this.authContext.getPrivateKey();
	}
	
	public void setPrivateKeyPassphrase(String keyPassword){
		this.authContext.setPrivateKeyPassphrase(keyPassword);
	}
	
	public String getPrivateKeyPassword(){
		return this.authContext.getPrivateKeyPassword();
	}

	/**
	 * Attempts to call getUri().toString(). If an URISyntaxException is thrown,
	 * generate a String like: sftp://[login]@[host]:[port]
	 */
	@Override
	public String toString() {
		try {
			return toUri().toString();
		} catch (URISyntaxException e) {
			return "sftp://" + getLogin() + "@" + getHost() + ":" + getPort();
		}
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
