package org.atom.quark.cmis.context;

import org.atom.quark.core.context.base.HelperContext;

public class CMISContext implements HelperContext {

	private CMISBindingContext bindingContext;
	
	private String repositoryId;

	public CMISContext(CMISBindingContext bindingContext, String repositoryId) {
		super();
		this.bindingContext = bindingContext;
		this.repositoryId = repositoryId;
	}

	public CMISContext() {
		super();
		this.bindingContext = EmptyBindingContext.instance();
	}

	public CMISBindingContext getBindingContext() {
		return bindingContext;
	}

	public void setBindingContext(CMISBindingContext bindingContext) {
		this.bindingContext = bindingContext;
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	
}
