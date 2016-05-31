package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;

import org.apache.commons.net.ftp.FTPFile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpHelperIT extends BaseFtpHelperIT<FtpHelper> {

	@Factory
	@Parameters({"ftp-host", "ftp-port", "ftp-login", "ftp-password"})
	public static Object[] beforeClass(String host, int port, String login, String password) throws IOException, InitialisationException{
		String testFileContent = "abcd";
		return new Object[]{
			new FtpHelperIT(new FtpHelper(new FtpContext(host, port, login, password)), createDummyFile(testFileContent))
		};
	}
	
	private static File createDummyFile(String content) throws IOException{
		File testFile = File.createTempFile("quarkftp", null);
		Files.write(testFile.toPath(), content.getBytes()); 
		return testFile;
	}

	private File testFile;
	
	public FtpHelperIT(FtpHelper helper, File testFile) {
		super(helper);
		this.testFile = testFile;
	}

	@AfterClass
	public void afterClass(){
		helper.dispose();
	}

	@Test
	public void upload() throws IOException{
		helper.upload(Files.newInputStream(testFile.toPath()), "/testUpload");
		Assert.assertTrue(helper.isFile("/testUpload"));
	}

	@Test
	public void listFiles() throws IOException{
		String testDir = "/testListFiles";
		helper.makeDirectory(testDir);
		helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile1");
		helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile2");
		helper.upload(Files.newInputStream(testFile.toPath()), testDir + "/testListFile3");

		FTPFile[] files = helper.listFiles(testDir);
		logger.info("listFile: found {} files", files.length);
		Assert.assertEquals(files.length, 3);
	}

	@Test
	public void listDirectories() throws IOException{
		String testDir = "/testListDir";
		helper.makeDirectory(testDir);
		helper.makeDirectory(testDir + "/dir1");
		helper.makeDirectory(testDir + "/dir2");
		helper.makeDirectory(testDir + "/dir3");

		FTPFile[] files = helper.listDirectories(testDir);
		logger.info("listDir: found {} dirs", files.length);
		Assert.assertEquals(files.length, 3);
	}

	@Test
	public void isFilePositive() throws SocketException, IOException{
		helper.upload(testFile, "/testIsFile");
		Assert.assertEquals(helper.isFile("/testIsFile"), true);
	}

	@Test
	public void isFileNegativeNoExist() throws SocketException, IOException{
		Assert.assertEquals(helper.isFile("/doNotExists"), false);
	}

	@Test
	public void isFileNegativeDir() throws SocketException, IOException{
		helper.makeDirectory("/isDir");
		Assert.assertEquals(helper.isFile("/isDir"), false);
	}

	@Test
	public void isDirPositive() throws IOException{
		Assert.assertEquals(helper.isDirectory("/"), true);
	}

	@Test
	public void isDirNegativeFile() throws IOException{
		helper.upload(testFile, "/testIsDirNegativeFile");
		Assert.assertEquals(helper.isDirectory("/testIsDirNegativeFile"), false);
	}

	@Test
	public void isDirNegativeNoExist() throws IOException{
		Assert.assertEquals(helper.isDirectory("/nonExistingDir"), false);
	}

//
//	@Test
//	public void clean() throws SocketException, IOException {
//		String dirToClean = "/cleanerDir";
//		String archiveDir = "/cleanderArchiveDir";
//
//		helper.makeDirectory(dirToClean);
//		helper.makeDirectory(archiveDir);
//		helper.upload(testFile, dirToClean + "/" + testFile.getName());
//
//		FtpCleaner cleaner = createCleaner();
//		cleaner.clean(dirToClean, archiveDir);
//
//		logger.info("{} cleaned to {}", dirToClean, archiveDir);
//
//		Assert.assertEquals(helper.isFile(dirToClean + "/" + testFile.getName()), false);
//		Assert.assertEquals(helper.isFile(archiveDir + "/" + testFile.getName()), true);
//	}
//
//	@Test
//	public void cleanToLocalDir() throws IOException {
//		String dirToClean = "/cleanerDirLocal";
//		helper.makeDirectory(dirToClean);
//		helper.upload(testFile, dirToClean + "/" + testFile.getName());
//
//		FtpCleaner cleaner = createCleaner();
//		String archiveDir = cleaner.cleanToLocalDir(dirToClean);
//
//		logger.info("{} cleaned to {}", dirToClean, archiveDir);
//
//		Assert.assertEquals(helper.isFile(dirToClean + "/" + testFile.getName()), false);
//		Assert.assertEquals(helper.isFile(archiveDir + "/" + testFile.getName()), true);
//	}
}
