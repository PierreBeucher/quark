package com.github.pierrebeucher.quark.mantisbt.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.authentication.PasswordAuthContext;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;

public class MantisBTContextTest {
	
	private URL url;
	
	private String username;
	
	private String password;
	
	private String project;
	
	private PasswordAuthContext authContext;
	
	@BeforeClass
	public void beforeClass() throws MalformedURLException{
		this.url = new URL("http://localhost:80/path");
		this.username = "username";
		this.password = "password";
		this.project = "project";
		this.authContext = new PasswordAuthContext(username, password);
	}

	@Test
	public void MantisBTContext() {
		MantisBTContext ctx = new MantisBTContext();
		Assert.assertNotNull(ctx.getAuthContext());
		Assert.assertNull(ctx.getUrl());
		Assert.assertNull(ctx.getProjectName());
	}

	@Test
	public void MantisBTContextURLStringString() {
		MantisBTContext ctx = new MantisBTContext();
		Assert.assertNotNull(ctx.getAuthContext());
		Assert.assertNull(ctx.getUrl());
		Assert.assertNull(ctx.getProjectName());
	}

	@Test
	public void MantisBTContextURLStringStringString() {
		MantisBTContext ctx = new MantisBTContext(url, username, password, project);
		Assert.assertEquals(ctx.getUrl(), url);
		Assert.assertEquals(ctx.getAuthContext().getLogin(), username);
		Assert.assertEquals(ctx.getAuthContext().getPassword(), password);
		Assert.assertEquals(ctx.getProjectName(), project);
	}
	
	@Test
	public void MantisBTContextCopy() {
		MantisBTContext base = new MantisBTContext(url, username, password, project);
		MantisBTContext ctx = new MantisBTContext(base);
		Assert.assertEquals(ctx.getUrl(), url);
		Assert.assertEquals(ctx.getAuthContext().getLogin(), username);
		Assert.assertEquals(ctx.getAuthContext().getPassword(), password);
		Assert.assertEquals(ctx.getProjectName(), project);
	}

	@Test
	public void setAuthContext() {
		MantisBTContext ctx = new MantisBTContext();
		ctx.setAuthContext(authContext);
		Assert.assertEquals(ctx.getAuthContext(), authContext);
	}

	@Test
	public void setProjectName() {
		MantisBTContext ctx = new MantisBTContext();
		ctx.setProjectName(project);
		Assert.assertEquals(ctx.getProjectName(), project);
	}

	@Test
	public void setUrl() {
		MantisBTContext ctx = new MantisBTContext();
		ctx.setUrl(url);
		Assert.assertEquals(ctx.getUrl(), url);
	}
}
