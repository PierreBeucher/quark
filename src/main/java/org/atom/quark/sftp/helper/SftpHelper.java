package org.atom.quark.sftp.helper;

import java.io.File;
import org.atom.quark.core.helper.Helper;
import org.atom.quark.core.result.HelperResult;
import org.atom.quark.sftp.context.SftpContext;

/**
 * The SftpHelper is used to perform various test actions involving SFTP servers and files.
 * @author Pierre Beucher
 *
 */
public interface SftpHelper extends Helper<SftpContext> {

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
	public void context(SftpContext context);
	
	/**
	 * Set the host to be used by this Helper. Overwrit any previously defined host.
	 * @param host
	 */
	public void host(String host);
	
	/**
	 * Set the port to be used by this Helper. Overwrite any previously defined port.
	 * @param port
	 */
	public void port(int port);
	
	/**
	 * Set the login to be used by this Helper.  Overwrite any previously defined login.
	 * @param login
	 */
	public void login(String login);
	
	/**
	 * Set the password to be used by this Helper.  Overwrite any previously defined password.
	 * @param password
	 */
	public void password(String password);
	
	/**
	 * Set the privateKey file to be used by this Helper.  Overwrite any previously defined privateKey.
	 * @param privateKey
	 */
	public void privateKey(String privateKey);
	
	/**
	 * Set the private key password to be used by this Helper.  Overwrite any previously defined private key password.
	 * @param privateKeyPassword
	 */
	public void privateKeyPassword(String privateKeyPassword);
	
	/**
	 * Connect to the SFTP server using the available context. Return result as
	 * success or failure.
	 * @return result as success or failure
	 * @throws Exception
	 */
	public HelperResult<Boolean> connect() throws Exception;
	
	/**
	 * Disconnect the underlying client from the SFTP server. Will return
	 * success if the client is correctly disconnected. If the client
	 * is not connected when calling, failure is returned.
	 * @return result as success or failure
	 * @throws Exception
	 */
	public HelperResult<String> disconnect() throws Exception;
	
	/**
	 * Upload a file on the server in the specified directory, using the default client upload mode.
	 * Return result as the final filename being uploaded.
	 * @param f
	 * @param dest
	 * @return Result as filename uploaded
	 * @throws Exception 
	 */
	public HelperResult<String> upload(File file, String dest) throws Exception;
	
	/**
	 * Upload a file on the server in the specified directory, using the specified upload mode.
	 * Return result as the final filename being uploaded.
	 * @param f
	 * @param dest
	 * @param mode
	 * @return Result as filename uploaded
	 * @throws Exception
	 */
	public HelperResult<String> upload(File file, String dest, int mode) throws Exception;

}
