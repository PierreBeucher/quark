package org.atom.quark.mantisbt.helper;

import java.math.BigInteger;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import org.atom.quark.core.helper.AbstractHelper;
import org.atom.quark.core.helper.Helper;
import org.atom.quark.mantisbt.context.MantisBTContext;
import org.atom.quark.mantisbt.utils.MantisBTClient;

public class AbstractMantisBTHelper extends AbstractHelper<MantisBTContext> implements Helper {

	protected MantisBTClient client;
	
	protected BigInteger projectId;
	
	public AbstractMantisBTHelper() {
		super(new MantisBTContext());
	}

	
	public AbstractMantisBTHelper(MantisBTContext context) throws RemoteException, ServiceException {
		super(context);
		init();
	}
	
	/**
	 * Init this Helper using its current context.
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public AbstractMantisBTHelper init() throws ServiceException, RemoteException{
		this.client = buildClient();
		this.projectId = initProjectId();
		return this;
	}
	
	/**
	 * Create a new MantisBTClient if this Helper is ready.
	 * @return a new client
	 * @throws ServiceException 
	 */
	protected MantisBTClient buildClient() throws ServiceException{
		if(isReady()){
			return new MantisBTClient(context.getUrl(),
					context.getAuthContext().getLogin(),
					context.getAuthContext().getPassword());
		} else {
			throw new RuntimeException("Cannot build a client if Helper is not ready.");
		}
	}
	
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
	
	@Override
	public void setContext(MantisBTContext context) throws Exception {
		super.setContext(context);
		init();
	}

	public void setClient(MantisBTClient client) {
		this.client = client;
	}

	public MantisBTClient getClient() {
		return client;
	}

	public BigInteger getProjectId() {
		return projectId;
	}

}
