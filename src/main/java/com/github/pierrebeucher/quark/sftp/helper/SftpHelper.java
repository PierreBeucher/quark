package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.LifecycleHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.core.result.BaseExpectingHelperResult;
import com.github.pierrebeucher.quark.core.result.BaseHelperResult;
import com.github.pierrebeucher.quark.core.result.ResultBuilder;
import com.github.pierrebeucher.quark.core.waiter.SimpleWaiter;
import com.github.pierrebeucher.quark.core.waiter.Waiter;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class SftpHelper extends AbstractLifecycleHelper<SftpContext>
		implements LifecycleHelper, Initialisable, Disposable{

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
	
	/*
	 * SFTP error codes
	 */
	public static final int ERRID_FILE_ALREADY_EXISTS = 11;
	public static final int ERRID_FAILURE = 4;
	public static final int ERRID_NO_SUCH_FILE = 2;
	
	/**
	 * Default timeout used when connecting a session
	 */
	public static final int DEFAULT_SESSION_CONNECT_TIMEOUT = 60000;

	/**
	 * Default timeout used when connecting a channel
	 */
	public static final int DEFAULT_CHANNEL_CONNECT_TIMEOUT = 60000;

	private static final String CHANNEL_SFTP = "sftp";
	
	/*
	 * Known host file used by JSch
	 */
	private File knownHosts;
	
	/*
	 * Session used to open Sftp Channel
	 */
	private Session session;

	/**
	 * The SFTP channel created once connected. 
	 */
	private ChannelSftp channelSftp;

	/**
	 * Timeout for session connection
	 */
	private int sessionConnectTimeout = DEFAULT_SESSION_CONNECT_TIMEOUT;

	/**
	 * timeout for channel creation
	 */
	private int channelConnectTimeout = DEFAULT_CHANNEL_CONNECT_TIMEOUT;
	
	public SftpHelper() {
		this(new SftpContext());
	}

	/**
	 * Create a new SftpHelper, setting by default $HOME/.ssh/known_hosts as known hosts.
	 * @param context
	 */
	public SftpHelper(SftpContext context) {
		super(context);
		this.knownHosts = new File(System.getProperty("user.home"), "/.ssh/known_hosts");
	}
	
	public SftpHelper context(SftpContext context) {
		setContext(context);
		return this;
	}

	public SftpHelper host(String host) {
		context.setHost(host);
		return this;
	}

	public SftpHelper port(int port) {
		context.setPort(port);
		return this;
	}

	public SftpHelper login(String login) {
		context.getAuthContext().setLogin(login);
		return this;
	}

	public SftpHelper password(String password) {
		context.getAuthContext().setPassword(password);
		return this;
	}

	public SftpHelper privateKey(String privateKey) {
		context.getAuthContext().setPrivateKey(privateKey);
		return this;
	}
	
	public SftpHelper privateKeyPassword(String privateKeyPassword) {
		getContext().getAuthContext().setPrivateKeyPassphrase(privateKeyPassword);
		return this;
	}
	
	public SftpHelper addOption(String option, Object value){
		context.getOptions().put(option, value);
		return this;
	}

	@Override
	public String toString() {
		return getContext().toString();
	}
	
	/**
	 * <code>SftpHelper</code> ready when initialised. Same as calling {@link #isInitialised()}.
	 */
	@Override
	public boolean isReady() {
		return isInitialised();
	}
	
	@Override
	public boolean isDisposed() {
		return lifecycleManager.isDisposed();
	}

	@Override
	public boolean isInitialised() {
		return lifecycleManager.isInitialised();
	}
	
	@Override
	public void initialise() throws InitialisationException {
		lifecycleManager.initialise();
		try{
			JSch jsch = buildJSch();
			session = initSession(jsch);
			channelSftp = (ChannelSftp) initChannel(session, CHANNEL_SFTP);
		} catch (JSchException e){
			throw new InitialisationException(e);
		}
	}
	
	/**
	 * Initialize a new JSch instance using the configured konwnHosts file,
	 * and setting the private key if one is available.
	 * @return
	 * @throws JSchException 
	 */
	protected JSch buildJSch() throws JSchException{
		JSch jsch = new JSch();
		
		//set known hosts if configured
		if(knownHosts != null && knownHosts.exists() && knownHosts.isFile()){
			logger.debug("Using known hosts: {}", knownHosts.getAbsolutePath());
			jsch.setKnownHosts(knownHosts.getAbsolutePath());
		} else {
			logger.debug("No known hosts set for {} (is null or not a file)", knownHosts);
		}
		
		//add private key if existing
		if(getContext().getAuthContext().getPrivateKey() != null){
			if(getContext().getAuthContext().getPrivateKeyPassword() != null){
				jsch.addIdentity(getContext().getAuthContext().getPrivateKey(), 
						getContext().getAuthContext().getPrivateKeyPassword());
			} else {
				jsch.addIdentity(getContext().getAuthContext().getPrivateKey());
			}
		}
		
		return jsch;
	}
	
	/**
	 * Initialise a fresh session using the given JSch and 
	 * this Helper's context. If an exception is thrown,
	 * this session is safely disconnected. 
	 * @param jsch
	 * @return
	 * @throws JSchException
	 */
	protected Session initSession(JSch jsch) throws JSchException{
		Session session = null;
		try{
			session = jsch.getSession(getContext().getAuthContext().getLogin(),
				getContext().getHost(), getContext().getPort());
			session.setPassword(getContext().getAuthContext().getPassword());
			session.setConfig(getContext().getOptions());
			session.connect(sessionConnectTimeout);
		} catch (JSchException e) {
			if(session != null){
				session.disconnect();
			}
			throw e;
		}
		return session;
	}
	
	/**
	 * Initialise a new channel using the given session and type.
	 * Channel is safely disconnected on error.
	 * @param session
	 * @param type
	 * @return
	 * @throws JSchException
	 */
	protected Channel initChannel(Session session, String type) throws JSchException{
		Channel channel = null;
		try{
			channel = session.openChannel(type);
			channel.connect(channelConnectTimeout);	
		} catch(JSchException e){
			if(channel != null){
				channel.disconnect();
			}
			throw e;
		}
		return channel;
	}

	@Override
	public void dispose() {
		lifecycleManager.dispose();
		
		if(session != null){
			session.disconnect();
		}
		if(channelSftp != null){
			channelSftp.disconnect();
		}
	}
	
	/**
	 * Retrieve the ChannelSftp instance used by this Helper to perform SFTP operations.
	 * If {@link #isReady()} is false, this method may return null or a closed channel.
	 * @return
	 */
	public ChannelSftp getChannelSftp(){
		return channelSftp;
	}
	
	/**
	 * Get the currently set known hosts. 
	 * @return
	 */
	public File getKnownHosts() {
		return knownHosts;
	}

	/**
	 * Define a known_hosts file for this Helper. This method is to be called
	 * prior to initialisation. If called after initialisation, a warning will be issued.
	 * @param knownHosts
	 */
	public void setKnownHosts(File knownHosts) {
		if(lifecycleManager.isInitialised() || lifecycleManager.isDisposed()){
			logger.warn("Setting known hosts on already initialised or disposed Helper. Known hosts ignored.");
			return;
		}
		
		this.knownHosts = knownHosts;
	}

	/**
	 * Generate an InputStream for the given file. Used when uploading stream.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	protected InputStream streamFile(File f) throws FileNotFoundException{
		return new FileInputStream(f);
	}
	
	/**
	 * Remove the file at the given path.
	 * @param filepath
	 * @throws SftpException
	 */
	public void remove(String filepath) throws SftpException {
		channelSftp.rm(filepath);
	}

	/**
	 * Remove the directory at the given path
	 * @param dirpath
	 * @throws SftpException
	 */
	public void removeDir(String dirpath) throws SftpException {
		channelSftp.rmdir(dirpath);
	}

	/**
	 * Retrieve an InputStream for the content point by the given path.
	 * @param dest
	 * @return
	 * @throws SftpException
	 */
	public InputStream getInputStream(String dest) throws SftpException {
		return channelSftp.get(dest);
	}

	/**
	 * Check if a file or directory exists in the given parent directory.
	 * @param parent
	 * @param filename
	 * @return true if a file or directory is found matching the given name
	 * @throws SftpException
	 */
	public boolean exists(String parent, String filename) throws SftpException{
		Vector<?> vector = channelSftp.ls(parent);
		for(Object o : vector){
			if(((LsEntry) o).getFilename().equals(filename)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if a file or directory exists at the given path.
	 * @param dest
	 * @return
	 * @throws SftpException
	 */
	public boolean exists(String dest) throws SftpException{
		try {
			channelSftp.stat(dest);
			return true;
		} catch (SftpException e) {
			if(e.id == ERRID_NO_SUCH_FILE){
				return false;
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * Move a file from origin to destination. TODO works for directory? 
	 * @param origin
	 * @param dest
	 * @throws SftpException
	 */
	public void move(String origin, String dest) throws SftpException{
		channelSftp.rename(origin, dest);
	}
	
	/**
	 * Create a directory at the given path if no directory with the same
	 * path does not already exists. If a file or directory already exists at 
	 * the given path, this function does nothing. 
	 * @param dest
	 * @throws SftpException
	 */
	public void mkdirIfNotExists(String dest) throws SftpException {
		try {
			channelSftp.mkdir(dest);
		} catch (SftpException e) {
			if(e.id == ERRID_FILE_ALREADY_EXISTS || e.id == ERRID_FAILURE){ // 11: SFTP code "File Already Exists"
				return;
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * Upload content from the given stream to a file pointed by this given destination, using the given writing mode.
	 * @param stream
	 * @param dest
	 * @param mode
	 * @return
	 * @throws SftpException
	 */
	public void upload(InputStream stream, String dest, int mode) throws SftpException {
		int jschMode = 0;
		switch(mode){
		case SftpHelper.MODE_APPEND:
			jschMode = ChannelSftp.APPEND;
			break;
		case SftpHelper.MODE_OVERWRITE:
			jschMode = ChannelSftp.OVERWRITE;
			break;
		case SftpHelper.MODE_RESUME:
			jschMode = ChannelSftp.RESUME;
			break;
		default:
			throw new IllegalArgumentException("Unknow upload mode " + mode + " for JSch client.");
		}

		channelSftp.put(stream, dest, jschMode);
	}

	/**
	 * Upload the given file using {@linkplain #MODE_OVERWRITE} mode.
	 * @param file
	 * @param dest
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public void upload(File file, String dest) throws SftpException, FileNotFoundException {
		upload(streamFile(file), dest, SftpHelper.MODE_OVERWRITE);
	}

	/**
	 * Upload the given file with specified mode.
	 * @param file
	 * @param dest 
	 * @param mode
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public void upload(File file, String dest, int mode) throws SftpException, FileNotFoundException {
		upload(streamFile(file), dest, mode);
	}

	/**
	 * Upload the content of a stream to the specified destination
	 * @param stream
	 * @param dest
	 * @throws SftpException
	 */
	public void upload(InputStream stream, String dest) throws SftpException {
		upload(stream, dest, SftpHelper.MODE_OVERWRITE);
	}
	
	/**
	 * List the content of the given directory.
	 * @param dir
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("unchecked")
	public Vector<LsEntry> list(String dir) throws SftpException {
		return (Vector<LsEntry>) channelSftp.ls(dir);
	}
	
	/**
	 * List files (no directories) from the given directory.
	 * @param dir
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listFiles(String dir) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(!entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	/**
	 * List files matching the specified pattern from the given directory.
	 * @param dir
	 * @param pattern
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listFiles(String dir, Pattern pattern) throws SftpException{
		Vector<LsEntry> ls = list(dir);
		Vector<LsEntry> result = new Vector<LsEntry>();
		for(LsEntry entry : ls){
			if(pattern.matcher(entry.getFilename()).matches()){
				result.add(entry);
			}
		}
		return result;
	}
	
	/**
	 * List directories from the given directory
	 * @param dir
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listDirectories(String dir) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	/**
	 * List directories matching speficied pattern from the given directory
	 * @param dir
	 * @param pattern
	 * @return
	 * @throws SftpException
	 */
	public Vector<LsEntry> listDirectoriesMatching(String dir, Pattern pattern) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	/**
	 * Generate the MD5 checksum of the specified file
	 * @param dest
	 * @return
	 * @throws IOException
	 * @throws SftpException
	 */
	public String getChecksum(String dest) throws IOException, SftpException {
		return DigestUtils.md5Hex(getInputStream(dest));
	}

	/**
	 * Compare checksum for the given stream against the specified destination.
	 * @param src
	 * @param dest
	 * @return result with success if both checksum are identical.
	 * @throws IOException
	 * @throws SftpException
	 */
	public BaseExpectingHelperResult<String, String> compareChecksum(InputStream src, String dest) throws IOException, SftpException {
		String expected = DigestUtils.md5Hex(src);
		String actual = getChecksum(dest);
		
		return ResultBuilder.expectingResult(expected.equals(actual), actual, expected, "Comparing checksum of '" + dest + "'");
	}

	/**
	 *  Compare checksum for the given file against the specified destination.
	 * @param src
	 * @param dest
	 * @return result with success if both checksum are identical.
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws SftpException
	 */
	public BaseExpectingHelperResult<String, String> compareChecksum(File src, String dest)
			throws NoSuchAlgorithmException, IOException, SftpException {
		return compareChecksum(streamFile(src), dest);
	}
	
	/**
	 * Check whether a directory contains a file matching the specified filename
	 * @param dir
	 * @param filename
	 * @return
	 * @throws SftpException
	 */
	public boolean containsFile(String dir, String filename) throws SftpException {
		Vector<LsEntry> ls = list(dir);
		for(LsEntry entry : ls){
			if(entry.getFilename().equals(filename)){
				return true;
			}
		}
		return false;
		
	}

	/**
	 * Check if a directory contains a file matching the given pattern
	 * @param dir
	 * @param pattern
	 * @return
	 * @throws SftpException
	 */
	public BaseHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern) throws SftpException {
		Vector<LsEntry> result = listFiles(dir, pattern);
		return ResultBuilder.result(!result.isEmpty(), result, "Checking for '" + dir + "' content matching " + pattern.pattern());
	}
	
	/**
	 * Check if a directory contains a specified number of files matching the given pattern
	 * @param dir directory to list
	 * @param pattern pattern to match
	 * @param count exact number of files to find
	 * @return successful result ig the exact number of file is found
	 * @throws SftpException
	 */
	public BaseHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern, int count)
			throws SftpException {
		Vector<LsEntry> result = listFiles(dir, pattern);
		return ResultBuilder.expectingResult(result.size() == count, result, count, 
				"Counting occurences of content matching " + pattern.pattern() + " in " + dir);
	}

	/**
	 * Check if a directory contains a child directory matching the given name
	 * @param parentdir
	 * @param dirname
	 * @return
	 * @throws SftpException
	 */
	public boolean containsDirectory(String parentdir, String dirname) throws SftpException {
		Vector<LsEntry> ls = listDirectories(parentdir);
		for(LsEntry entry : ls){
			if(entry.getAttrs().isDir() && entry.getFilename().equals(dirname)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Wait for a directory to contain a file matching the given pattern. 
	 * @param dir directory to list 
	 * @param pattern pattern to match
	 * @param timeout maximum time to wait for a file matching the given pattern
	 * @param period period between checks
	 * @return successful result if at least one file is found before timeout
	 * @throws InterruptedException
	 */
	public BaseHelperResult<Vector<LsEntry>> waitForContainsFile(final String dir, final Pattern pattern, long timeout,
			long period) throws InterruptedException {
		Waiter<BaseHelperResult<Vector<LsEntry>>> waiter = new SimpleWaiter<BaseHelperResult<Vector<LsEntry>>>(timeout, period){
			@Override
			public BaseHelperResult<Vector<LsEntry>> performCheck(BaseHelperResult<Vector<LsEntry>> latestResult)  {
				try {
					return containsFile(dir, pattern);
				} catch (SftpException e) {
					throw new SftpHelperException(e);
				}
			}
		};
		return waiter.call(); 
	}

	/**
	 * Wait for a directory to contain a specified count of files matching the given pattern. 
	 * @param dir directory to list 
	 * @param pattern pattern to match
	 * @parem count exact number of files to be found
	 * @param timeout maximum time to wait for a file matching the given pattern
	 * @param period period between checks
	 * @return successful result if at the exact count of file is found before timeout
	 * @throws InterruptedException
	 */
	public BaseHelperResult<Vector<LsEntry>> waitForContainsFile(final String dir, final Pattern pattern, final int count,
			long timeout, long period) throws InterruptedException {
		Waiter<BaseHelperResult<Vector<LsEntry>>> waiter = new SimpleWaiter<BaseHelperResult<Vector<LsEntry>>>(timeout, period){
			@Override
			public BaseHelperResult<Vector<LsEntry>> performCheck(BaseHelperResult<Vector<LsEntry>> latestResult) {
				try {
					return containsFile(dir, pattern, count);
				} catch (SftpException e) {
					throw new SftpHelperException(e);
				}
			}
		};
		return waiter.call();
	}
	
	/**
	 * Move the entire directory content from an origin to a destination. Both origin
	 * and destination must be writable directories.
	 * @param origin
	 * @param dest
	 * @throws SftpException
	 */
	public void moveDirectoryContent(String origin, String dest) throws SftpException {
		Vector<LsEntry> ls = list(origin);
		for(LsEntry entry : ls){
			if(isParentOrCurrent(entry)){
				continue;
			}
			
			String toMove = origin + "/" + entry.getFilename();
			String destMove = dest + "/" + entry.getFilename();
			
			logger.info("Moving {} to {}", toMove, destMove);
			
			move(toMove, destMove);
		}
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isParentOrCurrent(LsEntry e){
		return ".".equals(e.getFilename()) || "..".equals(e.getFilename());
	}

}
