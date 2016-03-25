package org.atom.quark.core.result;

/**
 * <p>Abstract HelperResult class providing basic infrastructure for success
 * and action output which can be used by most Helper Result. </p>
 * @author Pierre Beucher
 *
 * @param <E> The data output type for this result
 */
public abstract class AbstractHelperResult<E> implements HelperResult<E>{

	private boolean success;
	
	private E actionOutput;
	
	public AbstractHelperResult(boolean success, E actionOutput) {
		super();
		this.success = success;
		this.actionOutput = actionOutput;
	}

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public E getActionOutput() {
		return actionOutput;
	}

	public void setActionOutput(E actionOutput) {
		this.actionOutput = actionOutput;
	}

	
}
