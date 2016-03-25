package org.atom.quark.utils.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * SFTP client interface wrapping a JSch instance.
 * 
 * @author Pierre Beucher
 *
 */
public class JSchSftpClient {
	
	public static final String CHANNEL_SFTP = "sftp";
	public static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
	public static final int DEFAULT_PORT = 22;
	
	/**
	 * Default timeout used when connecting a session
	 */
	public static final int DEFAULT_SESSION_CONNECT_TIMEOUT = 60000;
	
	/**
	 * Default timeout used when connecting a channel
	 */
	public static final int DEFAULT_CHANNEL_CONNECT_TIMEOUT = 60000;
	
	/*
	 * Connection parameters
	 */
	private String host;
	private int port;
	private String login;
	
	private String password;
	
	private int sessionConnectTimeout;
	private int channelConnectTimeout;
	
	private JSch jsch;
	private Session session;
	private ChannelSftp channelSftp;
	
	/**
	 * Default constructor using port 22.
	 * Equivalent to SftpClient(host, 22). Require to call login() before use.
	 * @param login
	 * @param host
	 * @param port
	 * @throws JSchException
	 */
	public JSchSftpClient(String login, String host) throws JSchException {
		this(login, host, DEFAULT_PORT);
	}

	
	/**
	 * Create an SFTP client for the given host and port. Host key checking
	 * may be strict or not. 
	 * @param host host for this client
	 * @param port port for this client
	 * @param strictHostKeyChecking true to enforce host key checking, false otherwise
	 * @throws JSchException 
	 */
	public JSchSftpClient(String login, String host, int port) throws JSchException {
		super();
		
		this.login = login;
		this.host = host;
		this.port = port;
		this.jsch = new JSch();
		
		this.channelConnectTimeout = DEFAULT_CHANNEL_CONNECT_TIMEOUT;
		this.sessionConnectTimeout = DEFAULT_SESSION_CONNECT_TIMEOUT;

	}
	
	/**
	 * Set the password for password authentication to be used.
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * Add an identity to the wrapped JSch instance.
	 * @param privateKeyFile path to the private key file
	 * @throws JSchException 
	 */
	public void addIdentify(String privateKeyFile) throws JSchException {
		jsch.addIdentity(privateKeyFile);
	}
	
	/**
	 * Add an identity to the wrapped JSch instance.
	 * @param privateKeyFile path to the private key file
	 * @param privateKeyPassphrase key passphrase
	 * @throws JSchException 
	 */
	public void addIdentity(String privateKeyFile, String privateKeyPassphrase) throws JSchException{
		jsch.addIdentity(privateKeyFile, privateKeyPassphrase);
	}

	/**
	 * <p>Connect and open an SFTP channel. Uses the password and key defined previously.</p>
	 * <p>Calling this method will disconnect and reset any previously existing session.</p>
	 * @throws JSchException 
	 * 
	 */
	public void connect() throws JSchException{
		createNewSession();
		connectCurrentSession();
		initChannelSftp();
	}
	
	/**
	 * Create a new session, disconnecting the previously existing one if needed
	 * @return
	 * @throws JSchException
	 */
	private void createNewSession() throws JSchException{
		if(session != null && session.isConnected()){
			session.disconnect();
		}
		session = jsch.getSession(login, host, port);
	}
	
	private void connectCurrentSession() throws JSchException{
		session.setPassword(password);
		session.connect(sessionConnectTimeout);
	}
	
	private void initChannelSftp() throws JSchException{
		Channel channel = session.openChannel(CHANNEL_SFTP);
		channel.connect(channelConnectTimeout);
		channelSftp = (ChannelSftp) channel;
	}
	
	/**
	 * Check whether or not the channel SFTP used by this client is
	 * created and connected. 
	 * @return true if connected, false if channel is null or not connected
	 */
	public boolean isChannelSftpConnected(){
		return this.channelSftp != null && this.channelSftp.isConnected();
	}
	
	/**
	 * <p>Copy a file through this client SFTP channel. Any existing file is overwritten,
	 * using the ChannelSftp.OVERWRITE mode.</p>
	 * @param file file to be copied
	 * @param dest path of the destination file or folder. May be absolute or relative.
	 * @throws SftpException
	 * @throws IOException 
	 */
	public void uploadFile(File file, String dest) throws SftpException, IOException{
		uploadFile(file, dest, ChannelSftp.OVERWRITE);
	}
	
	/**
	 * <p>Copy a file through this client SFTP channel, using given mode. Mode
	 * can be specified using the ChannelSftp class. </p>
	 * <p>Example: uploadFile(file, dest, ChannelSftp.OVERWRITE); </p>
	 * TODO add various methods using String, InputStream, etc.
	 * @param file file to be copied
	 * @param dest path of the destination file or folder. May be absolute or relative.
	 * @param mode writing mode
	 * @throws SftpException
	 * @throws IOException 
	 */
	public void uploadFile(File file, String dest, int mode) throws SftpException, IOException{
		FileInputStream stream = null;
		try{
			stream = new FileInputStream(file);
			channelSftp.put(stream, dest, ChannelSftp.OVERWRITE);
		} finally {
			if(stream != null){
				stream.close();
			}
		}
	}
	
	/**
	 * Remove the distant file represented by the given String
	 * @param dest file to remove
	 * @throws SftpException
	 */
	public void removeFile(String dest) throws SftpException{
		channelSftp.rm(dest);
	}

	
//	/**
//	 * Check if a file can be found in a directory
//	 * @param filePathRegex
//	 * @return
//	 * @throws SftpException
//	 */
//	public String checkFileInDirectory(String directory, String filePathRegex) throws SftpException{
////		File file = new File(directory, filePathRegex);
//		Pattern pattern = Pattern.compile(filePathRegex); //pattern used to verify the read file is the one expected
////		String parentDir = ".";
////		if(file.getParent() != null){
////			//workaround for Windows when connecting on Unix, backslash not being recognized... TODO maybe use Commons Utils to retrieve parent instead of File?
////			parentDir = file.getParent().replace("\\", "/");
////		}
//		
//		String foundFilename = null;
//		@SuppressWarnings("unchecked")
//		Vector<ChannelSftp.LsEntry> sftpEntryVector =(Vector<ChannelSftp.LsEntry>) channelSftp.ls(directory);
//		for(ChannelSftp.LsEntry e : sftpEntryVector){
//			Matcher matcher = pattern.matcher(e.getFilename());
//			if(matcher.matches()){	
//				logger.debug("Found {} matching {}", e.getFilename(), filePathRegex);
//				foundFilename = e.getFilename();
//				break;
//			} 
//		}
//		return foundFilename;
//	}
	
//	public void removeFilesFromDirectory(String directoryPath) throws SftpException{
//		if(logger.isDebugEnabled()){
//			logger.debug("cd to " + directoryPath);
//		}
//		
//		channelSftp.cd(directoryPath);
//		
//		for(String fileName : listFiles(directoryPath)){
//			if(logger.isDebugEnabled()){
//				logger.debug("Try to remove " + fileName);
//			}
//			channelSftp.rm(fileName);
//		}
//	}
	
	/**
	 * Return the files from the given path
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	public List<LsEntry> listFiles(String path) throws SftpException{
		return listDirectory(path, true, false);
	}
	
	/**
	 * Return directories (except . and ..) from the given path
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	public List<LsEntry> listDirectories(String path) throws SftpException{
		return listDirectory(path, false, true);
	}

	/**
	 * Return the directories and/or files found in the given path
	 * @param path
	 * @param includeFiles true to include files
	 * @param includeDirectories true to include directories (except . and ..), false to ignore directories
	 * @return
	 * @throws SftpException
	 */
	public List<LsEntry> listDirectory(String path, boolean includeFiles, boolean includeDirectories) throws SftpException{
		
		@SuppressWarnings("unchecked")
		Vector<LsEntry> entries = channelSftp.ls(path);
		
		//filter the entries depending on parameters
		List<LsEntry> result = new ArrayList<LsEntry>();
		for(LsEntry e : entries){
		
			if(includeDirectories && e.getAttrs().isDir()){
				if(!".".equals(e.getFilename()) && !"..".equals(e.getFilename())){
					result.add(e);
				}
			}
			
			if(includeFiles && !e.getAttrs().isDir()){
				result.add(e);
			}
		}
		
		return result;	
	}
	
	/**
	 * Retrieve the stats for the given path
	 * @param path
	 * @return 
	 * @throws SftpException
	 */
	public SftpATTRS getStat(String path) throws SftpException{
		return channelSftp.stat(path);
	}
	
	/**
	 * Disconnect both the session and sftp channel managed
	 * by this client.
	 */
	public void disconnect(){
		if (channelSftp != null)
        {
            channelSftp.disconnect();
        }
        if ((session != null) && session.isConnected())
        {
            session.disconnect();
        }
	}


	/**
	 * The underlying JSch used by this client. You can
	 * use this method to perform operations not available
	 * directly through this client interface. 
	 * @return The underlying JSch used by this client.
	 */
	public JSch getJsch() {
		return jsch;
	}

}
