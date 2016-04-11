package org.atom.quark.mantisbt.helper;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.AbstractCleaningHelper;
import org.atom.quark.core.helper.CleaningHelper;
import org.atom.quark.core.helper.Helper;
import org.atom.quark.mantisbt.context.MantisBTContext;
import org.atom.quark.mantisbt.utils.MantisBTClient;
import org.atom.quark.mantisbt.utils.MantisBTClient.IssueStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.futureware.mantis.rpc.soap.client.AttachmentData;
import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.ObjectRef;

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
	
//	private MantisBTClient client;
//	
//	private BigInteger projectId;
//	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private long issueRetrievingPageSize = DEFAULT_ISSUE_RETRIEVING_PAGE_SIZE;
	
	private long issueRetrievingPageCount = DEFAULT_ISSUE_RETRIEVING_PAGE_COUNT;
	
	private MantisBTCleaningHelper cleaner;
	
	public MantisBTHelper() {
		super();
		this.cleaner = new MantisBTCleaningHelper();
	}

	public MantisBTHelper(MantisBTContext context) throws ServiceException, RemoteException {
		super(context);
		this.cleaner = new MantisBTCleaningHelper();
		init();
	}
//	
//	/**
//	 * Init this Helper using its current context.
//	 * @throws ServiceException 
//	 * @throws RemoteException 
//	 */
//	public void init() throws ServiceException, RemoteException{
//		this.client = buildClient();
//		this.projectId = initProjectId();
//	}
//	
//	/**
//	 * Create a new MantisBTClient if this Helper is ready.
//	 * @return a new client
//	 * @throws ServiceException 
//	 */
//	private MantisBTClient buildClient() throws ServiceException{
//		if(isReady()){
//			return new MantisBTClient(context.getUrl(),
//					context.getAuthContext().getLogin(),
//					context.getAuthContext().getPassword());
//		} else {
//			throw new RuntimeException("Cannot build a client if Helper is not ready.");
//		}
//	}
//	
//	private BigInteger initProjectId() throws RemoteException{
//		return this.client.mc_project_get_id_from_name(context.getProjectName());
//	}
//
//	/**
//	 * MantisBTHelper is ready if its URL, username and password are set properly.
//	 */
//	@Override
//	public boolean isReady() {
//		return context.getAuthContext().getLogin() != null
//				&& context.getAuthContext().getPassword() != null
//				&& context.getUrl() != null;
//	}
	
	private Set<IssueData> _getIssuesForProject() throws RemoteException{
		Set<IssueData> result = new HashSet<IssueData>();		
		
		//retrieve issue on each page
		//stop at the first empty page
		for(int page=1; page<=getCleanPageCount(); page++){
			IssueData[] issueArray = client.mc_project_get_issues(projectId, BigInteger.valueOf(page), BigInteger.valueOf(getPageSize()));
			
			if(issueArray.length == 0){
				break;
			}
			
			for(IssueData issue : issueArray){
				result.add(issue);
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
		IssueData[] issueArray = client.mc_project_get_issues(projectId, BigInteger.valueOf(1), BigInteger.valueOf(DEFAULT_ISSUE_RETRIEVING_PAGE_SIZE));
		Set<IssueData> result = new HashSet<IssueData>();
		for(IssueData issue : issueArray){
			AttachmentData[] attachArray = issue.getAttachments();
			for(AttachmentData attach : attachArray){
				if(pattern.matcher(attach.getFilename()).find()){
					result.add(issue);
				}
			}
		}
		return result;
	}
	
	/**
	 * Retrieve all the issues from the managed context project.
	 * @return
	 * @throws RemoteException 
	 */
	public Set<IssueData> getProjectIssues() throws RemoteException{
		return _getIssuesForProject();
	}

	@Override
	public void clean() throws Exception {
		cleaner.clean();
	}

	@Override
	public void clean(CleaningMethod cleaningMethod) throws Exception {
		cleaner.clean(cleaningMethod);
	}

//	@Override
//	public void setContext(MantisBTContext context) throws Exception {
//		super.setContext(context);
//		init();
//	}

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

	public class MantisBTCleaningHelper extends AbstractCleaningHelper<MantisBTContext> implements Helper, CleaningHelper{

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
			Set<IssueData> issueSet = _getIssuesForProject();
			for(IssueData issue : issueSet){
				closeIssue(issue);
			}
		}

		/**
		 * Hard cleaning will delete the most recent issues.
		 * The issues represented on the first getCleanPageCount() pages
		 * with getPageSize() issues par pages are cleaned
		 */
		@Override
		protected void cleanHard() throws Exception {
			Set<IssueData> issueSet = _getIssuesForProject();
			for(IssueData issue : issueSet){
				deleteIssue(issue);
			}
		}
		
		private void closeIssue(IssueData issue) throws RemoteException{
			ObjectRef closedStatus = new ObjectRef(IssueStatus.CLOSED.getId(), "Closed");
			logger.debug("Closing issue: {}", issue.getId());
			issue.setStatus(closedStatus);
			client.mc_issue_update(issue.getId(), issue);
		}
		
		private void deleteIssue(IssueData issue) throws RemoteException{
			logger.debug("Deleting issue: {}", issue.getId());
			client.mc_issue_delete(issue.getId());
		}
		
	}


}
