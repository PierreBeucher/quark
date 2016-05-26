package com.github.pierrebeucher.quark.sftp.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * The DirectorySftpContext represents an SftpContext,
 * including a specific directory in which this context is 
 * restricted. 
 * @author pierreb
 *
 */
public class DirectorySftpContext extends SftpContext{

	private String directory;
	
	public DirectorySftpContext() {
		super();
	}

	public DirectorySftpContext(String host, int port, SftpAuthContext authContext, String directory) {
		super(host, port, authContext);
		this.directory = directory;
	}
	
	public DirectorySftpContext(String host, int port, SftpAuthContext authContext, String directory,
			Properties options) {
		super(host, port, authContext, options);
		this.directory = directory;
	}
	
	public DirectorySftpContext(DirectorySftpContext base) {
		super(base);
		this.directory = base.directory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	@Override
	public URI toUri() throws URISyntaxException {
		return new URI("sftp", 
				getAuthContext().getLogin(),
				getHost(),
				getPort(),
				getDirectory(),
				null,
				null);
	}
	
}
