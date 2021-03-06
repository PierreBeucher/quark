package com.github.pierrebeucher.quark.core.context.authentication;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.pierrebeucher.quark.core.context.authentication.LoginAuthContext;

public class LoginAuthContextTest {

  @Test
  public void LoginAuthContextString() {
    Assert.assertEquals(new LoginAuthContext().getLogin(), null);
  }

  @Test
  public void LoginAuthContext() {
	  LoginAuthContext ctx = new LoginAuthContext("login");
	  Assert.assertEquals(ctx.getLogin(), "login");
  }
  
  @Test
  public void LoginAuthContextCopy() {
	  LoginAuthContext base = new LoginAuthContext("login");
	  LoginAuthContext ctx = new LoginAuthContext(base);
	  Assert.assertEquals(ctx.getLogin(), "login");
  }
  
  @Test
  public void login(){
	  LoginAuthContext ctx = new LoginAuthContext();
	  ctx.setLogin("login");
	  Assert.assertEquals(ctx.getLogin(), "login");
  }
}
