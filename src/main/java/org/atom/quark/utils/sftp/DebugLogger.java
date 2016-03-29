package org.atom.quark.utils.sftp;

public class DebugLogger implements com.jcraft.jsch.Logger {
    static java.util.Hashtable<Integer, String> name=new java.util.Hashtable<Integer, String>();
    static{
      name.put(new Integer(DEBUG), "DEBUG: ");
      name.put(new Integer(INFO), "INFO: ");
      name.put(new Integer(WARN), "WARN: ");
      name.put(new Integer(ERROR), "ERROR: ");
      name.put(new Integer(FATAL), "FATAL: ");
    }
    public boolean isEnabled(int level){
      return true;
    }
    public void log(int level, String message){
      System.err.print(name.get(new Integer(level)));
      System.err.println(message);
    }
  }
