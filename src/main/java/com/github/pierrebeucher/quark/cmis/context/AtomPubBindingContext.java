package com.github.pierrebeucher.quark.cmis.context;

import java.net.URL;

public class AtomPubBindingContext extends CMISBindingContext{
	
	private URL atomPubUrl;

	public AtomPubBindingContext(String user, String password, URL atomPubUrl) {
		super(CMISBindingContext.BINDING_ATOMPUB, user, password);
		this.atomPubUrl = atomPubUrl;
	}
	
	public AtomPubBindingContext(AtomPubBindingContext ctx) {
		this(ctx.user, ctx.password, ctx.atomPubUrl);
	}

	public AtomPubBindingContext() {
		super();
	}

	public URL getAtomPubUrl() {
		return atomPubUrl;
	}

	public void setAtomPubUrl(URL atomPubUrl) {
		this.atomPubUrl = atomPubUrl;
	}

	@Override
	public String toString() {
		return getUser() + "@" + getAtomPubUrl();
	}

}
