package com.github.pierrebeucher.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;
import com.github.pierrebeucher.quark.mantisbt.helper.MantisBTHelper;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient.IssueStatus;

import biz.futureware.mantis.rpc.soap.client.IssueData;

public class MantisBTHelperIT extends BaseMantisBTHelperIT<MantisBTHelper>{
	
	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project", "mantisbt-project-clean-hard" })
	@Factory
	public static Object[] factory(String url, String username, String password, String project, String projectCleanHard)
			throws MalformedURLException{
		return new Object[] {
				new MantisBTHelperIT(new MantisBTHelper(new MantisBTContext(new URL(url), username, password, project)))
		};
	}
	
	public MantisBTHelperIT(MantisBTHelper helper) {
		super(helper);
	}

	@BeforeClass
	@Override
	public void beforeClass() {
		super.beforeClass();
		
		//clean safe before test
		try {
			new MantisBTCleaner(helper).cleanSafe();
		} catch (Exception e) {
			logger.error("Cannot clean before test: " + e.getMessage(), e);
		}
	}

	@Test
	public void addDummyIssue() throws RemoteException, ServiceException{
		IssueData created = helper.addDummyIssue();
		
		Assert.assertNotNull(created, "Created issue should not be null");
	}

	@Test
	public void getIssueWithAttachment() throws RemoteException, ServiceException {
		
		//create a dummy issue with attachment
		String attachment = "file.txt";
		IssueData issue = helper.addDummyIssue();
		helper.getClient().mc_issue_attachment_add(issue.getId(), attachment, "txt", "Test content".getBytes());
		
		//check the issue is found
		Pattern pattern = Pattern.compile(attachment);
		Set<IssueData> result = helper.getIssuesWithAttachment(pattern);
		
		Assert.assertEquals(result.size(), 1, "Incorrect number of issue found with attachment '" + attachment + "'");
		
		//cleanup after test
		helper.updateIssue(issue, IssueStatus.CLOSED);
	}
	
	@Test
	public void getIssueWithAttachmentFilter() throws RemoteException, ServiceException {
		
		//create a dummy issue with attachment
		String attachment = "filterAttachFile";
		
		IssueData issueOpen = helper.addDummyIssue();
		IssueData issueClosed = helper.addDummyIssue();
		
		helper.getClient().mc_issue_attachment_add(issueOpen.getId(), attachment + "open.txt" , "txt", "Test content open".getBytes());
		helper.getClient().mc_issue_attachment_add(issueClosed.getId(), attachment + "closed.txt", "txt", "Test content closed".getBytes());
		
		helper.updateIssue(issueOpen, IssueStatus.ACKNOWLEDGED);
		helper.updateIssue(issueClosed, IssueStatus.CLOSED);
		
		//check the issue is found
		Pattern pattern = Pattern.compile(attachment + ".*");
		Set<IssueData> result = helper.getIssuesWithAttachment(pattern, IssueFilter.nonClosedFilterInstance());
		
		Assert.assertEquals(result.size(), 1, "Incorrect number of issue found with attachment '" + attachment + "'");
		
		IssueData foundIssue = (IssueData) result.toArray()[0];
		Assert.assertEquals(foundIssue.getStatus().getId(), IssueStatus.ACKNOWLEDGED.getId(),
				"Issue found is not in expected status ");
		
		//cleanup after test
		helper.updateIssue(issueOpen, IssueStatus.CLOSED);
	}
	
	@Test
	public void updateIssue() throws RemoteException, ServiceException{
		IssueData dummy = helper.addDummyIssue();
		
		helper.updateIssue(dummy, IssueStatus.RESOLVED);
		IssueData afterUpdate = helper.getClient().mc_issue_get(dummy.getId());
		Assert.assertEquals(afterUpdate.getStatus().getId(), IssueStatus.RESOLVED.getId());
	}
	
	@Test
	public void deleteIssue() throws RemoteException, ServiceException{
		IssueData dummy = helper.addDummyIssue();
		
		helper.deleteIssue(dummy);
		try {
			IssueData found = helper.getClient().mc_issue_get(dummy.getId());
			Assert.fail("Expected an exception to be thrown, but found issue: " + found.getId());
		} catch (RemoteException e) {
			Assert.assertTrue(e.getMessage().contains("Issue does not exist"),
					"Error does not contain 'does not exists' after issue delete, but:" + e);
		}
	}
	
//	@Test
//	public void cleanSoft() throws Exception{
//		
//		//clean a first time, ensure no issues are present, create dummy and re-clean
//		helper.clean();
//		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
//		
//		IssueData issue = helper.addDummyIssue();
//		helper.clean();
//		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
//		
//		//soft cleaning should not completely delete issue
//		IssueData afterCleanIssue = helper.getClient().mc_issue_get(issue.getId());
//		Assert.assertNotNull(afterCleanIssue, "Issue should still be retrievable by ID after soft cleaning");
//		Assert.assertEquals(afterCleanIssue.getSummary(), issue.getSummary(), "Issue summary is not the same after soft cleaning");
//	}
	
	//TODO create Cleaner test
//	/**
//	 * Test the clean hard functions. This test does not use the other test parameters,
//	 * it has its own 
//	 * @throws Exception
//	 */
//	@Test
//	public void cleanHard() throws Exception{
//		final MantisBTHelper helper = buildHelper(projectCleanHard);
//		
//		//clean a first time, ensure no issues are present, create dummy and re-clean
//		helper.clean(CleaningMethod.HARD);
//		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
//		
//		final IssueData issue = helper.addDummyIssue();
//		helper.clean(CleaningMethod.HARD);
//		Assert.assertEquals(helper.getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
//		
//		//hard cleaning should completely delete issues
//		Assert.ThrowingRunnable throwingRunnable = new Assert.ThrowingRunnable() {
//			@Override
//			public void run() throws RemoteException {
//				helper.getClient().mc_issue_get(issue.getId());
//			}
//		};
//		
//		Assert.expectThrows(RemoteException.class, throwingRunnable);
//	}

}
