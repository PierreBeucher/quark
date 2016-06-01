package com.github.pierrebeucher.quark.cmis.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.context.WebServiceBindingContext;

public class WebServiceSessionHandler extends SessionHandler{

	public WebServiceSessionHandler(CMISContext context) {
		super(context, WebServiceBindingContext.class);
	}

	/**
	 * This handler is ready if user, password, repositoryId and all the servicesURL are set.
	 */
	@Override
	public boolean isReady() {
		WebServiceBindingContext bindingContext = (WebServiceBindingContext)context.getBindingContext();
		return super.isReady()
				&& StringUtils.isNotEmpty(bindingContext.getUser())
				&& StringUtils.isNotEmpty(bindingContext.getPassword())
				&& StringUtils.isNotEmpty(context.getRepositoryId())
				&& StringUtils.isNotEmpty(bindingContext.getBindingType())
				&& bindingContext.getAclService() != null
				&& bindingContext.getDiscoveryService() != null
				&& bindingContext.getMultifilingService() != null
				&& bindingContext.getNavigationService()!= null
				&& bindingContext.getObjectService() != null 
				&& bindingContext.getPolicyService() != null
				&& bindingContext.getRelationshipService() != null
				&& bindingContext.getRepositoryService() != null
				&& bindingContext.getVersioningService() != null;
	}

	@Override
	protected Map<String, String> generateBindingParameters(CMISBindingContext bindingContext) {
		Map<String, String> params = new HashMap<String, String>();
		WebServiceBindingContext wsBindingContext = (WebServiceBindingContext)context.getBindingContext();
		
		params.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
		
		params.put(SessionParameter.WEBSERVICES_ACL_SERVICE, wsBindingContext.getAclService().toString());
		params.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, wsBindingContext.getDiscoveryService().toString());
		params.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, wsBindingContext.getMultifilingService().toString());
		params.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, wsBindingContext.getNavigationService().toString());
		params.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, wsBindingContext.getObjectService().toString());
		params.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, wsBindingContext.getPolicyService().toString());
		params.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, wsBindingContext.getRelationshipService().toString());
		params.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, wsBindingContext.getRepositoryService().toString());
		params.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, wsBindingContext.getVersioningService().toString());
		
		return params;
	}

}
