package org.atom.quark.sftp.helper;

import java.io.File;
import java.io.InputStream;
import org.atom.quark.core.helper.HelperException;
import org.atom.quark.core.result.HelperResult;
import org.atom.quark.core.result.ResultBuilder;
import org.atom.quark.sftp.context.SftpContext;
import org.atom.quark.utils.sftp.DebugLogger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * A SFTP Helper using JSch.
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelper extends AbstractSftpHelper {
	
	/**
	 * Default timeout used when connecting a session
	 */
	public static final int DEFAULT_SESSION_CONNECT_TIMEOUT = 60000;
	
	/**
	 * Default timeout used when connecting a channel
	 */
	public static final int DEFAULT_CHANNEL_CONNECT_TIMEOUT = 60000;
	
	private static final String CHANNEL_SFTP = "sftp";
	
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
	}

	public JSchSftpHelper(SftpContext sftpContext) {
		super(sftpContext);
	}

	public HelperResult<Boolean> connect() throws JSchException {
		disconnect();
		try{
		
			JSch jsch = new JSch();
			JSch.setLogger(new DebugLogger());
			
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
			
			return ResultBuilder.success();
		} catch (JSchException e){
			
			//disconnect safely upon error and rethrow
			disconnect();
			throw e;
		}
	}
	
	public HelperResult<String> disconnect(){		
		if(this.channelSftp != null){
			this.channelSftp.disconnect();
		}
		return ResultBuilder.success("Disconnected.");
	}
	
	public HelperResult<String> upload(InputStream stream, String dest, int mode) throws Exception {
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
				throw new HelperException("Unknow upload mode " + mode + " for JSch client.");
		}
		
		channelSftp.put(stream, dest, jschMode);

		return buildSuccessUploadResult(dest);
	}

	private HelperResult<String> buildSuccessUploadResult(String dest){
		return ResultBuilder.success(new File(dest).getName());
	}
	
	

}
