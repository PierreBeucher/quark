package org.atom.quark.cmis.context;

import org.atom.quark.core.context.base.HelperContext;

/**
 * Base context for a CMIS Binding. 
 * @author Pierre Beucher
 *
 */
public abstract class CMISBindingContext implements HelperContext{
	
	public static final String BINDING_ATOMPUB = "atompub";
	public static final String BINDING_WEBSERVICES = "webservices";
	public static final String BINDING_BROWSER = "browser";
	
	protected String user;
	
	protected String password;
	
	protected String bindingType;

	protected CMISBindingContext() {
		super();
	}
	
	protected CMISBindingContext(String bindingType, String user, String password) {
		super();
		this.user = user;
		this.password = password;
		this.bindingType = bindingType;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBindingType() {
		return bindingType;
	}

	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}

}
