package org.atom.quark.sftp.helper;

import java.io.File;

import org.atom.quark.core.result.HelperResult;
import org.atom.quark.sftp.context.SftpAuthContext;
import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test SftpHelper using various context: password and public key authentication 
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelperIT {

	private String host;
	
	private int port;
	
	private String login;
	
	private String password;
	
	private String privateKey;
	
	private String privateKeyPassword;
	
	private File file;
	
	/**
	 * TODO This method is here for debug purpose. Use dependency injection to define context.
	 */
	public JSchSftpHelperIT() {
		super();
		this.host = "192.168.100.42";
		this.port = 22;
		this.login = "quark";
		this.password = "password";
		this.privateKey = "todo";
		this.privateKeyPassword = "privatekeypassword";
		this.file = new File("/tmp/test");
	}
	
	/**
	 * Provider creating SftpHelperBuilder using various context, so as to perform
	 * our test in various contexts.
	 * @return
	 */
	@DataProvider(name = "jschSftpHelperITDataProvider")
	public SftpHelperBuilder[][] createData() {
	
		SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, privateKey, privateKeyPassword);
		
		return new SftpHelperBuilder[][] {
			{ new JSchSftpHelperBuilder(new SftpContext(host, port, file, passwordAuthContext)) },
			{ new JSchSftpHelperBuilder(new SftpContext(host, port, file, publicKeyAuthContext)) },
		};
	}
	
	@Test(dataProvider = "jschSftpHelperITDataProvider")
	public void connect(SftpHelperBuilder builder) throws Exception {
		HelperResult<Boolean> result = builder.build().connect();
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test(dataProvider = "jschSftpHelperITDataProvider")
	public void disconnect(SftpHelperBuilder builder) throws Exception {
		SftpHelper helper = builder.build();
		helper.connect();
		HelperResult<String> result = helper.disconnect();
		Assert.assertEquals(result.isSuccess(), true, result.getActionOutput());
	}
	
	@Test
	public void uploadFileString() {
	  //throw new RuntimeException("Test not implemented");
	}
	
	@Test
	public void uploadFileStringint() {
	  //throw new RuntimeException("Test not implemented");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}
	
}
