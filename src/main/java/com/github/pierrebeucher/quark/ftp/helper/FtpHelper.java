package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.core.lifecycle.LifecycleManager;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpHelper extends AbstractHelper<FtpContext> implements Initialisable, Disposable{
	
	private FTPClient ftpClient;
	
	private LifecycleManager lifecycleManager;
	
	public FtpHelper() {
		this(new FtpContext());
	}
	
	public FtpHelper(FtpContext context) {
		super(context);
		this.lifecycleManager = new LifecycleManager();
	}
	
	public FtpHelper context(FtpContext ctx){
		setContext(ctx);
		return this;
	}
	
	public FtpHelper login(String login){
		context.setLogin(login);
		return this;
	}
	
	public FtpHelper password(String password){
		context.setPassword(password);
		return this;
	}
	
	public FtpHelper host(String host){
		context.setHost(host);
		return this;
	}
	
	public FtpHelper port(int port){
		context.setPort(port);
		return this;
	}
	
	public void dispose() {
		lifecycleManager.dispose();
		doDispose();
	}

	public boolean isDisposed() {
		return lifecycleManager.isDisposed();
	}

	public void initialise() throws InitialisationException {
		lifecycleManager.initialise();
		doInitialise();
	}

	public boolean isInitialised() {
		return lifecycleManager.isInitialised();
	}

	protected void doInitialise() throws InitialisationException {
		FTPClient _ftpClient = null;
		try{
			_ftpClient = new FTPClient();
			_ftpClient.connect(context.getHost(), context.getPort());
			_ftpClient.enterLocalPassiveMode();
			if(!_ftpClient.login(context.getLogin(), context.getPassword())){
				throw new FtpHelperException("Connected to FTP but login failed for " + context);
			};
			ftpClient = _ftpClient;
			
		} catch(IOException e){
			try{
				if(_ftpClient != null){
					_ftpClient.logout();
					_ftpClient.disconnect();
				}
			} catch (IOException cleaningException){
				logger.error("Initialisation error for FTPClient but failed to clean up,"
						+ " an FTP connection may still be active: " + e.getMessage(), e);
			}
			throw new InitialisationException(e);
		} 
	}

	protected void doDispose() {
		try{
			if(ftpClient != null){
				ftpClient.disconnect();
			}
		} catch(IOException e){
			logger.error("Disposing error: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean isReady() {
		return !StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0
				&& !StringUtils.isEmpty(context.getLogin());
	}
	
//	/**
//	 * Instanciate, connect and login the underlying FTP client used by this Helper.
//	 * Same as calling connect() and login() in this order. Any existing client will be
//	 * reinitialized.
//	 * @throws IOException 
//	 * @throws SocketException 
//	 * @throws FtpHelperException
//	 */
//	public void initialise() throws SocketException, IOException{
//		initClient();
//		connect();
//		boolean success = login();
//		if(!success){
//			throw new FtpHelperException("Login failed for " + context);
//		}
//	}
	
//	public void connect() throws IOException{
//		if(ftpClient == null){
//			initClient();
//		}
//		
//		try{
//			ftpClient.connect(context.getHost(), context.getPort());
//			ftpClient.enterLocalPassiveMode();
//		} catch(IOException e){
//			if(ftpClient != null){
//				ftpClient.disconnect();
//			}
//			throw e;
//		}
//	}
//	
//	public boolean login() throws IOException{
//		return ftpClient.login(context.getLogin(), context.getPassword());
//	}
//	
//	public boolean logout() throws IOException{
//		return ftpClient.logout();
//	}
//	
//	public void disconnect() throws IOException{
//		ftpClient.disconnect();
//	}
	
	/**
	 * List files in the given directory
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public FTPFile[] listFiles(String parent) throws IOException{
		return ftpClient.listFiles(parent);
	}
	
	/**
	 * List directories in the given directory
	 * @param parent
	 * @return
	 * @throws IOException
	 */
	public FTPFile[] listDirectories(String parent) throws IOException{
		return ftpClient.listDirectories(parent);
	}
	
	/**
	 * Upload the content of the given file ot the given destination.
	 * The file is read using the default system charset. 
	 * @param file
	 * @param dest
	 * @return true if successfully completed, false if not.
	 * @throws IOException 
	 */
	public void upload(File file, String dest) throws IOException{
		upload(new FileInputStream(file), dest);
	}
	
	/**
	 * Upload the content of the given stream on the given destination 
	 * @param content
	 * @param dest
	 * @return true if successfully completed, false if not.
	 * @throws IOException
	 */
	public void upload(InputStream content, String dest) throws IOException{
		boolean success = ftpClient.storeFile(dest, content);
		if(!success){
			throw new FtpHelperException("Upload of " + dest + " failed.");
		}
	}

	/**
	 * Check if the specified file exists.
	 * @param path
	 * @return true if path is a file
	 * @throws IOException 
	 */
	public boolean isFile(String path) throws IOException{
		return ftpClient.listFiles(path).length == 1;
	}
	
	/**
	 * Check if the specified file exists.
	 * @param path
	 * @return true if path is a file
	 * @throws IOException 
	 */
	public boolean isDirectory(String parent) throws IOException{
		return ftpClient.changeWorkingDirectory(parent);
	}
	
	/**
	 * Create the directory if it does not already exists. Does nothing
	 * if directory already exists.
	 * @param path
	 * @throws IOException
	 */
	public void makeDirectory(String path) throws IOException{
		if(isDirectory(path)) return;
		
		boolean success = ftpClient.makeDirectory(path);
		if(!success){
			throw new FtpHelperException("Directory creation of " + path + " failed.");
		}
	}

	public boolean rename(String from, String to) throws IOException {
		return ftpClient.rename(from, to);
	}
	
	/**
	 * Move the file from the given path to its destination. 
	 * @param from
	 * @param to
	 * @throws IOException
	 * @throws FtpHelperException 
	 */
	public void move(String from, String to) throws IOException, FtpHelperException {
		boolean result = ftpClient.rename(from, to);
		if(!result){
			throw new FtpHelperException("Cannot move " + from + " to "+ to +
					" (" + ftpClient.getReplyCode() + ": " + ftpClient.getReplyString() + ")");
		}
	}
}
