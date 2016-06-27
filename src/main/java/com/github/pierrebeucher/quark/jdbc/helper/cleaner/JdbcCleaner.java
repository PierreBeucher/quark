package com.github.pierrebeucher.quark.jdbc.helper.cleaner;

import com.github.pierrebeucher.quark.core.helper.AbstractWrapperHelper;
import com.github.pierrebeucher.quark.core.helper.Cleaner;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.DisposeException;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.SpringJdbcHelper;

/**
 * <p>Base class for a <code>JdbcCleaner</code>, defining functions to be implemented
 * by concrete cleaner. Use with care as cleaned data may be lost definitively. Consider
 * using a <code>JdbcExtractor</code> to back-up or archive data.</p>
 * @author pierreb
 *
 */
abstract class JdbcCleaner extends AbstractWrapperHelper<JdbcContext, SpringJdbcHelper>
		implements Cleaner, Helper, Initialisable, Disposable{
	
	public JdbcCleaner(JdbcContext context) {
		super(context, new SpringJdbcHelper(context));
	}
	
	public JdbcCleaner(JdbcContext context, SpringJdbcHelper h) {
		super(context, h);
	}

	@Override
	public void dispose() throws DisposeException {
		helper.dispose();
	}

	@Override
	public boolean isDisposed() {
		return helper.isDisposed();
	}

	@Override
	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	@Override
	public boolean isInitialised() {
		return helper.isInitialised();
	}
	
	/**
	 * Clean the given table. This method ensure that any
	 * SELECT query will return an empty set of row 
	 * @param table
	 */
	public abstract void clean(String table);
}
