package com.github.pierrebeucher.quark.mantisbt.helper;

import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient.IssueStatus;

import biz.futureware.mantis.rpc.soap.client.IssueData;

/**
 * MantisBT Issue filter used by the MantisBTHelper when querying issues.
 * @author pierreb
 *
 */
public abstract class IssueFilter {
	
	public abstract boolean accept(IssueData issue);
	
	/**
	 * Return an all-accepting instance of IssueFilter. This
	 * instance's {@link #accept(IssueData)} method will always return true.
	 * @return an all-acceptin IssueFilter instance
	 */
	public static IssueFilter allAcceptingFilterInstance(){
		return new IssueFilter(){
			@Override
			public boolean accept(IssueData issue) {
				return true;			
			}
		};
	}
	
	/**
	 * Return an IssueFilter only accepting non-closed issues, such as
	 * {@link #accept(IssueData)} act like:
	 * <pre>
	 * issue.getId() != IssueStatus.CLOSED.getId()
	 * </pre>
	 * @return an IssueFilter instance rejecting closed issues
	 */
	public static IssueFilter nonClosedFilterInstance(){
		return new IssueFilter(){
			@Override
			public boolean accept(IssueData issue) {
				return issue.getId() != IssueStatus.CLOSED.getId();			
			}
		};
	}

}
