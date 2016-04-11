package org.atom.quark.mantisbt.helper;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.CleaningHelper.CleaningMethod;
import org.atom.quark.mantisbt.context.MantisBTContext;
import org.atom.quark.mantisbt.utils.MantisBTClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.ObjectRef;

public class MantisBTHelperIT {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	private URL url;
	private String username;
	private String password;
	private String project;
	
	/*
	 * ObjectRef of the test project
	 */
	private ObjectRef projectRef;
	
	/*
	 * Test category for dummy issues
	 */
	private String category;

	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project" })
	@BeforeClass
	public void beforeClass(String url, String username, String password, String project)
			throws MalformedURLException, ServiceException, RemoteException {
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		this.project = project;
		
		//retrieve test ObjectRefs from configured instance
		MantisBTClient client = buildClient();
		BigInteger projectId = client.mc_project_get_id_from_name(project);
		projectRef = new ObjectRef(projectId, project);
		category = client.mc_project_get_categories(projectId)[0]; //any category will do
	}
	
	private MantisBTClient buildClient() throws ServiceException{
		return new MantisBTClient(url, username, password);
	}
	
	private MantisBTHelper buildHelper() throws RemoteException, ServiceException{
		return new MantisBTHelper(buildContext());
	}
	
	private MantisBTContext buildContext(){
		return new MantisBTContext(url, username, password, project);
	}
	
	private IssueData buildDummyIssue(){
		IssueData issue = new IssueData();
		issue.setDescription("Dummy issue create for test purpose at [" + System.currentTimeMillis() + "]");
		issue.setSummary("Dummy issue [" + System.currentTimeMillis() + "]");
		issue.setProject(projectRef);
		issue.setCategory(category);
		return issue;
	}
	
	private BigInteger addDummyIssue(String issueSummary) throws RemoteException, ServiceException{
		MantisBTClient client = buildClient();
		IssueData issue = buildDummyIssue();
		issue.setSummary(issueSummary);
		return client.mc_issue_add(issue);
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
		
		//create a dummy issue with attachment
		String attachment = "file.txt";
		MantisBTClient client = buildClient();
		IssueData issue = buildDummyIssue();
		BigInteger issueId = client.mc_issue_add(issue);
		client.mc_issue_attachment_add(issueId, attachment, "txt", "Test content".getBytes());
		
		//check the issue is found
		MantisBTHelper helper = buildHelper();
		Pattern pattern = Pattern.compile(attachment);
		Set<IssueData> result = helper.getIssuesWithAttachment(pattern);
		
		Assert.assertEquals(result.size(), 1, "Incorrect number of issue found with attachment '" + attachment + "'");
	}
	
	@Test
	public void cleanSoft() throws Exception{
		MantisBTHelper helper = buildHelper();
		
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.clean();
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		String issueSummary = "TestCleanSoftIssue-" + System.currentTimeMillis();
		BigInteger issueId = addDummyIssue(issueSummary);
		helper.clean();
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		//soft cleaning should not completely delete issue
		IssueData afterCleanIssue = helper.getClient().mc_issue_get(issueId);
		Assert.assertNotNull(afterCleanIssue, "Issue should still be retrievable by ID after soft cleaning");
		Assert.assertEquals(afterCleanIssue.getSummary(), issueSummary, "Issue summary is not the same after soft cleaning");
	}
	
	@Test
	public void cleanHard() throws Exception{
		final MantisBTHelper helper = buildHelper();
		
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.clean(CleaningMethod.HARD);
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		String issueSummary = "TestCleanHardIssue-" + System.currentTimeMillis();
		final BigInteger issueId = addDummyIssue(issueSummary);
		helper.clean(CleaningMethod.HARD);
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		//hard cleaning should completely delete issues
		Assert.ThrowingRunnable throwingRunnable = new Assert.ThrowingRunnable() {
			@Override
			public void run() throws RemoteException {
				helper.getClient().mc_issue_get(issueId);
			}
		};
		
		Assert.expectThrows(RemoteException.class, throwingRunnable);
	}

}
