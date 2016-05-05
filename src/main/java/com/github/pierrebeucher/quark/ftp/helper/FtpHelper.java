package com.github.pierrebeucher.quark.ftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpHelper extends AbstractHelper<FtpContext>{

	//private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	public boolean isReady() {
		return !StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0
				&& !StringUtils.isEmpty(context.getLogin());
	}
	
	/**
	 * Instanciate, connect and login the underlying FTP client used by this Helper.
	 * Same as calling connect() and login() in this order. Any existing client will be
	 * reinitialized.
	 * @throws IOException 
	 * @throws SocketException 
	 * @throws FtpHelperException
	 */
	public void init() throws SocketException, IOException{
		initClient();
		connect();
		boolean success = login();
		if(!success){
			throw new FtpHelperException("Login failed for " + context);
		}
	}
	
	public void connect() throws IOException{
		if(ftpClient == null){
			initClient();
		}
		
		try{
			ftpClient.connect(context.getHost(), context.getPort());
			ftpClient.enterLocalPassiveMode();
		} catch(IOException e){
			if(ftpClient != null){
				ftpClient.disconnect();
			}
			throw e;
		}
	}
	
	public boolean login() throws IOException{
		return ftpClient.login(context.getLogin(), context.getPassword());
	}
	
	public boolean logout() throws IOException{
		return ftpClient.logout();
	}
	
	public void disconnect() throws IOException{
		ftpClient.disconnect();
	}
	
	private void initClient() throws SocketException, IOException {
		this.ftpClient = new FTPClient(); 
	}
	
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
}
