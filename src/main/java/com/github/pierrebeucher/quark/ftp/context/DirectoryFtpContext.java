package com.github.pierrebeucher.quark.ftp.context;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * FtpContext including a specific Directory
 * @author pierreb
 *
 */
public class DirectoryFtpContext extends FtpContext{

	private String directory;
	
	public DirectoryFtpContext() {
		super();
	}

	public DirectoryFtpContext(DirectoryFtpContext context) {
		super(context);
		this.directory = context.directory;
	}

	public DirectoryFtpContext(String host, int port, String login, String password, String directory) {
		super(host, port, login, password);
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	@Override
	public URI toUri() throws URISyntaxException {
		return new URI("ftp",
				getLogin(),
				getHost(),
				getPort(),
				getDirectory(),
				null,
				null);
	}

}
