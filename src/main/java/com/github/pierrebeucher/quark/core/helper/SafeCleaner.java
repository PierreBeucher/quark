package com.github.pierrebeucher.quark.core.helper;

/**
 * <p><code>SafeCleaner</code> extends <code>Cleaner</code> to ensure
 * any cleaned data is backed-up or archived before cleaning,
 * ensuring any deleted data may be retrieved after deletion. </p>
 * <p>Archive or back-up methods may vary upon implementation.</p>
 * @author pierreb
 *
 */
public interface SafeCleaner extends Cleaner {

}
