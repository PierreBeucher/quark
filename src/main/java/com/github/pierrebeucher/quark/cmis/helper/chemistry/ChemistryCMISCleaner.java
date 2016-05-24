package com.github.pierrebeucher.quark.cmis.helper.chemistry;

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
import com.github.pierrebeucher.quark.cmis.util.ChemistryCMISUtils;
import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.FileCleaner;
import com.github.pierrebeucher.quark.core.helper.InitializationException;

public class ChemistryCMISCleaner extends AbstractLifecycleHelper<CMISContext>
	implements FileCleaner {
	
	private ChemistryCMISHelper helper;

	public ChemistryCMISCleaner(CMISContext context) {
		super(context);
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	protected void doInitialise() throws InitializationException {
		helper = createCmisHelper();
		helper.initialise();
	}

	@Override
	protected void doDispose() {
		helper.dispose();
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
			if(ChemistryCMISUtils.isDocument(o)){
				((Document)o).delete();
			}
		}
	}

	@Override
	public String cleanToLocalDir(String dirToClean) {
		Folder folderToClean = (Folder) helper.getSession().getObjectByPath(dirToClean);
		
		//create the local archive directories before archiving
		DateFormat dateFormat = new SimpleDateFormat(DEFAULT_CLEAN_DIR_DATE_FORMAT);
		Folder quarkTrashFolder = helper.createFolderIfNotExists(dirToClean, DEFAULT_CLEAN_DIR);
		Folder archiveFolder = helper.createFolderIfNotExists(quarkTrashFolder.getPath(), dateFormat.format(new Date()));
		_clean(folderToClean, archiveFolder, helper);
		return archiveFolder.getPath();
	}
	
	private void _clean(String dirToClean, String archiveDir, ChemistryCMISHelper helper){
		Session session = helper.getSession();
		_clean((Folder) session.getObjectByPath(dirToClean),
				(Folder) session.getObjectByPath(archiveDir),
				helper);
	}
	
	private void _clean(Folder dirToClean, Folder archiveDir, ChemistryCMISHelper helper){
		Session session = helper.getSession();
		ObjectId cmisToClean = session.createObjectId(dirToClean.getId());
		ObjectId cmisArchive = session.createObjectId(archiveDir.getId());
		
		ItemIterable<CmisObject> list = dirToClean.getChildren();
		for(CmisObject o : list){
			if(ChemistryCMISUtils.isDocument(o)){
				((Document)o).move(cmisToClean, cmisArchive);
			}
		}
	}
	
	private ChemistryCMISHelper createCmisHelper(){
		return new ChemistryCMISHelper(context);
	}

}
