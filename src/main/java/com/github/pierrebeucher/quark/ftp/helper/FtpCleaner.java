package com.github.pierrebeucher.quark.ftp.helper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;

import com.github.pierrebeucher.quark.core.helper.AbstractWrapperHelper;
import com.github.pierrebeucher.quark.core.helper.FileCleaner;
import com.github.pierrebeucher.quark.core.helper.HelperException;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.ftp.context.FtpContext;

public class FtpCleaner extends AbstractWrapperHelper<FtpContext, FtpHelper> implements
		FileCleaner, Initialisable, Disposable{
	
	/**
	 * Utility method to clean an FtpContext. Instanciate a new FtpCleaner
	 * and call the {@link #clean(String)} method.
	 * @param ctx
	 */
	public static void clean(FtpContext context, String directory){
		FtpCleaner cleaner = new FtpCleaner(context);
		try{
			cleaner.initialise();
			cleaner.clean(directory);
		} finally {
			cleaner.dispose();
		}
	}
	
	public FtpCleaner(FtpContext context) {
		super(context, new FtpHelper(context));
	}
	
	public FtpCleaner(FtpHelper helper) {
		super(helper.getContext(), helper);
	}

	@Override
	public void dispose() {
		helper.dispose();
	}

	@Override
	public boolean isDisposed() {
		return helper.isDisposed();
	}

	@Override
	public void initialise() throws InitialisationException {
		helper.initialise();
	}

	@Override
	public boolean isInitialised() {
		return helper.isInitialised();
	}

	@Override
	public boolean isReady() {
		return !StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0
				&& !StringUtils.isEmpty(context.getLogin());
	}

	@Override
	public String clean(String dirToClean) throws HelperException {
		try {
			String archiveDir = generateArchiveDir(dirToClean);
			_clean(dirToClean, archiveDir, helper);
			return archiveDir;
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}

	@Override
	public void clean(String dirToClean, String archiveDir) {
		try {
			_clean(dirToClean, archiveDir, helper);
		} catch (IOException e) {
			throw new FtpHelperException(e);
		}
	}

	/**
	 * Generate the archive directory, by happending the
	 * {@link FileCleaner#DEFAULT_CLEAN_DIR} and the current timestamp in ms
	 * to the given directory. Al required directories and sub-directories are created
	 * if required.
	 * @param dirToClean directory for which to generate archive directory path
	 * @return archive directory for directory to clean
	 */
	protected String generateArchiveDir(String dirToClean){
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
		String quarkTrashDir = dirToClean + "/" + DEFAULT_CLEAN_DIR;
		String archiveDir = quarkTrashDir + "/" + dateFormat.format(new Date());	
		
		//create directories
		helper.makeDirectory(quarkTrashDir);
		helper.makeDirectory(archiveDir);
		
		return archiveDir;
	}

//	@Deprecated
//	@Override
//	public String clean(String dirToClean) throws FtpHelperException {
//		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
//		String quarkTrashDir = dirToClean + "/" + DEFAULT_CLEAN_DIR;
//		String archiveDir = quarkTrashDir + "/" + dateFormat.format(new Date());
//
//		try {
//			//create directories before cleaning
//			helper.makeDirectory(quarkTrashDir);
//			helper.makeDirectory(archiveDir);
//
//			_clean(dirToClean, archiveDir, helper);
//		} catch (IOException e) {
//			throw new FtpHelperException(e);
//		}
//
//		return archiveDir;
//	}

	private void _clean(String dirToClean, String archiveDir, FtpHelper helper) throws FtpHelperException, IOException{
		logger.debug("Cleaning {} on {} to {}.", dirToClean, context, archiveDir);
		for(FTPFile file : helper.listFiles(dirToClean)){
			helper.move(dirToClean + "/" + file.getName(),
					archiveDir + "/" + file.getName());
		}
	}
	
	/**
	 * 
	 * @return the underlying Helper used by this Cleaner
	 */
	public FtpHelper getHelper() {
		return helper;
	}

}
