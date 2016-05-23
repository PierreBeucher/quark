package com.github.pierrebeucher.quark.mantisbt.helper;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.github.pierrebeucher.quark.core.helper.AbstractHelperBuilder;
import com.github.pierrebeucher.quark.core.helper.HelperBuilder;
import com.github.pierrebeucher.quark.mantisbt.context.MantisBTContext;

public class MantisBTHelperBuilder extends AbstractHelperBuilder<MantisBTContext, MantisBTHelper>
		implements HelperBuilder {

	public MantisBTHelperBuilder() {
		super(new MantisBTContext());
	}
	
	public MantisBTHelperBuilder(MantisBTContext baseContext) {
		super(baseContext);
	}
	
	@Override
	public void setBaseContext(MantisBTContext baseContext) {
		super.setBaseContext(baseContext);
	}

	@Override
	protected MantisBTHelper buildBaseHelper() {
		return new MantisBTHelper()
				.url(baseContext.getUrl())
				.username(baseContext.getAuthContext().getLogin())
				.password(baseContext.getAuthContext().getPassword())
				.project(baseContext.getProjectName());
	}
	
	protected MantisBTContext cloneBaseContext(){
		MantisBTContext ctx = new MantisBTContext();
		ctx.getAuthContext().setLogin(baseContext.getAuthContext().getLogin());
		ctx.getAuthContext().setPassword(baseContext.getAuthContext().getPassword());
		ctx.setProjectName(baseContext.getProjectName());
		ctx.setUrl(baseContext.getUrl());
		return ctx;
	}
	
	/**
	 * Build a MantisBTHelper using this builder base context.
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public MantisBTHelper build() throws RemoteException, ServiceException{
		return buildBaseHelper();
	}
	
	/**
	 * Build a MantisBTHelper using this builder base context, overriding
	 * any existing URL.
	 * @param url
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public MantisBTHelper build(URL url) throws RemoteException, ServiceException{
		MantisBTHelper helper = buildBaseHelper().url(url);
		return helper;
	}
	
	/**
	 * Build a MantisBTHelper using this builder base context,
	 * overriding the username and password.
	 * @param username
	 * @param password
	 * @return
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public MantisBTHelper build(String username, String password) throws RemoteException, ServiceException{
		MantisBTHelper helper = buildBaseHelper().username(username).password(password);
		return helper;
	}

}
