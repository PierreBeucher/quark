package com.github.pierrebeucher.quark.sftp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.JSchSftpHelperBuilder;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelper;

/**
 * Test SftpHelper authentication using various context: password and public key authentication 
 * @author Pierre Beucher
 *
 */
public class SftpHelperAuthIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JSchSftpHelperBuilder builder;

	public SftpHelperAuthIT(JSchSftpHelperBuilder builder) {
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
		
		JSchSftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, passwordAuthContext));
		JSchSftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, publicKeyAuthContext));
		
		return new Object[] {
			new SftpHelperAuthIT(passwordAuthBuilder),
			new SftpHelperAuthIT(publicKeyAuthBuilder),
		};
	}
	
	@Test
	public void nonStricthostChecking() throws Exception {
		JSchSftpHelper helper = (JSchSftpHelper) builder.build().addOption("StrictHostKeyChecking", "no");
		
		logger.info("nonStricthostChecking: {}", helper);
		helper.initialise();
		logger.info("Initialised:{}", helper.hashCode());
		helper.dispose();
		logger.info("Disposed:{}", helper.hashCode());
		logger.info("Ready to dispose {}: {}", helper.hashCode(), helper.isFinaliseReady());
	}
	
	/**
	 * Ensure the knwon_hosts file(s) are correctly being configured
	 * @throws Exception 
	 */
	@Test
	public void strictHostChecking() throws Exception{
		SftpHelper helper = builder.build();
		
		logger.info("stricthostChecking: {}", helper);
		helper.initialise();
		logger.info("Initialised:{}", helper.hashCode());
		helper.dispose();
		logger.info("Disposed:{}", helper.hashCode());
	}
}