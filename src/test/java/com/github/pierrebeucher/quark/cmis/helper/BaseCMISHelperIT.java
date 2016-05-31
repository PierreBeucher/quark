package com.github.pierrebeucher.quark.cmis.helper;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.Repository;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.github.pierrebeucher.quark.BaseHelperIT;
import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.cmis.util.ChemistryCMISUtils;
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
	 * Return the repositoryID of the first available repository, or '-default-' if not repository is found.
	 * @param baseContext
	 * @return
	 */
	public static String retrieveFirstAvailableRepositoryID(CMISBindingContext bindingcontext){
		List<Repository> repositories = ChemistryCMISUtils.getRepositories(bindingcontext);
		
		if(repositories.size() == 0){
			//logger.warn("No repository found for {}. Returning -default- by default.");
			return "-default-";
		}
		
		return repositories.get(0).getId();
	}

}
