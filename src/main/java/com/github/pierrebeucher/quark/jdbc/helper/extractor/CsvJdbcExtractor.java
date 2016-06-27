package com.github.pierrebeucher.quark.jdbc.helper.extractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Deque;
import java.util.LinkedList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.github.pierrebeucher.quark.core.helper.AbstractWrapperHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.DisposeException;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.jdbc.context.JdbcContext;
import com.github.pierrebeucher.quark.jdbc.helper.JdbcHelperException;
import com.github.pierrebeucher.quark.jdbc.helper.SpringJdbcHelper;

/**
 * <p>Extractor using a CSV file to export queried data. This Extractor will
 * query data from specified database or table, and write result into a CSV file.</p>
 * <p>Note: depending on your DMBS systems, certain data may not be exported properly.
 * The following system have known issues:
 * <ul>
 * <li>MySQL: Blob values are treated as bye arrays and are not converted.</li>
 * </ul>
 * 
 * @author pierreb
 *
 */
public class CsvJdbcExtractor extends AbstractWrapperHelper<JdbcContext, SpringJdbcHelper>
		implements Initialisable, Disposable {

	public CsvJdbcExtractor(JdbcContext context, SpringJdbcHelper h) {
		super(context, h);
	}

	public CsvJdbcExtractor(JdbcContext context) {
		super(context, new SpringJdbcHelper(context));
	}
	
	@Override
	public void dispose() throws DisposeException {
		helper.dispose();
	}

	@Override
	public boolean isDisposed() {
		return helper.isDisposed();
	}

	@Override
	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	@Override
	public boolean isInitialised() {
		return helper.isInitialised();
	}
	
	/**
	 * <p>Generate a dump of the given table into the given dump file using the DEFAULT CSVFormat. </p>
	 * @param dumpFile
	 * @param queryResult
	 */
	public void extractInto(String table, File dumpFile) {
		extractInto(table, dumpFile, CSVFormat.DEFAULT);
	}
	
	/**
	 * <p>Generate a dump of the given table into the given dump file using the given CSVFormat. </p>
	 * @param table
	 * @param dumpFile
	 * @param format
	 */
	public void extractInto(String table, File dumpFile, CSVFormat format){
		try{
			int count = getCount(table);
			if(count == 0){
				logger.info("Table {} is empty. No dump to generate, {} left unmodified.", table, dumpFile);
				return;
			}
			
			ResultSet rs = getRecords(table);
			Deque<String> columns = extractColumns(rs);
			FileWriter writer = new FileWriter(dumpFile);
			CSVPrinter csvPrinter = null;
			try{
				csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
				csvPrinter.printRecord(columns); //header
				csvPrinter.printRecords(rs); //values
			} finally {
				if(csvPrinter != null)
					csvPrinter.close();
			}
		} catch(IOException | SQLException e){
			throw new JdbcHelperException(e);
		}
	}
	
	/**
	 * Extract columns from the given result set and return a Deque in the
	 * order data appear in the ResultSet.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Deque<String> extractColumns(ResultSet rs) throws SQLException{
		ResultSetMetaData md = rs.getMetaData();
		LinkedList<String> list = new LinkedList<String>();
		for(int i=1; i<=md.getColumnCount(); i++){
			list.add(md.getColumnName(i));
		}
		return list;
	}
	
	private ResultSet getRecords(String table) throws SQLException{
		Statement statement = helper.getConnection().createStatement();
		return statement.executeQuery("SELECT * FROM " + table);
	}
	
	private int getCount(String table) throws SQLException{
		Statement statement = helper.getConnection().createStatement();
		ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + table);
		if(result.next()){
			return result.getInt(1);
		} else {
			throw new RuntimeException("No result after querying count(*) on " + table);
		}
	}
	
//	/**
//	 * Generate an InputStream represent the INSERT INTO command for the given row.
//	 * Each Object in the row is converted into a InputStream depending on its type.
//	 * @param row
//	 * @return
//	 * @throws SQLException 
//	 */
//	private InputStream generateInsertCommandSream(String table, SortedMap<String, Object> sortedRow) throws SQLException{
//		String columns = "INSERT INTO " + table +  " (" + StringUtils.join(sortedRow.keySet(), ',') + ")";
//		InputStream columnStream = toStream(columns);
//		
//		//concat everything to form VALUES ('...','...')
//		InputStream finalStream = concat(columnStream, toStream(" VALUES ("));
//		Iterator<Object> it = sortedRow.values().iterator();
//		while(it.hasNext()){
//			Object value = it.next();
//			finalStream = concat(finalStream, toStream("'"));
//			finalStream = concat(finalStream, toStream(value));
//			finalStream = concat(finalStream, toStream("'"));
//			if(it.hasNext()){
//				finalStream = concat(finalStream, toStream(","));
//			}
//		}
//		finalStream = concat(finalStream, toStream(String.format(");%n")));
//		
//		return finalStream;
//	}
	
//	private InputStream concat(InputStream stream1, InputStream stream2){
//		return new SequenceInputStream(stream1, stream2);
//	}
//	
//	private InputStream toStream(Object o) throws SQLException{
//		//logger.info("Converting {} ({}) to stream.", o, o.getClass());
//		if(o == null){
//			return new ByteArrayInputStream(new byte[0]);
//		}
//		if(o instanceof java.sql.Blob){
//			Blob blob = (java.sql.Blob) o;
//			return blob.getBinaryStream();
//		} else if (o instanceof byte[]){
//			return new ByteArrayInputStream((byte[]) o);
//		} else {
//			return new ByteArrayInputStream(o.toString().getBytes());
//		}
//	}
	
	
	

}
