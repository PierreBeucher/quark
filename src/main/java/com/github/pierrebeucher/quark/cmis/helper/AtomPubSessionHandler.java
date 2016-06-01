package com.github.pierrebeucher.quark.cmis.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;

public class AtomPubSessionHandler extends SessionHandler {

	public AtomPubSessionHandler(CMISContext context) {
		super(context, AtomPubBindingContext.class);
	}
	
	@Override
	protected Map<String, String> generateBindingParameters(CMISBindingContext bindingContext) {
		Map<String, String> parameters = new HashMap<String, String>();
		AtomPubBindingContext atomBindingContext = (AtomPubBindingContext) bindingContext;

		parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		parameters.put(SessionParameter.ATOMPUB_URL, atomBindingContext.getAtomPubUrl().toString());
		
		return parameters;
	}

	@Override
	public boolean isReady() {
		AtomPubBindingContext bindingContext = (AtomPubBindingContext) context.getBindingContext();
		return super.isReady()
				&& StringUtils.isNotEmpty(bindingContext.getUser())
				&& StringUtils.isNotEmpty(bindingContext.getPassword())
				&& bindingContext.getAtomPubUrl() != null;
	}

}
