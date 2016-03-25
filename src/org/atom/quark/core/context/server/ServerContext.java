package org.atom.quark.core.context.server;

import org.atom.quark.core.context.base.HelperContext;

/**
 * An abstract Descriptor used to represent a connection to a server.
 * A server descriptor is identified by its host and port. 
 * @author Pierre Beucher
 *
 */
public class ServerContext implements HelperContext{

	private String host;
	
	private int port;
	
	/**
	 * Empty constructor. Use setters to define context.
	 */
	public ServerContext() {
		super();
	}

	/**
	 * 
	 * @param host hostname or IP of the server
	 * @param port port used to connect on server
	 */
	public ServerContext(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
