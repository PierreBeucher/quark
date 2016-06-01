package com.github.pierrebeucher.quark.sftp.helper;

import com.github.pierrebeucher.quark.core.helper.AbstractHelperBuilder;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;

/**
 * 
 * @author Pierre Beucher
 *
 * @param <E> the concrete SftpHelper type used by this Builder
 */
public class SftpHelperBuilder 
		extends AbstractHelperBuilder<SftpContext, SftpHelper> {

	public SftpHelperBuilder(SftpContext baseContext) {
		super(baseContext);
	}
	
	@Override
	protected SftpHelper buildBaseHelper() {
		return new SftpHelper(new SftpContext(baseContext));
	}

	/**
	 * Build a fresh SftpHelper using the base context, without any change.
	 * @return
	 */
	public SftpHelper build(){
		return this.buildBaseHelper();
	}
	
	/**
	 * Build a fresh SftpHelper using the base context, overwriting host
	 * @param host
	 * @return
	 */
	public SftpHelper build(String host){
		return this.buildBaseHelper()
				.host(host);
	}
	
	/**
	 * Build a fresh SftpHelper using the base context, overwriting host and port
	 * @param host
	 * @return
	 */
	public SftpHelper build(String host, int port){
		return this.buildBaseHelper()
				.host(host)
				.port(port);
	}
	
	/**
	 * Build a fresh SftpHelper using the base context, overwriting login and password
	 * @param host
	 * @return
	 */
	public SftpHelper build(String login, String password){
		return this.buildBaseHelper()
				.login(login)
				.password(password);
	}
	
	/**
	 * Build a fresh SftpHelper using the base context, overwriting login, private key
	 * and private key password
	 * @param host
	 * @return
	 */
	public SftpHelper build(String login, String privateKey, String privateKeyPassword){
		return this.buildBaseHelper()
				.login(login)
				.privateKey(privateKey)
				.privateKeyPassword(privateKeyPassword);
	}

	public SftpContext getBaseContext() {
		return baseContext;
	}

	public void setBaseContext(SftpContext baseContext) {
		this.baseContext = baseContext;
	}

}
