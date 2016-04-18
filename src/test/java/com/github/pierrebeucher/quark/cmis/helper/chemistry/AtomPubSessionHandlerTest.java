package com.github.pierrebeucher.quark.cmis.helper.chemistry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.cmis.context.AtomPubBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.helper.chemistry.AtomPubSessionHandler;

public class AtomPubSessionHandlerTest {
	
	private String user = "user";
	private String password = "password";
	private String atomPubUrl = "http://localhost/cmis/atom";
	private String repository = "repo";

	private AtomPubSessionHandler buildHandler() throws MalformedURLException{
		AtomPubBindingContext ctx = new AtomPubBindingContext(user, password, new URL(atomPubUrl));
		return new AtomPubSessionHandler(new CMISContext(ctx, repository));
	}

	@Test
	public void generateSessionParameter() throws MalformedURLException {
		AtomPubSessionHandler handler = buildHandler();
		Map<String, String> result = handler.generateSessionParameter();
		
		Assert.assertEquals(result.get(SessionParameter.USER), user);
		Assert.assertEquals(result.get(SessionParameter.PASSWORD), password);
		Assert.assertEquals(result.get(SessionParameter.REPOSITORY_ID), repository);
		Assert.assertEquals(result.get(SessionParameter.BINDING_TYPE), CMISBindingContext.BINDING_ATOMPUB);
		
		Assert.assertEquals(result.get(SessionParameter.ATOMPUB_URL), atomPubUrl);	
	}

	@Test
	public void isReadyPositive() throws MalformedURLException {
		AtomPubSessionHandler handler = buildHandler();
		Assert.assertEquals(handler.isReady(), true);
	}
	
	@Test
	public void isReadyNegative() throws MalformedURLException  {
		CMISBindingContext bindingContext = new AtomPubBindingContext(null, null, null);
		AtomPubSessionHandler handler = new AtomPubSessionHandler(new CMISContext(bindingContext, null));
		Assert.assertEquals(handler.isReady(), false);
	}
}
