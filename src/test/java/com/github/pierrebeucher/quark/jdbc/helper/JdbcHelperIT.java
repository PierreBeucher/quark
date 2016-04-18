package com.github.pierrebeucher.quark.jdbc.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.JdbcHelper;

public class JdbcHelperIT {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String host;
	private int port;
	private String login;
	private String password;

	@Parameters({ "db-host", "db-port", "db-login", "db-password" })
	@BeforeTest
	public void beforeTest(String host, int port, String login, String password) {
		this.host = host;
		this.port = port;
		this.login = login;
		this.password = password;
		
		logger.info("Testing with host={}, port={}, login={}, password={}", host, port, login, password);
	}

	private JdbcHelper createHelper(){
		String url = "jdbc:mysql://" + host + ":" + port;
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, login, password);
		
		//set properties to ignore SSL, for test purpose only
		Properties connProperties = new Properties();
		connProperties.put("useSSL", "false");
		dataSource.setConnectionProperties(connProperties);
		
		return new JdbcHelper(new JdbcContext(dataSource));
	}

	@Test
	public void connect() throws SQLException {
		JdbcHelper helper = createHelper();
		helper.connect();
	}

	@Test
	public void connectStringString() throws SQLException {
		JdbcHelper helper = createHelper();
		helper.connect(login, password);
	}

	@Test
	public void getConnection() throws SQLException {
		JdbcHelper helper = createHelper();
		Connection c = helper.getConnection();
		Assert.assertNotNull(c);
	}
	
	@Test
	public void getConnectionStringString() throws SQLException {
		JdbcHelper helper = createHelper();
		Connection c = helper.getConnection(login, password);
		Assert.assertNotNull(c);
	}
	
	@Test
	public void close() throws SQLException {
		JdbcHelper helper = createHelper();
		helper.connect();
		helper.close();
	}

	@Test
	public void isReadyPositive() throws SQLException {
		JdbcHelper helper = createHelper();
		helper.connect();
		Assert.assertEquals(helper.isReady(), true);
	}
	
	@Test
	public void isReadyNegativeNoConnect() throws SQLException {
		JdbcHelper helper = createHelper();
		Assert.assertEquals(helper.isReady(), false);
	}
	
	@Test
	public void isReadyNegativeDisconnect() throws SQLException {
		JdbcHelper helper = createHelper();
		helper.connect();
		helper.close();
		Assert.assertEquals(helper.isReady(), false);
	}
}
