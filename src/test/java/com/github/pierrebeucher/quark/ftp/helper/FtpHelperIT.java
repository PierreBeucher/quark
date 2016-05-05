package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FtpHelperIT {

	private String testFileContent;
	private File testFile;
	
	private String host;
	private String login;
	private String password;
	private int port;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@BeforeClass
	@Parameters({"ftp-host", "ftp-port", "ftp-login", "ftp-password"})
	public void beforeClass(String host, int port, String login, String password) throws IOException{
		this.host = host;
		this.login = login;
		this.port = port;
		this.password = password;
		this.testFileContent = "abcd";
		
		//create dummy testfile
		testFile = File.createTempFile("quarkftp", null);
		Files.write(testFile.toPath(), testFileContent.getBytes());
	}

	private FtpHelper createFtpHelper(){
		return new FtpHelper().host(host)
				.port(port)
				.login(login)
				.password(password);
	}

	@Test
	public void connect() throws IOException {
		FtpHelper helper = createFtpHelper();
		try{
			helper.connect();
		} finally {
			helper.disconnect();
		}
	}

	@Test
	public void login() throws IOException {
		FtpHelper helper = createFtpHelper();
		try{
			helper.connect();
			helper.login();
		} finally {
			helper.disconnect();
		}
	}

	@Test
	public void init() throws IOException {
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void upload() throws IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			helper.upload(Files.newInputStream(testFile.toPath()), "/testUpload");
			Assert.assertTrue(helper.isFile("/testUpload"));
		} finally {
			helper.disconnect();
		}
	}

	@Test
	public void listFiles() throws IOException{
		FtpHelper helper = createFtpHelper();
		String testDir = "/testListFiles";
		try{
			helper.init();
			helper.makeDirectory(testDir);
			helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile1");
			helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile2");
			helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile3");
			
			FTPFile[] files = helper.listFiles(testDir);
			logger.info("listFile: found {} files", files.length);
			Assert.assertEquals(files.length, 3);
			
		} finally {
			helper.disconnect();
		}
	}

	@Test
	public void listDirectories() throws IOException{
		FtpHelper helper = createFtpHelper();
		String testDir = "/testListDir";
		try{
			helper.init();
			helper.makeDirectory(testDir);
			helper.makeDirectory(testDir + "/dir1");
			helper.makeDirectory(testDir + "/dir2");
			helper.makeDirectory(testDir + "/dir3");

			FTPFile[] files = helper.listDirectories(testDir);
			logger.info("listDir: found {} dirs", files.length);
			Assert.assertEquals(files.length, 3);
			
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isFilePositive() throws SocketException, IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			helper.upload(testFile, "/testIsFile");
			Assert.assertEquals(helper.isFile("/testIsFile"), true);
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isFileNegativeNoExist() throws SocketException, IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			Assert.assertEquals(helper.isFile("/doNotExists"), false);
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isFileNegativeDir() throws SocketException, IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			helper.makeDirectory("/isDir");
			Assert.assertEquals(helper.isFile("/isDir"), false);
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isDirPositive() throws IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			Assert.assertEquals(helper.isDirectory("/"), true);
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isDirNegativeFile() throws IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			helper.upload(testFile, "/testIsDirNegativeFile");
			Assert.assertEquals(helper.isDirectory("/testIsDirNegativeFile"), false);
		} finally {
			helper.disconnect();
		}
	}
	
	@Test
	public void isDirNegativeNoExist() throws IOException{
		FtpHelper helper = createFtpHelper();
		try{
			helper.init();
			Assert.assertEquals(helper.isDirectory("/nonExistingDir"), false);
		} finally {
			helper.disconnect();
		}
	}
}
