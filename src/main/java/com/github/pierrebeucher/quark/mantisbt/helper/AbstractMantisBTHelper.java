package com.github.pierrebeucher.quark.mantisbt.helper;

import java.math.BigInteger;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import com.github.pierrebeucher.quark.core.helper.AbstractLifecycleHelper;
import com.github.pierrebeucher.quark.core.helper.Helper;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;
import com.github.pierrebeucher.quark.mantisbt.utils.MantisBTClient;

public class AbstractMantisBTHelper extends AbstractLifecycleHelper<MantisBTContext> implements Helper, Initialisable {
	
	protected MantisBTClient client;
	
	protected BigInteger projectId;
	
	public AbstractMantisBTHelper() {
		this(new MantisBTContext());
	}

	public AbstractMantisBTHelper(MantisBTContext context) {
		super(context);
	}

	@Override
	public void initialise() throws InitialisationException {
		lifecycleManager.initialise();
		try{
			this.client = buildClient();
			this.projectId = initProjectId();
		} catch(ServiceException | RemoteException e){
			throw new InitialisationException(e);
		}
	}

	@Override
	public boolean isInitialised() {
		return lifecycleManager.isInitialised();
	}
	
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
