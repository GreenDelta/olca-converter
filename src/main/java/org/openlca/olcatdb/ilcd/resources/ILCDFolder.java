package org.openlca.olcatdb.ilcd.resources;

import java.io.File;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;

/**
 * Specification of an ILCD directory. An ILCD directory has the following form:
 * 
 * <ul>
 * <li>root directory</li>
 * <ul>
 * <li>ILCD</>
 * <ul>
 * <li>contacts</li>
 * <li>external_docs</li>
 * <li>flowproperties</li>
 * <li>flows</li>
 * <li>processes</li>
 * <li>sources</li>
 * <li>unitgroups</li>
 * </ul>
 * <li>schemas</li>
 * <li>stylesheets</li>
 * </ul>
 * </ul>
 * 
 * The ILCD directory is also called the 'data directory'.
 * 
 * @author Michael Srocka
 */
public class ILCDFolder extends ResourceFolder {

	public ILCDFolder(File rootDir) {
		super(new File(rootDir, "ILCD"));
	}

	@Override
	public void createContent() {

		// schema files
		File folder = new File(rootDir, "schemas");
		extract("schemas", folder);

		// style-sheets
		folder = new File(rootDir, "stylesheets");
		extract("stylesheets", folder);

		// ILCD manifest
		folder = new File(rootDir, "META-INF");
		extract("metainf", folder);

		// data directory
		File dataDir = new File(rootDir, "ILCD");
		extract("data", dataDir);

		// contacts
		folder = new File(dataDir, "contacts");
		extract("contacts", folder);

		// external documents
		folder = new File(dataDir, "external_docs");
		extract("external_docs", folder);

		// flow properties
		folder = new File(dataDir, "flowproperties");
		extract("flowproperties", folder);

		// flows
		folder = new File(dataDir, "flows");
		if (!folder.exists())
			folder.mkdir();

		// processes
		folder = new File(dataDir, "processes");
		if (!folder.exists())
			folder.mkdir();

		// sources
		folder = new File(dataDir, "sources");
		extract("sources", folder);

		// unit groups
		folder = new File(dataDir, "unitgroups");
		extract("unitgroups", folder);

	}

	/**
	 * Get the contact directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/contacts.
	 */
	public File getContactDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "contacts");
	}

	public File getDataDir() {
		return new File(rootDir, "ILCD");
	}

	/**
	 * Get the contact directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/external_docs.
	 */
	public File getExternalDocDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "external_docs");
	}

	/**
	 * Get the flow directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/flows.
	 */
	public File getFlowDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "flows");
	}

	/**
	 * Get the flow property directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/flowproperties.
	 */
	public File getFlowPropDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "flowproperties");
	}

	/**
	 * Get the process directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/processes.
	 */
	public File getProcessDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "processes");
	}

	/**
	 * Get the directory where the schemas are located.
	 */
	public File getSchemaDir() {
		return new File(rootDir, "schemas");
	}

	/**
	 * Get the source directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/sources.
	 */
	public File getSourceDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "sources");
	}

	/**
	 * Get the directory where the style-sheets are located.
	 */
	public File getStyleDir() {
		return new File(rootDir, "stylesheets");
	}

	/**
	 * Get the source directory of the ILCD folder: <br/>
	 * 'root directory'/ILCD/unitgroups.
	 */
	public File getUnitGroupDir() {
		File dataDir = new File(rootDir, "ILCD");
		return new File(dataDir, "unitgroups");
	}

	/**
	 * Returns true if a data set described by the given reference exists in
	 * this folder. For this test the type, UUID, and version of the reference
	 * are required as the naming conventions for stored files in ILCD are: <br/>
	 * ILCD/'folder of the referenced type'/'uuid'_'version'.xml
	 */
	public boolean exists(DataSetReference ref) {
		File f = file(ref);
		return f != null && f.exists();

	}

	/**
	 * Returns the file of the data set described by the given reference in this
	 * folder. For the lookup the type, UUID, and version of the reference are
	 * required as the naming conventions for stored files in ILCD are: <br/>
	 * ILCD/'folder of the referenced type'/'uuid'_'version'.xml
	 */
	public File file(DataSetReference ref) {
		File file = null;

		if (ref.getType() != null && ref.getVersion() != null
				&& ref.getRefObjectId() != null) {

			ILCDDataSetType type = ILCDDataSetType.forName(ref.getType());
			if (type != null) {

				File lookUpFolder = null;

				switch (type) {
				case Contact:
					lookUpFolder = this.getContactDir();
					break;
				case Flow:
					lookUpFolder = this.getFlowDir();
					break;
				case FlowProperty:
					lookUpFolder = this.getFlowPropDir();
					break;
				case Process:
					lookUpFolder = this.getProcessDir();
					break;
				case Source:
					lookUpFolder = this.getSourceDir();
					break;
				case UnitGroup:
					lookUpFolder = this.getUnitGroupDir();
					break;
				default:
					lookUpFolder = this.getExternalDocDir();
					break;
				}

				// file = new File(lookUpFolder, ref.getRefObjectId() + "_"
				// + ref.getVersion() + ".xml");
				file = new File(lookUpFolder, ref.getRefObjectId() + ".xml");
			}
		}

		return file;
	}

}