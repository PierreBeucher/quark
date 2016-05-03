package com.github.pierrebeucher.quark.sftp.helper;

import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class JSchSftpHelper extends AbstractSftpHelper {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	
	private File knownHosts;

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
		super();
		this.knownHosts = new File(System.getProperty("user.home"), "/.ssh/known_hosts");
	}

	public JSchSftpHelper(SftpContext sftpContext) {
		super(sftpContext);
		this.knownHosts = new File(System.getProperty("user.home"), "/.ssh/known_hosts");
	}

	/**
	 * Check the authentication context is sane, and print warning when inconsistency are found
	 */
	private void checkAuthContextSanity(){
		if(StringUtils.isNotEmpty(getContext().getAuthContext().getPrivateKey())){
			File key = new File(getContext().getAuthContext().getPrivateKey());
			if(!key.isFile() || !key.canRead()){
				logger.warn("Key {} does not exists or cannot be read", key.getPath());
			}
		}
	}

	public boolean connect() throws JSchException {
		disconnect();

		checkAuthContextSanity();

		try{

			JSch jsch = initJSch();
			//JSch.setLogger(new DebugLogger());

			//add private key if existing
			if(getContext().getAuthContext().getPrivateKey() != null){
				if(getContext().getAuthContext().getPrivateKeyPassword() != null){
					jsch.addIdentity(getContext().getAuthContext().getPrivateKey(), 
							getContext().getAuthContext().getPrivateKeyPassword());
				} else {
					jsch.addIdentity(getContext().getAuthContext().getPrivateKey());
				}
			}


			Session session = jsch.getSession(getContext().getAuthContext().getLogin(),
					getContext().getHost(), getContext().getPort());
			session.setPassword(getContext().getAuthContext().getPassword());
			session.setConfig(getContext().getOptions());
			session.connect(sessionConnectTimeout);

			Channel channel = session.openChannel(CHANNEL_SFTP);
			channel.connect(channelConnectTimeout);
			channelSftp = (ChannelSftp) channel;

			return true;
		} catch (JSchException e){

			//disconnect safely upon error and rethrow
			disconnect();
			throw e;
		}
	}
	
	/**
	 * initialize the JSch instance, providing basic configuration for our context
	 * @return
	 * @throws JSchException 
	 */
	private JSch initJSch() throws JSchException{
		JSch jsch = new JSch();
		
		if(knownHosts != null && knownHosts.exists() && knownHosts.isFile()){
			logger.debug("Using {} as known hosts", knownHosts.getAbsolutePath());
			jsch.setKnownHosts(knownHosts.getAbsolutePath());
		} else {
			logger.debug("No known_hosts set for {} (is null or not a file)", knownHosts);
		}
		
		return jsch;
		
	}

	public boolean disconnect(){		
		if(this.channelSftp != null){
			this.channelSftp.disconnect();
		}
		return true;
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
