package org.openlca.olcatdb.ecospold2.resources;

import java.io.File;

import org.openlca.olcatdb.ResourceFolder;

/**
 * The folder definition of the EcoSpold 02 format. The folder has the following
 * definition: <br/>
 * <code>
 * + Content <br/>
 * --- + MasterData <br/>
 * + Schemas <br/>
 * --- + MasterData <br/>
 * </code> <br/>
 * The activity data sets are located directly under the 'Content' folder.
 * 
 * @author Michael Srocka
 */
public class ES2Folder extends ResourceFolder {

	/**
	 * Creates a new EcoSpold 02 folder.
	 */
	public ES2Folder(File rootDir) {
		super(new File(rootDir, "EcoSpold02"));
	}

	@Override
	public void createContent() {

		// create the content and master data folder
		File mdFolder = new File(rootDir, "Content/MasterData");
		if (!mdFolder.exists()) {
			mdFolder.mkdirs();
		}
		extract("masterdata", mdFolder);

		// create and extract the schema folders and files
		File schemaFolder = new File(rootDir, "Schemas");
		extract("schemas", schemaFolder);
		File schemaMdFolder = new File(schemaFolder, "MasterData");
		extract("schemas/masterdata", schemaMdFolder);
		File schemaChildFolder = new File(schemaFolder, "Child");
		extract("schemas/child", schemaChildFolder);

		// create the folder for pictures
		File pictFolder = new File(rootDir, "Pictures");
		if (!pictFolder.exists()) {
			pictFolder.mkdirs();
		}
	}

	/**
	 * Get the folder where the activity data sets are stored.
	 */
	public File getActivityFolder() {
		return new File(rootDir, "Content");
	}

	/**
	 * Get the folder where the master-data are stored.
	 */
	public File getMasterDataFolder() {
		return new File(rootDir, "Content/MasterData");
	}

	/**
	 * Get the folder where the pictures are stored.
	 */
	public File getPictureFolder() {
		return new File(rootDir, "Pictures");
	}

}
