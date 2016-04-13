package org.atom.quark.cmis.util;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.atom.quark.cmis.context.AtomPubBindingContext;
import org.atom.quark.cmis.context.CMISBindingContext;
import org.atom.quark.cmis.context.CMISContext;
import org.atom.quark.cmis.context.WebServiceBindingContext;
import org.atom.quark.cmis.helper.chemistry.AtomPubSessionHandler;
import org.atom.quark.cmis.helper.chemistry.SessionHandler;
import org.atom.quark.cmis.helper.chemistry.WebServiceSessionHandler;

public class ChemistryCMISUtils {
	
	private ChemistryCMISUtils(){}
	
	public static List<Repository> getRepositories(CMISBindingContext ctx){
		SessionHandler handler = getSessionHandler(new CMISContext(ctx, null));
		SessionFactory factory = SessionFactoryImpl.newInstance();
		return factory.getRepositories(handler.generateSessionParameter());
	}
	
	/**
	 * Generate a new BindingHandler for the given CMISContext.
	 * @param ctx
	 * @return
	 */
	public static SessionHandler getSessionHandler(CMISContext ctx){
		CMISBindingContext bindingContext = ctx.getBindingContext();
		if(bindingContext instanceof AtomPubBindingContext){
			 return new AtomPubSessionHandler(ctx);
		} else if(bindingContext instanceof WebServiceBindingContext){
			 return new WebServiceSessionHandler(ctx);
		} else {
			throw new RuntimeException("This helper does not manage this binding context: " + bindingContext.getClass());
		}
	}

}
