package org.atom.quark.core.helper;

import org.atom.quark.core.context.base.EmptyContext;
import org.testng.Assert;
import org.testng.annotations.Test;

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
