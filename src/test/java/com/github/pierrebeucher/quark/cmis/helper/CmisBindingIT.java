package com.github.pierrebeucher.quark.cmis.helper;

import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.context.WebServiceBindingContext;
import com.github.pierrebeucher.quark.cmis.helper.chemistry.ChemistryCMISHelper;

public class CmisBindingIT extends BaseCMISHelperIT<ChemistryCMISHelper> {

	@Factory
	@Parameters({ "chemistry-user", "chemistry-password", "chemistry-atompub-url", "chemistry-ws-base-url" })
	public static Object[] factory(String user, String password, String atomPubUrlStr, String wsBaseUrlStr) throws MalformedURLException{
		URL wsBaseUrl = new URL(wsBaseUrlStr);
		URL atomPubUrl = new URL(atomPubUrlStr);
		
		WebServiceBindingContext wsBindingContext = new WebServiceBindingContext(user, password, wsBaseUrl);
		AtomPubBindingContext atomPubBindingContext = new AtomPubBindingContext(user, password, atomPubUrl);

		return new Object[] {
			new CmisBindingIT(createHelper(atomPubBindingContext)),
			new CmisBindingIT(createHelper(wsBindingContext))
		};
	}
	
	private static ChemistryCMISHelper createHelper(CMISBindingContext bindingContext){
		String repo = retrieveFirstAvailableRepositoryID(bindingContext);
		CMISContext ctx = new CMISContext(bindingContext, repo);
		ChemistryCMISHelper helper = new ChemistryCMISHelper(ctx);
		
		return helper;
	}
	
	public CmisBindingIT(ChemistryCMISHelper helper) {
		super(helper);
	}
	
	@Test
	public void checkBinding(){
		//binding is done with beforeClass, ensuring the Helper works properly
		Assert.assertTrue(helper.getSession() != null &&
				helper.getSession().getBinding().getSessionId() != null);
	}

}
