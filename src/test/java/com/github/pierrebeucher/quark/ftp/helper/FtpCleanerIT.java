package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpCleanerIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private FtpCleaner cleaner;
	private File testFile;
	
	@BeforeClass
	@Parameters({"ftp-host", "ftp-port", "ftp-login", "ftp-password"})
	public void beforeClass(String host, int port, String login, String password) throws IOException{
		//create dummy testfile
		testFile = File.createTempFile("quarkftp", null);
		Files.write(testFile.toPath(), "somecontent".getBytes());
		
		cleaner = new FtpCleaner(new FtpContext(host, port, login, password));
		cleaner.initialise();
	}
	
	@AfterClass
	public void afterClass(){
		cleaner.dispose();
	}
	

	@Test
	public void clean() throws SocketException, IOException {
		String dirToClean = "/cleanerDir";
		String archiveDir = "/cleanderArchiveDir";

		FtpHelper helper = cleaner.getHelper();
		helper.makeDirectory(dirToClean);
		helper.makeDirectory(archiveDir);
		helper.upload(testFile, dirToClean + "/" + testFile.getName());

		cleaner.clean(dirToClean, archiveDir);

		logger.info("{} cleaned to {}", dirToClean, archiveDir);

		Assert.assertEquals(helper.isFile(dirToClean + "/" + testFile.getName()), false);
		Assert.assertEquals(helper.isFile(archiveDir + "/" + testFile.getName()), true);
	}

	@Test
	public void cleanToLocalDir() throws IOException {
		String dirToClean = "/cleanerDirLocal";
		
		FtpHelper helper = cleaner.getHelper();
		helper.makeDirectory(dirToClean);
		helper.upload(testFile, dirToClean + "/" + testFile.getName());

		String archiveDir = cleaner.cleanToLocalDir(dirToClean);
		logger.info("{} cleaned to {}", dirToClean, archiveDir);

		Assert.assertEquals(helper.isFile(dirToClean + "/" + testFile.getName()), false);
		Assert.assertEquals(helper.isFile(archiveDir + "/" + testFile.getName()), true);
	}

}
