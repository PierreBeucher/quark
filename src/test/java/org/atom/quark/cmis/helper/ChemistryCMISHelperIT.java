package org.atom.quark.cmis.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.atom.quark.cmis.context.AtomPubBindingContext;
import org.atom.quark.cmis.context.CMISBindingContext;
import org.atom.quark.cmis.context.CMISContext;
import org.atom.quark.cmis.context.WebServiceBindingContext;
import org.atom.quark.cmis.helper.chemistry.ChemistryCMISHelper;
import org.atom.quark.cmis.util.ChemistryCMISUtils;
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

	@Test(dataProvider = "chemistry-cmis-helper")
	public void listDirectory(ChemistryCMISHelper helper) throws Exception{
		helper.init();
		
		String folder = "testQuarkFolder";
		String parent = "/";
		
		helper.createFolderIfNotExists(parent, folder);
		ItemIterable<CmisObject> result = helper.listDirectory("/");
		String found = null;
		for(CmisObject o : result){
			if(o.getName().equals(folder)){
				found = folder;
				break;
			}
		}
		
		Assert.assertEquals(found, folder);

	}

}
