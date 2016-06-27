package com.github.pierrebeucher.quark.core.helper;

/**
 * <p><code>Cleaner</code> is capable of cleaning an environment with its current context.
 * Classes implementing this interface may or may not delete data permanently. For a safe
 * implementation ensuring data are backed-up or saved before cleaning, use <code>SafeCleaner</code>
 * instead.</p>
 * @author pierreb
 *  
 */
public interface Cleaner extends Helper{

}
