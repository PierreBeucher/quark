package com.github.pierrebeucher.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.result.ExpectingHelperResult;
import com.github.pierrebeucher.quark.core.result.HelperResult;
import com.github.pierrebeucher.quark.core.result.ResultBuilder;

public class ResultBuilderTest {
	
	private static final String ACTUAL = "actual";
	private static final List<String> EXPECTED = new ArrayList<String>();

	@Test
	public void resultAPositive() {
		HelperResult<String> result = ResultBuilder.result(true, ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}
	
	@Test
	public void resultANegative() {
		HelperResult<String> result = ResultBuilder.result(false, ACTUAL + "abc");
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertNotEquals(result.getActual(), ACTUAL);
	}
	
	@Test
	public void failureA() {
		HelperResult<String> result = ResultBuilder.failure(ACTUAL);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void successA() {
		HelperResult<String> result = ResultBuilder.success(ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void resultAEPositive() {
		ExpectingHelperResult<String, List<String>> result = ResultBuilder.expectingResult(true, ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void resultAENegative() {
		ExpectingHelperResult<String, Object> result = ResultBuilder.expectingResult(false, ACTUAL + "abc", null);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertNotEquals(result.getActual(), ACTUAL);
		Assert.assertNotEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void successAE() {
		ExpectingHelperResult<String, List<String>> result = ResultBuilder.expectingSuccess(ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void failureAE() {
		ExpectingHelperResult<String, List<String>> result = ResultBuilder.expectingRailure(ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
}
