package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.ExpectingHelperResult;
import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelper;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

/**
 * Test SftpHelper files manipulation methods 
 * @author Pierre Beucher
 *
 */
public class SftpHelperFilesIT extends BaseSftpIT<SftpHelper>{
	
	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-filepath", "sftp-static-testdir", "sftp-dynamic-testdir"})
	public static Object[] factory(String host, int port, String login, String password,
			String filePath,String staticSftpDir, String dynamicSftpDir){
		return new Object[]{
			new SftpHelperFilesIT(
				new JSchSftpHelper(new SftpContext(host, port, new SftpAuthContext(login, password))),
				filePath, staticSftpDir, dynamicSftpDir
			)
		};
	}
	
	/**
	 * Checksum for src/test/resources/files/file.xml
	 * TODO replace with parameter
	 */
	private static final String TESTFILE_CHECKSUM = "79f0583f98948d3587b33bbb6183c6ae";
	
	private File testFile;
	
	/*
	 * This directory contains static files: they are always present on the machine
	 * (either mounted directly on the container or VM, or part of the setup)
	 */
	private String staticSftpDir;
	
	/*
	 * This directory is dynamic and cleanded before each test run
	 */
	private String dynamicSftpDir;

	public SftpHelperFilesIT(SftpHelper helper, String testfilePath, String staticSftpDir,
			String dynamicSftpDir) {
		super(helper);
		this.testFile = new File(testfilePath);
		this.staticSftpDir = staticSftpDir;
		this.dynamicSftpDir = dynamicSftpDir;
	}
	
	@Override
	@BeforeTest
	public void beforeClass() {
		super.beforeClass();
		try {
			rmBeforeTest(helper.getChannelSftp(), this.dynamicSftpDir);
		} catch (SftpException e) {
			logger.error("Cannot clean before test: " + e.getMessage(), e);
		}
	}
	
	private void rmBeforeTest(ChannelSftp channelSftp, String rmDir) throws SftpException{
		for(Object o : channelSftp.ls(rmDir)) {
			LsEntry entry = ((LsEntry) o);
			if(".".equals(entry.getFilename()) || "..".equals(entry.getFilename())){
				continue; //skip . and ..
			}
			
			String rmPath = rmDir + "/" + entry.getFilename();
			
			logger.info("beforeTest: Removing {}", rmPath);
			
			if(entry.getAttrs().isDir()){
				rmBeforeTest(channelSftp, rmPath);
				channelSftp.rmdir(rmPath);
			} else {
				channelSftp.rm(rmPath);
			}
		}
	}
	

	@Test
	public void list() throws Exception{
		Vector<LsEntry> ls = helper.list(staticSftpDir + "/containsTwoFilesOneDir");
		
		//. .. directory file1 file2
		Assert.assertEquals(ls.size(), 5);
	}
	
	@Test
	public void listDirectoryNominal() throws Exception{
		Vector<LsEntry> ls = helper.listDirectories(staticSftpDir + "/containsOneDirectory");
		
		// . .. dir
		Assert.assertEquals(ls.size(), 3, ls.toString());
	}
	
	@Test
	public void listDirectoryNegative() throws Exception{
		Vector<LsEntry> ls = helper.listDirectories(staticSftpDir + "/containsOneFile");
		
		// . ..
		Assert.assertEquals(ls.size(), 2, ls.toString());
	}
	
	@Test
	public void listFilesNominal() throws Exception{
		Vector<LsEntry> ls = helper.listFiles(staticSftpDir + "/containsOneFile");
		
		// file
		Assert.assertEquals(ls.size(), 1, ls.toString());
	}
	
	@Test
	public void listFilesNegative() throws Exception{
		Vector<LsEntry> ls = helper.listFiles(staticSftpDir + "/containsOneDirectory");
		
		// file
		Assert.assertEquals(ls.size(), 0, ls.toString());
	}
	
	@Test
	public void listFilesMatching() throws Exception{
		Vector<LsEntry> result = helper.listFiles(staticSftpDir + "/containsMultipleFiles", Pattern.compile(".*\\.log"));
		boolean success = result.size() == 1 && result.get(0).getFilename().equals("file2.log");
		Assert.assertEquals(success, true, "Result: " + result + " does not contains expected file matching.");
	}
	
	@Test
	public void remove() throws Exception{
		String fileToRemove = dynamicSftpDir + "/fileToRemove";
		helper.upload(testFile, fileToRemove);
		
		helper.remove(fileToRemove);
		Assert.assertEquals(helper.exists(fileToRemove), false);
	}
	
	@Test
	public void removeDir() throws Exception{
		String dirToRemove = dynamicSftpDir + "/dirToRemove";
		helper.mkdirIfNotExists(dirToRemove);
		
		helper.removeDir(dirToRemove);
		Assert.assertEquals(helper.exists(dirToRemove), false);
	}
	
	@Test
	public void getChecksum() throws Exception{
		String checksum = helper.getChecksum(staticSftpDir + "/file.xml");
		Assert.assertEquals(checksum, TESTFILE_CHECKSUM, "Checksum for SFTP test file and original file does not match.");
	}
	
	@Test 
	public void compareChecksumStream() throws Exception{
		InputStream stream = new FileInputStream(testFile);
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(stream, staticSftpDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test 
	public void compareChecksumFile() throws Exception{
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(testFile, staticSftpDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test
	public void uploadSimple() throws Exception{
		String uploadAs = "simpleUpload.txt";
				
		helper.upload(testFile, dynamicSftpDir + "/" + uploadAs);
		
		Vector<LsEntry> ls = helper.list(dynamicSftpDir);
		boolean success = false;
		for(LsEntry entry : ls){
			if(entry.getFilename().equals(uploadAs)){
				success = true;
				break;
			}
		}
		
		Assert.assertEquals(success, true, "Cannot find file " + uploadAs + " after upload");
	}
	
	@Test
	public void uploadOverwrite() throws Exception{
		String uploadAs = "simpleUploadOverwrite.txt";
		
		//upload and overwrite
		helper.upload(testFile, dynamicSftpDir + "/" + uploadAs);
		boolean result = helper.upload(testFile, dynamicSftpDir + "/" + uploadAs, SftpHelper.MODE_OVERWRITE);
		Assert.assertEquals(result, true, "File upload failed.");
	}
	
	@Test
	public void waitForContainsFile() throws Exception{
		final String uploadAs = "simpleWaitedUpload.txt";
		
		
		//upload the file in a different thread
		Thread t = new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					helper.upload(testFile, dynamicSftpDir + "/" + uploadAs);
				} catch (Exception e) {
					logger.error("Cannot upload file: {}", e);
					throw new RuntimeException(e);
				}
			}
		};
		
		//launch the thread and check for file
		t.start();
		Pattern pattern = Pattern.compile(uploadAs);
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(dynamicSftpDir, pattern, 10000, 1000);
		
		Assert.assertEquals(result.getActual().size(), 1);
		Assert.assertEquals(result.getActual().get(0).getFilename(), uploadAs);
	}
	
	@Test
	public void waitForContainsFileNegative() throws Exception{
		Pattern pattern = Pattern.compile("NonExistingFile.txt");
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(dynamicSftpDir, pattern, 3000, 500);
		
		Assert.assertEquals(result.getActual().size(), 0);
		Assert.assertEquals(result.isSuccess(), false);
	}
	
	@Test
	public void waitForContainsFileCount() throws Exception{
		final String uploadAs1 = "simpleWaitedUpload1.txt";
		final String uploadAs2 = "simpleWaitedUpload2.txt";
		final String uploadAs3 = "simpleWaitedUpload3.txt";
		String uploadAsPattern = "simpleWaitedUpload[0-9].txt";
		
		
		//upload the file in a different thread
		Thread t = new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					helper.upload(testFile, dynamicSftpDir + "/" + uploadAs1);
					helper.upload(testFile, dynamicSftpDir + "/" + uploadAs2);
					helper.upload(testFile, dynamicSftpDir + "/" + uploadAs3);
				} catch (Exception e) {
					logger.error("Cannot upload file: {}", e);
					throw new RuntimeException(e);
				}
			}
		};
		
		//launch the thread and check for file
		t.start();
		Pattern pattern = Pattern.compile(uploadAsPattern);
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(dynamicSftpDir, pattern, 10000, 1000);
		
		Assert.assertEquals(result.getActual().size(), 3);
	}
	
	@Test
	public void existsStringPositive() throws Exception{
		boolean result = helper.exists(staticSftpDir + "/" + testFile.getName());
		Assert.assertEquals(result, true);
	}
	
	@Test
	public void existsStringNegative() throws Exception{
		boolean result = helper.exists(staticSftpDir + "/" + "DoNotExists");
		Assert.assertEquals(result, false);
	}
	
	@Test
	public void existsStringStringPositive() throws Exception{
		boolean result = helper.exists(staticSftpDir, testFile.getName());
		Assert.assertEquals(result, true);
	}
	
	@Test
	public void existsStringStringNegative() throws Exception{
		boolean result = helper.exists(staticSftpDir, "DoNotExists");
		Assert.assertEquals(result, false);
	}
	
	@Test
	public void mkdirIfNotExists() throws Exception{
		String dirname = "Dir_mkdirIfNotExists";

		logger.info("Creating {} if not exists.", dirname);
		helper.mkdirIfNotExists(dynamicSftpDir + "/" + dirname);
		Assert.assertEquals(helper.exists(dynamicSftpDir, dirname), true);
		
		logger.info("Creating again {} if not exists.", dirname);
		helper.mkdirIfNotExists(dynamicSftpDir + "/" + dirname);
		Assert.assertEquals(helper.exists(dynamicSftpDir, dirname), true);	
	}
	
	@Test
	public void moveDirectory() throws Exception{
		//prepare: clean and create dummy file
		String originName = "dirToMove";
		String origin = dynamicSftpDir + "/" + originName;
		String destination = dynamicSftpDir + "/dirMoveDestination";
		helper.mkdirIfNotExists(origin);
		helper.mkdirIfNotExists(destination);
		
		//test
		logger.info("Moving {} to {}/{}", origin, destination, originName);
		helper.move(origin, destination + "/" + originName);
		helper.exists(destination, originName);
	}
	
	@Test
	public void moveDirectoryContent() throws Exception{
		//prepare: create complex directory tree
		String dest = dynamicSftpDir + "/moveDirectoryContentDest";
		String origin = dynamicSftpDir + "/moveDirectoryContentOrigin";
		
		String originChildDir = origin + "/childDir";
		String originChildFile = origin + "/childFile";
		String originChildDirFile = originChildDir + "/testFile";
		
		String destChildDir = dest + "/childDir";
		String destChildFile = dest + "/childFile";
		String destChildDirFile = destChildDir + "/testFile";
		
		helper.mkdirIfNotExists(dest);
		helper.mkdirIfNotExists(origin);
		helper.mkdirIfNotExists(originChildDir);
		
		helper.upload(testFile, originChildFile);
		helper.upload(testFile, originChildDirFile);
		
		//test
		helper.moveDirectoryContent(origin, dest);
		Assert.assertEquals(helper.exists(destChildDir), true);
		Assert.assertEquals(helper.exists(destChildFile), true);
		Assert.assertEquals(helper.exists(destChildDirFile), true);
		Assert.assertEquals(helper.exists(originChildDir), false);
		Assert.assertEquals(helper.exists(originChildFile), false);
		Assert.assertEquals(helper.exists(originChildDirFile), false);
	}
	
}