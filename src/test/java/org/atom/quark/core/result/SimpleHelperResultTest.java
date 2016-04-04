package org.atom.quark.core.result;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleHelperResultTest {

	private static final String ACTUAL = "test";
	private static final String EXPECTED = "expected";

	@Test
	public void SimpleHelperResult() {
		HelperResult result = new SimpleHelperResult<String>(true, EXPECTED, ACTUAL);
		Assert.assertEquals(result.getActual(), ACTUAL);
		Assert.assertEquals(result.getExpected(), EXPECTED);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	
}
