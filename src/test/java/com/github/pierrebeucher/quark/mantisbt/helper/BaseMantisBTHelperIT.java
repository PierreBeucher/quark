package com.github.pierrebeucher.quark.mantisbt.helper;

import org.testng.annotations.BeforeClass;

import com.github.pierrebeucher.quark.BaseHelperIT;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;

/**
 * Base class for a MantisBT Helper Integration Test. Automatically initialise 
 * tested Helper with a BbeforeClass annotation. 
 * @author pierreb
 *
 * @param <H>
 */
public abstract class BaseMantisBTHelperIT<H extends Helper & Initialisable> extends BaseHelperIT<H>{

	public BaseMantisBTHelperIT(H helper) {
		super(helper);
	}
	
	@BeforeClass
	public void beforeClass(){
		super.beforeClass();
		helper.initialise();
	}

}
