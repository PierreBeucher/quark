package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpHelper extends AbstractLifecycleHelper<FtpContext> implements Initialisable, Disposable{
	
	private FTPClient ftpClient;
	
	public FtpHelper() {
		this(new FtpContext());
	}
	
	public FtpHelper(FtpContext context) {
		super(context);
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
	
	@Override
	public void dispose() {
		lifecycleManager.dispose();
		doDispose();
	}

	@Override
	public boolean isDisposed() {
		return lifecycleManager.isDisposed();
	}

	@Override
	public void initialise() throws InitialisationException {
		lifecycleManager.initialise();
		doInitialise();
	}

	@Override
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
	
	/**
	 * List files in the given directory
	 * @param dest
	 * @return
	 * @throws FtpHelperException
	 */
	public FTPFile[] listFiles(String parent) throws FtpHelperException{
		try {
			return ftpClient.listFiles(parent);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}
	
	/**
	 * List directories in the given directory
	 * @param parent
	 * @return
	 * @throws FtpHelperException
	 */
	public FTPFile[] listDirectories(String parent) throws FtpHelperException{
		try {
			return ftpClient.listDirectories(parent);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}
	
	/**
	 * Upload the content of the given file ot the given destination.
	 * The file is read using the default system charset. 
	 * @param file
	 * @param dest
	 * @return true if successfully completed, false if not.
	 * @throws FileNotFoundException 
	 * @throws FtpHelperException 
	 */
	public void upload(File file, String dest) throws FtpHelperException, FileNotFoundException{
		upload(new FileInputStream(file), dest);
	}
	
	/**
	 * Upload the content of the given stream on the given destination 
	 * @param content
	 * @param dest
	 * @return true if successfully completed, false if not.
	 * @throws FtpHelperException
	 */
	public void upload(InputStream content, String dest) throws FtpHelperException{
		boolean success;
		try {
			success = ftpClient.storeFile(dest, content);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
		
		if(!success){
			throw new FtpHelperException("Upload of " + dest + " failed.");
		}
	}

	/**
	 * Check if the specified file exists.
	 * @param path
	 * @return true if path is a file
	 * @throws FtpHelperException 
	 */
	public boolean isFile(String path) throws FtpHelperException{
		return listFiles(path).length == 1;
	}
	
	/**
	 * Check if the specified file exists.
	 * @param path
	 * @return true if path is a file
	 * @throws FtpHelperException 
	 */
	public boolean isDirectory(String parent) throws FtpHelperException{
		try {
			return ftpClient.changeWorkingDirectory(parent);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}
	
	/**
	 * Create the directory if it does not already exists. Does nothing
	 * if directory already exists.
	 * @param path
	 * @throws FtpHelperException
	 */
	public void makeDirectory(String path) throws FtpHelperException{
		if(isDirectory(path)) return;
		
		boolean success;
		try {
			success = ftpClient.makeDirectory(path);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
		
		if(!success){
			throw new FtpHelperException("Directory creation of " + path + " failed.");
		}
	}

	/**
	 * Rename the given file or folder
	 * @param from
	 * @param to
	 * @return
	 * @throws FtpHelperException
	 */
	public boolean rename(String from, String to) throws FtpHelperException {
		try {
			return ftpClient.rename(from, to);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}
	
	/**
	 * Move the file from the given path to its destination. 
	 * @param from
	 * @param to
	 * @throws FtpHelperException 
	 */
	public void move(String from, String to) throws FtpHelperException {
		boolean result = rename(from, to);
		if(!result){
			throw new FtpHelperException("Cannot move " + from + " to "+ to +
					" (" + ftpClient.getReplyCode() + ": " + ftpClient.getReplyString() + ")");
		}
	}
}
