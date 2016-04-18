package com.github.pierrebeucher.quark.sftp.context;

import com.github.pierrebeucher.quark.ssh.descriptor.SshAuthContext;

/**
 * Descriptor representing methods of SFTP authentication: login with password or private key
 * @author Pierre Beucher
 *
 */
public class SftpAuthContext extends SshAuthContext{
	
	/**
	 * Empty constructor. Use setters to define context.
	 */
	public SftpAuthContext() {
		super();
	}

	/**
	 * Constructor for a SftpAuthDescriptor using password authentication.
	 * @param login
	 * @param password
	 */
	public SftpAuthContext(String login, String privateKey, String privateKeyPassword) {
		super(login, privateKey, privateKeyPassword);
	}

	/**
	 * Constructor for a SftpAuthDescriptor using public key authentication.
	 * @param login
	 * @param privateKey
	 */
	public SftpAuthContext(String login, String password) {
		super(login, password);
	}


}
