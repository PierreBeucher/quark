package org.atom.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ResultBuilderTest {
	
	private static final String ACTUAL = "actual";
	private static final List<String> EXPECTED = new ArrayList<String>();

	@Test
	public void resultAPositive() {
		HelperResult result = ResultBuilder.result(true, ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}
	
	@Test
	public void resultANegative() {
		HelperResult result = ResultBuilder.result(false, ACTUAL + "abc");
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertNotEquals(result.getActual(), ACTUAL);
	}
	
	@Test
	public void failureA() {
		HelperResult result = ResultBuilder.failure(ACTUAL);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void successA() {
		HelperResult result = ResultBuilder.success(ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void resultAEPositive() {
		ExpectingHelperResult result = ResultBuilder.result(true, ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void resultAENegative() {
		ExpectingHelperResult result = ResultBuilder.result(false, ACTUAL + "abc", new Object[]{});
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertNotEquals(result.getActual(), ACTUAL);
		Assert.assertNotEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void successAE() {
		ExpectingHelperResult result = ResultBuilder.success(ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
	
	@Test
	public void failureAE() {
		ExpectingHelperResult result = ResultBuilder.failure(ACTUAL, EXPECTED);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
}
