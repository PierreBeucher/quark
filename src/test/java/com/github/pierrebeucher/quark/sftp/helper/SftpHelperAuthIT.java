package com.github.pierrebeucher.quark.sftp.helper;

import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.SftpException;

/**
 * Test SftpHelper authentication using various context: password and public key authentication 
 * @author Pierre Beucher
 *
 */
public class SftpHelperAuthIT extends BaseSftpIT<JSchSftpHelper> {

	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-key", "sftp-key-passphrase"})
	public static Object[] createInstances(String host, int port, String login,
			String password, String key, String keyPassphrase){
	
		SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		JSchSftpHelper passwordAuthHelper = new JSchSftpHelper(new SftpContext(host, port, passwordAuthContext));
		JSchSftpHelper publicKeyAuthHelper = new JSchSftpHelper(new SftpContext(host, port, publicKeyAuthContext));
		
		return new Object[] {
			new SftpHelperAuthIT(passwordAuthHelper),
			new SftpHelperAuthIT(publicKeyAuthHelper),
		};
	}
	
	public SftpHelperAuthIT(JSchSftpHelper helper) {
		super(helper);
	}
	
	@Test
	public void checkAuthentication() throws SftpException{
		//authentication should be done by beforeTest
		//calling a simple function to make sure the Helper works properly
		helper.list("/"); // throw an exception if authentication failed
		
		Assert.assertTrue(helper.isInitialised(), helper + " should be initialised.");
	}
}