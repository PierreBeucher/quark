package org.atom.quark.sftp.helper;

import java.io.File;

import org.atom.quark.core.result.HelperResult;
import org.atom.quark.sftp.context.SftpAuthContext;
import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Test SftpHelper using various context: password and public key authentication 
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelperIT {
	
	private SftpHelperBuilder builder;

	public JSchSftpHelperIT(SftpHelperBuilder builder) {
		super();
		this.builder = builder;
	}
	
	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-key", "sftp-key-passphrase", "sftp-filepath"})
	public static Object[] createInstances(String host, int port, String login,
			String password, String key, String keyPassphrase, String filePath){
	
		File testFile = new File(filePath);
		SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		SftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, passwordAuthContext));
		SftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, publicKeyAuthContext));
		
		return new Object[] {
			new JSchSftpHelperIT(passwordAuthBuilder),
			new JSchSftpHelperIT(publicKeyAuthBuilder),
		};
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper(SftpHelperBuilder builder){
		return builder.build().addOption("StrictHostKeyChecking", "no");
	}
	
	@Test
	public void connect() throws Exception {
		SftpHelper helper = buildNonStrictHostCheckingHelper(builder);
		HelperResult<Boolean> result = helper.connect();
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void disconnect() throws Exception {
		SftpHelper helper = buildNonStrictHostCheckingHelper(builder);
		helper.connect();
		HelperResult<String> result = helper.disconnect();
		Assert.assertEquals(result.isSuccess(), true, result.getActionOutput());
	}
}
