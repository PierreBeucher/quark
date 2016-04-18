package com.github.pierrebeucher.quark.cmis.helper;

import com.github.pierrebeucher.quark.cmis.context.CMISBindingContext;
import com.github.pierrebeucher.quark.core.helper.Helper;

public interface CMISHelper extends Helper {
	
	public CMISHelper bindingContext(CMISBindingContext bindingContext);
	
}
