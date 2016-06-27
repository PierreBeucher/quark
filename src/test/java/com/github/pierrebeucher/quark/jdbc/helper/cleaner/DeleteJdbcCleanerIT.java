package com.github.pierrebeucher.quark.jdbc.helper.cleaner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.BaseJdbcHelperIT;

public class DeleteJdbcCleanerIT extends BaseJdbcHelperIT<DeleteJdbcCleaner>{

	public static final String IT_CLEAN_TABLE = "quark_it_clean";
	
	private String cleanTable;
	
	@Parameters({ "db-host", "db-port", "db-login", "db-password", "db-schema"  })
	@Factory
	public static Object[] beforeTest(String host, int port, String login, String password,
			String schema)
				throws InitialisationException {
		return new Object[]{
			new DeleteJdbcCleanerIT(createHelper(host, port, login, password, schema))
		};
	}

	private static DeleteJdbcCleaner createHelper(String host, int port, String login, String password,
			String schema){
		String url = "jdbc:mysql://" + host + ":" + port + "/" + schema;
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, login, password);
		
		//set properties to ignore SSL, for test purpose only
		Properties connProperties = new Properties();
		connProperties.put("useSSL", "false");
		dataSource.setConnectionProperties(connProperties);
		
		return new DeleteJdbcCleaner(new JdbcContext(dataSource, schema));
	}

	public DeleteJdbcCleanerIT(DeleteJdbcCleaner helper) {
		super(helper);
		this.cleanTable = IT_CLEAN_TABLE;
	}
	
	/**
	 * Create the given table if not exists, delete any existing data and insert new dummy data.
	 * @param table
	 */
	private void prepareDummyTable(String table){
		JdbcTemplate jdbcTemplate = helper.getWrappedHelper().getTemplate();
		jdbcTemplate.execute("create table if not exists " + table + " (id INT NOT NULL AUTO_INCREMENT,"
				+ " name VARCHAR(255),"
				+ " insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
				+ " bigvalue BLOB," 
				+ " PRIMARY KEY (id)"
				+ ")");
		
		jdbcTemplate.update("DELETE FROM " + table);
		
		jdbcTemplate.update("INSERT INTO " + table + " (bigvalue,name) VALUES ('value for a name', 'a name')");
		jdbcTemplate.update("INSERT INTO " + table + " (bigvalue,name) VALUES ('value for some name', 'some name')");
		jdbcTemplate.update("INSERT INTO " + table + " (bigvalue,name) VALUES ('value for my name', 'my name')");
		jdbcTemplate.update("INSERT INTO " + table + " (bigvalue,name) VALUES ('value for pretty name', 'pretty name')");
		jdbcTemplate.update("INSERT INTO " + table + " (bigvalue,name) VALUES ('value for a girl with no name', 'a girl has no name')");
	}

	/**
	 * Test a cleaned table is empty after clean
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testCleanEmpty() throws SQLException, IOException {
		logger.info("Test clean empty with {} table {}", helper, cleanTable);
		prepareDummyTable(cleanTable);
		
		helper.clean(cleanTable);
		
		List<?> result = helper.getWrappedHelper().getTemplate().queryForList("SELECT * FROM " + cleanTable);
		Assert.assertEquals(result.size(), 0, "Table is not empty after clean.");
	}
	
//	/**
//	 * Test a cleaned table archived data represents deleted data properly.
//	 * Dummy data are queried before test, table is cleaned, the archive script is run again
//	 * and another query is performed before comparing before/after query result.
//	 * @throws SQLException
//	 * @throws IOException
//	 */
//	@Test
//	public void testCleanArchive() throws SQLException, IOException {
//		logger.info("Test clean archive with {} table {}", helper, cleanTable);
//		prepareDummyTable(cleanTable);
//
//		//select before clean
//		List<Map<String, Object>> resultBefore = selectAllFrom(helper.getWrappedHelper(), cleanTable);
//		
//		//perform clean
//		File dumpFile = File.createTempFile("quarkit_sqldump", null);
//		helper.clean(cleanTable, dumpFile);
//		
//		logger.info("Dumped SQL to {}", dumpFile);
//		
////		//run archive script
////		Reader reader = new BufferedReader(new FileReader(dumpFile));
////		ScriptRunner sr = new ScriptRunner(helper.getWrappedHelper().getConnection());
////		sr.runScript(reader);
//		
////		//compare before/after
////		List<Map<String, Object>> resultAfter = selectAllFrom(helper.getWrappedHelper(), cleanTable);
////		Assert.assertEquals(resultBefore,
////				resultAfter,
////				"Data queried are different before clean and after clean archive script is re-inserted.");
//	}
//	
//	private List<Map<String, Object>> selectAllFrom(SpringJdbcHelper helper, String table){
//		return helper.getTemplate().queryForList("SELECT * FROM " + cleanTable);
//	}
	
	
}
