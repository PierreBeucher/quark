package com.github.pierrebeucher.quark.cmis.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.util.FileUtils;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.github.pierrebeucher.quark.BaseHelperIT;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.util.CMISUtils;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;

public class BaseCMISHelperIT<H extends Helper & Initialisable & Disposable> extends BaseHelperIT<H> {

	public BaseCMISHelperIT(H helper) {
		super(helper);
	}
	
	@BeforeClass
	public void beforeClass(){
		super.beforeClass();
		helper.initialise();
	}
	
	@AfterClass
	public void afterClass(){
		super.afterClass();
		helper.dispose();
	}
	
	/**
	 * Build a Helper for this test using the given binding context. A repository 
	 * is automatically retrieved using the first repository found available.
	 * @param bindingContext
	 * @return
	 */
	public static CMISHelper buildTestHelper(CMISBindingContext bindingContext){
		String repo = retrieveFirstAvailableRepositoryID(bindingContext);
		CMISContext ctx = new CMISContext(bindingContext, repo);
		CMISHelper helper = new CMISHelper(ctx);
		
		return helper;
	}
	
	/**
	 * Return the repositoryID of the first available repository, or '-default-' if not repository is found.
	 * @param baseContext
	 * @return
	 */
	public static String retrieveFirstAvailableRepositoryID(CMISBindingContext bindingcontext){
		List<Repository> repositories = CMISUtils.getRepositories(bindingcontext);
		
		if(repositories.size() == 0){
			//logger.warn("No repository found for {}. Returning -default- by default.");
			return "-default-";
		}
		
		return repositories.get(0).getId();
	}
	
	public Document createDocumentFromFileIfNotExists(String parent, File file, Session session) throws FileNotFoundException{
		return FileUtils.createDocumentFromFile(parent, file, null, VersioningState.MINOR, session);
	}

}
