package org.atom.quark.core.result;

import java.util.List;

public class ListHelperResult<A> extends BaseHelperResult<List<A>>
		implements HelperResult<List<A>>{

	public ListHelperResult(boolean success, List<A> actual, String message, Throwable cause) {
		super(success, actual, message, cause);
	}

	public ListHelperResult(boolean success, List<A> actual, String message) {
		super(success, actual, message);
	}

	public ListHelperResult(boolean success, List<A> actual) {
		super(success, actual);
	}
}
