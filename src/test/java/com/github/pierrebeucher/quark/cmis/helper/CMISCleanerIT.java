package com.github.pierrebeucher.quark.cmis.helper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.WebServiceBindingContext;

public class CMISCleanerIT extends BaseCMISHelperIT<CMISCleaner> {

	@Factory
	@Parameters({ "chemistry-user", "chemistry-password", "chemistry-atompub-url", "chemistry-ws-base-url" })
	public static Object[] factory(String user, String password, String atomPubUrl, String wsBaseUrl) throws MalformedURLException{		
		return new Object[]{
			new CMISCleanerIT(
				new CMISCleaner(buildTestHelper(new WebServiceBindingContext(user, password, new URL(wsBaseUrl)))),
				"/"
			)
		};
	}
	
	private String parentTestFolder;
	
	public CMISCleanerIT(CMISCleaner helper, String testFolder) {
		super(helper);
		this.parentTestFolder = testFolder;
	}

	@Test
	public void clean() throws IOException{
		//create dummy files and folders
		String cleanFolderName = "quarkCleanFolder";
		Folder cleanFolder = helper.getWrappedHelper().createFolderIfNotExists(parentTestFolder, 
				cleanFolderName);
		
		File tmpFile = File.createTempFile("quark", "");
		tmpFile.deleteOnExit();
		createDocumentFromFileIfNotExists(cleanFolder.getPath(), tmpFile, helper.getWrappedHelper().getSession());
		
		CMISCleaner cleaner = new CMISCleaner(helper.getContext());
		cleaner.initialise();
		String archiveDir = cleaner.clean(cleanFolder.getPath());
		
		logger.info("Cleaned from {} to {}", cleanFolder.getPath(), archiveDir);
		cleaner.dispose();
		
		Assert.assertEquals(helper.getWrappedHelper().listDirectory(cleanFolder.getPath()).getTotalNumItems(), 0);
		Assert.assertEquals(helper.getWrappedHelper().listDirectory(archiveDir).getTotalNumItems(), 1);
		
	}
}
