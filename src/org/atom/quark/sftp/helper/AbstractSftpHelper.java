package org.atom.quark.sftp.helper;

import org.apache.commons.lang3.StringUtils;
import org.atom.quark.sftp.context.SftpContext;

public abstract class AbstractSftpHelper implements SftpHelper {

	private SftpContext sftpContext;
	
	public AbstractSftpHelper() {
		this.sftpContext = new SftpContext();
	}

	public AbstractSftpHelper(SftpContext sftpContext) {
		this.sftpContext = sftpContext;
	}

	public SftpContext getContext() {
		return sftpContext;
	}

	public void context(SftpContext context) {
		this.sftpContext = context;
	}

	public void host(String host) {
		sftpContext.setHost(host);
		
	}

	public void port(int port) {
		sftpContext.setPort(port);
	}

	public void login(String login) {
		sftpContext.getAuthContext().setLogin(login);
	}

	public void password(String password) {
		sftpContext.getAuthContext().setPassword(password);
		
	}

	public void privateKey(String privateKey) {
		sftpContext.getAuthContext().setPrivateKey(privateKey);
	}
	
	/**
	 * The SftpHelper is ready if its Context is properly 
	 * configured with at least a host and a login. Other values
	 * may be defined by default or left empty. 
	 */
	public boolean isReady() {
		return !StringUtils.isEmpty(getContext().getHost())
				&& !StringUtils.isEmpty(getContext().getAuthContext().getLogin());
	}

	public void privateKeyPassword(String privateKeyPassword) {
		getContext().getAuthContext().setPrivateKeyPassword(privateKeyPassword);
	}
	
	

}
