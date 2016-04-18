package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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
import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * Test SftpHelper files manipulation methods 
 * @author Pierre Beucher
 *
 */
public class SftpHelperFilesIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Checksum for src/test/resources/files/file.xml
	 */
	private static final String TESTFILE_CHECKSUM = "79f0583f98948d3587b33bbb6183c6ae";
	
	private SftpHelperBuilder builder;
	
	private File testFile;
	
	private String testDir;

	public SftpHelperFilesIT(SftpHelperBuilder builder, File testFile, String testDir) {
		super();
		this.builder = builder;
		this.testFile = testFile;
		this.testDir = testDir;
	}
	
	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-key", "sftp-key-passphrase", "sftp-filepath", "sftp-testdir"})
	public static Object[] createInstances(String host, int port, String login,
			String password, String key, String keyPassphrase, String filePath, String testDir){
	
		//SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		//SftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, passwordAuthContext));
		SftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, publicKeyAuthContext));
		
		return new Object[] {
			//new SftpHelperFilesIT(passwordAuthBuilder, new File(filePath), testDir),
			new SftpHelperFilesIT(publicKeyAuthBuilder, new File(filePath), testDir),
		};
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper() throws Exception{
		SftpHelper helper = builder.build().addOption("StrictHostKeyChecking", "no");
		
		logger.info("Connect {}", helper);
		
		helper.connect();
		return helper;
	}
	
	@Test
	public void list() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
		Vector<LsEntry> ls = helper.list(testDir + "/containsTwoFilesOneDir");
		
		//. .. directory file1 file2
		Assert.assertEquals(ls.size(), 5);
	}
	
	@Test
	public void listDirectoryNominal() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listDirectories(testDir + "/containsOneDirectory");
		
		// . .. dir
		Assert.assertEquals(ls.size(), 3, ls.toString());
	}
	
	@Test
	public void listDirectoryNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listDirectories(testDir + "/containsOneFile");
		
		// . ..
		Assert.assertEquals(ls.size(), 2, ls.toString());
	}
	
	@Test
	public void listFilesNominal() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listFiles(testDir + "/containsOneFile");
		
		// file
		Assert.assertEquals(ls.size(), 1, ls.toString());
	}
	
	@Test
	public void listFilesNegative() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> ls = helper.listFiles(testDir + "/containsOneDirectory");
		
		// file
		Assert.assertEquals(ls.size(), 0, ls.toString());
	}
	
	@Test
	public void listFilesMatching() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		Vector<LsEntry> result = helper.listFiles(testDir + "/containsMultipleFiles", Pattern.compile(".*\\.log"));
		boolean success = result.size() == 1 && result.get(0).getFilename().equals("file2.log");
		Assert.assertEquals(success, true, "Result: " + result + " does not contains expected file matching.");
	}
	
	@Test
	public void getChecksum() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		String checksum = helper.getChecksum(testDir + "/file.xml");
		Assert.assertEquals(checksum, TESTFILE_CHECKSUM, "Checksum for SFTP test file and original file does not match.");
	}
	
	@Test 
	public void compareChecksumStream() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		InputStream stream = new FileInputStream(testFile);
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(stream, testDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test 
	public void compareChecksumFile() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		ExpectingHelperResult<?, ?> result = helper.compareChecksum(testFile, testDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match, actual:" + result.getActual() + ", expected:" + result.getExpected());
	}
	
	@Test
	public void uploadSimple() throws Exception{
		String uploadAs = "simpleUpload.txt";
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
				
		helper.upload(testFile, testDir + "/" + uploadAs);
		
		Vector<LsEntry> ls = helper.list(testDir);
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
		helper.upload(testFile, testDir + "/" + uploadAs);
		boolean result = helper.upload(testFile, testDir + "/" + uploadAs, SftpHelper.MODE_OVERWRITE);
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
					helper.upload(testFile, testDir + "/" + uploadAs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		//launch the thread and check for file
		t.start();
		Pattern pattern = Pattern.compile(uploadAs);
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(testDir, pattern, 10000, 1000);
		
		Assert.assertEquals(result.getActual().size(), 1);
		Assert.assertEquals(result.getActual().get(0).getFilename(), uploadAs);
	}
	
	@Test
	public void waitForContainsFileNegative() throws Exception{
		final SftpHelper helper = buildNonStrictHostCheckingHelper();
		
		Pattern pattern = Pattern.compile("NonExistingFile.txt");
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(testDir, pattern, 3000, 500);
		
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
					helper.upload(testFile, testDir + "/" + uploadAs1);
					helper.upload(testFile, testDir + "/" + uploadAs2);
					helper.upload(testFile, testDir + "/" + uploadAs3);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		//launch the thread and check for file
		t.start();
		Pattern pattern = Pattern.compile(uploadAsPattern);
		BaseHelperResult<Vector<LsEntry>> result = helper.waitForContainsFile(testDir, pattern, 10000, 1000);
		
		Assert.assertEquals(result.getActual().size(), 3);
	}
	
}