package com.github.pierrebeucher.quark.mantisbt.helper;

import java.math.BigInteger;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.helper.InitializationException;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient;

public class AbstractMantisBTHelper extends AbstractLifecycleHelper<MantisBTContext> implements Helper {

	protected MantisBTClient client;
	
	protected BigInteger projectId;
	
	public AbstractMantisBTHelper() {
		super(new MantisBTContext());
	}

	public AbstractMantisBTHelper(MantisBTContext context) {
		super(context);
	}
	
	@Override
	protected void doInitialise() throws InitializationException {
		try{
			this.client = buildClient();
			this.projectId = initProjectId();
		} catch(ServiceException | RemoteException e){
			throw new InitializationException(e);
		}
	}

	@Override
	protected void doDispose() {
		//do nothing
	}

//	/**
//	 * Init this Helper using its current context.
//	 * @throws ServiceException 
//	 * @throws RemoteException 
//	 */
//	public AbstractMantisBTHelper initialise() throws ServiceException, RemoteException{
//		this.client = buildClient();
//		this.projectId = initProjectId();
//		return this;
//	}
	
	/**
	 * Create a new MantisBTClient if this Helper is ready.
	 * @return a new client
	 * @throws ServiceException 
	 */
	protected MantisBTClient buildClient() throws ServiceException{
		return new MantisBTClient(context.getUrl(),
				context.getAuthContext().getLogin(),
				context.getAuthContext().getPassword());
	}
	
	/**
	 * Use the Helper client to retrieve the projectID using the configured project name. 
	 * @return
	 * @throws RemoteException
	 */
	protected BigInteger initProjectId() throws RemoteException{
		return this.client.mc_project_get_id_from_name(context.getProjectName());
	}

	/**
	 * MantisBTHelper is ready if its URL, username and password are set properly.
	 */
	@Override
	public boolean isReady() {
		return context.getAuthContext().getLogin() != null
				&& context.getAuthContext().getPassword() != null
				&& context.getUrl() != null;
	}

	public MantisBTClient getClient() {
		return client;
	}

	public BigInteger getProjectId() {
		return projectId;
	}

}
