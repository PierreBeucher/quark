package org.atom.quark.cmis.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.atom.quark.cmis.context.AtomPubBindingContext;
import org.atom.quark.cmis.context.CMISBindingContext;
import org.atom.quark.cmis.context.CMISContext;
import org.atom.quark.cmis.context.WebServiceBindingContext;
import org.atom.quark.cmis.helper.chemistry.ChemistryCMISHelper;
import org.atom.quark.cmis.util.ChemistryCMISUtils;
import org.atom.quark.core.result.TypedHelperResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Base IT Class for CMISChemistry helper
 * @author Pierre Beucher
 *
 */
public class ChemistryCMISHelperIT {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String user;
	private String password;
	
	private URL wsBaseUrl;
	private URL atomPubUrl;
	
	private String testFolder = "testQuarkFolder";
	private String parentTestFolder = "/";
	
	@BeforeClass
	@Parameters({ "chemistry-user", "chemistry-password", "chemistry-atompub-url", "chemistry-ws-base-url" })
	public void beforeClass(String user, String password, String atomPubUrl, String wsBaseUrl) throws MalformedURLException{
		this.user = user;
		this.password = password;
		this.atomPubUrl = new URL(atomPubUrl);
		this.wsBaseUrl = new URL(wsBaseUrl);
	}
	
	/**
	 * Build a Helper for this test using the given binding context. A repository 
	 * is automatically retrieved using the first repository found available.
	 * @param bindingContext
	 * @return
	 */
	private ChemistryCMISHelper buildTestHelper(CMISBindingContext bindingContext){
		String repo = retrieveFirstAvailableRepositoryID(bindingContext);
		CMISContext ctx = new CMISContext(bindingContext, repo);
		ChemistryCMISHelper helper = new ChemistryCMISHelper(ctx);
		
		logger.debug("Built test helper {}", helper);
		
		return helper;
	}

	/**
	 * Return the repositoryID of the first available repository, or '-default-' if not repository is found.
	 * @param baseContext
	 * @return
	 */
	private String retrieveFirstAvailableRepositoryID(CMISBindingContext bindingcontext){
		List<Repository> repositories = ChemistryCMISUtils.getRepositories(bindingcontext);
		
		if(repositories.size() == 0){
			logger.warn("No repository found for {}. Returning -default- by default.");
			return "-default-";
		}
		
		return repositories.get(0).getId();
	}
	
	@DataProvider(name = "chemistry-cmis-helper")
	public Object[][] dataProvider() throws MalformedURLException {
		WebServiceBindingContext wsBindingContext = new WebServiceBindingContext(user, password, wsBaseUrl);
		AtomPubBindingContext atomPubBindingContext = new AtomPubBindingContext(user, password, atomPubUrl);

		return new Object[][] {
			{ buildTestHelper(atomPubBindingContext) },
			{ buildTestHelper(wsBindingContext) },
		};
	}

	public Folder createFolderIfNotExists(String parent, String name, Session session){
		try {
			return FileUtils.createFolder(parent, name, null, session);
		} catch (CmisContentAlreadyExistsException e) {
			//expected, folder already exists, return it
			return FileUtils.getFolder(parent, session);
		}
	}

	private Document createDocumentFromFileIfNotExists(String parent, File file, Session session) throws FileNotFoundException{
		return FileUtils.createDocumentFromFile(parent, file, null, VersioningState.MINOR, session);
	}

	@Test(dataProvider = "chemistry-cmis-helper")
	public void listDirectory(ChemistryCMISHelper helper) throws Exception{
		helper.init();
		
		createFolderIfNotExists(parentTestFolder, testFolder, helper.getSession());
		
		ItemIterable<CmisObject> result = helper.listDirectory("/");
		Folder found = null;
		for(CmisObject o : result){
			if(o.getName().equals(testFolder) && ChemistryCMISUtils.isFolder(o)){
				found = (Folder) o;
				break;
			}
		}
		Assert.assertEquals(found.getName(), testFolder);
		
		//cleanup
		helper.getSession().delete(found);
	}
	
	@Test(dataProvider = "chemistry-cmis-helper")
	public void containsFile(ChemistryCMISHelper helper) throws IOException{
		helper.init();
		
		File tmpFile = File.createTempFile("quark", "");
		tmpFile.deleteOnExit();
		Document doc = this.createDocumentFromFileIfNotExists(parentTestFolder, tmpFile, helper.getSession());
		
		TypedHelperResult<List<Document>> result = helper.containsDocument(parentTestFolder, Pattern.compile(tmpFile.getName()));
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual().size(), 1);
		Assert.assertEquals(result.getActual().get(0).getName(), doc.getName());
		
		logger.info("File found: {}", result.getActual().get(0).getName());
		
		//cleanup
		helper.getSession().delete(doc);
	}
	
	@Test(dataProvider = "chemistry-cmis-helper")
	public void containsDocument(ChemistryCMISHelper helper) throws Exception{
		helper.init();
		
		final File tmpFile = File.createTempFile("quark", "");
		tmpFile.deleteOnExit();
		createDocumentFromFileIfNotExists(parentTestFolder, tmpFile, helper.getSession());
		
		TypedHelperResult<Document> result = helper.containsDocument(parentTestFolder + tmpFile.getName());
		Assert.assertNotNull(result.getActual(), "A Document is expected to be found");
		Assert.assertEquals(result.getActual().getName(), tmpFile.getName(), "A document matching the name is expected to be found");
	}
	
	@Test(dataProvider = "chemistry-cmis-helper")
	public void waitForContainsDocument(final ChemistryCMISHelper helper) throws Exception{
		helper.init();
		
		final File tmpFile = File.createTempFile("quark", "");
		tmpFile.deleteOnExit();
		
		Thread waiterRunnable = new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					logger.error("Error during wait before create Document: {}", e);
				}
				
				try {
					createDocumentFromFileIfNotExists(parentTestFolder, tmpFile, helper.getSession());
				} catch (FileNotFoundException e) {
					logger.error("Error when creating Document: {}", e);
				}
			}
		};
		waiterRunnable.start();
		
		TypedHelperResult<Document> result = helper.waitForContainsDocument(parentTestFolder + tmpFile.getName(), 10000, 1000);
		
		Assert.assertNotNull(result.getActual(),"A document is expected to be found");
		Assert.assertEquals(result.getActual().getName(), tmpFile.getName(), "A Document matching the name is expected to be found");
	}

}
