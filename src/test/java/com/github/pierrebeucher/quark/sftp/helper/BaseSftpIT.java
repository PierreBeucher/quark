package com.github.pierrebeucher.quark.sftp.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.DisposeException;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;

/**
 * Base class for SFTP Helpers IT. Provide a Helper, a Logger
 * and before/afterClass functions
 * @author pierreb
 *
 */
public abstract class BaseSftpIT<H extends Helper & Disposable & Initialisable> {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected H helper;
	
	public BaseSftpIT(H helper) {
		this.helper = helper;
	}

	/**
	 * Initialize this test Helper
	 * @throws InitialisationException 
	 */
	@BeforeClass
	public void beforeClass() throws InitialisationException{
		initHelper();
	}
	
	/**
	 * Dispose this test Helper
	 * @throws DisposeException 
	 */
	@AfterClass
	public void afterClass() throws DisposeException{
		disposeHelper();
	}
	
	protected void initHelper() throws InitialisationException{
		helper.initialise();
	}
	
	protected void disposeHelper() throws DisposeException{
		helper.dispose();
	}
}
