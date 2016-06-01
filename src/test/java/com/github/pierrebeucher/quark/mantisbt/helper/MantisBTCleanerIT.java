package com.github.pierrebeucher.quark.mantisbt.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;

import biz.futureware.mantis.rpc.soap.client.IssueData;

public class MantisBTCleanerIT extends BaseMantisBTHelperIT<MantisBTCleaner>{
	
	@Parameters({ "mantisbt-url", "mantisbt-username", "mantisbt-password", "mantisbt-project-clean-hard" })
	@Factory
	public static Object[] factory(String url, String username, String password, String project)
			throws MalformedURLException{
		return new Object[] {
			new MantisBTCleanerIT(new MantisBTCleaner(new MantisBTContext(new URL(url), username, password, project)))
		};
	}
	
	public MantisBTCleanerIT(MantisBTCleaner helper) {
		super(helper);
	}
	
	@Test
	public void cleanSoft() throws Exception{
				
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.cleanSafe();
		Assert.assertEquals(helper.getWrappedHelper().getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		IssueData issue = helper.getWrappedHelper().addDummyIssue();
		helper.cleanSafe();
		Assert.assertEquals(helper.getWrappedHelper().getProjectIssues().size(), 0, "Issues can still be retrieved after cleaning.");
		
		//soft cleaning should not completely delete issue
		IssueData afterCleanIssue = helper.getWrappedHelper().getClient().mc_issue_get(issue.getId());
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
		//clean a first time, ensure no issues are present, create dummy and re-clean
		helper.cleanHard();
		Assert.assertEquals(helper.getWrappedHelper().getProjectIssues().size(), 0,
				"Issues can still be retrieved after cleaning.");
		
		final IssueData issue = helper.getWrappedHelper().addDummyIssue();
		helper.cleanHard();
		Assert.assertEquals(helper.getWrappedHelper().getProjectIssues().size(), 0,
				"Issues can still be retrieved after cleaning.");
		
		//hard cleaning should completely delete issues
		Assert.ThrowingRunnable throwingRunnable = new Assert.ThrowingRunnable() {
			@Override
			public void run() throws RemoteException {
				helper.getWrappedHelper().getClient().mc_issue_get(issue.getId());
			}
		};
		
		Assert.expectThrows(RemoteException.class, throwingRunnable);
	}

}
