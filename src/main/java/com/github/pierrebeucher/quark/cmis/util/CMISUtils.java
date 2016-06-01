package com.github.pierrebeucher.quark.cmis.util;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.context.WebServiceBindingContext;
import com.github.pierrebeucher.quark.cmis.helper.AtomPubSessionHandler;
import com.github.pierrebeucher.quark.cmis.helper.SessionHandler;
import com.github.pierrebeucher.quark.cmis.helper.WebServiceSessionHandler;

public class CMISUtils {
	
	private CMISUtils(){}
	
	public static boolean isFolder(CmisObject o){
		return isType(o, BaseTypeId.CMIS_FOLDER);
	}
	
	public static boolean isDocument(CmisObject o){
		return isType(o, BaseTypeId.CMIS_DOCUMENT);
	}
	
	public static boolean isType(CmisObject o, BaseTypeId type){
		return o.getBaseTypeId() == type;
	}
	
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
