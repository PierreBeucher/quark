package com.github.pierrebeucher.quark.mantisbt.helper;

import java.math.BigInteger;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pierrebeucher.quark.core.helper.AbstractCleaningHelper;
import com.github.pierrebeucher.quark.core.helper.CleaningHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient.IssueStatus;

import biz.futureware.mantis.rpc.soap.client.AttachmentData;
import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.IssueNoteData;
import biz.futureware.mantis.rpc.soap.client.ObjectRef;

/**
 * 
 * @author Pierre Beucher
 *
 */
public class MantisBTHelper extends AbstractMantisBTHelper implements Helper, CleaningHelper{

	/**
	 * Default number of pages scanned when cleaning. The issues
	 * on the first DEFAULT_CLEAN_PAGE_COUNT will be cleaned.
	 */
	public static final long DEFAULT_ISSUE_RETRIEVING_PAGE_COUNT = 10;
	
	/**
	 * Default number of issues per pages.
	 */
	public static final long DEFAULT_ISSUE_RETRIEVING_PAGE_SIZE = 50;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private long issueRetrievingPageSize = DEFAULT_ISSUE_RETRIEVING_PAGE_SIZE;
	
	private long issueRetrievingPageCount = DEFAULT_ISSUE_RETRIEVING_PAGE_COUNT;
	
	private MantisBTCleaningHelper cleaner;
	
	public MantisBTHelper() {
		super();
		this.cleaner = new MantisBTCleaningHelper();
	}

	public MantisBTHelper(MantisBTContext context) {
		super(context);
		this.cleaner = new MantisBTCleaningHelper();
	}
	
	/**
	 * Update the URL managed by this helper.
	 * It is required to call init() after a call to this function
	 * to ensure the Helper get initialized using the newly configured parameters.
	 * @param url
	 * @return 
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public MantisBTHelper url(URL url){
		getContext().setUrl(url);
		return this;
	}
	
	/**
	 * Update the username managed by this helper. It is required to call init() after a call to this function
	 * to ensure the Helper get initialized using the newly configured parameters.
	 * @param username
	 * @throws ServiceException 
	 * @throws RemoteException
	 * @return  
	 */
	public MantisBTHelper username(String username){
		getContext().getAuthContext().setLogin(username);
		return this;
	}
	
	/**
	 * Update the password managed by this helper. It is required to call init() after a call to this function
	 * to ensure the Helper get initialized using the newly configured parameters.
	 * @param password
	 * @throws ServiceException 
	 * @throws RemoteException
	 * @return 
	 */
	public MantisBTHelper password(String password){
		getContext().getAuthContext().setPassword(password);
		return this;
	}
	
	/**
	 * Update the project managed by this helper.
	 * @param projectName
	 * @throws RemoteException 
	 * @return
	 */
	public MantisBTHelper project(String projectName){
		getContext().setProjectName(projectName);
		return this;
	}
	
//	@Override
//	public MantisBTHelper initialise() throws ServiceException, RemoteException {
//		super.initialise();
//		return this;
//	}

	private Set<IssueData> _getIssuesForProject(IssueFilter filter) throws RemoteException{
		Set<IssueData> result = new HashSet<IssueData>();		
		
		//retrieve issue on each page
		//stop at the first empty page
		for(int page=1; page<=getCleanPageCount(); page++){
			IssueData[] issueArray = client.mc_project_get_issues(projectId, BigInteger.valueOf(page), BigInteger.valueOf(getPageSize()));
			
			if(issueArray.length == 0){
				break;
			}
			
			for(IssueData issue : issueArray){
				if(filter.accept(issue)){
					result.add(issue);
				}
			}
		}
		
		return result;
	}

	/**
	 * Retrieve the most recent issues containing an attachment matching the given pattern.
	 * The first page with a DEFAULT_PAGE_SIZE issues per page is checked by this function.
	 * @return found issues containing the given attachment.
	 * @throws RemoteException 
	 */
	public Set<IssueData> getIssuesWithAttachment(Pattern pattern) throws RemoteException{
		return getIssuesWithAttachment(pattern, IssueFilter.allAcceptingFilterInstance());
	}
	
	public Set<IssueData> getIssuesWithAttachment(Pattern pattern, IssueFilter filter) throws RemoteException{
		IssueData[] issueArray = client.mc_project_get_issues(projectId, BigInteger.valueOf(1), BigInteger.valueOf(DEFAULT_ISSUE_RETRIEVING_PAGE_SIZE));
		Set<IssueData> result = new HashSet<IssueData>();
		for(IssueData issue : issueArray){
			AttachmentData[] attachArray = issue.getAttachments();
			for(AttachmentData attach : attachArray){
				if(filter.accept(issue) && pattern.matcher(attach.getFilename()).find()){
					result.add(issue);
				}
			}
		}
		return result;}
	
//	/**
//	 * Wait for an issue with an attachment matching pattern to be found. Will success
//	 * once one or more issues matching the given pattern is found, or fail after
//	 * timeout. 
//	 * @param pattern
//	 * @param timeout
//	 * @param period
//	 * @return
//	 * @throws InterruptedException  
//	 */
//	public BaseHelperResult<Set<IssueData>> waitForIssueWithAttachment(final Pattern pattern, long timeout, long period) throws InterruptedException {
//		Waiter<BaseHelperResult<Set<IssueData>>> waiter = new SimpleWaiter<BaseHelperResult<Set<IssueData>>>(timeout, period){
//			@Override
//			public BaseHelperResult<Set<IssueData>> performCheck(BaseHelperResult<Set<IssueData>> latestResult) {
//				Set<IssueData> result;
//				try {
//					result = getIssuesWithAttachment(pattern);
//				} catch (RemoteException e) {
//					throw new MantisHelperException(e);
//				}
//				return ResultBuilder.result(!result.isEmpty(), result, "Waiting for issue with attachment '" + pattern.pattern() + "'");
//			}
//		};
//		return waiter.call();
//	}
	
	/**
	 * Generate a dummy issue on the MantisBT server managed by this Helper.
	 * The dummy issue is added to the project managed by this Helper, with a description and summary
	 * like "Dummy Issue {current system time}". The first found category is used. 
	 * @return the ID of the generated issue
	 * @throws RemoteException 
	 */
	public IssueData addDummyIssue() throws RemoteException{
		IssueData dummy = generateDummyIssueData();
		BigInteger created = client.mc_issue_add(dummy);
		return client.mc_issue_get(created);
	}
	
	/**
	 * Generate a dummy issue data containing automatically generated minimal configuration
	 * (description, summary and category).
	 * The generated issue data is not added to the MantisBT server. The generated issue
	 * is associated to the first usable category found on the server.
	 * @return 
	 * @throws RemoteException 
	 */
	public IssueData generateDummyIssueData() throws RemoteException{
		String[] categories = client.mc_project_get_categories(projectId);
		if(categories.length == 0){
			throw new RuntimeException("There is no categories available of the MantisBT server. A category is required to create issues.");
		}
		
		String category = categories[0];
		ObjectRef projRef = new ObjectRef(projectId, context.getProjectName());
		
		IssueData issue = new IssueData();
		issue.setDescription("Dummy issue created by " + this.toString() + " at [" + System.currentTimeMillis() + "]");
		issue.setSummary("Dummy issue [" + System.currentTimeMillis() + "]");
		issue.setProject(projRef);
		issue.setCategory(category);
		return issue;
	}
	
	/**
	 * Retrieve all the issues from the managed context project.
	 * @return
	 * @throws RemoteException 
	 */
	public Set<IssueData> getProjectIssues() throws RemoteException{
		return _getIssuesForProject(IssueFilter.allAcceptingFilterInstance());
	}
	
	/**
	 * Add a note on the given issue.
	 * @param issue
	 * @param noteText
	 * @return
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public IssueNoteData addNote(IssueData issue, String noteText) throws RemoteException, ServiceException{
		IssueNoteData noteData = new IssueNoteData();
		noteData.setText(noteText);
		
		client.mc_issue_note_add(issue.getId(), noteData);
		
		return noteData;
	}
	
	public void deleteIssue(IssueData issue) throws RemoteException{
		logger.debug("Deleting issue: {}", issue.getId());
		client.mc_issue_delete(issue.getId());
	}
	
	public void updateIssue(IssueData issue, IssueStatus status) throws RemoteException{
		ObjectRef newStatus = new ObjectRef(status.getId(), status.name());
		
		logger.debug("Updating issue {} to {}", issue.getId(), status.name());
		
		issue.setStatus(newStatus);
		client.mc_issue_update(issue.getId(), issue);
	}

	@Override
	public void clean() throws Exception {
		cleaner.clean();
	}

	@Override
	public void clean(CleaningMethod cleaningMethod) throws Exception {
		cleaner.clean(cleaningMethod);
	}

	public void setClient(MantisBTClient client) {
		this.client = client;
	}

	public MantisBTClient getClient() {
		return client;
	}

	public BigInteger getProjectId() {
		return projectId;
	}
	
	public long getPageSize() {
		return issueRetrievingPageSize;
	}

	public void setPageSize(long pageSize) {
		this.issueRetrievingPageSize = pageSize;
	}

	public long getCleanPageCount() {
		return issueRetrievingPageCount;
	}

	public void setCleanPageCount(long cleanPageCount) {
		this.issueRetrievingPageCount = cleanPageCount;
	}

	//TODO externalize class
	private class MantisBTCleaningHelper extends AbstractCleaningHelper<MantisBTContext> implements Helper, CleaningHelper{

		public MantisBTCleaningHelper() {
			super(MantisBTHelper.this.context);
		}

		@Override
		public boolean isReady() {
			return MantisBTHelper.this.isReady();
		}

		/**
		 * Safe cleaning will close the most recent issues.
		 * The issues represented on the first getCleanPageCount() pages
		 * with getPageSize() issues par pages are cleaned
		 */
		@Override
		protected void cleanSafe() throws Exception {
			//only close non closed issues
			Set<IssueData> issueSet = _getIssuesForProject(IssueFilter.nonClosedFilterInstance());
			for(IssueData issue : issueSet){
				updateIssue(issue, IssueStatus.CLOSED);
			}
		}

		/**
		 * Hard cleaning will delete the most recent issues.
		 * The issues represented on the first getCleanPageCount() pages
		 * with getPageSize() issues par pages are cleaned
		 */
		@Override
		protected void cleanHard() throws Exception {
			//get all issues regardless of there states
			Set<IssueData> issueSet = _getIssuesForProject(IssueFilter.allAcceptingFilterInstance());
			for(IssueData issue : issueSet){
				deleteIssue(issue);
			}
		}
		
	}


}
