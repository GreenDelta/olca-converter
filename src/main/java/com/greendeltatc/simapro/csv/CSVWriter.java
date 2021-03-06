package com.greendeltatc.simapro.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.greendeltatc.simapro.csv.model.IDistribution;
import com.greendeltatc.simapro.csv.model.SPCalculatedParameter;
import com.greendeltatc.simapro.csv.model.SPDataSet;
import com.greendeltatc.simapro.csv.model.SPDocumentation;
import com.greendeltatc.simapro.csv.model.SPElementaryFlow;
import com.greendeltatc.simapro.csv.model.SPInputParameter;
import com.greendeltatc.simapro.csv.model.SPLiteratureReference;
import com.greendeltatc.simapro.csv.model.SPLiteratureReferenceEntry;
import com.greendeltatc.simapro.csv.model.SPLogNormalDistribution;
import com.greendeltatc.simapro.csv.model.SPProcess;
import com.greendeltatc.simapro.csv.model.SPProductFlow;
import com.greendeltatc.simapro.csv.model.SPQuantity;
import com.greendeltatc.simapro.csv.model.SPReferenceProduct;
import com.greendeltatc.simapro.csv.model.SPSystemDescription;
import com.greendeltatc.simapro.csv.model.SPUnit;
import com.greendeltatc.simapro.csv.model.SPWasteSpecification;
import com.greendeltatc.simapro.csv.model.SPWasteToTreatmentFlow;
import com.greendeltatc.simapro.csv.model.SPWasteTreatment;
import com.greendeltatc.simapro.csv.model.types.BoundaryWithNature;
import com.greendeltatc.simapro.csv.model.types.CutOffRule;
import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.DistributionType;
import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.Geography;
import com.greendeltatc.simapro.csv.model.types.ProcessAllocation;
import com.greendeltatc.simapro.csv.model.types.ProcessCategory;
import com.greendeltatc.simapro.csv.model.types.ProductFlowType;
import com.greendeltatc.simapro.csv.model.types.Representativeness;
import com.greendeltatc.simapro.csv.model.types.Status;
import com.greendeltatc.simapro.csv.model.types.Substitution;
import com.greendeltatc.simapro.csv.model.types.SystemBoundary;
import com.greendeltatc.simapro.csv.model.types.Technology;
import com.greendeltatc.simapro.csv.model.types.TimePeriod;
import com.greendeltatc.simapro.csv.model.types.WasteTreatmentAllocation;

/**
 * The CSV writer creates a new SimaPro CSV file and writes the given data set
 * into it
 * 
 * @author Sebastian Greve
 * @author Imo Graf
 * 
 */
public class CSVWriter {

	private char separator = ',';

	/**
	 * The buffered writer for writing the CSV file
	 */
	private BufferedWriter writer = null;

	public CSVWriter() {
	}

	public CSVWriter(char separator) {
		this.separator = separator;
	}

	/**
	 * Searches the data set for system descriptions
	 * 
	 * @param dataSet
	 *            The data set to search in
	 * @return All system descriptions the data set contains
	 */
	private SPSystemDescription[] collectSystemDescriptions(SPDataSet dataSet) {
		List<SPSystemDescription> systemDescriptions = new ArrayList<SPSystemDescription>();
		List<String> descriptionNames = new ArrayList<String>();
		for (SPProcess process : dataSet.getProcesses()) {
			if (process.getDocumentation().getSystemDescriptionEntry() != null) {
				SPSystemDescription sd = process.getDocumentation()
						.getSystemDescriptionEntry().getSystemDescription();
				if (!descriptionNames.contains(sd.getName())) {
					descriptionNames.add(sd.getName());
					systemDescriptions.add(sd);
				}
			}
		}
		for (SPWasteTreatment wt : dataSet.getWasteTreatments()) {
			if (wt.getDocumentation().getSystemDescriptionEntry() != null) {
				SPSystemDescription sd = wt.getDocumentation()
						.getSystemDescriptionEntry().getSystemDescription();
				if (!descriptionNames.contains(sd.getName())) {
					descriptionNames.add(sd.getName());
					systemDescriptions.add(sd);
				}
			}
		}
		return systemDescriptions
				.toArray(new SPSystemDescription[systemDescriptions.size()]);
	}

	/**
	 * Searches the data set for literature references
	 * 
	 * @param dataSet
	 *            The data set to search in
	 * @return All literature references the data set contains
	 */
	private SPLiteratureReference[] collectLiteratureReferences(
			SPDataSet dataSet) {
		List<SPLiteratureReference> literatureReferences = new ArrayList<SPLiteratureReference>();
		List<String> referenceNames = new ArrayList<String>();
		for (SPProcess process : dataSet.getProcesses()) {
			for (SPLiteratureReferenceEntry entry : process.getDocumentation()
					.getLiteratureReferencesEntries()) {
				SPLiteratureReference lr = entry.getLiteratureReference();
				if (!referenceNames.contains(lr.getName())) {
					referenceNames.add(lr.getName());
					literatureReferences.add(lr);
				}
			}
		}
		for (SPWasteTreatment wt : dataSet.getWasteTreatments()) {
			for (SPLiteratureReferenceEntry entry : wt.getDocumentation()
					.getLiteratureReferencesEntries()) {
				SPLiteratureReference lr = entry.getLiteratureReference();
				if (!referenceNames.contains(lr.getName())) {
					referenceNames.add(lr.getName());
					literatureReferences.add(lr);
				}
			}
		}
		return literatureReferences
				.toArray(new SPLiteratureReference[literatureReferences.size()]);
	}

	/**
	 * Writes a literature reference into the CSV file
	 * 
	 * @param literatureReference
	 *            The reference to write
	 * @throws java.io.IOException
	 */
	private void writeLiteratureReference(
			SPLiteratureReference literatureReference) throws IOException {
		writeln("Literature reference");
		writer.newLine();
		writeEntry("Name", literatureReference.getName());
		writer.newLine();
		writeEntry(
				"Category",
				literatureReference.getCategory() != null ? literatureReference
						.getCategory() : "Others");
		writer.newLine();
		writeEntry("Description", literatureReference.getContent());
		writer.newLine();
		writeln("End");
		writer.newLine();
	}

	/**
	 * Creates the distribution part of a flow line
	 * 
	 * @param distribution
	 *            The distribution of the flow
	 * @return The distribution part of a flow line in a CSV file
	 */
	private String getDistributionPart(IDistribution distribution) {
		String line = "";
		if (distribution == null) {
			line = "Undefined;0;0;0;";
		} else {
			DistributionType type = distribution.getType();
			if (type == null) {
				type = DistributionType.UNDEFINED;
			}
			line = type.getValue().replace('.', separator) + ";";
			switch (distribution.getType()) {
			case LOG_NORMAL:
				SPLogNormalDistribution logNormalDistribution = (SPLogNormalDistribution) distribution;

				line += distribution
						.getDistributionParameter(DistributionParameterType.SQUARED_STANDARD_DEVIATION)
						+ ";0;0;"
						+ logNormalDistribution.getPedigreeMatrix()
								.getPedigreeCommentString();
				break;
			case NORMAL:
				line += distribution
						.getDistributionParameter(DistributionParameterType.DOUBLED_STANDARD_DEVIATION)
						+ ";0;0;";
				break;
			case TRIANGLE:
				line += "0;"
						+ distribution
								.getDistributionParameter(DistributionParameterType.MINIMUM)
						+ ";"
						+ distribution
								.getDistributionParameter(DistributionParameterType.MAXIMUM)
						+ ";";
				break;
			case UNIFORM:
				line += "0;"
						+ distribution
								.getDistributionParameter(DistributionParameterType.MINIMUM)
						+ ";"
						+ distribution
								.getDistributionParameter(DistributionParameterType.MAXIMUM)
						+ ";";
				break;
			case UNDEFINED:
				line += "0;0;0;";
				break;
			}
		}
		return line;
	}

	/**
	 * Creates an elementary flow line
	 * 
	 * @param flow
	 *            The flow to be written
	 * @return The elementary flow line
	 */
	private String getElementaryFlowLine(SPElementaryFlow flow) {
		String line = flow.getSubstance().getName() + ";";
		if (flow.getSubCompartment() != null) {
			line += flow.getSubCompartment().getValue();
		}
		line += ";" + flow.getUnit().getName() + ";"
				+ flow.getAmount().replace('.', separator) + ";"
				+ getDistributionPart(flow.getDistribution())
				+ flow.getComment().replaceAll("\\r?\\n", "" + (char) 127)
				+ (char) 127;
		return line;
	}

	/**
	 * Creates the product input line for a process
	 * 
	 * @param product
	 *            The product flow to write
	 * @return A product input entry for a process
	 */
	private String getProductLine(SPProductFlow product) {
		String line = product.getName() + ";" + product.getUnit().getName()
				+ ";" + product.getAmount().replace('.', separator) + ";"
				+ getDistributionPart(product.getDistribution());
		if (product.getComment() != null) {
			line += product.getComment().replaceAll("\\r?\\n", "" + (char) 127)
					+ (char) 127;
		}
		return line;
	}

	/**
	 * Creates the flow line for a waste to treatment flow
	 * 
	 * @param flow
	 * @return line
	 */
	private String getWasteToTreatmentLine(SPWasteToTreatmentFlow flow) {
		String line = flow.getName() + ";" + flow.getUnit().getName() + ";"
				+ flow.getAmount() + ";"
				+ flow.getDistributionType().getValue() + ";"
				+ flow.getStandardDeviation() + ";" + flow.getMin() + ";"
				+ flow.getMax() + ";";

		return line;
	}

	/**
	 * Creates the product line for a process
	 * 
	 * @param product
	 *            The reference product flow of the process
	 * @return The product line for a process in a CSV file
	 */
	private String getProductLine(SPReferenceProduct product, String subCategory) {
		String line = product.getName() + ";" + product.getUnit().getName()
				+ ";" + product.getAmount().replace('.', separator) + ";"
				+ product.getAllocation() + ";";
		if (product.getWasteType() != null
				&& !product.getWasteType().equals("")) {
			line += product.getWasteType();
		} else {
			line += "not defined";
		}
		line += ";";
		if (subCategory != null) {
			line += subCategory;
		} else {
			line += "Others";
		}
		line += ";";
		if (product.getComment() != null) {
			line += product.getComment().replaceAll("\\r?\\n", "" + (char) 127)
					+ (char) 127;
		}
		line += ";";
		return line;
	}

	/**
	 * Creates a waste specification line
	 * 
	 * @param wasteSpecification
	 *            The waste specification
	 * @param subCategory
	 *            The sub category of the waste treatment holding the waste
	 *            specification
	 * @return The waste treatment line for a CSV file
	 */
	private String getWasteSpecificationLine(
			SPWasteSpecification wasteSpecification, String subCategory) {
		String line = wasteSpecification.getName() + ";"
				+ wasteSpecification.getUnit().getName() + ";"
				+ wasteSpecification.getAmount().replace('.', separator) + ";";
		if (wasteSpecification.getWasteType() != null
				&& !wasteSpecification.getWasteType().equals("")) {
			line += wasteSpecification.getWasteType();
		} else {
			line += "All waste types";
		}
		line += ";Others";
		if (subCategory != null) {
			line += subCategory;
		}
		line += ";";
		if (wasteSpecification.getComment() != null) {
			line += wasteSpecification.getComment().replaceAll("\\r?\\n",
					"" + (char) 127)
					+ (char) 127;
		}
		line += ";";
		return line;
	}

	/**
	 * Initializes the writer for the CSV file
	 * 
	 * @param fileName
	 *            The name of the file
	 * @param directory
	 *            The directory where the file should be written
	 * @throws java.io.IOException
	 */
	private void setUp(String fileName, File directory) throws IOException {
		File file = new File(directory.getAbsolutePath() + File.separator
				+ fileName + ".csv");
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));
	}

	/**
	 * Flushes and closes the writer
	 * 
	 * @throws java.io.IOException
	 */
	private void tearDown() throws IOException {
		writer.flush();
		writer.close();
	}

	/**
	 * Writes a calculated parameter into the CSV file
	 * 
	 * @param parameter
	 *            The calculated parameter to be written
	 * @throws java.io.IOException
	 */
	private void writeCalculatedParameter(SPCalculatedParameter parameter)
			throws IOException {
		writeln(parameter.getName()
				+ ";"
				+ parameter.getExpression().replace(".",
						String.valueOf(separator))
				+ ";"
				+ (parameter.getComment() != null ? parameter.getComment()
						.replace(';', ',') : "").replaceAll("\\r?\\n", ""
						+ (char) 127) + (char) 127);
	}

	/**
	 * Writes a documentation of a data entry into the CSV file
	 * 
	 * @param documentation
	 *            The documentation to be written
	 * @param process
	 *            true if it is a process documentation, false if it is a waste
	 *            treatment documentation
	 */
	private void writeDocumentation(SPDocumentation documentation,
			boolean process) throws IOException {
		if (process) {
			writeEntry("Category type",
					documentation.getCategory() != null ? documentation
							.getCategory().getValue()
							: ProcessCategory.MATERIAL.getValue());
		} else {
			writeEntry("Category type",
					ProcessCategory.WASTE_TREATMENT.getValue());
		}
		writeEntry("Process identifier", null);
		writeEntry("Type", null);
		// TODO write not only UNIT_PROCESS
		// documentation.getProcessType() != null ? documentation
		// .getProcessType().getValue() : ProcessType.UNIT_PROCESS
		// .getValue());
		writeEntry("Process name", documentation.getName());
		writeEntry("Status", documentation.getStatus() != null ? documentation
				.getStatus().getValue() : Status.FINISHED.getValue());
		writeEntry(
				"Time period",
				documentation.getTimePeriod() != null ? documentation
						.getTimePeriod().getValue() : TimePeriod.UNSPECIFIED
						.getValue());
		writeEntry(
				"Geography",
				documentation.getGeography() != null ? documentation
						.getGeography().getValue() : Geography.UNSPECIFIED
						.getValue());
		writeEntry(
				"Technology",
				documentation.getTechnology() != null ? documentation
						.getTechnology().getValue() : Technology.UNSPECIFIED
						.getValue());
		writeEntry("Representativeness",
				documentation.getRepresentativeness() != null ? documentation
						.getRepresentativeness().getValue()
						: Representativeness.UNSPECIFIED.getValue());
		if (process) {
			writeEntry(
					"Multiple output allocation",
					documentation.getProcessAllocation() != null ? documentation
							.getProcessAllocation().getValue()
							: ProcessAllocation.UNSPECIFIED.getValue());
			writeEntry("Substitution allocation",
					documentation.getSubstitution() != null ? documentation
							.getSubstitution().getValue()
							: Substitution.UNSPECIFIED.getValue());
		} else {
			writeEntry(
					"Waste treatment allocation",
					documentation.getWasteTreatmentAllocation() != null ? documentation
							.getWasteTreatmentAllocation().getValue()
							: WasteTreatmentAllocation.UNSPECIFIED.getValue());
		}
		writeEntry(
				"Cut off rules",
				documentation.getCutOffRule() != null ? documentation
						.getCutOffRule().getValue() : CutOffRule.UNSPECIFIED
						.getValue());
		writeEntry("Capital goods",
				documentation.getSystemBoundary() != null ? documentation
						.getSystemBoundary().getValue()
						: SystemBoundary.UNSPECIFIED.getValue());
		writeEntry("Boundary with nature",
				documentation.getBoundaryWithNature() != null ? documentation
						.getBoundaryWithNature().getValue()
						: BoundaryWithNature.UNSPECIFIED.getValue());
		writeEntry("Infrastructure",
				documentation.isInfrastructureProcess() ? "Yes" : "No");
		writeEntry("Date", documentation.getCreationDate());
		writeEntry("Record",
				documentation.getRecord()
						.replaceAll("\\r?\\n", "" + (char) 127) + (char) 127);
		writeEntry(
				"Generator",
				documentation.getGenerator().replaceAll("\\r?\\n",
						"" + (char) 127)
						+ (char) 127);
		writeln("Literature references");
		boolean atLeastOneReference = false;
		for (SPLiteratureReferenceEntry entry : documentation
				.getLiteratureReferencesEntries()) {
			String literatureReference = entry.getLiteratureReference() != null ? entry
					.getLiteratureReference().getName() : null;
			if (literatureReference != null && !literatureReference.equals("")
					&& entry.getComment() != null) {
				literatureReference += ";"
						+ entry.getComment().replaceAll("\\r?\\n",
								"" + (char) 127) + (char) 127;
			}
			if (literatureReference != null && !literatureReference.equals("")) {
				writeln(literatureReference);
				atLeastOneReference = true;
			}
		}
		if (!atLeastOneReference) {
			writer.newLine();
		}
		writer.newLine();
		writeEntry("Collection method", documentation.getCollectionMethod()
				.replaceAll("\\r?\\n", "" + (char) 127) + (char) 127);
		writeEntry("Data treatment", documentation.getDataTreatment()
				.replaceAll("\\r?\\n", "" + (char) 127) + (char) 127);
		writeEntry(
				"Verification",
				documentation.getVerification().replaceAll("\\r?\\n",
						"" + (char) 127)
						+ (char) 127);
		writeEntry(
				"Comment",
				"\""
						+ (documentation.getComment().replace('"', '\'') + "\"")
								.replaceAll("\\r?\\n", "" + (char) 127)
						+ (char) 127);
		writeEntry("Allocation rules", documentation.getAllocationRules()
				.replaceAll("\\r?\\n", "" + (char) 127) + (char) 127);
		String systemDescription = documentation.getSystemDescriptionEntry() != null ? documentation
				.getSystemDescriptionEntry().getSystemDescription().getName()
				: null;
		if (systemDescription != null
				&& !systemDescription.equals("")
				&& documentation.getSystemDescriptionEntry().getComment() != null) {
			systemDescription += ";"
					+ documentation.getSystemDescriptionEntry().getComment();
		}
		if (systemDescription != null)
			systemDescription = systemDescription.replaceAll("\\r?\\n", ""
					+ (char) 127)
					+ (char) 127;
		writeEntry("System description", systemDescription);
	}

	/**
	 * Writes an entry into the CSV file
	 * 
	 * @param name
	 *            The name of the entry
	 * @param value
	 *            The value of the entry
	 */
	private void writeEntry(String name, String value) throws IOException {
		writeln(name);
		writeln(value != null ? value : "");
		writer.newLine();
	}

	/**
	 * Writes the header of the CSV file
	 * 
	 * @param project
	 *            The name of the project
	 * @throws java.io.IOException
	 */
	private void writeHeader(String project) throws IOException {
		writeln("{SimaPro 7.2}");
		writeln("{Processes}");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
		writeln("{Date: " + dateFormat.format(new Date()) + "}");
		writeln("{Time: " + timeFormat.format(new Date()) + "}");
		writeln("{Project: " + project + "}");
		writeln("{CSV Format version: 7.0.0}");
		writeln("{CSV separator: Semicolon}");
		writeln("{Decimal separator: " + separator + "}");
		writeln("{Date separator: .}");
		writeln("{Short date format: dd.MM.yyyy}");
		writer.newLine();
	}

	/**
	 * Writes an input parameter into the CSV file
	 * 
	 * @param parameter
	 *            The input parameter to be written
	 * @throws java.io.IOException
	 */
	private void writeInputParameter(SPInputParameter parameter)
			throws IOException {
		String line = parameter.getName()
				+ ";"
				+ Double.toString(parameter.getValue()).replace(".",
						String.valueOf(separator)) + ";";
		line += getDistributionPart(parameter.getDistribution());
		line += (parameter.isHidden() ? "Yes" : "No") + ";";
		if (parameter.getComment() != null) {
			line += parameter.getComment().replaceAll("\\r?\\n",
					"" + (char) 127)
					+ (char) 127;
		}
		writeln(line);
	}

	/**
	 * Writes a line and jumps into the next line
	 * 
	 * @param line
	 *            The line to write
	 * @throws java.io.IOException
	 */
	private void writeln(String line) throws IOException {
		writer.write(line);
		writer.newLine();
	}

	/**
	 * Writes a process into the CSV file
	 * 
	 * @param process
	 *            The process to be written
	 */
	private void writeProcess(SPProcess process) throws IOException {
		writeln("Process");
		writer.newLine();
		writeDocumentation(process.getDocumentation(), true);

		writeln("Products");
		for (SPReferenceProduct product : process.getReferenceProducts()) {
			writeln(getProductLine(product, process.getSubCategory()));
		}
		writer.newLine();

		writeln("Avoided products");
		for (SPProductFlow product : process
				.getProductFlows(ProductFlowType.AVOIDED_PRODUCT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Resources");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.RESOURCE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Materials/fuels");
		for (SPProductFlow product : process
				.getProductFlows(ProductFlowType.MATERIAL_INPUT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Electricity/heat");
		for (SPProductFlow product : process
				.getProductFlows(ProductFlowType.ELECTRICITY_INPUT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Emissions to air");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_AIR)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Emissions to water");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_WATER)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Emissions to soil");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_SOIL)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Final waste flows");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.FINAL_WASTE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Non material emissions");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.NON_MATERIAL_EMISSIONS)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Social issues");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.SOCIAL_ISSUE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Economic issues");
		for (SPElementaryFlow flow : process
				.getElementaryFlows(ElementaryFlowType.ECONOMIC_ISSUE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Waste to treatment");
		for (SPWasteToTreatmentFlow flow : process.getWasteToTreatmentFlows()) {
			writeln(getWasteToTreatmentLine(flow));
		}
		writer.newLine();

		writeln("Input parameters");
		if (process.getInputParameters() != null
				&& process.getInputParameters().length > 0) {
			for (SPInputParameter parameter : process.getInputParameters()) {
				writeInputParameter(parameter);
			}
		} else {
			writer.newLine();
		}
		writer.newLine();

		writeln("Calculated parameters");
		if (process.getCalculatedParameters() != null
				&& process.getCalculatedParameters().length > 0) {
			for (SPCalculatedParameter parameter : process
					.getCalculatedParameters()) {
				writeCalculatedParameter(parameter);
			}
		} else {
			writer.newLine();
		}
		writer.newLine();

		writeln("End");
		writer.newLine();
	}

	/**
	 * Writes a quantity into the CSV file
	 * 
	 * @param quantity
	 *            The quantity to be written
	 * @throws java.io.IOException
	 */
	private void writeQuantity(SPQuantity quantity) throws IOException {
		writeln(quantity.getName() + ";"
				+ (quantity.isDimensional() ? "Yes" : "No"));
	}

	/**
	 * Writes a system description into the CSV file
	 * 
	 * @param systemDescription
	 *            The system description to be written
	 * @throws java.io.IOException
	 */
	private void writeSystemDescription(SPSystemDescription systemDescription)
			throws IOException {
		writeln("System description");
		writer.newLine();
		writeEntry("Name", systemDescription.getName());
		writeEntry(
				"Category",
				systemDescription.getCategory() != null ? systemDescription
						.getCategory() : "Others");
		writeEntry("Description", systemDescription.getDescription());
		writeEntry("Sub-systems", systemDescription.getSubSystems());
		writeEntry("Cut-off rules", systemDescription.getCutOffRules());
		writeEntry("Energy model", systemDescription.getEnergyModel());
		writeEntry("Transport model", systemDescription.getTransportModel());
		writeEntry("Waste model", systemDescription.getWasteModel());
		writeEntry("Other assumptions", systemDescription.getOtherAssumptions());
		writeEntry("Other information", systemDescription.getOtherInformation());
		writeEntry("Allocation rules", systemDescription.getAllocationRules());
		writeln("End");
		writer.newLine();
	}

	/**
	 * Writes a unit into the CSV file
	 * 
	 * @param unit
	 *            The unit to be written
	 * @throws java.io.IOException
	 */
	private void writeUnit(SPQuantity quantity, SPUnit unit) throws IOException {
		writeln(unit.getName() + ";" + quantity.getName() + ";"
				+ unit.getConversionFactor() + ";"
				+ quantity.getReferenceUnit().getName());
	}

	/**
	 * Writes a waste treatment into the CSV file
	 * 
	 * @param wasteTreatment
	 *            The waste treatment to be written
	 * @throws java.io.IOException
	 */
	private void writeWasteTreatment(SPWasteTreatment wasteTreatment)
			throws IOException {
		writeln("Process");
		writer.newLine();
		writeDocumentation(wasteTreatment.getDocumentation(), false);

		writeln("Waste treatment");
		writeln(getWasteSpecificationLine(
				wasteTreatment.getWasteSpecification(),
				wasteTreatment.getSubCategory()));
		writer.newLine();

		writeln("Avoided products");
		for (SPProductFlow product : wasteTreatment
				.getProductFlows(ProductFlowType.AVOIDED_PRODUCT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Resources");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.RESOURCE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Materials/fuels");
		for (SPProductFlow product : wasteTreatment
				.getProductFlows(ProductFlowType.MATERIAL_INPUT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Electricity/heat");
		for (SPProductFlow product : wasteTreatment
				.getProductFlows(ProductFlowType.ELECTRICITY_INPUT)) {
			writeln(getProductLine(product));
		}
		writer.newLine();

		writeln("Emissions to air");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_AIR)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Emissions to water");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_WATER)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Emissions to soil");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.EMISSION_TO_SOIL)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Final waste flows");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.FINAL_WASTE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Non material emissions");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.NON_MATERIAL_EMISSIONS)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Social issues");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.SOCIAL_ISSUE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Economic issues");
		for (SPElementaryFlow flow : wasteTreatment
				.getElementaryFlows(ElementaryFlowType.ECONOMIC_ISSUE)) {
			writeln(getElementaryFlowLine(flow));
		}
		writer.newLine();

		writeln("Waste to treatment");
		for (SPWasteToTreatmentFlow flow : wasteTreatment
				.getWasteToTreatmentFlows()) {
			writeln(getWasteToTreatmentLine(flow));
		}
		writer.newLine();

		writeln("Input parameters");
		if (wasteTreatment.getInputParameters() != null
				&& wasteTreatment.getInputParameters().length > 0) {
			for (SPInputParameter parameter : wasteTreatment
					.getInputParameters()) {
				writeInputParameter(parameter);
			}
		} else {
			writer.newLine();
		}
		writer.newLine();
		writeln("End");
		writer.newLine();

		writeln("Calculated parameters");
		if (wasteTreatment.getCalculatedParameters() != null
				&& wasteTreatment.getCalculatedParameters().length > 0) {
			for (SPCalculatedParameter parameter : wasteTreatment
					.getCalculatedParameters()) {
				writeCalculatedParameter(parameter);
			}
		} else {
			writer.newLine();
		}
		writer.newLine();

		writeln("End");
		writer.newLine();
	}

	/**
	 * Set the decimal separator. Default: ','
	 * 
	 * @param separator
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	/**
	 * Creates a new file with the given filename in the given directory and
	 * writes the given data set into it
	 * 
	 * @param directory
	 *            The directory where the file should be written
	 * @param dataSet
	 *            The data set to write
	 * @throws java.io.IOException
	 */
	public void write(File directory, SPDataSet dataSet) throws IOException {
		write(directory, dataSet, false, true);
	}

	/**
	 * Writes the given {@link com.greendeltatc.simapro.csv.model.SPDataSet}'s into one file which created from the
	 * first given {@link com.greendeltatc.simapro.csv.model.SPDataSet}. For the last given {@link com.greendeltatc.simapro.csv.model.SPDataSet} it is
	 * necessary that to set the boolean writeGeneralDate to true, because the
	 * writer will closed.
	 * 
	 * @param directory
	 *            The directory where the file should be written
	 * @param dataSet
	 *            The data set to write
	 * @throws java.io.IOException
	 */
	public void write(File directory, SPDataSet dataSet,
			boolean splitGivenDatasets, boolean writeGeneralData)
			throws IOException {
		boolean writeHeader = true;

		if (splitGivenDatasets) {
			if (writer == null) {
				setUp(dataSet.getProject(), directory);
			} else {
				writeHeader = false;
			}
		} else {
			setUp(dataSet.getProject(), directory);
		}

		if (writeHeader)
			writeHeader(dataSet.getProject());

		for (SPProcess process : dataSet.getProcesses()) {
			writeProcess(process);
		}

		for (SPWasteTreatment wasteTreatment : dataSet.getWasteTreatments()) {
			writeWasteTreatment(wasteTreatment);
		}

		if (writeGeneralData) {
			for (SPSystemDescription systemDescription : collectSystemDescriptions(dataSet)) {
				writeSystemDescription(systemDescription);
			}

			for (SPLiteratureReference literatureReference : collectLiteratureReferences(dataSet)) {
				writeLiteratureReference(literatureReference);
			}

			SPQuantity[] quantities = dataSet.getQuantities();
			if (quantities != null && quantities.length > 0) {
				writeln("Quantities");
				for (SPQuantity quantity : quantities) {
					writeQuantity(quantity);
				}
				writer.newLine();
				writeln("End");
				writer.newLine();

				writeln("Units");
				for (SPQuantity quantity : quantities) {
					for (SPUnit unit : quantity.getUnits()) {
						writeUnit(quantity, unit);
					}
				}
				writer.newLine();
				writeln("End");
				writer.newLine();
			}

			// TODO: eventually write substances

			writeEntry("Database Input parameters", null);
			writeln("End");
			writer.newLine();

			writeEntry("Database Calculated parameters", null);
			writeln("End");
			writer.newLine();

			writeln("Project Input parameters");
			if (dataSet.getInputParameters() != null
					&& dataSet.getInputParameters().length > 0) {
				for (SPInputParameter parameter : dataSet.getInputParameters()) {
					writeInputParameter(parameter);
				}
			} else {
				writer.newLine();
			}
			writeln("End");
			writer.newLine();

			writeln("Project Calculated parameters");
			if (dataSet.getCalculatedParameters() != null
					&& dataSet.getCalculatedParameters().length > 0) {
				for (SPCalculatedParameter parameter : dataSet
						.getCalculatedParameters()) {
					writeCalculatedParameter(parameter);
				}
			} else {
				writer.newLine();
			}
			writeln("End");
			writer.newLine();

			tearDown();
		}
	}
}
