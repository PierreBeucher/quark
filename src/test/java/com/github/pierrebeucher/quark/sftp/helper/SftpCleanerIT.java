package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SftpCleanerIT {
	
	private String login;
	private String password;
	private String host;
	private int port;
	private String dynamicTestdir;
	private File testFile;

	@BeforeClass
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password", "sftp-filepath",
		"sftp-dynamic-testdir"})
	public void beforeClass(String host, int port, String login, String password, String testFile,
			String dynamicTestdir){
		this.login = login;
		this.password = password;
		this.host = host;
		this.port = port;
		this.dynamicTestdir = dynamicTestdir;
		this.testFile = new File(testFile);
	}
	
	private SftpContext createSftpContext(){
		return new SftpContext(host, port, new SftpAuthContext(login, password));
	}
	
	private SftpCleaner createSftpCleaner(){
		return new SftpCleaner(createSftpContext());
	}
	
	private JSchSftpHelper createSftpHelper() throws JSchException{
		JSchSftpHelper helper = new JSchSftpHelper(createSftpContext());
		helper.addOption("StrictHostKeyChecking", "no");
		helper.connect();
		return helper;
	}
	
	@Test
	public void clean() throws SftpException, FileNotFoundException, JSchException {
		//create dummy directories and files
		String dirToClean = dynamicTestdir + "/sftpCleaner_toClean";
		String archiveDir = dynamicTestdir + "/sftpCleaner_archiveDir";
		String filenameToClean = "filetoClean";
		
		JSchSftpHelper helper = createSftpHelper();
		helper.mkdirIfNotExists(dirToClean);		
		helper.mkdirIfNotExists(archiveDir); 
		helper.upload(testFile, dirToClean + "/" + filenameToClean);
		
		//run test
		SftpCleaner cleaner = createSftpCleaner();
		cleaner.clean(dirToClean, archiveDir);
		
		Assert.assertEquals(helper.exists(dirToClean, filenameToClean), false);
		Assert.assertEquals(helper.exists(archiveDir, filenameToClean), true);
	}

	@Test
	public void cleanToLocalDir() throws SftpException, JSchException, FileNotFoundException {
		//create dummy directories and files
		String dirToClean = dynamicTestdir + "/sftpCleaner_toCleanLocal";
		String filenameToClean = "filetoClean";
		
		JSchSftpHelper helper = createSftpHelper();
		helper.mkdirIfNotExists(dirToClean);		
		helper.upload(testFile, dirToClean + "/" + filenameToClean);
		
		//run test
		SftpCleaner cleaner = createSftpCleaner();
		String archiveDir = cleaner.cleanToLocalDir(dirToClean);
		
		Assert.assertEquals(helper.exists(dirToClean, filenameToClean), false);
		Assert.assertEquals(helper.exists(dirToClean + "/." + SftpCleaner.DEFAULT_CLEAN_DIR), true);
		Assert.assertEquals(helper.exists(archiveDir), true);
	}

//	@Test
//	public void cleanToTempDir() {
//		throw new RuntimeException("Test not implemented");
//	}
}
