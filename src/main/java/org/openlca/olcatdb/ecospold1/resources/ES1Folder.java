package org.openlca.olcatdb.ecospold1.resources;

import java.io.File;

import org.openlca.olcatdb.ResourceFolder;

/**
 * The format folder of the EcoSpold format.
 * 
 * @author Michael Srocka
 *
 */
public class ES1Folder extends ResourceFolder {

	/**
	 * Creates a new instance of the EcoSpold 01 format folder.
	 * 
	 * @param rootDir the root directory of the folder.
	 */
	public ES1Folder(File rootDir) {
		super(new File(rootDir, "EcoSpold01"));
	}

	@Override
	public void createContent() {		

		// create and extract the schema files
		File schemaFolder = new File(rootDir, "schemas");
		extract("schemas", schemaFolder);

		// File masterDataFolder = new File(es1Dir, "masterdata");
		// extract(masterDataFolder, "masterdata");

		// create and extract the schema files
		File styleSheetFolder = new File(rootDir, "stylesheets");
		extract("stylesheets", styleSheetFolder);

		// folder where the (created) processes are located
		File processFolder = new File(rootDir, "processes");
		if (!processFolder.exists())
			processFolder.mkdirs();
		
	}	

	/**
	 * Get the folder where the (created) processes are located.
	 */
	public File getProcessFolder() {
		return new File(rootDir, "processes");
	}

}
