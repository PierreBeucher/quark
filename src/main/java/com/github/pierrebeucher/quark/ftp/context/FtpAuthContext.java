package com.github.pierrebeucher.quark.ftp.context;

import com.github.pierrebeucher.quark.core.context.authentication.PasswordAuthContext;
import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * Represent the authentication context for an FTP server. As of now,
 * only support anonymous and basic authentication. 
 * @author pierreb
 *
 */
public class FtpAuthContext implements HelperContext {
	
	private PasswordAuthContext passwordAuthContext;

	public FtpAuthContext() {
		super();
		this.passwordAuthContext = new PasswordAuthContext();
	}
	
	public FtpAuthContext(FtpAuthContext baseContext) {
		this.passwordAuthContext = new PasswordAuthContext(baseContext.passwordAuthContext);
	}
	
	/**
	 * Anonymous login constructor
	 * @param login
	 */
	public FtpAuthContext(String login) {
		this(login, "");
	}
	
	/**
	 * Login password constructor
	 * @param login
	 * @param password
	 */
	public FtpAuthContext(String login, String password) {
		this.passwordAuthContext = new PasswordAuthContext(login, password);
	}

	public String getLogin() {
		return passwordAuthContext.getLogin();
	}

	public void setLogin(String login) {
		passwordAuthContext.setLogin(login);
	}

	public String getPassword() {
		return passwordAuthContext.getPassword();
	}

	public void setPassword(String password) {
		passwordAuthContext.setPassword(password);
	}

	
}
