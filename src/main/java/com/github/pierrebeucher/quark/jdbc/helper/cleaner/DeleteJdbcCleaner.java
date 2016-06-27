package com.github.pierrebeucher.quark.jdbc.helper.cleaner;

import org.springframework.dao.DataAccessException;

import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.JdbcHelperException;
import com.github.pierrebeucher.quark.jdbc.helper.SpringJdbcHelper;

/**
 * <code>JdbcCleaner</code> using DELETE update to perform cleaning.
 * @author pierreb
 *
 */
public class DeleteJdbcCleaner extends JdbcCleaner {

	/**
	 * Utility method to clean a Jdbc context. Instanciate a new
	 * DeleteJdbcCleaner and calls {@link #clean(String)} method.
	 * @param context
	 * @param table
	 */
	public static void clean(JdbcContext context, String table){
		DeleteJdbcCleaner cleaner = new DeleteJdbcCleaner(context);
		try{
			cleaner.initialise();
			cleaner.clean(table);
		} finally {
			cleaner.dispose();
		}
	}
	
	public DeleteJdbcCleaner(SpringJdbcHelper h) {
		this(h.getContext(), h);
	}
	
	public DeleteJdbcCleaner(JdbcContext context, SpringJdbcHelper h) {
		super(context, h);
	}

	public DeleteJdbcCleaner(JdbcContext context) {
		super(context);
	}

	@Override
	public void clean(String table) throws JdbcHelperException {
		logger.info("Cleaning {} on {}.", table, context);
		int rows;
		try {
			rows = helper.getTemplate().update("DELETE FROM " + context.getDatabase() + "." + table);
		} catch (DataAccessException e) {
			throw new JdbcHelperException(e);
		}
		logger.debug("Cleaning {} on {}: {} rows deleted.", table, context, rows);
	}

}
