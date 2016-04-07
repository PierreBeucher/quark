package org.atom.quark.mantisbt.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import biz.futureware.mantis.rpc.soap.client.ProjectData;
import biz.futureware.mantis.rpc.soap.client.UserData;

public class MantisBTClientIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private URL url;
	private String username;
	private String password;
	private String project;
	
	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project" })
	@BeforeTest
	public void beforeTest(String url, String username, String password, String project) throws MalformedURLException {
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		
		logger.info("Testing with url={}, username={}, password={}", url, username, password);
	}
	
	@Test
	public void constructorRemoteTest() throws ServiceException, RemoteException {
		MantisBTClient client = new MantisBTClient(url, username, password);
		UserData data = client.mc_login();
		Assert.assertEquals(data.getAccount_data().getName(), username);
	}
	
	@Test
	public void setterRemoteTest() throws ServiceException, RemoteException {
		MantisBTClient client = new MantisBTClient();
		client.setUsername(username);
		client.setPassword(password);
		client.setUrl(url);
		UserData data = client.mc_login();
		Assert.assertEquals(data.getAccount_data().getName(), username);
	}
	
	private MantisBTClient buildClient() throws ServiceException{
		return new MantisBTClient(url, username, password);
	}
	
	public void mc_projects_get_user_accessible() throws RemoteException, ServiceException{
		ProjectData[] projects = buildClient().mc_projects_get_user_accessible();
		Assert.assertEquals(projects.length, 1);
		Assert.assertEquals(projects[0].getName(), project);
	}
	
	//TODO add more test...
	
}
