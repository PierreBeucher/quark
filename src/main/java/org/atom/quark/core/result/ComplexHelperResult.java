package org.atom.quark.core.result;

/**
 * <p>The ComplexHelperResult represents result for a complex action,
 * where actual and expected values are not enough to describe the result.</p>
 * <p>It defines another element, the globalActionOutput, representing a complex
 * element obtained during the test action, from which is obtained the actual result.
 * The globalActionOutput may be a Set or List of objects or a complex POJO obtained
 * as output from an action, against which is run some logical treatment
 * to deduce the actual with the expected. </p>
 * <p>For example, when querying a set of data as Map and checking
 * for the presence of key/value pairs matching certain condition,
 * the actual value may be null or empty, and the expected value a simple
 * element used for comparison. In this case, with only these two elements,
 * there mey not be enough usable information to define the how and why. Using
 * the global output, our Map with all the data, more information may be 
 * provided to define the reason of success or failure</p> 
 * 
 * @author Pierre Beucher
 *
 */
public class ComplexHelperResult<E, A, O> extends TypedHelperResult<E, A> {

	private O globalOutput;
	
	public ComplexHelperResult(boolean success, E expected, A actual, O globalOutput) {
		super(success, expected, actual);
		this.globalOutput = globalOutput;
	}

	public O getGlobalOutput() {
		return globalOutput;
	}

}
