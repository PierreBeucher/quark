package org.atom.quark.jdbc.helper;

import java.sql.Connection;
import java.sql.SQLException;

import org.atom.quark.core.helper.AbstractHelper;
import org.atom.quark.core.helper.Helper;
import org.atom.quark.jdbc.context.JdbcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcHelper extends AbstractHelper<JdbcContext> implements Helper{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Connection connection;

	public JdbcHelper(JdbcContext context) {
		super(context);
	}
	
	/**
	 * Connect using the wrapper DataSource.
	 * @throws SQLException
	 */
	public void connect() throws SQLException{
		Connection conn = getConnection();
		setConnection(conn);
	}

	/**
	 * Connect using the provided login and password. The provided 
	 * login and password will overwrite any existing authentication
	 * context.
	 * @param username
	 * @param password
	 * @throws SQLException
	 */
	public void connect(String username, String password) throws SQLException{
		Connection conn = getConnection(username, password);
		setConnection(conn);
	}
	
	/**
	 * Close the current connection. No effect if connection
	 * is null or alredy closed.
	 * @throws SQLException 
	 */
	public void close() throws SQLException{
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
	}
	
	private void setConnection(Connection conn){
		this.connection = conn;
	}
	
	/**
	 * Return the current connection. Create a new connection if required.
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{
		logger.debug("Connect {}", context.toString());
		return context.getDataSource().getConnection();
	}
	
	public Connection getConnection(String username, String password) throws SQLException{
		logger.debug("Connect {} username: {}", context.toString(), username);
		return context.getDataSource().getConnection(username, password);
	}

	/**
	 * The JdbcHelper is ready if its connection is valid.
	 */
	public boolean isReady() {
		try {
			return connection != null && connection.isValid(0);
		} catch (SQLException e) {
			logger.warn("Error verifying connection validity: {}", e);
			return false;
		}
	}

}
