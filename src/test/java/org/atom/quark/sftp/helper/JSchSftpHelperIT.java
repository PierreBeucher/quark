package org.atom.quark.sftp.helper;

import java.io.File;

import org.atom.quark.core.result.HelperResult;
import org.atom.quark.sftp.context.SftpAuthContext;
import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
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
			//new JSchSftpHelperIT(publicKeyAuthBuilder),
		};
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper(SftpHelperBuilder builder){
		return builder.build().addOption("StrictHostKeyChecking", "no");
	}


//	private String host;
//	
//	private int port;
//	
//	private String login;
//	
//	private String password;
//	
//	private String privateKey;
//	
//	private String privateKeyPassword;
//	
//	private File file;
	
//	/**
//	 * TODO This method is here for debug purpose. Use dependency injection to define context.
//	 */
//	public JSchSftpHelperIT() {
//		super();
////		this.host = "192.168.100.42";
////		this.port = 22;
////		this.login = "quark";
////		this.password = "password";
////		this.privateKey = "todo";
////		this.privateKeyPassword = "privatekeypassword";
////		this.file = new File("/tmp/test");
//	}
	
	
	
//	/**
//	 * Provider creating SftpHelperBuilder using various context, so as to perform
//	 * our test in various contexts.
//	 * @return
//	 */
//	@DataProvider(name = "jschSftpHelperITDataProvider")
//	@Parameters({ "sftp-host"})
//	public SftpHelperBuilder[][] createData(String host) {
//		
//		return null;
////		File testFile = new File(filePath);
////		SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
////		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, privateKey, privateKeyPassword);
////		
////		return new SftpHelperBuilder[][] {
////			{ new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, passwordAuthContext)) },
////			{ new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, publicKeyAuthContext)) },
////		};
//	}
//	
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
//	
//	@Test
//	public void uploadFileString() {
//	  //throw new RuntimeException("Test not implemented");
//	}
//	
//	@Test
//	public void uploadFileStringint() {
//	  //throw new RuntimeException("Test not implemented");
//	}

//	public String getHost() {
//		return host;
//	}
//
//	public void setHost(String host) {
//		this.host = host;
//	}
//
//	public int getPort() {
//		return port;
//	}
//
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//	public String getLogin() {
//		return login;
//	}
//
//	public void setLogin(String login) {
//		this.login = login;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getPrivateKey() {
//		return privateKey;
//	}
//
//	public void setPrivateKey(String privateKey) {
//		this.privateKey = privateKey;
//	}
//
//	public String getPrivateKeyPassword() {
//		return privateKeyPassword;
//	}
//
//	public void setPrivateKeyPassword(String privateKeyPassword) {
//		this.privateKeyPassword = privateKeyPassword;
//	}
	
}
