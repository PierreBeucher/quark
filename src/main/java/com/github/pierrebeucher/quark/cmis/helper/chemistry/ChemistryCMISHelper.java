package com.github.pierrebeucher.quark.cmis.helper.chemistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.helper.CMISHelper;
import com.github.pierrebeucher.quark.cmis.util.ChemistryCMISUtils;
import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.ResultBuilder;
import com.github.pierrebeucher.quark.core.waiter.SimpleWaiter;

public class ChemistryCMISHelper extends AbstractHelper<CMISContext> implements Helper, CMISHelper{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Session session;
	
	/*
	 * Handle operations which depends on the concrete type of this Helper's binding context
	 */
	private SessionHandler bindingContextHandler;
	
	/**
	 * Constructor setting an empty CMISContext. Using setters to configure
	 * the Helper context is required. 
	 */
	public ChemistryCMISHelper() {
		this(new CMISContext());
	}

	public ChemistryCMISHelper(CMISContext context) {
		super(context);
	}
	
	/**
	 * Init the BindingContextHandler depending on the concrete BindingContext managed by this Helper.
	 */
	private SessionHandler createBindingContextHandler(){
		return ChemistryCMISUtils.getSessionHandler(context);
	}
	
	private Session createSession(){
		Map<String, String> params = generateSessionParameters();
		logger.debug("Creation session using {}", params);
		
	    //if repositoryId specified, create session directly
		//if not, use sole available session if possible
		SessionFactory factory = SessionFactoryImpl.newInstance();
		if(params.containsKey(SessionParameter.REPOSITORY_ID)){
			return factory.createSession(params);
		} else {
			List<Repository> repositoryList = factory.getRepositories(params);
			if(repositoryList.size() > 1){
				throw new IllegalStateException("Cannot create session with no repositoryId specified as more"
						+ " than one repository is available. Provide a repositoryId in your context.");
			}
			return repositoryList.get(0).createSession();
		} 
	}
	
	/**
	 * Initialize this Helper, creating a CMIS session
	 * @return
	 */
	public ChemistryCMISHelper init(){
		this.bindingContextHandler = createBindingContextHandler();
		this.session = createSession();
		return this;
	}
	
	public ChemistryCMISHelper bindingContext(CMISBindingContext bindingContext){
		context.setBindingContext(bindingContext);
		return this;
	}
	
//	public Folder createFolder(String parent, String name){
//		return FileUtils.createFolder(parent, name, null, session);
//	}
//	
//	public Folder createFolderIfNotExists(String parent, String name){
//		Folder folder = null;
//		try{
//			if(parent.endsWith("/")){
//				 FileUtils.getFolder(parent + name, session);
//			} else {
//				folder = FileUtils.getFolder(parent + "/" + name, session);
//			}
//		} catch (CmisObjectNotFoundException e){
//			//this is expected
//			return createFolder(parent, name);
//		}
//		return folder;
//	}
//	
//	public Document createDocumentFromFile(String parent, File file) throws FileNotFoundException{
//		return FileUtils.createDocumentFromFile(parent, file, null, VersioningState.MINOR, session);
//	}
//	
//	public Document createDocumentFromFileIfNotExists(String parent, File file) throws FileNotFoundException{
//		return FileUtils.createDocumentFromFile(parent, file, null, VersioningState.MINOR, session);
//	}
	
	public ItemIterable<CmisObject> listDirectory(String path){
		CmisObject o = session.getObjectByPath(path);
		if(!Folder.class.isAssignableFrom(o.getClass())){
			throw new RuntimeException("CmisObject with path " + path + " is not a Folder but: " + o.getClass());
		}
		
		return ((Folder) o).getChildren();
	}
	
	/**
	 * List Documents at the given path with a name matching the given pattern.
	 * @param path
	 * @param p
	 * @return
	 */
	public List<Document> listDocumentsMatching(String path, Pattern pattern){
		List<Document> result = new ArrayList<Document>();
		for(CmisObject o : listDirectory(path)){
			if(ChemistryCMISUtils.isDocument(o)
					&& (pattern.matcher(o.getName()).find())){
				result.add((Document) o);
			}
		}
		return result;
	}
	
	/**
	 * Check whether or not the given folder contains Documents which name matches Pattern.
	 * Result is success if one or more Document matching pattern is found. 
	 * @param directory
	 * @param pattern
	 * @return result as List of found Documents
	 */
	public BaseHelperResult<List<Document>> containsDocument(String directory, Pattern pattern){
		List<Document> result = listDocumentsMatching(directory, pattern);
		return ResultBuilder.result(!result.isEmpty(), result);
	}
	
	/**
	 * Check whether or not the specified folder contains a document with the given name.
	 * Success if a Document with the exact given name exists. Failure if nt object found
	 * or object found but is not a document.
	 * @param parent
	 * @param documentName
	 * @return result as object found
	 */
	public BaseHelperResult<Document> containsDocument(String path){
		try {
			CmisObject o = session.getObjectByPath(path);
			boolean success = o.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT;
			return ResultBuilder.result(success, (Document) o);
		} catch (CmisObjectNotFoundException e) {
			return ResultBuilder.failure(null);
		}
	}
	
	
	public BaseHelperResult<Document> waitForContainsDocument(final String path, long timeout, long period) throws Exception{
		
		SimpleWaiter<BaseHelperResult<Document>> waiter = new SimpleWaiter<BaseHelperResult<Document>>(timeout, period){
			@Override
			public BaseHelperResult<Document> performCheck(BaseHelperResult<Document> latestResult) {
				return containsDocument(path);
			}
		};
		return waiter.call();
	}
	
	/**
	 * Create a sub-folder using the given name in parent.
	 * The Folder is created with the minimal set of properties:
	 * <pre>
	 * properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
	 * properties.put(PropertyIds.NAME, name);
	 * </pre>
	 * @param name
	 * @param parent
	 * @return
	 */
	public Folder createFolder(String name, Folder parent){
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		properties.put(PropertyIds.NAME, name);

		return createFolder(name, parent, properties);
	}
	
	/**
	 * Create a Folder using the given properties. Identical to 
	 * <i>parent.createFolder(props)</i>
	 * @param name
	 * @param parent
	 * @param props
	 * @return
	 */
	public Folder createFolder(String name, Folder parent, Map<String, Object> props){
		return parent.createFolder(props);
	}
	
	public Folder createFolderIfNotExists(String parent, String name){
		try {
			return FileUtils.createFolder(parent, name, null, session);
		} catch (CmisContentAlreadyExistsException e) {
			if(parent.endsWith("/")){
				return FileUtils.getFolder(parent + name, session);
			} else {
				return FileUtils.getFolder(parent + "/" + name, session);
			}
		}
	}
	
	/**
	 * Generate the session parameters which will be used by this Helper.
	 * @return
	 */
	public Map<String, String> generateSessionParameters(){
		return bindingContextHandler.generateSessionParameter();
	}

	@Override
	public boolean isReady() {
		return bindingContextHandler.isReady();
	}
	
	/**
	 * Return the session currently used by this Helper. This method will
	 * return null if init() has not been called yet.
	 * @return the Helper's session, or null if Helper not initialized
	 */
	public Session getSession(){
		return session;
	}

}
