package org.atom.quark.core.helper;

import org.atom.quark.core.context.base.EmptyContext;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AbstractHelperTest {

	@Test
	public void AbstractHelper() {
		EmptyContext ctx = new EmptyContext();
		Helper helper = new AbstractHelper<EmptyContext>(ctx){
			@Override
			public boolean isReady() {
				return true;
			}
		};

		Assert.assertEquals(helper.getContext(), ctx);
	}
}
