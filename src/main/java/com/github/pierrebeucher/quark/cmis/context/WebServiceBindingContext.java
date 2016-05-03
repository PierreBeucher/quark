package com.github.pierrebeucher.quark.cmis.context;

import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceBindingContext extends CMISBindingContext {

	/*
	 * Web Services
	 */
	private URL aclService;
	private URL discoveryService;
	private URL multifilingService;
	private URL navigationService;
	private URL objectService;
	private URL policyService;
	private URL relationshipService;
	private URL repositoryService;
	private URL versioningService;
	
	/**
	 * Empty constructor. Use setters to set-up this context.
	 */
	public WebServiceBindingContext() {
		super();
	}
	
	/**
	 * Constructor using the given baseUrl to construct the final URl for each webservices, final URL
	 * being of the form: <i>{baseUrl}/{serviceName}?wsdl</i><br>
	 * Example: https://localhost/cmis/services/ACLService?wsdl (with baseUrl=https://localhost/cmis/services)
	 * The following services URL are generated, based on CMIS specifications:
	 * <ul>
	 * <li>{baseUrl}/ACLService?wsdl</li>
	 * <li>{baseUrl}/DiscoveryService?wsdl</li>
	 * <li>{baseUrl}/MultiFilingService?wsdl</li>
	 * <li>{baseUrl}/NavigationService?wsdl</li>
	 * <li>{baseUrl}/ObjectService?wsdl</li>
	 * <li>{baseUrl}/PolicyService?wsdl</li>
	 * <li>{baseUrl}/RelationshipService?wsdl</li>
	 * <li>{baseUrl}/RepositoryService?wsdl</li>
	 * <li>{baseUrl}/VersioningService?wsdl</li>
	 * </ul>
	 * @param user
	 * @param password
	 * @param repositoryId
	 * @param baseUrl
	 * @throws MalformedURLException 
	 */
	public WebServiceBindingContext(String user, String password, URL baseUrl) throws MalformedURLException{
		this(user, password,
				buildServiceURL(baseUrl, CMISService.ACL),
				buildServiceURL(baseUrl, CMISService.DISCOVERY),
				buildServiceURL(baseUrl, CMISService.MULTIFILING),
				buildServiceURL(baseUrl, CMISService.NAVIGATION),
				buildServiceURL(baseUrl, CMISService.OBJECT),
				buildServiceURL(baseUrl, CMISService.POLICY),
				buildServiceURL(baseUrl, CMISService.RELATIONSHIP),
				buildServiceURL(baseUrl, CMISService.REPOSITORY),
				buildServiceURL(baseUrl, CMISService.VERSIONING));
	}
	
	public WebServiceBindingContext(String user, String password,
			URL aclService, URL discoveryService, URL multifilingService, URL navigationService, URL objectService,
			URL policyService, URL relationShipService, URL repositoryService, URL versioningService) {
		super(CMISBindingContext.BINDING_WEBSERVICES, user, password);
		this.aclService = aclService;
		this.discoveryService = discoveryService;
		this.multifilingService = multifilingService;
		this.navigationService = navigationService;
		this.objectService = objectService;
		this.policyService = policyService;
		this.relationshipService = relationShipService;
		this.repositoryService = repositoryService;
		this.versioningService = versioningService;
	}
	
	public WebServiceBindingContext(WebServiceBindingContext ctx){
		this(ctx.user, ctx.password,
				ctx.aclService, ctx.discoveryService, ctx.multifilingService, ctx.navigationService, ctx.objectService,
				ctx.policyService, ctx.relationshipService, ctx.repositoryService, ctx.versioningService);
	}
	
	public static URL buildServiceURL(URL baseUrl, CMISService service) throws MalformedURLException{
		return new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), 
				baseUrl.getFile() + "/" + service.value() + "?wsdl", null);
	}

	@Override
	public String toString() {
		return "WebServiceBindingContext [aclService=" + aclService + ", discoveryService=" + discoveryService
				+ ", multifilingService=" + multifilingService + ", navigationService=" + navigationService
				+ ", objectService=" + objectService + ", policyService=" + policyService + ", relationShipService="
				+ relationshipService + ", repositoryService=" + repositoryService + ", versioningService="
				+ versioningService + "]";
	}

	public URL getAclService() {
		return aclService;
	}

	public void setAclService(URL aclService) {
		this.aclService = aclService;
	}

	public URL getDiscoveryService() {
		return discoveryService;
	}

	public void setDiscoveryService(URL discoveryService) {
		this.discoveryService = discoveryService;
	}

	public URL getMultifilingService() {
		return multifilingService;
	}

	public void setMultifilingService(URL multifilingService) {
		this.multifilingService = multifilingService;
	}

	public URL getNavigationService() {
		return navigationService;
	}

	public void setNavigationService(URL navigationService) {
		this.navigationService = navigationService;
	}

	public URL getObjectService() {
		return objectService;
	}

	public void setObjectService(URL objectService) {
		this.objectService = objectService;
	}

	public URL getPolicyService() {
		return policyService;
	}

	public void setPolicyService(URL policyService) {
		this.policyService = policyService;
	}

	public URL getRelationshipService() {
		return relationshipService;
	}

	public void setRelationshipService(URL relationShipService) {
		this.relationshipService = relationShipService;
	}

	public URL getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(URL repositoryService) {
		this.repositoryService = repositoryService;
	}

	public URL getVersioningService() {
		return versioningService;
	}

	public void setVersioningService(URL versioningService) {
		this.versioningService = versioningService;
	}
	
	
}
