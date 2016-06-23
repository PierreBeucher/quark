package com.github.pierrebeucher.quark.core.helper;

/**
 * <p><code>Cleaner</code> is capable of cleaning an environment with its current context.
 * It guarantees a safe cleaning implementation, <b>data are not deleted
 * permanently</b> and are either archived or backed-up. Depending on the
 * implementation, various methods may be applied to ensure this safety.</p>
 * @author pierreb
 *  
 */
public interface Cleaner extends Helper{

}
