package org.openlca.olcatdb.conversion.csv.resources;

import java.io.File;

import org.openlca.olcatdb.ResourceFolder;

public class CSVFolder extends ResourceFolder {

	public CSVFolder(File rootDir) {
		super(new File(rootDir, "CSV SimaPro"));
	}

	@Override
	public void createContent() {
		// nothing to do
	}

}
