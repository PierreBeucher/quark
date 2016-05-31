package com.github.pierrebeucher.quark.jdbc.helper;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.DisposeException;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;

public class JdbcHelper extends AbstractLifecycleHelper<JdbcContext> implements Helper, Initialisable, Disposable{
	
	private Connection connection;

	public JdbcHelper(JdbcContext context) {
		super(context);
	}
	
	public void initialise() throws InitialisationException {
		lifecycleManager.initialise();
		try {
			connect();
		} catch (SQLException e) {
			throw new InitialisationException(e);
		}
	}

	public boolean isInitialised() {
		return lifecycleManager.isInitialised();
	}
	
	public void dispose() throws DisposeException {
		lifecycleManager.dispose();
		try {
			close();
		} catch (SQLException e) {
			throw new DisposeException(e);
		}
	}

	public boolean isDisposed() {
		return lifecycleManager.isDisposed();
	}

	/**
	 * Connect using the wrapper DataSource.
	 * @throws SQLException
	 */
	private void connect() throws SQLException{
		Connection conn = context.getDataSource().getConnection();
		this.connection = conn;
	}

//	/**
//	 * Connect using the provided login and password. The provided 
//	 * login and password will overwrite any existing authentication
//	 * context.
//	 * @param username
//	 * @param password
//	 * @throws SQLException
//	 */
//	public void connect(String username, String password) throws SQLException{
//		Connection conn = getConnection(username, password);
//		setConnection(conn);
//	}
	
	/**
	 * Close the current connection. No effect if connection
	 * is null or alredy closed.
	 * @throws SQLException 
	 */
	private void close() throws SQLException{
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
	}
	
	/**
	 * Return the current connection.
	 * @return the Connection used by this Helper, null if not initialised
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{
		logger.debug("Connect {}", context.toString());
		return context.getDataSource().getConnection();
	}
	
//	public Connection getConnection(String username, String password) throws SQLException{
//		logger.debug("Connect {} username: {}", context.toString(), username);
//		return context.getDataSource().getConnection(username, password);
//	}

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
