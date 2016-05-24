package com.github.pierrebeucher.quark.cmis.helper;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.context.WebServiceBindingContext;
import com.github.pierrebeucher.quark.cmis.helper.chemistry.ChemistryCMISHelper;
import com.github.pierrebeucher.quark.cmis.util.ChemistryCMISUtils;

public class CmisBindingIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String user;
	private String password;
	
	private URL wsBaseUrl;
	private URL atomPubUrl;
		
	@BeforeClass
	@Parameters({ "chemistry-user", "chemistry-password", "chemistry-atompub-url", "chemistry-ws-base-url" })
	public void beforeClass(String user, String password, String atomPubUrl, String wsBaseUrlStr) throws MalformedURLException{
		this.user = user;
		this.password = password;
		this.atomPubUrl = new URL(atomPubUrl);
		this.wsBaseUrl = new URL(wsBaseUrlStr);
	}
	
	@DataProvider(name = "chemistry-cmis-binding")
	public Object[][] dataProvider() throws MalformedURLException {
		WebServiceBindingContext wsBindingContext = new WebServiceBindingContext(user, password, wsBaseUrl);
		AtomPubBindingContext atomPubBindingContext = new AtomPubBindingContext(user, password, atomPubUrl);

		return new Object[][] {
			{ atomPubBindingContext },
			{ wsBindingContext },
		};
	}
	
//	/**
//	 * Build a Helper for this test using the given binding context. A repository 
//	 * is automatically retrieved using the first repository found available.
//	 * @param bindingContext
//	 * @return
//	 */
//	private ChemistryCMISHelper buildTestHelper(CMISBindingContext bindingContext){
//		String repo = retrieveFirstAvailableRepositoryID(bindingContext);
//		CMISContext ctx = new CMISContext(bindingContext, repo);
//		ChemistryCMISHelper helper = new ChemistryCMISHelper(ctx);
//		
//		logger.debug("Built test helper {}", helper);
//		
//		return helper;
//	}
	
//	/**
//	 * Return the repositoryID of the first available repository, or '-default-' if not repository is found.
//	 * @param baseContext
//	 * @return
//	 */
//	private String retrieveFirstAvailableRepositoryID(CMISBindingContext bindingcontext){
//		List<Repository> repositories = ChemistryCMISUtils.getRepositories(bindingcontext);
//		
//		if(repositories.size() == 0){
//			logger.warn("No repository found for {}. Returning -default- by default.");
//			return "-default-";
//		}
//		
//		return repositories.get(0).getId();
//	}
	
	@Test(dataProvider="chemistry-cmis-binding")
	public void testBinding(CMISBindingContext bindingContext){
		logger.info("Test binding: {}", bindingContext);
		
		//retrieving the repository validate the basic binding context
		Repository repo = ChemistryCMISUtils.getRepositories(bindingContext).get(0);
		CMISContext ctx = new CMISContext(bindingContext, repo.getId());
		ChemistryCMISHelper helper = new ChemistryCMISHelper(ctx);
		
		//initialise create the session, validating the binding context with repo
		helper.initialise();
		helper.dispose();
	}

}
