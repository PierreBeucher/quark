package com.github.pierrebeucher.quark.ftp.helper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;


public class FtpServiceHelper extends AbstractHelper<FtpContext> {

	public FtpServiceHelper(FtpContext context) {
		super(context);
	}
	
	@Override
	public boolean isReady() {
		return StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0;
	}
	
	private FtpHelper getFtpHelper(){
		return new FtpHelper(context);
	}
	
	public void produceFile(InputStream data, String destination, String asFilename) throws IOException{
		FtpHelper helper = getFtpHelper();
		helper.init();
		helper.upload(data, destination + "/" + asFilename);
	}
	
	public boolean fileExists(String parent, String filename) throws IOException{
		FtpHelper helper = getFtpHelper();
		helper.init();
		return helper.isFile(parent + "/" + filename);
	} 

}
