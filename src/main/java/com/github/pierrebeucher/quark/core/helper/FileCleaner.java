package com.github.pierrebeucher.quark.core.helper;

/**
 * <p><code>FileCleaner</code> is capable of cleaning directories within its current context.
 * It guarantees a safe cleaning implementation, <b>data are not deleted
 * permanently</b> and are either archived or backed-up. Depending on the
 * implementation, various methods may be applied to ensure this safety.</p>
 * @author pierreb
 *  
 */
public interface FileCleaner extends SafeCleaner {
	
	/**
	 * Default archive directory name used when cleaning a directory
	 * with {@link #clean(String)}
	 */
	public static final String DEFAULT_CLEAN_DIR = ".quark_trash";
	
	public static final String DEFAULT_CLEAN_DIR_DATE_FORMAT = "yyyyMMddHHmmss";
	
	/**
	 * <p>Clean the given directory from files (existing directories are left unchanged).
	 * Data are cleaned safely, no data is deleted permanently, and any cleaned data 
	 * is either by archived or backed-up. </p>
	 * <p>Cleaned data are archived into a sub-directory of the directory to clean.
	 * By default, sub-directory name is the concatenation of {@link #DEFAULT_CLEAN_DIR}
	 * and current data formatted using {@link #DEFAULT_CLEAN_DIR_DATE_FORMAT}. 
	 * For example, cleaning <i>/home/user/toclean</i> into 
	 * <i>/home/user/toclean/.quark_trash/20160501225859</i>.</p>
	 * @param parameter to precise which part of the context to clean
	 * @return absolute path to effective archive directory
	 * @throws HelperException an exception occured during cleaning, of cleaning safety cannot be guaranteed.
	 */
	public String clean(String dir);
	
	/**
	 * Clean the dirToClean by moving any files to archiveDir. 
	 * archiveDir is assumed to be a writable directory.
	 * @param dirToClean
	 * @param archiveDir
	 */
	public void clean(String dirToClean, String archiveDir);
	
//	/**
//	 * <p>Clean the dirToClean by moving any files in this directory
//	 * to a local sub-directory of this directory. The sub-directory
//	 * will be named using a concatenation of dirToClean, {@value #DEFAULT_CLEAN_DIR}
//	 * and the current Date formatted with {@value #DEFAULT_CLEAN_DIR_DATE_FORMAT}.
//	 * </p>
//	 * <p>For example, calling cleanToLocalDir("/var/myapp/data") will archive files
//	 * to "/var/myapp/data/{@value #DEFAULT_CLEAN_DIR}/20160120120101." </p>
//	 * <p>Calling this method requires read/write on dirToClean. The archive 
//	 * directory will be created if it does not already exists. </p>
//	 * @param dirToClean directory to clean
//	 * @return path to the newly created archive dir
//	 * @deprecated use {@link #clean(String)} instead
//	 */
//	public String clean(String dirToClean);

}
