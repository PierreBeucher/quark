package com.github.pierrebeucher.quark.file.helper;

import com.github.pierrebeucher.quark.core.helper.AbstractHelperBuilder;
import com.github.pierrebeucher.quark.file.context.FileContext;

public class FileHelperBuilder extends AbstractHelperBuilder<FileContext, FileHelper>{

	public FileHelperBuilder(FileContext baseContext) {
		super(baseContext);
	}

	@Override
	protected FileHelper buildBaseHelper() {
		return FileHelper.helper(baseContext.getFile(), baseContext.getCharset());
	}

}
