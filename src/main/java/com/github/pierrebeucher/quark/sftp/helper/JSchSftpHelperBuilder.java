package com.github.pierrebeucher.quark.sftp.helper;

import java.util.Map.Entry;

import com.github.pierrebeucher.quark.sftp.context.SftpContext;

/**
 * 
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelperBuilder extends SftpHelperBuilder {

	public JSchSftpHelperBuilder(SftpContext baseContext) {
		super(baseContext);
	}

	@Override
	protected JSchSftpHelper buildBaseHelper() {
		JSchSftpHelper helper = (JSchSftpHelper) new JSchSftpHelper()
			.host(baseContext.getHost())
			.port(baseContext.getPort())
			.login(baseContext.getAuthContext().getLogin())
			.password(baseContext.getAuthContext().getPassword())
			.privateKey(baseContext.getAuthContext().getPrivateKey())
			.privateKeyPassword(baseContext.getAuthContext().getPrivateKeyPassword());
		
		for(Entry<Object, Object> o : this.baseContext.getOptions().entrySet()){
			helper.addOption((String) o.getKey(), o.getValue());
		}
		
		return helper;
	}

}
