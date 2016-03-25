package org.atom.quark.sftp.context;

import java.io.File;

import org.atom.quark.core.context.server.ServerContext;

/**
 * Descriptor a SFTP context. The SFTP context is represented by
 * its server hostname, port and authentication context.
 * @author Pierre Beucher
 *
 */
public class SftpContext extends ServerContext {

	private File file;
	
	private SftpAuthContext authContext;
	
	public SftpContext() {
		super();
		this.authContext = new SftpAuthContext();
	}

	public SftpContext(String host, int port, File file, SftpAuthContext authContext) {
		super(host, port);
		this.file = file;
		this.authContext = authContext;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public SftpAuthContext getAuthContext() {
		return authContext;
	}

	public void setAuthContext(SftpAuthContext authContext) {
		this.authContext = authContext;
	}
	
	

}
