package com.github.pierrebeucher.quark.ftp.helper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;


public class FtpServiceHelper extends AbstractHelper<FtpContext> implements Initialisable, Disposable {

	private FtpHelper helper;
	
	public FtpServiceHelper(FtpContext context) {
		super(context);
		helper = new FtpHelper(context);
	}

	public void dispose() {
		helper.dispose();
	}

	public boolean isDisposed() {
		return helper.isDisposed();
	}

	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	public boolean isInitialised() {
		return helper.isInitialised();
	}

	@Override
	public boolean isReady() {
		return StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0;
	}
	
	public void produceFile(InputStream data, String destination, String asFilename) throws IOException{
		helper.upload(data, destination + "/" + asFilename);
	}
	
	public boolean fileExists(String parent, String filename) throws IOException{
		return helper.isFile(parent + "/" + filename);
	} 

}
