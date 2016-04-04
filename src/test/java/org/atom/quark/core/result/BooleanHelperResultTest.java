package org.atom.quark.core.result;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BooleanHelperResultTest {

  @Test
  public void getActual() {
	  HelperResult result = new BooleanHelperResult(false);
	  Assert.assertEquals(result.getActual(), false);
  }

  @Test
  public void getExpected() {
	  HelperResult result = new BooleanHelperResult(false);
	  Assert.assertEquals(result.getExpected(), true);
  }

  @Test
  public void isSuccess() {
	  HelperResult result = new BooleanHelperResult(false);
	  Assert.assertEquals(result.isSuccess(), false);
  }
}
