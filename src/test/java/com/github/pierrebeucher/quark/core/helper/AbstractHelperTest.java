package com.github.pierrebeucher.quark.core.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.base.EmptyContext;
import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;

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
