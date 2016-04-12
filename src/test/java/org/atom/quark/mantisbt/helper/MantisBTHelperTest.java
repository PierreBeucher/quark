package org.atom.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * MantisBTHelper unit test
 * @author Pierre Beucher
 *
 */
public class MantisBTHelperTest {
	
	private String password;
	private String username;
	private String project;
	private URL url;
	
	@BeforeClass
	public void beforeClass() throws MalformedURLException{
		this.password = "password";
		this.username = "user";
		this.project = "project";
		this.url = new URL("http://localhost:80");
	}
	
	@Test
	public void password() {
		MantisBTHelper helper = new MantisBTHelper();
		helper.password(password);
		
		Assert.assertEquals(helper.getContext().getAuthContext().getPassword(), password);
	}

	@Test
	public void project() {
		MantisBTHelper helper = new MantisBTHelper();
		helper.project(project);
		
		Assert.assertEquals(helper.getContext().getProjectName(), project);
	}

	@Test
	public void url() {
		MantisBTHelper helper = new MantisBTHelper();
		helper.url(url);
		
		Assert.assertEquals(helper.getContext().getUrl(), url);
	}

	@Test
	public void username() {
		MantisBTHelper helper = new MantisBTHelper();
		helper.username(username);
		
		Assert.assertEquals(helper.getContext().getAuthContext().getLogin(), username);
	}
}
