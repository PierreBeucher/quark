package com.github.pierrebeucher.quark.core.context.authentication;

public class LoginAuthContext extends AuthContext{
	
	private String login;

	public LoginAuthContext(String login) {
		super();
		this.login = login;
	}

	/**
	 * Empty constructor. Use setters to define context.
	 */
	public LoginAuthContext() {
		
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
