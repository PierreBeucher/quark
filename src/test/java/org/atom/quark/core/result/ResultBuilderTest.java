package org.atom.quark.core.result;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ResultBuilderTest {
	
	private static final String EXPECTED = "expected";
	private static final String ACTUAL = "actual";

	@Test
	public void failure() {
		HelperResult result = ResultBuilder.failure();
		Assert.assertEquals(result.isSuccess(), false);
	}

	@Test
	public void failureEO() {
		HelperResult result = ResultBuilder.failure(EXPECTED, ACTUAL);
		Assert.assertEquals(result.isSuccess(), false);
		Assert.assertEquals(result.getExpected(), EXPECTED);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void result() {
		HelperResult result = ResultBuilder.result(true, EXPECTED, ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getExpected(), EXPECTED);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void success() {
		HelperResult result = ResultBuilder.success(EXPECTED, ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getExpected(), EXPECTED);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void successE() {
		HelperResult result = ResultBuilder.success(ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getExpected(), ACTUAL);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void successEO() {
		HelperResult result = ResultBuilder.success(EXPECTED, ACTUAL);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertEquals(result.getExpected(), EXPECTED);
		Assert.assertEquals(result.getActual(), ACTUAL);
	}
}
