package org.atom.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TypedHelperResultTest {
	
	private static final String  EXPECTED = "test";
	private static final List<Object> ACTUAL = new ArrayList<Object>();

	private HelperResult buildResult(){
		return new TypedHelperResult<String, List<Object>>(true, EXPECTED, ACTUAL);
	}

	@Test
	public void getActual() {
		Assert.assertEquals(buildResult().getActual(), ACTUAL);
	}

	@Test
	public void getExpected() {
		Assert.assertEquals(buildResult().getExpected(), EXPECTED);
	}

	@Test
	public void isSuccess() {
		Assert.assertEquals(buildResult().isSuccess(), true);
	}
}
