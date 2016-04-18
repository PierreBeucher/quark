package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.regex.Pattern;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.result.BaseExpectingHelperResult;
import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.SftpException;

/**
 * The SftpHelper is used to perform various test actions involving SFTP servers and files.
 * @author Pierre Beucher
 *
 */
public interface SftpHelper extends Helper {

	/**
	 * Defautl checksum algorithm used when comparing files
	 */
	public static final String DEFAULT_CHECKSUM_ALGORITHM = "MD5";
	
	/**
	 * Overwrite upload mode
	 */
	public static final int MODE_OVERWRITE = 1;
	
	/**
	 * Append upload mode
	 */
	public static final int MODE_APPEND = 2;
	
	/**
	 * Resume upload mode
	 */
	public static final int MODE_RESUME = 3;
	
	SftpContext getContext();
	
	/**
	 * Set the context to be used by this Helper. Overwrite previosuly defined context.
	 * @param context context to use
	 */
	public SftpHelper context(SftpContext context);
	
	/**
	 * Set the host to be used by this Helper. Overwrit any previously defined host.
	 * @param host
	 * @return 
	 */
	public SftpHelper host(String host);
	
	/**
	 * Set the port to be used by this Helper. Overwrite any previously defined port.
	 * @param port
	 */
	public SftpHelper port(int port);
	
	/**
	 * Set the login to be used by this Helper.  Overwrite any previously defined login.
	 * @param login
	 */
	public SftpHelper login(String login);
	
	/**
	 * Set the password to be used by this Helper.  Overwrite any previously defined password.
	 * @param password
	 */
	public SftpHelper password(String password);
	
	/**
	 * Set the privateKey file to be used by this Helper.  Overwrite any previously defined privateKey.
	 * @param privateKey
	 */
	public SftpHelper privateKey(String privateKey);
	
	/**
	 * Set the private key password to be used by this Helper.  Overwrite any previously defined private key password.
	 * @param privateKeyPassword
	 */
	public SftpHelper privateKeyPassword(String privateKeyPassword);
	
	/**
	 * Set an option to be used by this Helper's ssh client
	 * @param option
	 * @return
	 */
	public SftpHelper addOption(String option, Object value);
	
	/**
	 * Connect to the SFTP server using the available context. Return result as
	 * success or failure.
	 * @return result as success or failure
	 * @throws Exception
	 */
	public boolean connect() throws Exception;
	
	/**
	 * Disconnect the underlying client from the SFTP server. Will return
	 * success if the client is correctly disconnected. If the client
	 * is not connected when calling, failure is returned.
	 * @return result as success or failure
	 * @throws Exception
	 */
	public boolean disconnect() throws Exception;
	
	/**
	 * Upload content using a file
	 * @param f
	 * @param dest
	 * @return result as filename uploaded
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 */
	public boolean upload(File file, String dest) throws SftpException, FileNotFoundException;
	
	/**
	 * Upload content using a file. 
	 * @param f
	 * @param dest
	 * @param mode
	 * @return result as filename uploaded
	 * @throws SftpException
	 * @throws FileNotFoundException 
	 */
	public boolean upload(File file, String dest, int mode) throws SftpException, FileNotFoundException;
	
	/**
	 * Upload content using a stream.
	 * @param stream streamed data to upload
	 * @param dest path to the destination
	 * @param mode writing mode, either APPEND, OVERWRITE or RESUME
	 * @return result as filename uploaded
	 * @throws SftpException 
	 */
	public boolean upload(InputStream stream, String dest, int mode) throws SftpException;
	
	/**
	 * Upload content using a stream, overwriting existing destination.
	 * @param stream streamed data to upload
	 * @param dest path to the destination
	 * @return result as filename uploaded
	 * @throws SftpException 
	 */
	public boolean upload(InputStream stream, String dest) throws SftpException;
	
	/**
	 * List directories entry in the given directory, including . and .. if they exists.
	 * TODO using JSch component bind our interface to its JSch implementation!
	 * @param dir directory to list 
	 * @return Vector of entries
	 * @throws SftpException 
	 */
	public Vector<LsEntry> listDirectories(String dir) throws SftpException;
	
	/**
	 * List files entry in the given directory
	 * TODO using JSch component bind our interface to its JSch implementation!
	 * @param dir
	 * @return
	 * @throws SftpException 
	 */
	public Vector<LsEntry> listFiles(String dir) throws SftpException;
	
	/**
	 * List files in the given directory with a filename matching the given pattern
	 * @param dir
	 * @param pattern
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listFiles(String dir, Pattern pattern) throws SftpException;
	
	/**
	 * List both files and directories from the given directory (except .. and .)
	 * TODO using JSch component bind our interface to its JSch implementation!
	 * @param dir
	 * @return
	 * @throws SftpException 
	 */
	public Vector<LsEntry> list(String dir) throws SftpException;
	
	/**
	 * Get an InputStream for the given destination file
	 * @return result as InputStream
	 * @throws SftpException 
	 */
	public InputStream getInputStream(String dest) throws SftpException;
	
	/**
	 * Check whether a directory contains a file or not. The
	 * exact filename is matched. 
	 * @param dir
	 * @param filename
	 * @return result as found filename
	 * @throws SftpException 
	 */
	public boolean containsFile(String parentdir, String filename) throws SftpException;
	
	/**
	 * Check whether a directory contains a directory
	 * @param parentdir the parent directory
	 * @param dirname directory name looked for
	 * @return
	 * @throws SftpException
	 */
	public boolean containsDirectory(String parentdir, String dirname) throws SftpException;
	
	/**
	 * Check whether a directory contains files matching the given pattern.
	 * Success if one or more file matching the given pattern is found. The
	 * result returned contains a Vector of entry of files found matching
	 * the pattern.
	 * @param dir
	 * @param pattern
	 * @return result as list of found filenames
	 * @throws SftpException 
	 */
	public BaseHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern) throws SftpException;
	
	/**
	 * Waiter version for the containsFile() function. Check whether a directory contains files matching the given pattern.
	 * The check is perform periodically until the given timeout is reached. Success if one or more file matching the given pattern is found. The
	 * result returned contains a Vector of entry of files found matching
	 * the pattern.
	 * @param dir
	 * @param pattern
	 * @param timeout waiter check timeout
	 * @param period waiter check period
	 * @return result as list of found filenames
	 * @throws Exception 
	 * @throws SftpException 
	 */
	public BaseHelperResult<Vector<LsEntry>> waitForContainsFile(String dir, Pattern pattern, long timeout, long period) throws Exception;
	
	/**
	 * Check whether a directory contains files matching the given pattern. 
	 * Success if the specified number of file matching the given pattern is found. The
	 * result returned contains a Vector of entry of files found matching
	 * the pattern.
	 * @param dir
	 * @param pattern
	 * @return result as list of found filenames
	 * @throws SftpException 
	 */
	public BaseHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern, int count) throws SftpException;
	
	/**
	 * Waiter version of containsFile(). Check whether a directory contains files matching the given pattern. 
	 * The check is perform periodically until the given timeout is reached. 
	 * Success if the specified number of file matching the given pattern is found. The
	 * result returned contains a Vector of entry of files found matching the pattern.
	 * @param dir
	 * @param pattern
	 * @param timeout waiter check timeout
	 * @param period waiter check period
	 * @return result as list of found filenames
	 * @throws SftpException 
	 */
	public BaseHelperResult<Vector<LsEntry>> waitForContainsFile(String dir, Pattern pattern, int count,
			long timeout, long period) throws Exception;
	
	/**
	 * Retrieve the MD5 checksum for the given file on SFTP server. 
	 * @param dest file for which checksum is to be calculated
	 * @return checksum as String
	 * @throws SftpException 
	 * @throws IOException 
	 */
	public String getChecksum(String dest) throws IOException, SftpException;
	
	/**
	 * Compare checksum of the given source and destination to ensure they correspond.
	 * Success if both checksum match. Expected is source checksum as String, actual is destination
	 * checksum as String. 
	 * @param src data which will be digested to produce expected value
	 * @param dest data which will be digested as actual value
	 * @return result as expected and actual digested String 
	 * @throws SftpException 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public BaseExpectingHelperResult<String, String> compareChecksum(InputStream src, String dest) throws NoSuchAlgorithmException, IOException, SftpException;
	
	/**
	 * Compare checksum of the given source and destination to ensure they correspond.
	 * @param src source file to digest
	 * @param dest destination file to digest
	 * @return result as expected and actual digest String
	 * @throws SftpException 
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	public  BaseExpectingHelperResult<String, String> compareChecksum(File src, String dest) throws NoSuchAlgorithmException, IOException, SftpException;

}
