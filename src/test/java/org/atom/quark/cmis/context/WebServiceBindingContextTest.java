package org.atom.quark.cmis.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebServiceBindingContextTest {
	
	private String baseUrl = "http://localhost/cmis/ws";
	private String user = "user";
	private String password = "password";

	@Test
	public void WebServiceBindingContext() {
		WebServiceBindingContext ctx = new WebServiceBindingContext();
		Assert.assertNull(ctx.getUser());
		Assert.assertNull(ctx.getAclService());
		Assert.assertNull(ctx.getBindingType());
		Assert.assertNull(ctx.getDiscoveryService());
		Assert.assertNull(ctx.getMultifilingService());
		Assert.assertNull(ctx.getNavigationService());
		Assert.assertNull(ctx.getObjectService());
		Assert.assertNull(ctx.getPassword());
		Assert.assertNull(ctx.getPolicyService());
		Assert.assertNull(ctx.getRelationshipService());
		Assert.assertNull(ctx.getRepositoryService());
		Assert.assertNull(ctx.getVersioningService());
	}

	@Test
	public void WebServiceBindingContextStringStringStringURL() throws MalformedURLException {
		WebServiceBindingContext ctx = new WebServiceBindingContext(user, password, new URL(baseUrl));
		Assert.assertEquals(ctx.getUser(), user);
		Assert.assertEquals(ctx.getPassword(), password);
		Assert.assertEquals(ctx.getBindingType().toString(), CMISBindingContext.BINDING_WEBSERVICES);
		
		Assert.assertEquals(ctx.getAclService().toString(), baseUrl + "/ACLService?wsdl");
		Assert.assertEquals(ctx.getDiscoveryService().toString(), baseUrl + "/DiscoveryService?wsdl");
		Assert.assertEquals(ctx.getMultifilingService().toString(), baseUrl + "/MultiFilingService?wsdl");
		Assert.assertEquals(ctx.getNavigationService().toString(), baseUrl + "/NavigationService?wsdl");
		Assert.assertEquals(ctx.getObjectService().toString(), baseUrl + "/ObjectService?wsdl");
		Assert.assertEquals(ctx.getPolicyService().toString(), baseUrl + "/PolicyService?wsdl");
		Assert.assertEquals(ctx.getRelationshipService().toString(), baseUrl + "/RelationshipService?wsdl");
		Assert.assertEquals(ctx.getRepositoryService().toString(), baseUrl + "/RepositoryService?wsdl");
		Assert.assertEquals(ctx.getVersioningService().toString(), baseUrl + "/VersioningService?wsdl");
	}

	@Test
	public void WebServiceBindingContextStringStringStringURLURLURLURLURLURLURLURLURL() throws MalformedURLException {
		URL aclService = new URL(baseUrl + "/ACLService?wsdl");
		URL discoveryUrl = new URL(baseUrl + "/DiscoveryService?wsdl");
		URL multiFilingService = new URL(baseUrl + "/MultiFilingService?wsdl");
		URL navigationService = new URL(baseUrl + "/NavigationService?wsdl");
		URL objectService = new URL(baseUrl + "/ObjectService?wsdl");
		URL policyService = new URL(baseUrl + "/PolicyService?wsdl");
		URL relationshipService = new URL(baseUrl + "/RelationshipService?wsdl");
		URL repositoryService = new URL(baseUrl + "/RepositoryService?wsdl");
		URL versioningService = new URL(baseUrl + "/VersioningService?wsdl");
		
		WebServiceBindingContext ctx = new WebServiceBindingContext(
				user, password, new URL(baseUrl));
		Assert.assertEquals(ctx.getUser(), user);
		Assert.assertEquals(ctx.getPassword(), password);
		Assert.assertEquals(ctx.getBindingType().toString(), CMISBindingContext.BINDING_WEBSERVICES);
		
		Assert.assertEquals(ctx.getAclService(), aclService);
		Assert.assertEquals(ctx.getDiscoveryService(), discoveryUrl);
		Assert.assertEquals(ctx.getMultifilingService(), multiFilingService);
		Assert.assertEquals(ctx.getNavigationService(), navigationService);
		Assert.assertEquals(ctx.getObjectService(), objectService);
		Assert.assertEquals(ctx.getPolicyService(), policyService);
		Assert.assertEquals(ctx.getRelationshipService(), relationshipService);
		Assert.assertEquals(ctx.getRepositoryService(), repositoryService);
		Assert.assertEquals(ctx.getVersioningService(), versioningService);
	}
}
