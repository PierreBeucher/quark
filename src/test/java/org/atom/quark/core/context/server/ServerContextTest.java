package org.atom.quark.core.context.server;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ServerContextTest {

	private static final String HOST = "localhost";
	private static final int PORT = 22;
	
	private ServerContext buidEmptyServerContext(){
		return new ServerContext(){
			@Override
			public URI toUri() throws URISyntaxException {
				return null;
			}
		};
	}
	
	@Test
	public void ServerContextEmptyConstructor() {
		ServerContext ctx = buidEmptyServerContext();

		Assert.assertEquals(ctx.getHost(), null);
		Assert.assertEquals(ctx.getPort(), -1);
	}
	
	@Test
	public void ServerContextStringint() {
		ServerContext ctx = new ServerContext(HOST, PORT){
			@Override
			public URI toUri() throws URISyntaxException {
				return null;
			}
		};

		Assert.assertEquals(ctx.getHost(), HOST);
		Assert.assertEquals(ctx.getPort(), PORT);
	}

	@Test
	public void host() {
		ServerContext ctx = buidEmptyServerContext();
		
		ctx.setHost(HOST);
		Assert.assertEquals(ctx.getHost(), HOST);
	}

	@Test
	public void port() {
		ServerContext ctx = buidEmptyServerContext();
		ctx.setPort(PORT);
		Assert.assertEquals(ctx.getPort(), PORT);
	}
}
