package com.github.pierrebeucher.quark.core.helper;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.base.EmptyContext;
import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.helper.AbstractHelperBuilder;
import com.github.pierrebeucher.quark.core.helper.Helper;

public class AbstractHelperBuilderTest {

	@Test
	public void AbstractHelperBuilder() {
		final EmptyContext ctx = EmptyContext.context();
		AbstractHelperBuilder<EmptyContext, Helper> builder 
				= new AbstractHelperBuilder<EmptyContext, Helper>(ctx){
			@Override
			protected Helper buildBaseHelper() {
				return new AbstractHelper<EmptyContext>(ctx){
					@Override
					public boolean isReady() {
						return true;
					}
				};
			}
		};
		
		Assert.assertEquals(ctx, builder.getBaseContext());
	}

}
