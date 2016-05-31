package com.github.pierrebeucher.quark.ftp.helper;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.github.pierrebeucher.quark.BaseHelperIT;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;

public class BaseFtpHelperIT<H extends Helper & Initialisable & Disposable> extends BaseHelperIT<H> {

	public BaseFtpHelperIT(H helper) {
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

}
