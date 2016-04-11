package org.atom.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.atom.quark.mantisbt.context.MantisBTContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import biz.futureware.mantis.rpc.soap.client.IssueData;

public class MantisBTHelperIT {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	private URL url;
	private String username;
	private String password;
	private String project;

	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project" })
	@BeforeClass
	public void beforeClass(String url, String username, String password, String project) throws MalformedURLException {
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		this.project = project;
	}
	
	private MantisBTHelper buildHelper() throws RemoteException, ServiceException{
		return new MantisBTHelper(buildContext());
	}
	
	private MantisBTContext buildContext(){
		return new MantisBTContext(url, username, password, project);
	}
	
	@Test
	public void MantisBTHelper() {
		MantisBTHelper helper = new MantisBTHelper();
		Assert.assertNotNull(helper.getContext());
	}

	@Test
	public void MantisBTHelperMantisBTContext() throws RemoteException, ServiceException {
		MantisBTContext ctx = buildContext();
		MantisBTHelper helper = new MantisBTHelper(ctx);
		Assert.assertEquals(helper.getContext(), ctx);
	}
	
	@Test
	public void isReady() throws Exception {
		MantisBTHelper helper = new MantisBTHelper();
		Assert.assertFalse(helper.isReady());
		
		helper.setContext(buildContext());
		Assert.assertTrue(helper.isReady());
	}

	@Test
	public void getIssueWithAttachment() throws RemoteException, ServiceException {
		MantisBTHelper helper = buildHelper();
		String attachment = "file.txt";
		Pattern pattern = Pattern.compile(attachment);
		Set<IssueData> result = helper.getIssuesWithAttachment(pattern);
		
		Assert.assertEquals(result.size(), 1, "Incorrect number of issue found with attachment '" + attachment + "'");
	}

}
