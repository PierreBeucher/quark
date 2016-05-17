package com.github.pierrebeucher.quark.ftp.helper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pierrebeucher.quark.core.helper.AbstractHelper;
import com.github.pierrebeucher.quark.core.helper.FileCleaner;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpCleaner extends AbstractHelper<FtpContext> implements
	FileCleaner{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public FtpCleaner(FtpContext context) {
		super(context);
	}

	@Override
	public boolean isReady() {
		return !StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0
				&& !StringUtils.isEmpty(context.getLogin());
	}

	@Override
	public void clean(String dirToClean, String archiveDir) {
		FtpHelper helper = null;
		try {
			helper = createFtpHelper();
			helper.init();
			
			_clean(dirToClean, archiveDir, helper);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		} finally {
			if(helper != null){
				try {
					helper.disconnect();
				} catch (IOException e) {
					logger.error("Cannot disconnect {} after use: {}", helper, e);
				}
			}
		}
	}

	
	@Override
	public String cleanToLocalDir(String dirToClean) throws FtpHelperException {
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
		String quarkTrashDir = dirToClean + "/" + DEFAULT_CLEAN_DIR;
		String archiveDir = quarkTrashDir + "/" + dateFormat.format(new Date());
		
		FtpHelper helper = null;
		try {
			helper = createFtpHelper();
			helper.init();
			helper.makeDirectory(quarkTrashDir);
			helper.makeDirectory(archiveDir);
			
			_clean(dirToClean, archiveDir, helper);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		} finally {
			if(helper != null){
				try {
					helper.disconnect();
				} catch (IOException e) {
					logger.error("Cannot disconnect {} after use: {}", helper, e);
				}
			}
		}
		
		return archiveDir;
	}
	
	private void _clean(String dirToClean, String archiveDir, FtpHelper helper) throws FtpHelperException, IOException{
		for(FTPFile file : helper.listFiles(dirToClean)){
			helper.move(dirToClean + "/" + file.getName(),
					archiveDir + "/" + file.getName());
		}
	}
	
	private FtpHelper createFtpHelper(){
		return new FtpHelper(context);
	}

}
