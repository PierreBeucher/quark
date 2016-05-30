package com.github.pierrebeucher.quark.sftp.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.FileCleaner;
import com.github.pierrebeucher.quark.core.helper.InitializationException;
import com.github.pierrebeucher.quark.sftp.context.SftpContext;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * The SftpCleaner is a Helper used to clean SFTP directories to ensure no old or previous data
 * will collide or impact future Helper actions. The SftpCleaner work is no destructive: any
 * data cleaned is either moved or archived, and can be retrieved. 
 * @author pierreb
 *
 */
public class SftpCleaner extends AbstractLifecycleHelper<SftpContext> implements FileCleaner{
	
	//private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JSchSftpHelper helper;
	
	public SftpCleaner(SftpContext context) {
		super(context);
	}

	@Override
	protected void doInitialise() throws InitializationException {
		helper = new JSchSftpHelper(context);
		helper.initialise();
	}

	@Override
	protected void doDispose() {
		helper.dispose();
	}
	
	/**
	 * <p>Clean the given directory by copying
	 * its content into a sub-directory named <i>.quark_trash</i> (such as
	 * <i>/path/to/dirToClean/.trash_dir</i>.</p>
	 * <p>Same as calling <pre>cleanToLocalDir(dirToClean, ".quark_trash")</pre></p>
	 * @param dirToClean
	 * @return the path to the local directory where data have been archived
	 * @throws JSchException 
	 */
	@Override
	public String cleanToLocalDir(String dirToClean) throws SftpHelperException{
		//default archive dir is /path/to/dirToClean/.quark_trash/yyyyMMddHHmmss/data...
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
		String quarkTrashDir = dirToClean + "/" + DEFAULT_CLEAN_DIR;
		String archiveDir = quarkTrashDir + "/" + dateFormat.format(new Date());
		
		try {
			helper.mkdirIfNotExists(quarkTrashDir);
			helper.mkdirIfNotExists(archiveDir);
		
			_clean(dirToClean, archiveDir, helper);
			return archiveDir;
		} catch (SftpException e) {
			throw new SftpHelperException(e);
		}
	}
	
	/**
	 * Clean the given dirToClean directory data into the given archiveDir. Only files
	 * in the given directory are clean, sub-directories are ignored. ArchiveDir is supposed
	 * to be existing and writable. 
	 * @param dirToClean directory to clean
	 * @param archiveDir existing and writable archive directory
	 * @throws SftpHelperException 
	 */
	public void clean(String dirToClean, String archiveDir) throws SftpHelperException{
		try {
			_clean(dirToClean, archiveDir, helper);
		} catch (SftpException e) {
			throw new SftpHelperException(e);
		}
	}
	
	/**
	 * Private method used by clean* methods. ArchiveDir is supposed to exists, and
	 * helper will be used to effectively clean files.
	 * @param dirToClean
	 * @param archiveDir
	 * @throws SftpException 
	 */
	private void _clean(String dirToClean, String archiveDir, SftpHelper helper) throws SftpException{
		
		logger.debug("Cleaning content of {} to {} from {}", dirToClean, archiveDir, helper.getContext());
		
		for(LsEntry e : helper.listFiles(dirToClean)){
			helper.move(dirToClean + "/" + e.getFilename(), archiveDir + "/" + e.getFilename());
		}
	}
	
	/**
	 * <p>Clean the given directory into the system temporary directory. (such as /tmp on Linux systems).
	 * The data found in the given directory will be copied into $TMP_DIR/quark_trash/{current_timestamp}/path/to/dirToClean.
	 * The entire directory tree will be reproduce into a sub-directory named after the current system timestamp.</p>
	 * <p>For example, calling this method when current system time is 01-01-2010 12:34:56 (ms) using directory <i>/var/myapp/mydata</i>,
	 * data will be archived in a directory looking like /tmp/quark_trash/01012010123456/var/myapp/mydata</p>
	 * @param dirToClean
	 * @return path to the local directory where data have been archived
	 */
	public void cleanToTempDir(String dirToClean){
		//TODO implement
		throw new RuntimeException("Not implemented yet.");
	}

	@Override
	public boolean isReady() {
		return context.getAuthContext().getLogin() != null
				&& !StringUtils.isEmpty(context.getHost())
				&& context.getPort() > 0;
	}

	/**
	 * 
	 * @return the underlying Helper used by this Cleaner
	 */
	public JSchSftpHelper getHelper() {
		return helper;
	}
}
