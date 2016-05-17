package com.github.pierrebeucher.quark.cmis.context;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

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
	
	public CMISContext(CMISContext ctx) {
		this(ctx.bindingContext, ctx.repositoryId);
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

	@Override
	public String toString() {
		return "binding=" + bindingContext + ", repository=" + repositoryId;
	}
	
}
