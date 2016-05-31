package com.github.pierrebeucher.quark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.github.pierrebeucher.quark.core.helper.Helper;

/**
 * Base class for a single <code>Helper</code> instance Integration Test, providing
 * a Helper field and a Logger, and basic test configuration functions.
 * This base test can be used with Factory annotations
 * to create test instances for various Helpers.
 * @author pierreb
 *
 */
public class BaseHelperIT<H extends Helper>{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected H helper;

	public BaseHelperIT(H helper) {
		super();
		this.helper = helper;
	}
	
	/**
	 * This function is called before the test class using BeforeClass annotation.
	 * Does nothing by default.
	 */
	@BeforeClass
	public void beforeClass(){
		
	}
	
	/**
	 * Called after the test class using AfterClass annotation.
	 * Does nothing by default.
	 */
	@AfterClass
	public void afterClass(){
		
	}

}
