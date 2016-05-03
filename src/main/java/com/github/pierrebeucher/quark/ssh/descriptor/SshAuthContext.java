package com.github.pierrebeucher.quark.ssh.descriptor;

import com.github.pierrebeucher.quark.core.context.authentication.LoginAuthContext;

/**
 * This Descriptor represent various ways of login using the SSH protocol.
 * SSH protocol can be used with login/password and public key authentication.
 * @author Pierre Beucher
 *
 */
public class SshAuthContext extends LoginAuthContext{

	private String password;
	
	private String privateKey;
	
	private String privateKeyPassword;
	
	/**
	 * Empty constructor. Use setters to define context.
	 */
	public SshAuthContext() {
		super();
	}

	/**
	 * Constructor for a SshAuthDescriptor using password authentication.
	 * @param login
	 * @param password
	 */
	public SshAuthContext(String login, String password) {
		super(login);
		this.password = password;
	}

	/**
	 * Constructor for a SshAuthDescriptor using public key authentication.
	 * @param login
	 * @param privateKey
	 */
	public SshAuthContext(String login, String privateKey, String privateKeyPassword) {
		super(login);
		this.privateKey = privateKey;
		this.privateKeyPassword = privateKeyPassword;
	}
	
	/**
	 * Protected constructor taking login, password, private key and passphrase. 
	 * @param login
	 * @param password
	 * @param privateKey
	 * @param privateKeyPassword
	 */
	protected SshAuthContext(String login, String password, String privateKey, String privateKeyPassword) {
		super(login);
		this.password = password;
		this.privateKey = privateKey;
		this.privateKeyPassword = privateKeyPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public void setPrivateKeyPassphrase(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}
	

}
