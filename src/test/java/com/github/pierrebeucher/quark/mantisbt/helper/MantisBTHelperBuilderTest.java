package com.github.pierrebeucher.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.helper.AbstractHelperBuilderTest;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;

public class MantisBTHelperBuilderTest extends AbstractHelperBuilderTest{

	private String url = "http://host:80";
	private String login = "login";
	private String password = "pass";
	private String project = "project";
	
	@Test
	public void MantisBTHelperBuilder() {
		MantisBTContext ctx = new MantisBTContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		Assert.assertEquals(builder.getBaseContext(), ctx);
	}

	@Test
	public void MantisBTHelperBuilderMantisBTContext() {
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder();
		Assert.assertNotNull(builder.getBaseContext());
	}
	
	@Test
	public void testContextCloned(){
		MantisBTContext ctx = new MantisBTContext();
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		MantisBTHelper helper = builder.buildBaseHelper();
		Assert.assertNotEquals(helper.getContext().hashCode(), ctx.hashCode());
	}
	
	@Test
	public void build() throws MalformedURLException{
		URL theurl = new URL(url);
		MantisBTContext ctx = new MantisBTContext(theurl, login, password, project);
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		MantisBTHelper helper = builder.build();
		
		Assert.assertEquals(helper.getContext().getUrl(), theurl);
		Assert.assertEquals(helper.getContext().getLogin(), login);
		Assert.assertEquals(helper.getContext().getProjectName(), project);
		Assert.assertEquals(helper.getContext().getPassword(), password);
	}
	
	@Test
	public void buildUrl() throws MalformedURLException{
		MantisBTContext ctx = new MantisBTContext(new URL(url), login, password, project);
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		
		URL anotherUrl = new URL("https://anotherhost:80");
		MantisBTHelper helper = builder.build(anotherUrl);
		
		Assert.assertEquals(helper.getContext().getUrl(), anotherUrl); //url is different than base context
		Assert.assertEquals(helper.getContext().getLogin(), login);
		Assert.assertEquals(helper.getContext().getProjectName(), project);
		Assert.assertEquals(helper.getContext().getPassword(), password);
	}
	
	@Test
	public void buildStringString() throws MalformedURLException{
		URL theurl = new URL(url);
		MantisBTContext ctx = new MantisBTContext(theurl, login, password, project);
		MantisBTHelperBuilder builder = new MantisBTHelperBuilder(ctx);
		
		String anotherLogin = "anotherlogin";
		String anotherPassword = "anotherPassword";
		MantisBTHelper helper = builder.build(anotherLogin, anotherPassword);
		
		// login and password is different
		Assert.assertEquals(helper.getContext().getUrl(), theurl);
		Assert.assertEquals(helper.getContext().getLogin(), anotherLogin);
		Assert.assertEquals(helper.getContext().getProjectName(), project);
		Assert.assertEquals(helper.getContext().getPassword(), anotherPassword);
	}
}
