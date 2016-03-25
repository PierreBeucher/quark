package org.atom.quark.sftp.helper;

import org.atom.quark.sftp.context.SftpContext;

public class JSchSftpHelperBuilder extends SftpHelperBuilder {

	public JSchSftpHelperBuilder(SftpContext baseContext) {
		super(baseContext);
	}

	@Override
	protected SftpHelper buildBaseHelper() {
		return new JSchSftpHelper()
			.host(baseContext.getHost())
			.port(baseContext.getPort())
			.login(baseContext.getAuthContext().getLogin())
			.password(baseContext.getAuthContext().getPassword())
			.privateKey(baseContext.getAuthContext().getPrivateKey())
			.privateKeyPassword(baseContext.getAuthContext().getPrivateKeyPassword());
	}

}
