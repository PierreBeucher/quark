package org.atom.quark.cmis.helper.chemistry;

import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.atom.quark.cmis.context.CMISBindingContext;
import org.atom.quark.cmis.context.CMISContext;
import org.atom.quark.cmis.helper.CMISHelper;
import org.atom.quark.cmis.util.ChemistryCMISUtils;
import org.atom.quark.core.helper.AbstractHelper;
import org.atom.quark.core.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> params = generateSessionParameters();
		
		logger.debug("Creation session using {}", params);
		
		return factory.createSession(params);
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
	
	public Folder createFolder(String parent, String name){
		return FileUtils.createFolder(parent, name, null, session);
	}
	
	public Folder createFolderIfNotExists(String parent, String name){
		Folder folder = null;
		try{
			if(parent.endsWith("/")){
				 FileUtils.getFolder(parent + name, session);
			} else {
				folder = FileUtils.getFolder(parent + "/" + name, session);
			}
		} catch (CmisObjectNotFoundException e){
			//this is expected
			return createFolder(parent, name);
		}
		return folder;
	}
	
	public ItemIterable<CmisObject> listDirectory(String path){
		CmisObject o = session.getObjectByPath(path);
		if(!Folder.class.isAssignableFrom(o.getClass())){
			throw new RuntimeException("CmisObject with path " + path + " is not a Folder but: " + o.getClass());
		}
		
		return ((Folder) o).getChildren();
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

}
