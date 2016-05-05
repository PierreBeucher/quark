package com.github.pierrebeucher.quark.ftp.context;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.pierrebeucher.quark.core.context.server.ServerContext;

public class FtpContext extends ServerContext {
	
	private FtpAuthContext authContext;
	

	public FtpContext() {
		super();
		this.authContext = new FtpAuthContext();
	}
	
	public FtpContext(FtpContext context) {
		super(context);
		this.authContext = new FtpAuthContext(context.authContext);
	}
	
	public FtpContext(String host, int port, String login) {
		super(host, port);
		this.authContext = new FtpAuthContext(login);
	}
	
	public FtpContext(String host, int port, String login, String password) {
		super(host, port);
		this.authContext = new FtpAuthContext(login, password);
	}

	@Override
	public URI toUri() throws URISyntaxException {
		return new URI("ftp",
				authContext.getLogin(),
				getHost(),
				getPort(),
				null,
				null,
				null);
	}

	public String getLogin() {
		return authContext.getLogin();
	}

	public void setLogin(String login) {
		authContext.setLogin(login);
	}

	public String getPassword() {
		return authContext.getPassword();
	}

	public void setPassword(String password) {
		authContext.setPassword(password);
	}
	


}
