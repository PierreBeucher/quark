package org.atom.quark.core.helper;

/**
 * The Cleanable Helper can be used to clean a its testing context using the clean() functions.
 * 
 * @author Pierre Beucher
 *
 */
public interface CleaningHelper extends Helper {
	
	public static final CleaningMethod DEFAULT_CLEAN_METHOD = CleaningMethod.SAFE;
	
	/**
	 * Enumeration of possible cleaning methods.
	 * <ul>
	 * <li>None - No cleaning is applied, the context is left unchanged.</li>
	 * <li>Safe - This cleaning method will safely move, archive or preserve in any way the cleaned data. Context cleaned
	 * using a Safe method ensure no data loss: data may be retrieved if necessary.</li>
	 * <li>Hard - This cleaning method may definitively delete the cleaned data. Use with care, as context data cleaned this way may
	 * not be recoverable. (NEVER USE IN A PRODUCTION ENVIRONMENT) </li>
	 * </ul>
	 * @author Pierre Beucher
	 *
	 */
	public enum CleaningMethod {
		NONE,
		SAFE,
		HARD
	}
	
	/**
	 * Clean the context managed by this helper using the default clean method
	 * @throws Exception
	 */
	public void clean() throws Exception;
	
	/**
	 * Clean the context managed by this helper using the given clean method
	 * @param cleaningMethod cleaning method to be used
	 * @throws Exception 
	 */
	public void clean(CleaningMethod cleaningMethod) throws Exception;

}
