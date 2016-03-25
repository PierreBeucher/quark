package org.atom.quark.sftp.helper;

import java.io.File;
import java.io.IOException;

import org.atom.quark.core.helper.HelperException;
import org.atom.quark.core.result.HelperResult;
import org.atom.quark.core.result.ResultBuilder;
import org.atom.quark.sftp.context.SftpContext;
import org.atom.quark.utils.sftp.JSchSftpClient;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * A SFTP Helper using JSch.
 * @author Pierre Beucher
 *
 */
public class JSchSftpHelper extends AbstractSftpHelper {
	
	private JSchSftpClient client;
	
	public JSchSftpHelper() {
		
	}

	public JSchSftpHelper(SftpContext sftpContext) {
		super(sftpContext);
	}

	public HelperResult<Boolean> connect() throws JSchException {
		if(client != null){
			client.disconnect();
		}
		
		client = new JSchSftpClient(
				getContext().getAuthContext().getLogin(),
				getContext().getHost(),
				getContext().getPort()
				);
		client.connect();
		
		return ResultBuilder.success();
	}
	
	public HelperResult<String> disconnect(){
		if(client == null || !client.isChannelSftpConnected()){
			return ResultBuilder.failure("Cannot disconnect a null or not connected client");
		}
		
		client.disconnect();
		
		return ResultBuilder.success("Disconnected.");
	}

	public HelperResult<String> upload(File file, String dest) throws SftpException, IOException {
		client.uploadFile(file, dest);

		return buildSuccessUploadResult(dest);
	}
	
	public HelperResult<String> upload(File file, String dest, int mode) throws Exception {
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
		
		client.uploadFile(file, dest, jschMode);

		return buildSuccessUploadResult(dest);
	}

	private HelperResult<String> buildSuccessUploadResult(String dest){
		return ResultBuilder.success(new File(dest).getName());
	}

}
