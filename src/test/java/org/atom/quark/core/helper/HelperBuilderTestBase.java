package org.atom.quark.core.helper;

import org.testng.Assert;

/**
 * Base class for a HelperBuilder test.
 * @author Pierre Beucher
 *
 */
public class HelperBuilderTestBase {
	
	/**
	 * This test ensure the base context wrapped in the builder
	 * is not reused directly in created helper, and copied instead
	 * @param helper
	 */
	protected void testNoReuseBaseContext(AbstractHelperBuilder<?, ?> builder){
		Helper helper = builder.buildBaseHelper();
		Assert.assertNotEquals(
				helper.getContext().hashCode(),
				builder.getBaseContext().hashCode(),
				"A Builder should not use its base context directly when creating Helper");
		
	}

}
