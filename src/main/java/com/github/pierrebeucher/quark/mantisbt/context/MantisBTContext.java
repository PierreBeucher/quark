package com.github.pierrebeucher.quark.mantisbt.context;

import java.net.URL;

import com.github.pierrebeucher.quark.core.context.authentication.PasswordAuthContext;
import com.github.pierrebeucher.quark.core.context.base.HelperContext;

public class MantisBTContext implements HelperContext{

	private URL url;
	
	private PasswordAuthContext authContext;
	
	private String projectName;
	
	public MantisBTContext() {
		super();
		this.url = null;
		this.authContext = new PasswordAuthContext();
		this.projectName = null;
	}
	
	public MantisBTContext(URL url, String username, String password, String projectName){
		this.authContext = new PasswordAuthContext(username, password);
		this.url = url;
		this.projectName = projectName;
	}
	
	public MantisBTContext(MantisBTContext ctx){
		this(ctx.url, ctx.authContext.getLogin(), ctx.authContext.getPassword(), ctx.projectName);
	}

	public PasswordAuthContext getAuthContext() {
		return authContext;
	}

	public void setAuthContext(PasswordAuthContext authContext) {
		this.authContext = authContext;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getLogin() {
		return authContext.getLogin();
	}

	public void setLogin(String login) {
		authContext.setLogin(login);
	}

	public String getPassword() {
		return authContext.getPassword();
	}

	public void setPassword(String password) {
		authContext.setPassword(password);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(authContext.getLogin() + "@" + url.toString());
		if(projectName != null){
			buf.append("/").append(projectName);
		} else {
			buf.append("(no project)");
		}
		return buf.toString();
	}

}
