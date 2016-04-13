package org.atom.quark.cmis.helper.chemistry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.atom.quark.cmis.context.CMISBindingContext;
import org.atom.quark.cmis.context.CMISContext;
import org.atom.quark.cmis.context.WebServiceBindingContext;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebServiceBindingHandlerTest {
	
	private String user = "user";
	private String password = "pass";
	private String repository = "repositoryId";
	private String baseUrl = "http://localhost/cmis/ws";
	
	private WebServiceSessionHandler buildHandler() throws MalformedURLException{
		CMISBindingContext bindingContext = new WebServiceBindingContext(user, password, new URL(baseUrl));
		return new WebServiceSessionHandler(new CMISContext(bindingContext, repository));
	}

	@Test
	public void generateSessionParameter() throws MalformedURLException {
		WebServiceSessionHandler handler = buildHandler();
		Map<String, String> result = handler.generateSessionParameter();
		
		Assert.assertEquals(result.get(SessionParameter.USER), user);
		Assert.assertEquals(result.get(SessionParameter.PASSWORD), password);
		Assert.assertEquals(result.get(SessionParameter.REPOSITORY_ID), repository);
		Assert.assertEquals(result.get(SessionParameter.BINDING_TYPE), CMISBindingContext.BINDING_WEBSERVICES);
		
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_ACL_SERVICE), baseUrl + "/ACLService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE), baseUrl + "/DiscoveryService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE), baseUrl + "/MultiFilingService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE), baseUrl + "/NavigationService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_OBJECT_SERVICE), baseUrl + "/ObjectService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_POLICY_SERVICE), baseUrl + "/PolicyService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE), baseUrl + "/RelationshipService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE), baseUrl + "/RepositoryService?wsdl");
		Assert.assertEquals(result.get(SessionParameter.WEBSERVICES_VERSIONING_SERVICE), baseUrl + "/VersioningService?wsdl");
		
	}

	@Test
	public void isReadyPositive() throws MalformedURLException {
		WebServiceSessionHandler handler = buildHandler();
		Assert.assertEquals(handler.isReady(), true);
	}
	
	@Test
	public void isReadyNegative() {
		WebServiceSessionHandler handler = new WebServiceSessionHandler(new CMISContext(new WebServiceBindingContext(), ""));
		Assert.assertEquals(handler.isReady(), false);
	}
}
