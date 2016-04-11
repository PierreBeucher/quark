package org.atom.quark.core.context.authentication;

public class PasswordAuthContext extends LoginAuthContext {
	
	private String password;

	public PasswordAuthContext() {
		super();
	}

	public PasswordAuthContext(String login) {
		this(login, null);
	}

	public PasswordAuthContext(String login, String password) {
		super(login);
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
