package org.atom.quark.mantisbt.helper;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.AbstractHelper;
import org.atom.quark.core.helper.Helper;
import org.atom.quark.mantisbt.context.MantisBTContext;
import org.atom.quark.mantisbt.utils.MantisBTClient;

import biz.futureware.mantis.rpc.soap.client.AttachmentData;
import biz.futureware.mantis.rpc.soap.client.IssueData;

public class MantisBTHelper extends AbstractHelper<MantisBTContext> implements Helper{

	public static long DEFAULT_PAGE_SIZE = 50;
	
	private MantisBTClient client;
	
	private BigInteger projectId;
	
	public MantisBTHelper() {
		super(new MantisBTContext());
	}

	public MantisBTHelper(MantisBTContext context) throws ServiceException, RemoteException {
		super(context);
		init();
	}
	
	/**
	 * Init this Helper using its current context.
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public void init() throws ServiceException, RemoteException{
		this.client = buildClient();
		this.projectId = initProjectId();
	}
	
	/**
	 * Create a new MantisBTClient if this Helper is ready.
	 * @return a new client
	 * @throws ServiceException 
	 */
	private MantisBTClient buildClient() throws ServiceException{
		if(isReady()){
			return new MantisBTClient(context.getUrl(),
					context.getAuthContext().getLogin(),
					context.getAuthContext().getPassword());
		} else {
			throw new RuntimeException("Cannot build a client if Helper is not ready.");
		}
	}
	
	private BigInteger initProjectId() throws RemoteException{
		return this.client.mc_project_get_id_from_name(context.getProjectName());
	}

	/**
	 * MantisBTHelper is ready if its URL, username and password are set properly.
	 */
	@Override
	public boolean isReady() {
		return context.getAuthContext().getLogin() != null
				&& context.getAuthContext().getPassword() != null
				&& context.getUrl() != null;
	}

	/**
	 * Retrieve the most recent issues containing an attachment matching the given pattern.
	 * The first page with a DEFAULT_PAGE_SIZE issues per page is checked by this function.
	 * @return found issues containing the given attachment.
	 * @throws RemoteException 
	 */
	public Set<IssueData> getIssuesWithAttachment(Pattern pattern) throws RemoteException{
		IssueData[] issueArray = client.mc_project_get_issues(projectId, BigInteger.valueOf(1), BigInteger.valueOf(DEFAULT_PAGE_SIZE));
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
	
	@Override
	public void setContext(MantisBTContext context) throws Exception {
		super.setContext(context);
		init();
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


}
