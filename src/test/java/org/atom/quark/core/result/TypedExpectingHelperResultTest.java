package org.atom.quark.core.result;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TypedExpectingHelperResultTest {

	private static final String ACTUAL = "abc";
	private static final ArrayList<Object> EXPECTED = new ArrayList<Object>();
	
	private ExpectingHelperResult build(){
		return new TypedExpectingHelperResult<String, List<Object>>(true, ACTUAL, EXPECTED);
	}
	
	@Test
	public void isSuccess() {
		ExpectingHelperResult result = build();
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void getActual() {
		ExpectingHelperResult result = build();
		Assert.assertEquals(result.getActual(), ACTUAL);
	}

	@Test
	public void getExpected() {
		ExpectingHelperResult result = build();
		Assert.assertEquals(result.getExpected(), EXPECTED);
	}
}
