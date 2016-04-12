package org.atom.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.HelperBuilderTestBase;
import org.atom.quark.mantisbt.context.MantisBTContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MantisBTHelperBuilderIT extends HelperBuilderTestBase{
	
	private URL url;
	private String username;
	private String password;
	private String project;

	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project" })
	@BeforeClass
	public void beforeClass(String url, String username, String password, String project)
			throws MalformedURLException, ServiceException, RemoteException {
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		this.project = project;
	}
	
	private MantisBTContext buildContext(){
		return new MantisBTContext(url, username, password, project);
	}
	
	@Test
	public void MantisBTHelperBuilder() {
		MantisBTContext ctx = buildContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		Assert.assertEquals(builder.getBaseContext(), ctx, "getBaseContext() should return the context used on construction");
	}
	
	@Test
	public void testNoReuseBaseContext() throws Exception{
		MantisBTContext ctx = buildContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		testNoReuseBaseContext(builder);
	}

	@Test
	public void build() throws RemoteException, ServiceException {
		MantisBTContext ctx = buildContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		
		MantisBTHelper helper = builder.build();
		Assert.assertEquals(helper.getContext().getProjectName(), ctx.getProjectName());
		Assert.assertEquals(helper.getContext().getUrl(), ctx.getUrl());
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), ctx.getAuthContext().getLogin());
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), ctx.getAuthContext().getPassword());
	}

	@Test
	public void buildURL() throws MalformedURLException, RemoteException, ServiceException {
		MantisBTContext ctx = buildContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		
		URL url = new URL("http://another-url:80");
		MantisBTHelper helper = builder.build(url);
		Assert.assertEquals(helper.getContext().getProjectName(), ctx.getProjectName());
		Assert.assertEquals(helper.getContext().getUrl(), url);
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), ctx.getAuthContext().getLogin());
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), ctx.getAuthContext().getPassword());
	}

	@Test
	public void buildStringString() throws RemoteException, ServiceException {
		MantisBTContext ctx = buildContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		
		String newUsername = "anotheruser";
		String newPassword = "anotherpasswordxyz";
		MantisBTHelper helper = builder.build(newUsername, newPassword);
		Assert.assertEquals(helper.getContext().getProjectName(), ctx.getProjectName());
		Assert.assertEquals(helper.getContext().getUrl(), ctx.getUrl());
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), newUsername);
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), newPassword);
	}
}
