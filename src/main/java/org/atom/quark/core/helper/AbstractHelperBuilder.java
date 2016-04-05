package org.atom.quark.core.helper;

import org.atom.quark.core.context.base.HelperContext;

/**
 * <p>Base class for any HelperBuilder. A HelperBuilder is used
 * to instanciate fresh helper using a base Context. The Builder
 * is then aware of a base context, with which multiple
 * Helpers can be instanciated and used. </p>
 * <p>There are several ways for using a Builder: using a fully-set Context, or a partially-set
 * Context. <br>
 * With a fully-set Context, you can instanciate ready-to-work helper using
 * the exactly defined context, so as to create multiple helpers which you can
 * use parallely to perform various actions.<br>
 * With a partially-set Context, you can instanciate helpers by defining
 * your own attribute where required, thus having helper working in a similar but different
 * Context. For example, if you want to work on the same host/port bu with
 * different login and password.</p>
 * <p>Example: TODO</p>
 * @author Pierre Beucher
 *
 */
public abstract class AbstractHelperBuilder<C extends HelperContext, H extends Helper>
		implements HelperBuilder{

	protected C baseContext;
	
	public AbstractHelperBuilder(C baseContext) {
		super();
		this.baseContext = baseContext;
	}
	
	/**
	 * Build a Helper defined using the base context.
	 * Helpers created by this function must copy attributes from the base
	 * context, so as to void different Helpers sharing the same Context instance.
	 * @return a fresh empty Helper
	 */
	protected abstract H buildBaseHelper();
	
	public C getBaseContext() {
		return baseContext;
	}

}
