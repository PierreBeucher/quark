package com.github.pierrebeucher.quark.sftp.helper;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.SftpException;

/**
 * Utility class to produce, consume and perform various operations with SFTP files.
 * @author pierreb
 *
 */
public class SftpServiceHelper extends AbstractHelper<SftpContext> implements Disposable, Initialisable{

	private SftpHelper helper;
	
	/**
	 * The empty constructor cannot be used, as the underlying Helper is initialized on construction.
	 */
	private SftpServiceHelper(){
		super(new SftpContext());
	}
	
	public SftpServiceHelper(SftpContext context) {
		super(context);
		helper = new SftpHelper(context);
	}
	
	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	public void dispose() {
		helper.dispose();
	}

	public boolean isDisposed() {
		return helper.isDisposed();
	}

	public boolean isInitialised() {
		return helper.isInitialised();
	}

	/**
	 * Produce a set of file in the given destination directory.
	 * @param dataMap map of data to produce, key is stream data, value is filename to produce
	 * @param destDir destination directory
	 * @throws SftpException
	 */
	public void produceFile(Map<InputStream, String> dataMap, String destDir) throws SftpException{
		for(Entry<InputStream, String> e : dataMap.entrySet()){
			produceFile(e.getKey(), destDir, e.getValue());
		}
	}
	
	public void produceFile(InputStream data, String destination, String asFilename) throws SftpException{
		helper.upload(data, destination + "/" + asFilename);
	}
	
	public boolean fileExists(String parent, String filename) throws SftpException{
		return helper.exists(parent, filename);
	}
	
	public SftpHelper getSftpHelper(){
		return helper;
	}

	@Override
	public boolean isReady() {
		return helper.isReady();
	}
}
