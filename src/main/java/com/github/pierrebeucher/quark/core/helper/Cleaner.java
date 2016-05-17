package com.github.pierrebeucher.quark.core.helper;

/**
 * <p>Base class for a Cleaner Helper. Thiis interface
 * represents a Cleaner Helper, used to safely clean a component
 * before running test on it. Cleaning a component means ensuring
 * the component is cleaned of any data which may impact the action
 * we want to run, such as removing elements or resetting parameters.</p>
 * <p>A Cleaner should only provide <i>safe</i> functionalities, 
 * i.e. cleaning components in a non-destructive way allowing to retrieve
 * the previous state of said component easily.</p>
 * 
 * @author pierreb
 *
 */
public interface Cleaner {

}
