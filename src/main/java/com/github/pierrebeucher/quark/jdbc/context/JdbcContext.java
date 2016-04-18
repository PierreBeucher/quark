package com.github.pierrebeucher.quark.jdbc.context;

import javax.sql.DataSource;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

public class JdbcContext implements HelperContext{
	
	private DataSource dataSource;
	private String database;
	
	public JdbcContext(DataSource dataSource) {
		this(dataSource, null);
	}
	
	public JdbcContext(DataSource dataSource, String database) {
		this.dataSource = dataSource;
		this.database = database;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return dataSource.toString();
	}
	
	

}
