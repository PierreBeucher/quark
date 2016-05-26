package com.github.pierrebeucher.quark.jdbc.context;

import javax.sql.DataSource;

/**
 * A JdbcContext restricted to a single table in database.
 * @author pierreb
 *
 */
public class TableJdbcContext extends JdbcContext {
	
	private String table;

	public TableJdbcContext() {
		super();
	}

	public TableJdbcContext(DataSource dataSource, String database, String table) {
		super(dataSource, database);
		this.table = table;
	}
	
	public TableJdbcContext(TableJdbcContext base) {
		super(base);
		this.table = base.table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

}
