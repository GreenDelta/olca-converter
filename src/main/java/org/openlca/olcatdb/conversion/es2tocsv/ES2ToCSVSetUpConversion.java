package org.openlca.olcatdb.conversion.es2tocsv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.conversion.AbstractConversionImpl;
import org.openlca.olcatdb.conversion.csv.resources.CSVFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.masterdata.ES2MDDataSet;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.swing.ES2CompartmentPanel;
import org.openlca.olcatdb.swing.ES2ElecMappingPanel;
import org.openlca.olcatdb.swing.ES2GeoMappingPanel;

import com.greendeltatc.simapro.csv.CSVWriter;
import com.greendeltatc.simapro.csv.model.SPDataEntry;
import com.greendeltatc.simapro.csv.model.SPDataSet;
import com.greendeltatc.simapro.csv.model.SPInputParameter;
import com.greendeltatc.simapro.csv.model.SPProcess;
import com.greendeltatc.simapro.csv.model.SPReferenceProduct;
import com.greendeltatc.simapro.csv.model.SPUnit;
import com.greendeltatc.simapro.csv.model.SPWasteSpecification;
import com.greendeltatc.simapro.csv.model.SPWasteTreatment;
import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.Geography;
import com.greendeltatc.simapro.csv.model.types.SubCompartment;

/**
 * Converts EcoSpold 02 to SimaProCSV.
 * 
 * @author Imo Graf
 * 
 */
public class ES2ToCSVSetUpConversion extends AbstractConversionImpl {

	/**
	 * 
	 */
	private CSVWriter writer = new CSVWriter('.');

	/**
	 * Contains the general data from all files.
	 */
	private SPDataSet generalData = new SPDataSet("");

	/**
	 * 
	 */
	private boolean splitCSV = false;

	/**
	 * The folder where the created CSV data sets are stored.
	 */
	private CSVFolder folder;

	/**
	 * The index for all activities
	 */
	private Map<String, ActivityEntry> activityIndex = new HashMap<String, ActivityEntry>();

	/**
	 * Index for the parameters
	 */
	private Map<String, SPInputParameter> paramterIndex = new HashMap<String, SPInputParameter>();

	/**
	 * The mapping list for the compartments
	 */
	private Map<String, CompartmentModel> compartments = new HashMap<String, CompartmentModel>();

	/**
	 * The mapping list for the geography
	 */
	private Map<String, Geography> geoMap = new HashMap<String, Geography>();

	/**
	 * 
	 */
	private Set<String> electricityUnits = new HashSet<String>();

	/**
	 * 
	 */
	private ActivityType activityType;

	/**
	 * 
	 */
	private ES2MDDataSet md = new ES2MDDataSet();

	ProductNameSavePreferences saves = null;

	private File masterDataZip;

	private boolean writeRef = false;

	private DataSetReference reference;

	private OverwriteFile overwriteFile = null;

	public ES2ToCSVSetUpConversion(boolean splitCSV, String lang,
			File masterDataZip) {
		this.splitCSV = splitCSV;
		ConversionUtils.lang = lang;
		this.masterDataZip = masterDataZip;
		saves = ProductNameSavePreferences.readDatabase();
	}

	/**
	 * 
	 * @param splitFiles
	 */
	public void setSplitFiles(boolean splitFiles) {
		this.splitCSV = splitFiles;
	}

	@Override
	public void createFolder(File targetDir) {
		folder = new CSVFolder(targetDir);
		logFile(folder);
	}

	@Override
	public ResourceFolder getResult() {
		return folder;
	}

	@Override
	public void run() {
		// create the output folder
		createFolder(targetDir);

		// Read master data
		monitor.progress("Read the master data ...", 0);
		try {
			md.read(masterDataZip);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// create index
		monitor.progress("Indexing files ...", 0);
		createIndex();

		// mapping files
		setMappings();

		// set up the parser
		XmlContextParser parser = new XmlContextParser();

		// iterate over the data streams
		while (this.hasNext()) {
			InputStream is = this.next();
			if (is != null) {
				try {
					// parse and convert the data sets
					ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class,
							is);
					is.close();
					if (ecoSpold != null) {

						for (ES2Dataset dataset : ecoSpold.getDatasets()) {
							if (overwriteFile == OverwriteFile.BREAK)
								break;

							monitor.progress("Convert "
									+ dataset.description.id, 0);
							DataSetMerger merger = new DataSetMerger();
							if (dataset.isChildDataSet)
								merger.mergeChildAndParentDataset(dataset,
										activityIndex, masterDataZip,
										sourceZip, logger);
							DataSetReference reference = convert(dataset);
							if (reference != null)
								createdFiles.add(reference);
						}
					}

				} catch (Exception e) {
					logger.severe("Parse exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		// write general data
		if (!splitCSV)
			try {
				writer.write(targetDir, new SPDataSet(""), true, true);
			} catch (IOException e) {
				e.printStackTrace();
			}

		flush();
	}

	private void setMappings() {
		geoMap = ES2MappingFileUtil.readGeography(ES2GeoMappingPanel
				.getInstance().getRows());

		for (Geography geography : Geography.values()) {
			geoMap.put(geography.getValue(), geography);
		}

		for (CompartmentModel m : ES2MappingFileUtil
				.readCompartments(ES2CompartmentPanel.getInstance().getRows())) {
			compartments.put(m.getEs2Compartment() + m.getEs2Subcompartment(),
					m);
		}

		// Add default compartments
		for (ElementaryFlowType flowType : ElementaryFlowType.values()) {
			CompartmentModel m = new CompartmentModel();
			String typeName = flowType.getValue();
			m.setSimaProElemType(typeName);
			m.setEs2Compartment(typeName);
			compartments.put(typeName, m);

			for (SubCompartment subCompartment : flowType.getSubCompartments()) {
				CompartmentModel m2 = new CompartmentModel();
				String subName = subCompartment.getValue();
				m2.setSimaProElemType(typeName);
				m2.setEs2Compartment(typeName);
				m2.setEs2Subcompartment(subName);
				m2.setSimaProSubcompartment(subName);
				compartments.put(
						m2.getEs2Compartment() + m2.getEs2Subcompartment(), m2);

			}
		}

		electricityUnits.addAll(ES2MappingFileUtil
				.readElecricityUnits(ES2ElecMappingPanel.getInstance()
						.getRows()));

	}

	private void createIndex() {
		XmlContextParser parser = new XmlContextParser();

		try {

			if (sourceXml != null) {
				InputStream stream = new FileInputStream(sourceXml);
				ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class,
						stream);
				if (ecoSpold != null) {

					for (ES2Dataset dataset : ecoSpold.getDatasets()) {
						addActivityToIndex(dataset);
						createParameterIndex(dataset);
					}

				}

			} else if (sourceZip != null) {
				ZipFile zf = sourceZip;
				Enumeration<?> entries = zf.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();

					if (entry.getName().endsWith(".spold")) {
						InputStream stream = sourceZip.getInputStream(entry);

						ES2EcoSpold ecoSpold = parser.getContext(
								ES2EcoSpold.class, stream);
						if (ecoSpold != null) {

							for (ES2Dataset dataset : ecoSpold.getDatasets()) {
								addActivityToIndex(dataset);
								createParameterIndex(dataset);
							}
						}
					}
				}

			}

		} catch (Exception e) {
			logger.severe("Parse exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void createParameterIndex(ES2Dataset dataset) {

		for (ES2IntermediateExchange exchange : dataset
				.getIntermediateExchanges()) {

			if (exchange.variableName != null
					&& !exchange.variableName.equals("")) {

				SPInputParameter parameter = new SPInputParameter(
						exchange.variableName, exchange.amount);

				paramterIndex.put(exchange.variableName, parameter);

				// comment
				parameter.setComment("From Activity: "
						+ ConversionUtils.getLangEntry(dataset.description
								.getName()) + " Exchange: "
						+ ConversionUtils.getLangEntry(exchange.getName()));
			}

		}

		for (ES2ElementaryExchange exchange : dataset.getElementaryExchanges()) {

			if (exchange.variableName != null
					&& !exchange.variableName.equals("")) {

				SPInputParameter parameter = new SPInputParameter(
						exchange.variableName, exchange.amount);

				paramterIndex.put(exchange.variableName, parameter);

				// comment
				parameter.setComment("From Activity: "
						+ ConversionUtils.getLangEntry(dataset.description
								.getName()) + " Exchange: "
						+ ConversionUtils.getLangEntry(exchange.getName()));
			}

		}
	}

	private void addActivityToIndex(ES2Dataset dataset) {
		ActivityEntry entry = new ActivityEntry();
		activityIndex.put(dataset.description.id, entry);
		entry.fileName = getCurrentFileName();
		boolean foundRefFlow = false;
		entry.id = dataset.description.id;
		if (entry.id == null || entry.id.equals(""))
			throw new IllegalArgumentException("Null activity id in file: "
					+ entry.fileName);

		for (ES2IntermediateExchange exchange : dataset
				.getIntermediateExchanges()) {
			if (exchange.outputGroup != null && exchange.outputGroup == 0) {
				if (!foundRefFlow) {
					foundRefFlow = true;
					entry.refFlowId = exchange.id;
					entry.refFlowName = setFullRefFlowName(exchange, dataset);
					entry.refFlowUnit = new SPUnit(
							ConversionUtils.getLangEntry(exchange
									.getUnitNames()));
					entry.refFlowAmount = String.valueOf(exchange.amount);
					if (exchange.amount < 0)
						entry.activityType = ActivityType.WASTE_TREATMENT;

				} else {
					entry.changeProducts.add(exchange.id);
				}
			}
		}
		if (!foundRefFlow)
			logger.severe("For \""
					+ ConversionUtils.getLangEntry(dataset.description
							.getName())
					+ "\" no reference flow (outputGroup=0) exist.");
	}

	private String setFullRefFlowName(ES2IntermediateExchange exchange,
			ES2Dataset dataset) {
		String name = ConversionUtils.getLangEntry(exchange.getName());

		String geo = ConversionUtils.getLangEntry(dataset.geography
				.getShortNames());

		String activityName = ConversionUtils.getLangEntry(dataset.description
				.getName());

		String systemModelName = null;
		if (dataset.representativeness != null)
			systemModelName = ConversionUtils
					.getLangEntry(dataset.representativeness
							.getSystemModelName());

		if (systemModelName == null)
			systemModelName = "";

		int t = dataset.description.type;
		String type = "";
		switch (t) {
		case 1:
			type = "S";
			break;
		case 2:
			type = "U";
			break;
		}

		return saves.getSortedName(name, geo, activityName, systemModelName,
				type);
	}

	private DataSetReference convert(ES2Dataset dataset) throws IOException,
			IllegalAccessException {
		ActivityEntry entry = activityIndex.get(dataset.description.id);
		if (entry == null)
			throw new IllegalArgumentException("Activity \""
					+ ConversionUtils.getLangEntry(dataset.description
							.getName()) + "\" in index not found.");
		activityType = entry.activityType;

		String project;
		if (splitCSV)
			project = dataset.description.id;
		else
			project = "ES2 Conversion";

		SPDataSet spDataSet = new SPDataSet(project);
		SPDataEntry dataEntry = null;

		switch (activityType) {
		case PROCESS:
			SPProcess process = new SPProcess(new SPReferenceProduct(
					entry.refFlowName, entry.refFlowUnit, entry.refFlowAmount));
			dataEntry = (SPDataEntry) process;
			break;

		case WASTE_TREATMENT:
			SPWasteTreatment wasteTreatment = new SPWasteTreatment(
					new SPWasteSpecification(entry.refFlowName,
							entry.refFlowUnit, entry.refFlowAmount));
			dataEntry = (SPDataEntry) wasteTreatment;
			break;
		}

		// convert

		Conversion conversion = new Conversion(paramterIndex, activityType,
				logger);

		conversion.description(dataEntry, dataset);
		conversion.geography(dataEntry, dataset, geoMap);
		conversion.timePeriod(dataEntry, dataset);
		conversion.technology(dataEntry, dataset);
		conversion.paramter(dataEntry, dataset);
		conversion.intermediateExchanges(dataEntry, dataset, entry,
				electricityUnits);
		conversion.elementaryExchanges(dataEntry, dataset, compartments);

		// set comments
		ConversionComment.documentationComment(dataEntry, dataset);
		ConversionComment.verification(dataEntry, dataset);
		ConversionComment.record(dataEntry, dataset);
		ConversionComment.generator(dataEntry, dataset);
		ConversionComment.collectionMethodANDDataTreatment(dataEntry, dataset);
		ConversionComment.allocationRules(dataEntry, dataset);
		ConversionComment.literatureReference(dataEntry, dataset);

		// add
		switch (activityType) {
		case PROCESS:
			spDataSet.add((SPProcess) dataEntry);
			break;

		case WASTE_TREATMENT:
			spDataSet.add((SPWasteTreatment) dataEntry);
			break;
		}

		if (!writeRef) {
			reference = new DataSetReference();
			if (spDataSet.getProject() != null)
				reference.setRefObjectId(spDataSet.getProject());
			reference.setType("process data set");

			try {
				String uri = folder.getRootDir() + File.separator
						+ spDataSet.getProject() + ".csv";
				reference.setUri(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// the (first) name of the created process
			reference.setName(spDataSet.getProject());

		} else {
			reference = null;
		}

		File outFile = new File(folder.getRootDir() + File.separator + project
				+ ".csv");

		if (outFile.exists()) {
			if (!splitCSV) {
				if (overwriteFile != OverwriteFile.YES_TO_ALL) {
					String message = "The file "
							+ outFile.getAbsolutePath()
							+ " already exists. Do you want to overwrite the file?";
					Object buttons[] = { "Yes", "No" };
					int rc = JOptionPane.showOptionDialog(new JFrame(),
							message, "File exist", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, buttons,
							buttons[0]);
					switch (rc) {
					case 0:
						overwriteFile = OverwriteFile.YES_TO_ALL;
						writeDataset(spDataSet);
						break;

					case 1:
						overwriteFile = OverwriteFile.BREAK;
						reference = null;
						break;
					}

				} else {
					writeDataset(spDataSet);
				}
			} else {

				if (overwriteFile == OverwriteFile.YES_TO_ALL) {
					writeDataset(spDataSet);
				} else if (overwriteFile == OverwriteFile.NO_TO_ALL) {
					reference = null;
				} else {

					String message = "The file "
							+ outFile.getAbsolutePath()
							+ " already exists. Do you want to overwrite the file?";
					Object buttons[] = { "Yes", "Yes to all", "No", "No to all" };
					int rc = JOptionPane.showOptionDialog(new JFrame(),
							message, "File exist", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, buttons,
							buttons[0]);

					switch (rc) {
					// no click
					case -1:
						System.out
								.println("Dialog closed without clicking a button.");
						break;

					// yes
					case 0:
						writeDataset(spDataSet);
						break;

					// yes to all
					case 1:
						overwriteFile = OverwriteFile.YES_TO_ALL;
						writeDataset(spDataSet);
						break;

					// no
					case 2:
						reference = null;
						break;

					// no to all
					case 3:
						overwriteFile = OverwriteFile.NO_TO_ALL;
						reference = null;
						break;
					}

				}
			}
		} else {
			writeDataset(spDataSet);
		}

		return reference;
	}

	private void writeDataset(SPDataSet spDataSet) throws IOException {
		if (splitCSV) {
			writer.write(folder.getRootDir(), spDataSet);
		} else {
			writer.write(folder.getRootDir(), spDataSet, true, false);
			writeRef = true;
		}
	}

	private enum OverwriteFile {
		YES_TO_ALL, NO_TO_ALL, BREAK
	}

}
