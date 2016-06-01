package com.github.pierrebeucher.quark.mantisbt.helper;

import java.util.Set;

import com.github.pierrebeucher.quark.core.helper.CleaningHelper;
import com.github.pierrebeucher.quark.core.helper.WrapperCleaningHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient.IssueStatus;

import biz.futureware.mantis.rpc.soap.client.IssueData;

public class MantisBTCleaner extends WrapperCleaningHelper<MantisBTContext, MantisBTHelper>
		implements Initialisable, CleaningHelper {
	
	/**
	 * Create a <code>MantisBTCleaner</code> using the given context.
	 * A new <code>MantisBTHelper</code> is instanciated using the given Context 
	 * @param context
	 */
	public MantisBTCleaner(MantisBTContext context) {
		this(new MantisBTHelper(context));
	}
	
	/**
	 * Create a <code>MantisBTCleaner</code> using the given context.
	 * The new Helper share its context with the given Helper.
	 * @param helper
	 */
	public MantisBTCleaner(MantisBTHelper helper) {
		super(helper.getContext(), helper);
	}
	
	@Override
	public boolean isReady() {
		return helper.isReady();
	}
	
	@Override
	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	@Override
	public boolean isInitialised() {
		return helper.isInitialised();
	}

	/**
	 * Safe cleaning will close the most recent issues.
	 * The issues represented on the first getCleanPageCount() pages
	 * with getPageSize() issues par pages are cleaned
	 */
	public void cleanSafe() throws Exception {
		//only close non closed issues
		Set<IssueData> issueSet = helper.getIssuesForProject(IssueFilter.nonClosedFilterInstance());
		for(IssueData issue : issueSet){
			helper.updateIssue(issue, IssueStatus.CLOSED);
		}
	}

	/**
	 * Hard cleaning will delete the most recent issues.
	 * The issues represented on the first getCleanPageCount() pages
	 * with getPageSize() issues par pages are cleaned
	 */
	public void cleanHard() throws Exception {
		Set<IssueData> issueSet = helper.getIssuesForProject(IssueFilter.allAcceptingFilterInstance());
		for(IssueData issue : issueSet){
			helper.deleteIssue(issue);
		}
	}

}
