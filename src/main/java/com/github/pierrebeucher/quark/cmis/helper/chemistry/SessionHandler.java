package com.github.pierrebeucher.quark.cmis.helper.chemistry;

import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.context.EmptyBindingContext;

/**
 * CMISContextHandler is used to managed some functions depending on
 * the CMIS binding types, such as session parameter generation or
 * readiness check.
 * @author Pierre Beucher
 *
 */
public abstract class SessionHandler {

	protected CMISContext context;

	protected SessionHandler(CMISContext context, Class<?> bindingContextClass) {
		super();
		this.context = context;
		
		//ensure the bindingClass is correct
		if(bindingContextClass.getClass().isInstance(context.getBindingContext())){
			throw new ClassCastException("Cannot cast " + context + " to " + AtomPubBindingContext.class + "."
					+ " This session handler expects a " + AtomPubBindingContext.class + " BindingContext."
					+ " Make sure your CMISContext has been initialed with the proper CMISBindingContext. ");
		}
	}
	
	/**
	 * Generate the session parameter for the context managed by this handler. The session
	 * parameters generated will depend on the concrete CMISBindingContext used by the context.
	 * @return generated parameters
	 */
	public Map<String, String> generateSessionParameter(){
		Map<String, String> params = generateBindingParameters(context.getBindingContext());
		
		params.put(SessionParameter.USER, context.getBindingContext().getUser());
		params.put(SessionParameter.PASSWORD, context.getBindingContext().getPassword());
		params.put(SessionParameter.REPOSITORY_ID, context.getRepositoryId());
		
		return params;
	}
	
	/**
	 * Generate the binding session parameters for this binding context. The session
	 * parameters generated will depend on the concrete CMISBindingContext.
	 * @param bindingContext
	 * @return
	 */
	protected abstract Map<String, String> generateBindingParameters(CMISBindingContext bindingContext);
	
	/**
	 * Ensure this SessionHandler is ready to manage sessions. Ready if
	 * the CMISContext is not null, the CMISBindingContext is not null and not the EmptyBindingContext,
	 * and the required parameters to create a session are valid.
	 * @return true if ready, false otherwise
	 */
	public boolean isReady(){
		return context != null && context.getBindingContext() != null
				&& !EmptyBindingContext.instance().equals(context.getBindingContext());
	}
	
}
