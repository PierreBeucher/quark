package com.github.pierrebeucher.quark.jdbc.context;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

public class JdbcContext implements HelperContext{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataSource dataSource;
	private String database;
	
	/**
	 * Empty constructor. Use setters to setup context.
	 */
	public JdbcContext() {
		super();
	}
	
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
		try {
			return dataSource.getConnection().getMetaData().getURL() + ":" + database;
		} catch (SQLException e) {
			logger.error("Cannot generate dataSource URL: {}", e);
			return dataSource.toString() + ":" + database;
		}
	}
	
	

}
