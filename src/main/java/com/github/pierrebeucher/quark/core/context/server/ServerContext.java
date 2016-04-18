package com.github.pierrebeucher.quark.core.context.server;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * An abstract Descriptor used to represent a connection to a server.
 * A server descriptor is identified by its host and port. 
 * @author Pierre Beucher
 *
 */
public abstract class ServerContext implements HelperContext{

	private String host;
	
	private int port;
	
	/**
	 * Empty constructor. Use setters to define context. Port is set to -1.
	 */
	public ServerContext() {
		super();
		this.port = -1;
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
	
	public abstract URI toUri() throws URISyntaxException;
	
}
