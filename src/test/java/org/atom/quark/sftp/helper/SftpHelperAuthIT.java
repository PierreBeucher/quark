package org.atom.quark.sftp.helper;

import org.atom.quark.sftp.context.SftpAuthContext;
import org.atom.quark.sftp.context.SftpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Test SftpHelper authentication using various context: password and public key authentication 
 * @author Pierre Beucher
 *
 */
public class SftpHelperAuthIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SftpHelperBuilder builder;

	public SftpHelperAuthIT(SftpHelperBuilder builder) {
		super();
		this.builder = builder;
	}
	
	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-key", "sftp-key-passphrase"})
	public static Object[] createInstances(String host, int port, String login,
			String password, String key, String keyPassphrase){
	
		SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		SftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, null, passwordAuthContext));
		SftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, null, publicKeyAuthContext));
		
		return new Object[] {
			new SftpHelperAuthIT(passwordAuthBuilder),
			new SftpHelperAuthIT(publicKeyAuthBuilder),
		};
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper() throws Exception{
		SftpHelper helper = builder.build().addOption("StrictHostKeyChecking", "no");
		
		logger.info("Connect {}", helper);
		
		helper.connect();
		return helper;
	}
	
	@Test
	public void connect() throws Exception {
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		boolean result = helper.connect();
		Assert.assertEquals(result, true, "Connection to SFTP server failed.");
	}
	
	@Test
	public void disconnect() throws Exception {
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
		boolean result = helper.disconnect();
		Assert.assertEquals(result, true, "Disconnection from SFTP server failed.");
	}
}