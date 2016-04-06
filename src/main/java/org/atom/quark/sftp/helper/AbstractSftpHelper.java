package org.atom.quark.sftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.atom.quark.core.result.ResultBuilder;
import org.atom.quark.core.result.TypedExpectingHelperResult;
import org.atom.quark.core.result.TypedHelperResult;
import org.atom.quark.sftp.context.SftpContext;

import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * 
 * @author Pierre Beucher
 *
 */
public abstract class AbstractSftpHelper implements SftpHelper {

	private SftpContext sftpContext;
	
	public AbstractSftpHelper() {
		this.sftpContext = new SftpContext();
	}

	public AbstractSftpHelper(SftpContext sftpContext) {
		this.sftpContext = sftpContext;
	}

	public SftpContext getContext() {
		return sftpContext;
	}

	public SftpHelper context(SftpContext context) {
		this.sftpContext = context;
		return this;
	}

	public SftpHelper host(String host) {
		sftpContext.setHost(host);
		return this;
		
	}

	public SftpHelper port(int port) {
		sftpContext.setPort(port);
		return this;
	}

	public SftpHelper login(String login) {
		sftpContext.getAuthContext().setLogin(login);
		return this;
	}

	public SftpHelper password(String password) {
		sftpContext.getAuthContext().setPassword(password);
		return this;
	}

	public SftpHelper privateKey(String privateKey) {
		sftpContext.getAuthContext().setPrivateKey(privateKey);
		return this;
	}
	
	public SftpHelper addOption(String option, Object value){
		sftpContext.getOptions().put(option, value);
		return this;
	}
	
	@Override
	public String toString() {
		return getContext().toString();
	}

	/**
	 * The SftpHelper is ready if its Context is properly 
	 * configured with at least a host and a login. Other values
	 * may be defined by default or left empty. 
	 */
	public boolean isReady() {
		return !StringUtils.isEmpty(getContext().getHost())
				&& !StringUtils.isEmpty(getContext().getAuthContext().getLogin());
	}

	public SftpHelper privateKeyPassword(String privateKeyPassword) {
		getContext().getAuthContext().setPrivateKeyPassword(privateKeyPassword);
		return this;
	}
	
	/**
	 * Generate an InputStream for the given file. Used when uploading stream.
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	protected InputStream streamFile(File f) throws FileNotFoundException{
		return new FileInputStream(f);
	}

	public boolean upload(File file, String dest) throws Exception {
		return upload(streamFile(file), dest, SftpHelper.MODE_OVERWRITE);
	}

	public boolean upload(File file, String dest, int mode) throws Exception {
		return upload(streamFile(file), dest, mode);
	}

	public boolean upload(InputStream stream, String dest) throws Exception {
		return upload(stream, dest, SftpHelper.MODE_OVERWRITE);
	}
	
	public Vector<LsEntry> listFiles(String dir) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(!entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	public Vector<LsEntry> listFiles(String dir, Pattern pattern) throws SftpException{
		Vector<LsEntry> ls = list(dir);
		Vector<LsEntry> result = new Vector<LsEntry>();
		for(LsEntry entry : ls){
			if(pattern.matcher(entry.getFilename()).matches()){
				result.add(entry);
			}
		}
		return result;
	}
	
	public Vector<LsEntry> listDirectories(String dir) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	public Vector<LsEntry> listDirectoriesMatching(String dir, Pattern pattern) throws SftpException{
		Vector<LsEntry> baseResult = list(dir);
		Vector<LsEntry> filteredResult = new Vector<LsEntry>();
		for(LsEntry entry : baseResult){
			if(entry.getAttrs().isDir()){
				filteredResult.add(entry);
			}
		}
		return filteredResult;
	}
	
	public String getChecksum(String dest) throws IOException, SftpException {
		return DigestUtils.md5Hex(getInputStream(dest));
	}

	public TypedExpectingHelperResult<String, String> compareChecksum(InputStream src, String dest) throws IOException, SftpException {
		String expected = DigestUtils.md5Hex(src);
		String actual = getChecksum(dest);
		
		return ResultBuilder.result(expected.equals(actual), actual, expected);
	}

	public TypedExpectingHelperResult<String, String> compareChecksum(File src, String dest)
			throws NoSuchAlgorithmException, IOException, SftpException {
		return compareChecksum(streamFile(src), dest);
	}
	
	public boolean containsFile(String dir, String filename) throws SftpException {
		Vector<LsEntry> ls = list(dir);
		for(LsEntry entry : ls){
			if(entry.getFilename().equals(filename)){
				return true;
			}
		}
		return false;
		
	}

	public TypedHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern) throws SftpException {
		Vector<LsEntry> result = listFiles(dir, pattern);
		return ResultBuilder.result(!result.isEmpty(), result);
	}
	
	public TypedHelperResult<Vector<LsEntry>> containsFile(String dir, Pattern pattern, int count)
			throws SftpException {
		Vector<LsEntry> result = listFiles(dir, pattern);
		return ResultBuilder.result(result.size() == count, result);
	}

	public boolean containsDirectory(String parentdir, String dirname) throws SftpException {
		Vector<LsEntry> ls = listDirectories(parentdir);
		for(LsEntry entry : ls){
			if(entry.getAttrs().isDir() && entry.getFilename().equals(dirname)){
				return true;
			}
		}
		return false;
	}


	
	
	

}
