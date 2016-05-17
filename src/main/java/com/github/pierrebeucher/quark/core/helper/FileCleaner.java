package com.github.pierrebeucher.quark.core.helper;

/**
 * Cleaner managing files. 
 * @author pierreb
 *
 */
public interface FileCleaner extends Cleaner {
	
	/**
	 * Default archive directory name used when cleaning a directory
	 * with {@link #cleanToLocalDir(String)}
	 */
	public static final String DEFAULT_CLEAN_DIR = ".quark_trash";
	
	public static final String DEFAULT_CLEAN_DIR_DATE_FORMAT = "yyyyMMddHHmmss";
	
	/**
	 * Clean the dirToClean by moving any files to archiveDir. 
	 * archiveDir is assumed to be a writable directory.
	 * @param dirToClean
	 * @param archiveDir
	 */
	public void clean(String dirToClean, String archiveDir);
	
	/**
	 * <p>Clean the dirToClean by moving any files in this directory
	 * to a local sub-directory of this directory. The sub-directory
	 * will be named using a concatenation of dirToClean, {@value #DEFAULT_CLEAN_DIR}
	 * and the current Date formatted with {@value #DEFAULT_CLEAN_DIR_DATE_FORMAT}.
	 * </p>
	 * <p>For example, calling cleanToLocalDir("/var/myapp/data") will archive files
	 * to "/var/myapp/data/{@value #DEFAULT_CLEAN_DIR}/20160120120101." </p>
	 * <p>Calling this method requires read/write on dirToClean. The archive 
	 * directory will be created if it does not already exists. </p>
	 * @param dirToClean directory to clean
	 * @return path to the newly created archive dir
	 */
	public String cleanToLocalDir(String dirToClean);

}
