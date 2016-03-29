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

	public SftpHelper context(SftpContext context) {
		this.sftpContext = context;
		return this;
	}

	public SftpHelper host(String host) {
		sftpContext.setHost(host);
		return this;
		
	}

	public SftpHelper port(int port) {
		sftpContext.setPort(port);
		return this;
	}

	public SftpHelper login(String login) {
		sftpContext.getAuthContext().setLogin(login);
		return this;
	}

	public SftpHelper password(String password) {
		sftpContext.getAuthContext().setPassword(password);
		return this;
	}

	public SftpHelper privateKey(String privateKey) {
		sftpContext.getAuthContext().setPrivateKey(privateKey);
		return this;
	}
	
	public SftpHelper addOption(String option, Object value){
		sftpContext.getOptions().put(option, value);
		return this;
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

	public SftpHelper privateKeyPassword(String privateKeyPassword) {
		getContext().getAuthContext().setPrivateKeyPassword(privateKeyPassword);
		return this;
	}

}
