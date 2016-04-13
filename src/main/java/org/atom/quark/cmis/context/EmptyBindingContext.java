package org.atom.quark.cmis.context;

/**
 * Singleton used to represent an empty Binding Context.
 * Use by the CMISContext's empty constructor to define a non-null Binding Context.
 * @author Pierre Beucher
 *
 */
public class EmptyBindingContext extends CMISBindingContext {

	private static final EmptyBindingContext instance = new EmptyBindingContext();
	public static final EmptyBindingContext instance(){
		return instance;
	}
	
	private EmptyBindingContext(){}
}
