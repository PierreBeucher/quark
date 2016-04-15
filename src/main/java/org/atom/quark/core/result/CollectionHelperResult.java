package org.atom.quark.core.result;

import java.util.Collection;

public class CollectionHelperResult<A> extends BaseHelperResult<Collection<A>>
		implements HelperResult<Collection<A>>{

	public CollectionHelperResult(boolean success, Collection<A> actual, String message, Throwable cause) {
		super(success, actual, message, cause);
	}

	public CollectionHelperResult(boolean success, Collection<A> actual, String message) {
		super(success, actual, message);
	}

	public CollectionHelperResult(boolean success, Collection<A> actual) {
		super(success, actual);
	}
}
