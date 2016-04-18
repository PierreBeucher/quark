package com.github.pierrebeucher.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.HelperResult;

public class TypedHelperResultTest {
	
	private static final List<Object> ACTUAL = new ArrayList<Object>();

	private HelperResult<List<Object>> buildResult(){
		return new BaseHelperResult<List<Object>>(true, ACTUAL);
	}

	@Test
	public void getActual() {
		Assert.assertEquals(buildResult().getActual(), ACTUAL);
	}

	@Test
	public void isSuccess() {
		Assert.assertEquals(buildResult().isSuccess(), true);
	}
}
