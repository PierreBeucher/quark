package org.atom.quark.sftp.helper;

import java.util.Map.Entry;

import org.atom.quark.sftp.context.SftpContext;

public class JSchSftpHelperBuilder extends SftpHelperBuilder {

	public JSchSftpHelperBuilder(SftpContext baseContext) {
		super(baseContext);
	}

	@Override
	protected SftpHelper buildBaseHelper() {
		SftpHelper helper = new JSchSftpHelper()
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
