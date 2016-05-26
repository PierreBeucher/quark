package com.github.pierrebeucher.quark.sftp.helper;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Utility class to produce, consume and perform various operations with SFTP files.
 * @author pierreb
 *
 */
public class SftpServiceHelper extends AbstractHelper<SftpContext> {

	private JSchSftpHelper sftpHelper;
	
	/**
	 * The empty constructor cannot be used, as the underlying Helper is initialized on construction.
	 */
	private SftpServiceHelper(){
		super(null);
	}
	
	public SftpServiceHelper(SftpContext context) throws JSchException {
		super(context);
		this.sftpHelper = new JSchSftpHelper(context);
		this.sftpHelper.connect();
	}
	
	public void produceFile(Map<InputStream, String> dataMap) throws SftpException{
		JSchSftpHelper helper = getSftpHelper();
		
		for(Entry<InputStream, String> e : dataMap.entrySet()){
			helper.upload(e.getKey(), e.getValue());
		}
	}
	
	public void produceFile(InputStream data, String destination, String asFilename) throws SftpException{
		JSchSftpHelper helper = getSftpHelper();
		helper.upload(data, destination + "/" + asFilename);
	}
	
	public boolean fileExists(String parent, String filename) throws SftpException{
		JSchSftpHelper helper = getSftpHelper();
		return helper.exists(parent, filename);
	}
	
	protected JSchSftpHelper getSftpHelper(){
		return sftpHelper;
	}

	@Override
	public boolean isReady() {
		return sftpHelper.isReady();
	}
}
