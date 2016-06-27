package com.github.pierrebeucher.quark.jdbc.helper.extractor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.BaseJdbcHelperIT;

public class CsvJdbcExtractorIT extends BaseJdbcHelperIT<CsvJdbcExtractor>{

	public static final String IT_EXTRACT_TABLE = "quark_it_extract";
	
	private String extractTable;
	
	@Parameters({ "db-host", "db-port", "db-login", "db-password", "db-schema"  })
	@Factory
	public static Object[] beforeTest(String host, int port, String login, String password,
			String schema)
				throws InitialisationException {
		return new Object[]{
			new CsvJdbcExtractorIT(createHelper(host, port, login, password, schema))
		};
	}

	private static CsvJdbcExtractor createHelper(String host, int port, String login, String password,
			String schema){
		String url = "jdbc:mysql://" + host + ":" + port + "/" + schema;
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, login, password);
		
		//set properties to ignore SSL, for test purpose only
		Properties connProperties = new Properties();
		connProperties.put("useSSL", "false");
		dataSource.setConnectionProperties(connProperties);
		
		return new CsvJdbcExtractor(new JdbcContext(dataSource, schema));
	}

	public CsvJdbcExtractorIT(CsvJdbcExtractor helper) {
		super(helper);
		this.extractTable = IT_EXTRACT_TABLE;
	}
	
	/**
	 * Create the given table if not exists, delete any existing data and insert new dummy data.
	 * @param table
	 */
	private void prepareDummyTable(String table){
		JdbcTemplate jdbcTemplate = helper.getWrappedHelper().getTemplate();
		jdbcTemplate.execute("drop table " + table);
		jdbcTemplate.execute("create table if not exists " + table + " (id INT NOT NULL AUTO_INCREMENT,"
				+ " name VARCHAR(255),"
				+ " PRIMARY KEY (id)"
				+ ")");
		
		jdbcTemplate.update("DELETE FROM " + table);
		jdbcTemplate.update("INSERT INTO " + table + " (name) VALUES ('a girl has no name')");
	}

	
	/**
	 * Test a cleaned table archived data represents deleted data properly.
	 * Dummy data are queried before test, table is cleaned, the archive script is run again
	 * and another query is performed before comparing before/after query result.
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testCleanArchive() throws SQLException, IOException {
		logger.info("Test clean archive with {} table {}", helper, extractTable);
		prepareDummyTable(extractTable);

		List<Map<String, Object>> resultBefore = helper.getWrappedHelper().getTemplate().queryForList("SELECT * FROM " + extractTable);
		for(Map<String, Object> rowBefore: resultBefore){
			logger.info("Row before: {}", rowBefore);
		}
		
		File dumpFile = File.createTempFile("quarkit_sqldump", null);
		helper.extractInto(extractTable, dumpFile);
		
		logger.info("Dumped SQL to {}", dumpFile);
		
		CSVParser parser = new CSVParser(new FileReader(dumpFile), CSVFormat.DEFAULT.withFirstRecordAsHeader());
		List<CSVRecord> records = parser.getRecords();
		parser.close();
		for(int i=0; i<records.size(); i++){
			Assert.assertEquals(records.get(i).toMap().toString(), resultBefore.get(i).toString());
		}
		
	}

	
	
}
