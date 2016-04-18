package com.github.pierrebeucher.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.BaseExpectingHelperResult;
import com.github.pierrebeucher.quark.core.result.ExpectingHelperResult;

public class TypedExpectingHelperResultTest {

	private static final String ACTUAL = "abc";
	private static final ArrayList<Object> EXPECTED = new ArrayList<Object>();
	
	private ExpectingHelperResult<String, List<Object>> build(){
		return new BaseExpectingHelperResult<String, List<Object>>(true, ACTUAL, EXPECTED);
	}
	
	@Test
	public void isSuccess() {
		ExpectingHelperResult<String, List<Object>> result = build();
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void getActual() {
		ExpectingHelperResult<String, List<Object>> result = build();
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void getExpected() {
		ExpectingHelperResult<String, List<Object>> result = build();
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
}
