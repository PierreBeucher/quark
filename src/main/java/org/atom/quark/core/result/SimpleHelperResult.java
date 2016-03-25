package org.atom.quark.core.result;

/**
 * A HelperResult using a simple result description
 * @author Pierre Beucher
 *
 * @param <E>
 */
public class SimpleHelperResult<E> extends AbstractHelperResult<E>{

	public SimpleHelperResult(boolean success, E actionOutput) {
		super(success, actionOutput);
	}

	/**
	 * @return this Helper result as <i>"success|failure:actionOutput"</i>
	 * such as <i>"success:[Banana, Apple, Pear]"</i>
	 */
	public String getResultDescription() {
		
		StringBuffer buf = new StringBuffer(isSuccess() ? SUCCESS : FAILURE)
				.append(":");
		
		if(getActionOutput() != null){
			buf.append(this.getActionOutput().toString());
		} else {
			buf.append("null");
		}
		return buf.toString();
	}

}
