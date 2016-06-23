package com.github.pierrebeucher.quark.cmis.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;

import com.github.pierrebeucher.quark.cmis.context.CMISContext;
import com.github.pierrebeucher.quark.cmis.util.CMISUtils;
import com.github.pierrebeucher.quark.core.helper.AbstractWrapperHelper;
import com.github.pierrebeucher.quark.core.helper.FileCleaner;
import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;

public class CMISCleaner extends AbstractWrapperHelper<CMISContext, CMISHelper>
		implements FileCleaner, Initialisable, Disposable{

	public CMISCleaner(CMISContext context) {
		super(context, new CMISHelper(context));
	}
	
	public CMISCleaner(CMISHelper helper) {
		super(helper.getContext(), helper);
	}
	
	@Override
	public boolean isReady() {
		return true;
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
	public void initialise() {
		helper.initialise();
	}

	@Override
	public boolean isInitialised() {
		return helper.isInitialised();
	}

	@Override
	public void clean(String dirToClean, String archiveDir) {
		_clean(dirToClean, archiveDir, helper);
	}

	/**
	 * Delete all the files from thre given directory.
	 * @param dirToClean
	 */
	public void deleteAll(String dirToClean){		
		Folder folderToClean = (Folder) helper.getSession().getObjectByPath(dirToClean);
		for(CmisObject o : folderToClean.getChildren()){
			if(CMISUtils.isDocument(o)){
				((Document)o).delete();
			}
		}
	}

	@Override
	public String clean(String dirToClean) {
		Folder folderToClean = (Folder) helper.getSession().getObjectByPath(dirToClean);
		
		//create the local archive directories before archiving
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
		Folder quarkTrashFolder = helper.createFolderIfNotExists(dirToClean, DEFAULT_CLEAN_DIR);
		Folder archiveFolder = helper.createFolderIfNotExists(quarkTrashFolder.getPath(), dateFormat.format(new Date()));
		_clean(folderToClean, archiveFolder, helper);
		return archiveFolder.getPath();
	}
	
	private void _clean(String dirToClean, String archiveDir, CMISHelper helper){
		Session session = helper.getSession();
		_clean((Folder) session.getObjectByPath(dirToClean),
				(Folder) session.getObjectByPath(archiveDir),
				helper);
	}
	
	private void _clean(Folder dirToClean, Folder archiveDir, CMISHelper helper){
		Session session = helper.getSession();
		ObjectId cmisToClean = session.createObjectId(dirToClean.getId());
		ObjectId cmisArchive = session.createObjectId(archiveDir.getId());
		
		ItemIterable<CmisObject> list = dirToClean.getChildren();
		for(CmisObject o : list){
			if(CMISUtils.isDocument(o)){
				((Document)o).move(cmisToClean, cmisArchive);
			}
		}
	}
	
//	private CMISHelper createCmisHelper(){
//		return new CMISHelper(context);
//	}

}
