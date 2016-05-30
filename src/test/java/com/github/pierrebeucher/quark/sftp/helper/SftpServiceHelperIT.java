package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.sftp.context.SftpAuthContext;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.SftpException;

public class SftpServiceHelperIT extends BaseSftpIT<SftpServiceHelper>{
	
	private String testDir;
	
	private File dummyFile;

	public SftpServiceHelperIT(SftpServiceHelper helper, String testDir) throws IOException {
		super(helper);
		this.testDir = testDir;
		this.dummyFile = File.createTempFile("quark", null);
		FileUtils.write(dummyFile, "content");
	}

	@Factory
	@Parameters({"sftp-host", "sftp-port", "sftp-login", "sftp-password",
		"sftp-dynamic-testdir"})
	public static Object[] factory(String host, int port, String login, String password,
			String dynamicSftpDir) throws IOException {
		return new Object[]{
			new SftpServiceHelperIT(
				new SftpServiceHelper(new SftpContext(host, port, new SftpAuthContext(login, password))),
				dynamicSftpDir)
		};
	}
	
	/**
	 * Produce a single file and ensure its properly uploaded
	 * @throws FileNotFoundException
	 * @throws SftpException
	 */
	@Test
	public void testProduceFile() throws FileNotFoundException, SftpException{
		String filename = "produceFileTest";
		helper.produceFile(new FileInputStream(dummyFile), testDir, filename);
		
		Assert.assertTrue(helper.getSftpHelper().containsFile(testDir, filename),
				"Cannot find produced " + filename + " in " + testDir);
	}
	
	/**
	 * Produce multiple files and ensure each file is properly uploaded
	 * @throws FileNotFoundException
	 * @throws SftpException
	 */
	@Test
	public void testProduceFileMap() throws FileNotFoundException, SftpException{
		String produce1 = "produceFileTestMap1";
		String produce2 = "produceFileTestMap2";
		String produce3 = "produceFileTestMap3";
		
		Map<InputStream, String> produceMap = new HashMap<InputStream, String>();
		produceMap.put(new FileInputStream(dummyFile), produce1);
		produceMap.put(new FileInputStream(dummyFile), produce2);
		produceMap.put(new FileInputStream(dummyFile), produce3);
		
		helper.produceFile(produceMap, testDir);
		
		for(String filename : produceMap.values()){
			Assert.assertTrue(helper.getSftpHelper().containsFile(testDir, filename),
					"Cannot find produced " + filename + " in " + testDir);	
		}
		
	}
	
}
