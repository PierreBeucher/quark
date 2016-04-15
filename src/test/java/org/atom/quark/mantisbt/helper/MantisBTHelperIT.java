package org.atom.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.CleaningHelper.CleaningMethod;
import org.atom.quark.core.result.BaseHelperResult;
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
	private String standardProject;
	private String projectCleanHard;

	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project", "mantisbt-project-clean-hard" })
	@BeforeClass
	public void beforeClass(String url, String username, String password, String project, String projectCleanHard)
			throws MalformedURLException, ServiceException, RemoteException {
		this.url = new URL(url);
		this.username = username;
		this.password = password;
		this.standardProject = project;
		this.projectCleanHard = projectCleanHard;
	}
	
	/**
	 * Create a Helper for the given project
	 * @param project
	 * @return
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	private MantisBTHelper buildHelper(String project) throws RemoteException, ServiceException{
		return new MantisBTHelper(buildContext());
	}
	
	private MantisBTContext buildContext(){
		return new MantisBTContext(url, username, password, standardProject);
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
	public void addDummyIssue() throws RemoteException, ServiceException{
		MantisBTHelper helper = buildHelper(standardProject);
		IssueData created = helper.addDummyIssue();
		
		Assert.assertNotNull(created, "Created issue should not be null");
	}

	@Test
	public void getIssueWithAttachment() throws RemoteException, ServiceException {
		
		//create a dummy issue with attachment
		String attachment = "file.txt";
		MantisBTHelper helper = buildHelper(standardProject);
		IssueData issue = helper.addDummyIssue();
		helper.getClient().mc_issue_attachment_add(issue.getId(), attachment, "txt", "Test content".getBytes());
		
		//check the issue is found
		Pattern pattern = Pattern.compile(attachment);
		Set<IssueData> result = helper.getIssuesWithAttachment(pattern);
		
		Assert.assertEquals(result.size(), 1, "Incorrect number of issue found with attachment '" + attachment + "'");
	}
	
	@Test
	public void waitForIssueWithAttachment() throws Exception{
		
		final String attachment = "fileWithWait.txt";
		final MantisBTHelper helper = buildHelper(standardProject);
		Thread issueCreatorThread = new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					
					//create a dummy issue with attachment
					IssueData issue = helper.addDummyIssue();
					helper.getClient().mc_issue_attachment_add(issue.getId(), attachment, "txt", "Test content".getBytes());
				} catch (Exception e) {
					logger.error("Error on issue creator thread: {}", e);
				}
			}
		};
		
		issueCreatorThread.start();
		
		//check the issue is found
		Pattern pattern = Pattern.compile(attachment);
		BaseHelperResult<Set<IssueData>> result = helper.waitForIssueWithAttachment(pattern, 5000, 250);
		
		logger.info("result after wait for issue with attachment: " + result);
		
		Assert.assertTrue(result.isSuccess(), "Result after waiting for issue with attachent" + attachment + "' should be success");
		Assert.assertEquals(result.getActual().size(), 1, "Only 1 issue should be found after waiting for issue with attachment");
		
	}
	
	@Test
	public void cleanSoft() throws Exception{
		MantisBTHelper helper = buildHelper(standardProject);
		
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.clean();
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		IssueData issue = helper.addDummyIssue();
		helper.clean();
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		//soft cleaning should not completely delete issue
		IssueData afterCleanIssue = helper.getClient().mc_issue_get(issue.getId());
		Assert.assertNotNull(afterCleanIssue, "Issue should still be retrievable by ID after soft cleaning");
		Assert.assertEquals(afterCleanIssue.getSummary(), issue.getSummary(), "Issue summary is not the same after soft cleaning");
	}
	
	/**
	 * Test the clean hard functions. This test does not use the other test parameters,
	 * it has its own 
	 * @throws Exception
	 */
	@Test
	public void cleanHard() throws Exception{
		final MantisBTHelper helper = buildHelper(projectCleanHard);
		
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.clean(CleaningMethod.HARD);
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		final IssueData issue = helper.addDummyIssue();
		helper.clean(CleaningMethod.HARD);
		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		//hard cleaning should completely delete issues
		Assert.ThrowingRunnable throwingRunnable = new Assert.ThrowingRunnable() {
			@Override
			public void run() throws RemoteException {
				helper.getClient().mc_issue_get(issue.getId());
			}
		};
		
		Assert.expectThrows(RemoteException.class, throwingRunnable);
	}

}
