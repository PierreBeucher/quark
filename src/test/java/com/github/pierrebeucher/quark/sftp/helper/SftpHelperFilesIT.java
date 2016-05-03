package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.ExpectingHelperResult;
import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.github.pierrebeucher.quark.sftp.helper.JSchSftpHelperBuilder;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelper;
import com.github.pierrebeucher.quark.sftp.helper.SftpHelperBuilder;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

/**
 * Test SftpHelper files manipulation methods 
 * @author Pierre Beucher
 *
 */
public class SftpHelperFilesIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Checksum for src/test/resources/files/file.xml
	 * TODO replace with parameter
	 */
	private static final String TESTFILE_CHECKSUM = "79f0583f98948d3587b33bbb6183c6ae";
	
	private SftpHelperBuilder builder;
	
	private File testFile;
	
	//private String testDir;
	
	/*
	 * This directory contains static files: they are always present on the machine
	 * (either mounted directly on the container or VM, or part of the setup)
	 */
	private String staticSftpDir;
	
	/*
	 * This directory is dynamic and cleanded before each test run
	 */
	private String dynamicSftpDir;

	public SftpHelperFilesIT(SftpHelperBuilder builder, File testFile, String staticSftpDir,
			String dynamicSftpDir) {
		super();
		this.builder = builder;
		this.testFile = testFile;
		this.staticSftpDir = staticSftpDir;
		this.dynamicSftpDir = dynamicSftpDir;
	}
	
	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-key", "sftp-key-passphrase", "sftp-filepath", "sftp-static-testdir",
		"sftp-dynamic-testdir"})
	public static Object[] createInstances(String host, int port, String login,
			String password, String key, String keyPassphrase, String filePath,
			String staticSftpDir, String dynamicSftpDir){
	
		//SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		//SftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, passwordAuthContext));
		SftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, publicKeyAuthContext));
		
		return new Object[] {
			//new SftpHelperFilesIT(passwordAuthBuilder, new File(filePath), staticSftpDir, dynamicSftpDir),
			new SftpHelperFilesIT(publicKeyAuthBuilder, new File(filePath), staticSftpDir, dynamicSftpDir),
		};
	}
	
	@BeforeClass
	@Parameters
	public void beforeClass() throws Exception{
		//clean the dynamic directory, without using our helper directly
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		ChannelSftp channelSftp = helper.getChannelSftp();
		this.rmBeforeClass(channelSftp, dynamicSftpDir);
	}
	
	private void rmBeforeClass(ChannelSftp channelSftp, String rmDir) throws SftpException{
		for(Object o : channelSftp.ls(rmDir)) {
			LsEntry entry = ((LsEntry) o);
			if(".".equals(entry.getFilename()) || "..".equals(entry.getFilename())){
				continue; //skip . and ..
			}
			
			String rmPath = rmDir + "/" + entry.getFilename();
			
			logger.info("beforeClass: Removing {}", rmPath);
			
			if(entry.getAttrs().isDir()){
				rmBeforeClass(channelSftp, rmPath);
				channelSftp.rmdir(rmPath);
			} else {
				channelSftp.rm(rmPath);
			}
		}
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper() throws Exception{
		SftpHelper helper = builder.build().addOption("StrictHostKeyChecking", "no");
		
		logger.debug("Connect {}", helper);
		
		helper.connect();
		return helper;
	}
	
	@Test
	public void list() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
		Vector<LsEntry> ls = helper.list(staticSftpDir + "/containsTwoFilesOneDir");
		
		//. .. directory file1 file2
		Assert.assertEquals(ls.size(), 5);
	}
	
	@Test
	public void listDirectoryNominal() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listDirectories(staticSftpDir + "/containsOneDirectory");
		
		// . .. dir
		Assert.assertEquals(ls.size(), 3, ls.toString());
	}
	
	@Test
	public void listDirectoryNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listDirectories(staticSftpDir + "/containsOneFile");
		
		// . ..
		Assert.assertEquals(ls.size(), 2, ls.toString());
	}
	
	@Test
	public void listFilesNominal() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listFiles(staticSftpDir + "/containsOneFile");
		
		// file
		Assert.assertEquals(ls.size(), 1, ls.toString());
	}
	
	@Test
	public void listFilesNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listFiles(staticSftpDir + "/containsOneDirectory");
		
		// file
		Assert.assertEquals(ls.size(), 0, ls.toString());
	}
	
	@Test
	public void listFilesMatching() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> result = helper.listFiles(staticSftpDir + "/containsMultipleFiles", Pattern.compile(".*\\.log"));
		boolean success = result.size() == 1 && result.get(0).getFilename().equals("file2.log");
		Assert.assertEquals(success, true, "Result: " + result + " does not contains expected file matching.");
	}
	
	@Test
	public void remove() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		String fileToRemove = dynamicSftpDir + "/fileToRemove";
		helper.upload(testFile, fileToRemove);
		
		helper.remove(fileToRemove);
		Assert.assertEquals(helper.exists(fileToRemove), false);
	}
	
	@Test
	public void removeDir() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		String dirToRemove = dynamicSftpDir + "/dirToRemove";
		helper.mkdirIfNotExists(dirToRemove);
		
		helper.removeDir(dirToRemove);
		Assert.assertEquals(helper.exists(dirToRemove), false);
	}
	
	@Test
	public void getChecksum() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		String checksum = helper.getChecksum(staticSftpDir + "/file.xml");
		Assert.assertEquals(checksum, TESTFILE_CHECKSUM, "Checksum for SFTP test file and original file does not match.");
	}
	
	@Test 
	public void compareChecksumStream() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		InputStream stream = new FileInputStream(testFile);
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(stream, staticSftpDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test 
	public void compareChecksumFile() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(testFile, staticSftpDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test
	public void uploadSimple() throws Exception{
		String uploadAs = "simpleUpload.txt";
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
				
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
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
		
		//upload and overwrite
		helper.upload(testFile, dynamicSftpDir + "/" + uploadAs);
		boolean result = helper.upload(testFile, dynamicSftpDir + "/" + uploadAs, SftpHelper.MODE_OVERWRITE);
		Assert.assertEquals(result, true, "File upload failed.");
	}
	
	@Test
	public void waitForContainsFile() throws Exception{
		final SftpHelper helper = buildNonStrictHostCheckingHelper();
		final String uploadAs = "simpleWaitedUpload.txt";
		
		
		//upload the file in a different thread
		Thread t = new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					helper.upload(testFile, dynamicSftpDir + "/" + uploadAs);
				} catch (Exception e) {
					e.printStackTrace();
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
		final SftpHelper helper = buildNonStrictHostCheckingHelper();
		
		Pattern pattern = Pattern.compile("NonExistingFile.txt");
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(dynamicSftpDir, pattern, 3000, 500);
		
		Assert.assertEquals(result.getActual().size(), 0);
		Assert.assertEquals(result.isSuccess(), false);
	}
	
	@Test
	public void waitForContainsFileCount() throws Exception{
		final SftpHelper helper = buildNonStrictHostCheckingHelper();
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
					e.printStackTrace();
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
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		boolean result = helper.exists(staticSftpDir + "/" + testFile.getName());
		Assert.assertEquals(result, true);
	}
	
	@Test
	public void existsStringNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		boolean result = helper.exists(staticSftpDir + "/" + "DoNotExists");
		Assert.assertEquals(result, false);
	}
	
	@Test
	public void existsStringStringPositive() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		boolean result = helper.exists(staticSftpDir, testFile.getName());
		Assert.assertEquals(result, true);
	}
	
	@Test
	public void existsStringStringNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		boolean result = helper.exists(staticSftpDir, "DoNotExists");
		Assert.assertEquals(result, false);
	}
	
	@Test
	public void mkdirIfNotExists() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
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
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		
		//prepare: clean and create dummy file
		String originName = "dirToMove";
		String origin = dynamicSftpDir + "/" + originName;
		String destination = dynamicSftpDir + "/dirMoveDestination";
		helper.mkdirIfNotExists(origin);
		helper.mkdirIfNotExists(destination);
		
		//test
		helper.move(origin, destination + "/" + originName);
		helper.exists(destination, originName);
	}
	
	@Test
	public void moveDirectoryContent() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		
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