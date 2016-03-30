package org.atom.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.regex.Pattern;

import org.atom.quark.core.result.HelperResult;
import org.atom.quark.sftp.context.SftpAuthContext;
import org.atom.quark.sftp.context.SftpContext;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * Test SftpHelper files manipulation methods 
 * @author Pierre Beucher
 *
 */
public class SftpHelperFilesIT {
	
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
	
		File testFile = new File(filePath);
		//SftpAuthContext passwordAuthContext = new SftpAuthContext(login, password);
		SftpAuthContext publicKeyAuthContext = new SftpAuthContext(login, key, keyPassphrase);
		
		//SftpHelperBuilder passwordAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, passwordAuthContext));
		SftpHelperBuilder publicKeyAuthBuilder = new JSchSftpHelperBuilder(new SftpContext(host, port, testFile, publicKeyAuthContext));
		
		return new Object[] {
			//new SftpHelperFilesIT(passwordAuthBuilder, new File(filePath), testDir),
			new SftpHelperFilesIT(publicKeyAuthBuilder, new File(filePath), testDir),
		};
	}
	
	private SftpHelper buildNonStrictHostCheckingHelper() throws Exception{
		SftpHelper helper = builder.build().addOption("StrictHostKeyChecking", "no");
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
		HelperResult result = helper.compareChecksum(stream, testDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match: expected=" + result.getExpected() + ", actual=" + result.getActual());
	}
	
	@Test 
	public void compareChecksumFile() throws Exception{
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		HelperResult result = helper.compareChecksum(testFile, testDir + "/file.xml");
		Assert.assertEquals(result.isSuccess(), true, "Checksum does not match: expected=" + result.getExpected() + ", actual=" + result.getActual());
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
			success = entry.getFilename().equals(uploadAs);
		}
		
		Assert.assertEquals(success, true, "Cannot find file " + uploadAs + " after upload");
	}
	
	@Test
	public void uploadOverwrite() throws Exception{
		String uploadAs = "simpleUpload.txt";
		SftpHelper helper = buildNonStrictHostCheckingHelper();
		helper.connect();
		
		//upload and overwrite
		helper.upload(testFile, testDir + "/" + uploadAs);
		boolean result = helper.upload(testFile, testDir + "/" + uploadAs, SftpHelper.MODE_OVERWRITE);
		Assert.assertEquals(result, true, "File upload failed.");
	}
	
}