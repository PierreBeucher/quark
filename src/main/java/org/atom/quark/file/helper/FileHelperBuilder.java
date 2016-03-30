package org.atom.quark.file.helper;

import org.atom.quark.core.helper.AbstractHelperBuilder;
import org.atom.quark.file.context.FileContext;

public class FileHelperBuilder extends AbstractHelperBuilder<FileContext, FileHelper>{

	public FileHelperBuilder(FileContext baseContext) {
		super(baseContext);
	}

	@Override
	protected FileHelper buildBaseHelper() {
		return FileHelper.helper(baseContext.getFile(), baseContext.getCharset());
	}

}
