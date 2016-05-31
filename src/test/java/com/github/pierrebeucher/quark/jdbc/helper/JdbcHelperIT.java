package com.github.pierrebeucher.quark.jdbc.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.JdbcHelper;

public class JdbcHelperIT extends BaseJdbcHelperIT<JdbcHelper> {

	@Parameters({ "db-host", "db-port", "db-login", "db-password" })
	@Factory
	public static Object[] beforeTest(String host, int port, String login, String password) throws InitialisationException {
		return new Object[]{
			new JdbcHelperIT(createHelper(host, port, login, password))
		};
	}

	private static JdbcHelper createHelper(String host, int port, String login, String password){
		String url = "jdbc:mysql://" + host + ":" + port;
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, login, password);
		
		//set properties to ignore SSL, for test purpose only
		Properties connProperties = new Properties();
		connProperties.put("useSSL", "false");
		dataSource.setConnectionProperties(connProperties);
		
		return new JdbcHelper(new JdbcContext(dataSource));
	}
	
	public JdbcHelperIT(JdbcHelper helper) {
		super(helper);
	}

	@Test
	public void getConnection() throws SQLException {
		Connection c = helper.getConnection();
		Assert.assertNotNull(c);
	}
}
