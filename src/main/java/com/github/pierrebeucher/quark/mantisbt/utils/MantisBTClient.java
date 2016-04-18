package com.github.pierrebeucher.quark.mantisbt.utils;

import java.math.BigInteger;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import biz.futureware.mantis.rpc.soap.client.AccountData;
import biz.futureware.mantis.rpc.soap.client.CustomFieldDefinitionData;
import biz.futureware.mantis.rpc.soap.client.FilterData;
import biz.futureware.mantis.rpc.soap.client.HistoryData;
//import biz.futureware.mantis.rpc.soap.client.HistoryData;
import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.IssueHeaderData;
import biz.futureware.mantis.rpc.soap.client.IssueNoteData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectLocator;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import biz.futureware.mantis.rpc.soap.client.ObjectRef;
import biz.futureware.mantis.rpc.soap.client.ProfileDataSearchResult;
import biz.futureware.mantis.rpc.soap.client.ProjectAttachmentData;
import biz.futureware.mantis.rpc.soap.client.ProjectData;
import biz.futureware.mantis.rpc.soap.client.ProjectVersionData;
import biz.futureware.mantis.rpc.soap.client.RelationshipData;
import biz.futureware.mantis.rpc.soap.client.TagData;
import biz.futureware.mantis.rpc.soap.client.TagDataSearchResult;
import biz.futureware.mantis.rpc.soap.client.UserData;

/**
 * Utilitary MantisBT client wrapping the Mantis Axis Soap Client.
 * TODO This client uses MantisBTAxisClient dependency, which may change for any used version. We should externalize this client to another project, each version corresponding to a MantisBT version.
 * @author Pierre Beucher
 *
 */
public class MantisBTClient {
	
	public static final BigInteger NULL_PROJECT_ID = BigInteger.valueOf(0);

	/**
	 * Check if the given project ID as BigInteger represent
	 * the null project (i.e. id is not 0)
	 * @param id project id
	 * @return true if null project, false otherwise
	 */
	public static boolean isNullProject(BigInteger id){
		return NULL_PROJECT_ID.equals(id);
	}
	
	/**
	 * Issue status per ID
	 * @author Pierre Beucher
	 *
	 */
	public enum IssueStatus {
		NEW(10),
		FEEDBACK(20),
		ACKNOWLEDGED(30),
		CONFIRMED(40),
		ASSIGNED(50),
		RESOLVED(80),
		CLOSED(90);	
		
		private Integer statusId;
		
		IssueStatus(Integer statusId){
			this.statusId = statusId;
		}
		
		public BigInteger getId(){
			return BigInteger.valueOf(statusId);
		}
	}
	
	/*
	 * Remote representing the MantisBT server
	 */
	private MantisConnectPortType remoteMantis;
	
	/*
	 * Whether the Mantis remote has been initialized or not
	 */
	private boolean isReady;

	/*
	 * URL ot the MantisBT instance
	 */
	private URL url;
	
	/*
	 * Login and password 
	 */
	private String username;
	private String password;

	/**
	 * Empty constructor. Require setters and init() method to be called to configure the client.
	 */
	public MantisBTClient() {
	}

	/**
	 * Constructor which will initialize the client with the given parameters
	 * @param url
	 * @param username
	 * @param password
	 * @throws ServiceException
	 */
	public MantisBTClient(URL url, String username, String password) throws ServiceException {
		super();
		setUrl(url);
		setUsername(username);
		setPassword(password);
		init();
	}

	/**
	 * Initialize the Remote used to communicate with the configured MantisBT server using the configured URL
	 * @throws ServiceException 
	 */
	public void init() throws ServiceException{
		MantisConnectLocator locator = new MantisConnectLocator();
		this.remoteMantis = locator.getMantisConnectPort(url);
	}
	
	/**
	 * Ensure this client is correctly initialized.
	 */
	public void ensureReady(){
		if(!isReady()){
			throw new RuntimeException("Unable to use a unitialized Mantis client. Make sure the client is correctly configured"
					+ " and the initRemote() function has been called.");
		}
	}

	
	public String mc_version() throws RemoteException {
		return remoteMantis.mc_version();
	}

	
	public UserData mc_login() throws RemoteException {
		return remoteMantis.mc_login(username, password);
	}

	
	public ObjectRef[] mc_enum_status() throws RemoteException {
		return remoteMantis.mc_enum_status(username, password);
	}

	
	public ObjectRef[] mc_enum_priorities() throws RemoteException {
		return remoteMantis.mc_enum_priorities(username, password);
	}

	
	public ObjectRef[] mc_enum_severities() throws RemoteException {
		return remoteMantis.mc_enum_severities(username, password);
	}

	
	public ObjectRef[] mc_enum_reproducibilities() throws RemoteException {
		return remoteMantis.mc_enum_reproducibilities(username, password);
	}

	
	public ObjectRef[] mc_enum_projections() throws RemoteException {
		return remoteMantis.mc_enum_projections(username, password);
	}

	
	public ObjectRef[] mc_enum_etas() throws RemoteException {
		return remoteMantis.mc_enum_etas(username, password);
	}

	public ObjectRef[] mc_enum_resolutions() throws RemoteException {
		return remoteMantis.mc_enum_resolutions(username, password);
	}

	public ObjectRef[] mc_enum_access_levels() throws RemoteException {
		return remoteMantis.mc_enum_access_levels(username, password);
	}
	
	public ObjectRef[] mc_enum_project_status() throws RemoteException {
		return remoteMantis.mc_enum_project_status(username, password);
	}
	
	public ObjectRef[] mc_enum_project_view_states() throws RemoteException {
		return remoteMantis.mc_enum_project_view_states(username, password);
	}
	
	public ObjectRef[] mc_enum_view_states() throws RemoteException {
		return remoteMantis.mc_enum_view_states(username, password);
	}
	
	public ObjectRef[] mc_enum_custom_field_types() throws RemoteException {
		return remoteMantis.mc_enum_custom_field_types(username, password);
	}

	public String mc_enum_get(String enumeration) throws RemoteException {
		return remoteMantis.mc_enum_get(username, password, enumeration);
	}
	
	public boolean mc_issue_exists(BigInteger issue_id) throws RemoteException {
		return remoteMantis.mc_issue_exists(username, password, issue_id);
	}
	
	public IssueData mc_issue_get(BigInteger issue_id) throws RemoteException {
		return remoteMantis.mc_issue_get(username, password, issue_id);
	}
	
	public HistoryData[] mc_issue_get_history(BigInteger issue_id)
			throws RemoteException {
		return remoteMantis.mc_issue_get_history(username, password, issue_id);
	}
	
	public BigInteger mc_issue_get_biggest_id(BigInteger project_id)
			throws RemoteException {
		return remoteMantis.mc_issue_get_biggest_id(username, password, project_id);
	}
	
	public BigInteger mc_issue_get_id_from_summary(String summary)
			throws RemoteException {
		return remoteMantis.mc_issue_get_id_from_summary(username, password, summary);
	}
	
	public BigInteger mc_issue_add(IssueData issue) throws RemoteException {
		return remoteMantis.mc_issue_add(username, password, issue);
	}

	public boolean mc_issue_update(BigInteger issueId, IssueData issue)
			throws RemoteException {
		return remoteMantis.mc_issue_update(username, password, issueId, issue);
	}
	
	public boolean mc_issue_set_tags(BigInteger issue_id, TagData[] tags)
			throws RemoteException {
		return remoteMantis.mc_issue_set_tags(username, password, issue_id, tags);
	}

	public boolean mc_issue_delete(BigInteger issue_id) throws RemoteException {
		return remoteMantis.mc_issue_delete(username, password, issue_id);
	}
	
	public BigInteger mc_issue_note_add(BigInteger issue_id, IssueNoteData note)
			throws RemoteException {
		return remoteMantis.mc_issue_note_add(username, password, issue_id, note);
	}
	
	public boolean mc_issue_note_delete(BigInteger issue_note_id)
			throws RemoteException {
		return remoteMantis.mc_issue_note_delete(username, password, issue_note_id);
	}
	
	public boolean mc_issue_note_update(IssueNoteData note) throws RemoteException {
		return remoteMantis.mc_issue_note_update(username, password, note);
	}

	public BigInteger mc_issue_relationship_add(BigInteger issue_id,
			RelationshipData relationship) throws RemoteException {
		return remoteMantis.mc_issue_relationship_add(username, password, issue_id, relationship);
	}

	public boolean mc_issue_relationship_delete(BigInteger issue_id,
			BigInteger relationship_id) throws RemoteException {
		return remoteMantis.mc_issue_relationship_delete(username, password, issue_id, relationship_id);
	}
	
	public BigInteger mc_issue_attachment_add(BigInteger issue_id, String name,
			String file_type, byte[] content) throws RemoteException {
		return remoteMantis.mc_issue_attachment_add(username, password, issue_id, name, file_type, content);
	}
	
	public boolean mc_issue_attachment_delete(BigInteger issue_attachment_id)
			throws RemoteException {
		return remoteMantis.mc_issue_attachment_delete(username, password, issue_attachment_id);
	}

	public byte[] mc_issue_attachment_get(BigInteger issue_attachment_id)
			throws RemoteException {
		return remoteMantis.mc_issue_attachment_get(username, password, issue_attachment_id);
	}
	
	public BigInteger mc_project_add(ProjectData project) throws RemoteException {
		return remoteMantis.mc_project_add(username, password, project);
	}
	
	public boolean mc_project_delete(BigInteger project_id) throws RemoteException {
		return remoteMantis.mc_project_delete(username, password, project_id);
	}
	
	public boolean mc_project_update(BigInteger project_id, ProjectData project)
			throws RemoteException {
		return remoteMantis.mc_project_update(username, password, project_id, project);
	}
	
	public BigInteger mc_project_get_id_from_name(String project_name)
			throws RemoteException {
		return remoteMantis.mc_project_get_id_from_name(username, password, project_name);
	}
	
	public IssueData[] mc_project_get_issues_for_user(BigInteger project_id,
			String filter_type, AccountData target_user, BigInteger page_number, BigInteger per_page)
			throws RemoteException {
		return remoteMantis.mc_project_get_issues_for_user(username, password, project_id,
				filter_type, target_user, page_number, per_page);
	}
	
	public IssueData[] mc_project_get_issues(BigInteger project_id,
			BigInteger page_number, BigInteger per_page) throws RemoteException {
		return remoteMantis.mc_project_get_issues(username, password, project_id, page_number, per_page);
	}
	
	public IssueHeaderData[] mc_project_get_issue_headers(BigInteger project_id,
			BigInteger page_number, BigInteger per_page) throws RemoteException {
		return remoteMantis.mc_project_get_issue_headers(username, password, project_id, page_number, per_page);
	}
	
	public AccountData[] mc_project_get_users(BigInteger project_id,
			BigInteger access) throws RemoteException {
		
		return remoteMantis.mc_project_get_users(username, password, project_id, access);
	}
	
	public ProjectData[] mc_projects_get_user_accessible() throws RemoteException {
		
		return remoteMantis.mc_projects_get_user_accessible(username, password);
	}
	
	public String[] mc_project_get_categories(BigInteger project_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_get_categories(username, password, project_id);
	}
	
	public BigInteger mc_project_add_category(BigInteger project_id,
			String p_category_name) throws RemoteException {
		
		return remoteMantis.mc_project_add_category(username, password, project_id, p_category_name);
	}
	
	public BigInteger mc_project_delete_category(BigInteger project_id,
			String p_category_name) throws RemoteException {
		
		return remoteMantis.mc_project_delete_category(username, password, project_id, p_category_name);
	}
	
	public BigInteger mc_project_rename_category_by_name(BigInteger project_id,
			String p_category_name, String p_category_name_new, BigInteger p_assigned_to) throws RemoteException {
		
		return remoteMantis.mc_project_rename_category_by_name(username, password, project_id, p_category_name, 
				p_category_name_new, p_assigned_to);
	}
	
	public ProjectVersionData[] mc_project_get_versions(BigInteger project_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_get_versions(username, password, project_id);
	}
	
	public BigInteger mc_project_version_add(ProjectVersionData version)
			throws RemoteException {
		
		return remoteMantis.mc_project_version_add(username, password, version);
	}
	
	public boolean mc_project_version_update(BigInteger version_id,
			ProjectVersionData version) throws RemoteException {
		
		return remoteMantis.mc_project_version_update(username, password, version_id, version);
	}
	
	public boolean mc_project_version_delete(BigInteger version_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_version_delete(username, password, version_id);
	}
	
	public ProjectVersionData[] mc_project_get_released_versions(
			BigInteger project_id) throws RemoteException {
		
		return remoteMantis.mc_project_get_released_versions(username, password, project_id);
	}

	public ProjectVersionData[] mc_project_get_unreleased_versions(
			BigInteger project_id) throws RemoteException {
		
		return remoteMantis.mc_project_get_unreleased_versions(username, password, project_id);
	}
	
	public ProjectAttachmentData[] mc_project_get_attachments(BigInteger project_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_get_attachments(username, password, project_id);
	}
	
	public CustomFieldDefinitionData[] mc_project_get_custom_fields(
			BigInteger project_id) throws RemoteException {
		
		return remoteMantis.mc_project_get_custom_fields(username, password, project_id);
	}

	public byte[] mc_project_attachment_get(BigInteger project_attachment_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_attachment_get(username, password, project_attachment_id);
	}
	
	public BigInteger mc_project_attachment_add(BigInteger project_id, String name,
			String title, String description, String file_type, byte[] content) throws RemoteException {
		
		return remoteMantis.mc_project_attachment_add(username, password, project_id, name, title, description, file_type, content);
	}
	
	public boolean mc_project_attachment_delete(BigInteger project_attachment_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_attachment_delete(username, password, project_attachment_id);
	}
	
	public String[] mc_project_get_all_subprojects(BigInteger project_id)
			throws RemoteException {
		
		return remoteMantis.mc_project_get_all_subprojects(username, password, project_id);
	}

	public FilterData[] mc_filter_get(BigInteger project_id) throws RemoteException {
		
		return remoteMantis.mc_filter_get(username, password, project_id);
	}

	public IssueData[] mc_filter_get_issues(BigInteger project_id,
			BigInteger filter_id, BigInteger page_number, BigInteger per_page) throws RemoteException {
		
		return remoteMantis.mc_filter_get_issues(username, password, project_id, filter_id, page_number, per_page);
	}
	
	public IssueHeaderData[] mc_filter_get_issue_headers(BigInteger project_id,
			BigInteger filter_id, BigInteger page_number, BigInteger per_page) throws RemoteException {
		
		return remoteMantis.mc_filter_get_issue_headers(username, password, project_id, filter_id, page_number, per_page);
	}
	
	public String mc_config_get_string(String config_var) throws RemoteException {
		
		return remoteMantis.mc_config_get_string(username, password, config_var);
	}

	public boolean mc_issue_checkin(BigInteger issue_id, String comment,
			boolean fixed) throws RemoteException {
		
		return remoteMantis.mc_issue_checkin(username, password, issue_id, comment, fixed);
	}
	
	public String mc_user_pref_get_pref(BigInteger project_id, String pref_name)
			throws RemoteException {
		
		return remoteMantis.mc_user_pref_get_pref(username, password, project_id, pref_name);
	}
	
	public ProfileDataSearchResult mc_user_profiles_get_all(BigInteger page_number,
			BigInteger per_page) throws RemoteException {
		
		return remoteMantis.mc_user_profiles_get_all(username, password, page_number, per_page);
	}

	public TagDataSearchResult mc_tag_get_all(BigInteger page_number,
			BigInteger per_page) throws RemoteException {
		
		return remoteMantis.mc_tag_get_all(username, password, page_number, per_page);
	}
	
	public BigInteger mc_tag_add(TagData tag) throws RemoteException {
		
		return remoteMantis.mc_tag_add(username, password, tag);
	}
	
	public boolean mc_tag_delete(BigInteger tag_id) throws RemoteException {
		
		return remoteMantis.mc_tag_delete(username, password, tag_id);
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String login) {
		this.username = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isReady() {
		return isReady;
	}

}
