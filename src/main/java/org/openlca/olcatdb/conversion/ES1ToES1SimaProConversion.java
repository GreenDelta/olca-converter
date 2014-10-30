package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.ecospold1.ES1Dataset;
import org.openlca.olcatdb.ecospold1.ES1EcoSpold;
import org.openlca.olcatdb.ecospold1.resources.ES1Folder;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

public class ES1ToES1SimaProConversion extends AbstractConversionImpl {

	/**
	 * SimaPro sub converter
	 */
	private ES1SimaProValidConversion simaProConverter = new ES1SimaProValidConversion();

	/**
	 * The folder where the created EcoSpold 01 data sets are stored.
	 */
	private ES1Folder es1Folder = null;

	@Override
	public void createFolder(File targetDir) {
		super.createFolder(targetDir);
		this.es1Folder = new ES1Folder(targetDir);
		es1Folder.createContent();
		logFile(es1Folder);
	}

	@Override
	public ResourceFolder getResult() {
		return es1Folder;
	}

	@Override
	public void run() {
		// create the output folder
		createFolder(targetDir);

		// set up the parser
		XmlContextParser parser = new XmlContextParser();

		// iterate over the data streams
		while (this.hasNext()) {
			InputStream is = this.next();
			if (is != null) {
				try {

					// parse and convert the data sets
					ES1EcoSpold ecoSpold = parser.getContext(ES1EcoSpold.class,
							is);
					is.close();
					if (ecoSpold != null) {
						for (ES1Dataset dataset : ecoSpold.getDatasets()) {
							if (dataset.isProcess()) {
								DataSetReference reference = convert(dataset);
								if (reference != null)
									createdFiles.add(reference);
							}
						}
					}

				} catch (Exception e) {
					logger.severe("Parse exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		// finish the conversion
		flush();

	}

	private DataSetReference convert(ES1Dataset dataSet) {

		// Convert the data set into a valid SimaPro data set
		simaProConverter.validate(dataSet);

		ES1EcoSpold ecoSpold = new ES1EcoSpold();
		ecoSpold.getDatasets().add(dataSet);

		// write the file and create a data set reference
		DataSetReference ref = null;
		String uuid = UUID.randomUUID().toString();
		File outFile = new File(es1Folder.getProcessFolder(), uuid + ".xml");
		if (outFile.exists()) {
			logger.severe("The file " + outFile.getAbsolutePath()
					+ " already exists.");
		} else {
			XmlOutputter outputter = new XmlOutputter();
			outputter.output(ecoSpold, TemplateType.EcoSpold01, outFile, true);

			ref = new DataSetReference();
			String name = "";
			if (dataSet.referenceFunction != null)
				name = dataSet.referenceFunction.name;
			else
				name = outFile.getName();

			ref.setName(name);
			ref.setUri(outFile.toURI().toString());
			ref.setRefObjectId(uuid);
			ref.setType(ILCDDataSetType.Process.toString());
			ref.setVersion("01.00.000");
		}

		return ref;
	}

}
