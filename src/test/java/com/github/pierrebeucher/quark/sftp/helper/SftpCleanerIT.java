package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SftpCleanerIT extends BaseSftpIT<SftpCleaner> {

	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password", "sftp-filepath",
		"sftp-dynamic-testdir"})
	public static Object[] beforeClass(String host, int port, String login, String password, String testFile,
			String dynamicTestdir) throws JSchException{
		
		return new Object[]{
			new SftpCleanerIT(
				new SftpCleaner(new SftpContext(host, port, new SftpAuthContext(login, password))),
				testFile,
				dynamicTestdir
			)
		};
	}
	
	private String dynamicTestdir;
	private File testFile;
	
	public SftpCleanerIT(SftpCleaner helper, String testfilePath, String testDir) {
		super(helper);
		this.testFile = new File(testfilePath);
		this.dynamicTestdir = testDir;
	}

	
	@Test
	public void clean() throws SftpException, FileNotFoundException, JSchException {
		//create dummy directories and files
		String dirToClean = dynamicTestdir + "/sftpCleaner_toClean";
		String archiveDir = dynamicTestdir + "/sftpCleaner_archiveDir";
		String filenameToClean = "filetoClean";
		
		helper.getWrappedHelper().mkdirIfNotExists(dirToClean);		
		helper.getWrappedHelper().mkdirIfNotExists(archiveDir); 
		helper.getWrappedHelper().upload(testFile, dirToClean + "/" + filenameToClean);
		
		//run test
		logger.info("Cleaning: {}", helper);
		helper.clean(dirToClean, archiveDir);
		
		Assert.assertEquals(helper.getWrappedHelper().exists(dirToClean, filenameToClean), false);
		Assert.assertEquals(helper.getWrappedHelper().exists(archiveDir, filenameToClean), true);
	}

	@Test
	public void cleanToLocalDir() throws SftpException, JSchException, FileNotFoundException {
		//create dummy directories and files
		String dirToClean = dynamicTestdir + "/sftpCleaner_toCleanLocal";
		String filenameToClean = "filetoClean";
		
		helper.getWrappedHelper().mkdirIfNotExists(dirToClean);		
		helper.getWrappedHelper().upload(testFile, dirToClean + "/" + filenameToClean);


		String archiveDir = helper.clean(dirToClean);
		
		Assert.assertEquals(helper.getWrappedHelper().exists(dirToClean, filenameToClean), false,
				filenameToClean + " should not exists in " + dirToClean);
		Assert.assertEquals(helper.getWrappedHelper().exists(dirToClean + "/" + SftpCleaner.DEFAULT_CLEAN_DIR), true,
				SftpCleaner.DEFAULT_CLEAN_DIR + " should have been created as archive folder in " + dirToClean);
		Assert.assertEquals(helper.getWrappedHelper().exists(archiveDir), true,
				archiveDir + " should not be null and point to a valid archive directory ");
	}
}
