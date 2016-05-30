package com.github.pierrebeucher.quark.sftp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.helper.Lifecycle;

/**
 * Base class for SFTP Helpers IT. Provide a Helper, a Logger
 * and before/afterClass functions
 * @author pierreb
 *
 */
public abstract class BaseSftpIT<H extends Helper & Lifecycle> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected H helper;
	
	public BaseSftpIT(H helper) {
		this.helper = helper;
	}

	/**
	 * Initialize this test Helper
	 */
	@BeforeClass
	public void beforeClass(){
		initHelper();
	}
	
	/**
	 * Dispose this test Helper
	 */
	@AfterClass
	public void afterClass(){
		disposeHelper();
	}
	
	protected void initHelper(){
		helper.initialise();
	}
	
	protected void disposeHelper(){
		helper.dispose();
	}
}
