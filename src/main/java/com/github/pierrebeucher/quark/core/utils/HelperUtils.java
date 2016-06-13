package com.github.pierrebeucher.quark.core.utils;

import java.util.Collection;

import com.github.pierrebeucher.quark.core.lifecycle.Disposable;
import com.github.pierrebeucher.quark.core.lifecycle.Initialisable;
import com.github.pierrebeucher.quark.core.lifecycle.InitialisationException;

/**
 * <code>Helper</code> utility functions.
 * @author pierreb
 *
 */
public class HelperUtils {

	private HelperUtils(){
	}
	
	public static void initialiseAll(Collection<? extends Initialisable> col) throws InitialisationException{
		for(Initialisable o : col){
			o.initialise();
		}
	}
	
	public static void disposeAll(Collection<? extends Disposable> col){
		for(Disposable o : col){
			o.dispose();
		}
	}
}
	

