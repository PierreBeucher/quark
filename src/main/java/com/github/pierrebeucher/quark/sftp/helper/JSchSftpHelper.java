package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * A SFTP Helper using JSch. This Helper will trust any host found in {user.home}/.ssh/known_hosts,
 * unless knownHosts is set to null or to a non-existing file.
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelper extends AbstractSftpHelper implements Initialisable, Disposable, Helper{
	
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

	public JSchSftpHelper() {
		this(new SftpContext());
	}

	public JSchSftpHelper(SftpContext sftpContext) {
		super(sftpContext);
		this.knownHosts = new File(System.getProperty("user.home"), "/.ssh/known_hosts");
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

	public boolean isDisposed() {
		return lifecycleManager.isDisposed();
	}

	public boolean isInitialised() {
		return lifecycleManager.isInitialised();
	}

	public boolean upload(InputStream stream, String dest, int mode) throws SftpException {
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

		return true;
	}

	@SuppressWarnings("unchecked")
	public Vector<LsEntry> list(String dir) throws SftpException {
		return (Vector<LsEntry>) channelSftp.ls(dir);
	}

	@Override
	public void remove(String filepath) throws SftpException {
		channelSftp.rm(filepath);
	}

	@Override
	public void removeDir(String dirpath) throws SftpException {
		channelSftp.rmdir(dirpath);
	}

	public InputStream getInputStream(String dest) throws SftpException {
		return channelSftp.get(dest);
	}

	public boolean exists(String parent, String filename) throws SftpException{
		Vector<?> vector = channelSftp.ls(parent);
		for(Object o : vector){
			if(((LsEntry) o).getFilename().equals(filename)){
				return true;
			}
		}
		return false;
	}
	
	@Override
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

	@Override
	public void move(String origin, String dest) throws SftpException{
		channelSftp.rename(origin, dest);
	}
	
	@Override
	public ChannelSftp getChannelSftp(){
		return channelSftp;
	}

	@Override
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

	public File getKnownHosts() {
		return knownHosts;
	}

	public void setKnownHosts(File knownHosts) {
		this.knownHosts = knownHosts;
	}

}
