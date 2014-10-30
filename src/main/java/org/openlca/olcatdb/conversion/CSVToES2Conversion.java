package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.greendeltatc.simapro.csv.CSVParser;
import com.greendeltatc.simapro.csv.model.IDistribution;
import com.greendeltatc.simapro.csv.model.SPCalculatedParameter;
import com.greendeltatc.simapro.csv.model.SPDataEntry;
import com.greendeltatc.simapro.csv.model.SPDocumentation;
import com.greendeltatc.simapro.csv.model.SPElementaryFlow;
import com.greendeltatc.simapro.csv.model.SPInputParameter;
import com.greendeltatc.simapro.csv.model.SPLiteratureReference;
import com.greendeltatc.simapro.csv.model.SPLiteratureReferenceEntry;
import com.greendeltatc.simapro.csv.model.SPLogNormalDistribution;
import com.greendeltatc.simapro.csv.model.SPNormalDistribution;
import com.greendeltatc.simapro.csv.model.SPPedigreeMatrix;
import com.greendeltatc.simapro.csv.model.SPProcess;
import com.greendeltatc.simapro.csv.model.SPProductFlow;
import com.greendeltatc.simapro.csv.model.SPReferenceProduct;
import com.greendeltatc.simapro.csv.model.SPTriangleDistribution;
import com.greendeltatc.simapro.csv.model.SPUniformDistribution;
import com.greendeltatc.simapro.csv.model.SPWasteSpecification;
import com.greendeltatc.simapro.csv.model.SPWasteTreatment;
import com.greendeltatc.simapro.csv.model.pedigreetypes.Completeness;
import com.greendeltatc.simapro.csv.model.pedigreetypes.FurtherTechnologicalCorrelation;
import com.greendeltatc.simapro.csv.model.pedigreetypes.GeographicalCorrelation;
import com.greendeltatc.simapro.csv.model.pedigreetypes.Reliability;
import com.greendeltatc.simapro.csv.model.pedigreetypes.TemporalCorrelation;
import com.greendeltatc.simapro.csv.model.types.BoundaryWithNature;
import com.greendeltatc.simapro.csv.model.types.CutOffRule;
import com.greendeltatc.simapro.csv.model.types.DistributionParameterType;
import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.ProcessAllocation;
import com.greendeltatc.simapro.csv.model.types.ProcessCategory;
import com.greendeltatc.simapro.csv.model.types.Status;
import com.greendeltatc.simapro.csv.model.types.Substitution;
import com.greendeltatc.simapro.csv.model.types.WasteTreatmentAllocation;
import org.openlca.expressions.FormulaInterpreter;
import org.openlca.expressions.InterpreterException;
import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2Description;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2GeographyRef;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2LogNormalDistribution;
import org.openlca.olcatdb.ecospold2.ES2NormalDistribution;
import org.openlca.olcatdb.ecospold2.ES2Parameter;
import org.openlca.olcatdb.ecospold2.ES2PedigreeMatrix;
import org.openlca.olcatdb.ecospold2.ES2Representativeness;
import org.openlca.olcatdb.ecospold2.ES2Technology;
import org.openlca.olcatdb.ecospold2.ES2TimePeriod;
import org.openlca.olcatdb.ecospold2.ES2TriangularDistribution;
import org.openlca.olcatdb.ecospold2.ES2Uncertainty;
import org.openlca.olcatdb.ecospold2.ES2UniformDistribution;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityEntry;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityName;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityNameList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Compartment;
import org.openlca.olcatdb.ecospold2.masterdata.ES2CompartmentList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ElemFlow;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ElemFlowList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Geography;
import org.openlca.olcatdb.ecospold2.masterdata.ES2GeographyList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ProductFlow;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ProductFlowList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SubCompartment;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Tag;
import org.openlca.olcatdb.ecospold2.masterdata.ES2TagList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Unit;
import org.openlca.olcatdb.ecospold2.masterdata.ES2UnitList;
import org.openlca.olcatdb.ecospold2.resources.ES2Folder;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

public class CSVToES2Conversion extends AbstractConversionImpl {

	/**
	 * The directory where the created data sets are stored.
	 */
	private ES2Folder es2Folder;

	private File sourceFile;

	/**
	 * Index from the linked process and products
	 */
	private Map<String, Entry> index = new HashMap<String, Entry>();

	/**
	 * Parameters
	 */
	private Map<String, ES2Parameter> parameters = new HashMap<String, ES2Parameter>();

	/**
	 * The list with the created activities for the master data entries. This
	 * list is saved as a master data file.
	 */
	private ES2ActivityList activityList = new ES2ActivityList();

	/**
	 * The list with the created activity names for the master data entries.
	 * This list is saved as a master data file.
	 */
	private ES2ActivityNameList activityNameList = new ES2ActivityNameList();

	/**
	 * The list with the created geographies entries for the master data.
	 */
	private ES2GeographyList geographyList = new ES2GeographyList();

	/**
	 * The master data for the created elementary flows.
	 */
	private ES2ElemFlowList elemFlowList = new ES2ElemFlowList();

	/**
	 * The master data for the created product flows.
	 */
	private ES2ProductFlowList productFlowList = new ES2ProductFlowList();

	/**
	 * The master data for the created units.
	 */
	private ES2UnitList unitList = new ES2UnitList();

	/**
	 * The master data for the created compartments.
	 */
	private ES2CompartmentList compartmentList = new ES2CompartmentList();

	/**
	 * The master data for the created tags.
	 */
	private ES2TagList tagList = new ES2TagList();

	/**
	 * The master data for the created properties.
	 */

	private XmlOutputter outputter;
	private TemplateType template = TemplateType.EcoSpold02;

	public CSVToES2Conversion(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	protected void createFolder(File targetDir) {
		super.createFolder(targetDir);
		this.es2Folder = new ES2Folder(targetDir);
		es2Folder.createContent();
		logFile(es2Folder);
	}

	@Override
	public ResourceFolder getResult() {
		return es2Folder;
	}

	@Override
	public void run() {
		// create the output folder

		createFolder(targetDir);

		try {

			// convert the processes/waste treatments
			CSVParser parser = new CSVParser(sourceFile);
			monitor.progress("Count the processes", 0);
			int processes = parser.count();

			// set reference index
			setRefIndex(processes, parser);

			// convert processes
			monitor.begin(processes);
			parser.start();
			int counter = 0;
			while (parser.next() && !canceled) {
				// for processes
				for (SPDataEntry dataEntry : parser.getDataSet().getProcesses()) {
					DataSetReference reference = convert(dataEntry);
					if (reference != null)
						createdFiles.add(reference);
					monitor.progress("Convert process " + ++counter + "/"
							+ processes, counter);
				}

				// for waste treatments
				for (SPDataEntry dataEntry : parser.getDataSet()
						.getWasteTreatments()) {
					DataSetReference reference = convert(dataEntry);
					if (reference != null)
						createdFiles.add(reference);
					monitor.progress("Convert process " + ++counter + "/"
							+ processes, counter);
				}
			}

		} catch (Exception e) {
			logger.severe("Parse exception: " + e.getMessage());
			e.printStackTrace();
		}

		// write the master data
		flushMasterData();

		// conversion finished
		flush();
	}

	private void setRefIndex(int processes, CSVParser parser)
			throws IOException {
		monitor.begin(processes);
		parser.start();
		int counter = 0;

		while (parser.next() && !canceled) {
			for (SPProcess process : parser.getDataSet().getProcesses()) {
				monitor.progress("Create index " + ++counter + "/" + processes,
						counter);

				Entry entry = new Entry();
				entry.processId = UUID.randomUUID().toString();

				// for process
				index.put(process.getDocumentation().getIdentifier(), entry);

				// for products
				for (SPReferenceProduct flow : process.getReferenceProducts()) {
					entry.productId = UUID.randomUUID().toString();
					index.put(flow.getName(), entry);
				}

			}

			for (SPWasteTreatment wasteTreatment : parser.getDataSet()
					.getWasteTreatments()) {
				monitor.progress("Create index " + ++counter + "/" + processes,
						counter);

				Entry entry = new Entry();
				entry.processId = UUID.randomUUID().toString();
				entry.productId = UUID.randomUUID().toString();

				// for waste treatment
				index.put(wasteTreatment.getDocumentation().getIdentifier(),
						entry);

				// for waste specification
				index.put(wasteTreatment.getWasteSpecification().getName(),
						entry);
			}
		}

	}

	private class Entry {
		String processId = null;
		String productId = null;
	}

	/**
	 * Writes the master data files.
	 */
	private void flushMasterData() {
		// the activity index
		flushMasterData("ActivityIndex.xml", activityList,
				TemplateType.ES2ActivityList);
		activityList = null;

		// the activity names
		flushMasterData("ActivityNames.xml", activityNameList,
				TemplateType.ES2ActivityNameList);
		activityNameList = null;

		// the geographies
		flushMasterData("Geographies.xml", geographyList,
				TemplateType.ES2GeographyList);
		geographyList = null;

		// the ElementaryExchanges
		flushMasterData("ElementaryExchanges.xml", elemFlowList,
				TemplateType.ES2ElemFlowList);
		elemFlowList = null;

		// the intermediateExchanges
		flushMasterData("IntermediateExchanges.xml", productFlowList,
				TemplateType.ES2ProductFlowList);
		productFlowList = null;

		// units
		flushMasterData("Units.xml", unitList, TemplateType.ES2Units);
		unitList = null;

		// compartments
		flushMasterData("Compartments.xml", compartmentList,
				TemplateType.ES2CompartmentList);
		unitList = null;

		// units
		flushMasterData("Tags.xml", tagList, TemplateType.ES2TagList);
		tagList = null;
	}

	/**
	 * Writes the given content to the specified file.
	 */
	private void flushMasterData(String file, Object content,
			TemplateType template) {
		File mdDir = es2Folder.getMasterDataFolder();
		File outFile = new File(mdDir, file);
		if (outFile.exists()) {
			outFile.delete();
		}
		XmlOutputter outputter = new XmlOutputter();
		outputter.output(content, template, outFile, true);
	}

	private void addIntermediateExchangeToMasterData(
			ES2IntermediateExchange intermediateExchange) {
		ES2ProductFlow es2ProductFlow = new ES2ProductFlow();
		productFlowList.getProductFlows().add(es2ProductFlow);
		es2ProductFlow.id = intermediateExchange.id;
		es2ProductFlow.unitId = intermediateExchange.unitId;
		es2ProductFlow.getName().addAll(intermediateExchange.getName());
		es2ProductFlow.getComment().addAll(intermediateExchange.getComment());
	}

	/**
	 * 
	 * @param unitName
	 * @return
	 */
	private ES2Unit getUnit(String unitName) {
		ES2Unit es2Unit = null;
		for (ES2Unit unit : unitList.getUnits()) {
			for (LangString langString : unit.getNames()) {
				if (langString.getValue().equals(unitName)) {
					es2Unit = unit;
				}
			}
		}

		if (es2Unit == null) {
			es2Unit = new ES2Unit();
			unitList.getUnits().add(es2Unit);
			es2Unit.getNames().add(new LangString(unitName));
			es2Unit.id = UUID.randomUUID().toString();
		}
		return es2Unit;
	}

	/**
	 * 
	 * @param elementaryFlow
	 * @return
	 */
	private ES2Compartment getCompartment(SPElementaryFlow elementaryFlow) {
		ES2Compartment es2Compartment = null;
		ES2SubCompartment es2SubCompartment = null;

		if (elementaryFlow.getSubCompartment() != null) {

			for (ES2Compartment compartment : compartmentList.getCompartments()) {
				for (LangString langString : compartment.getNames()) {
					if (langString.getValue().equals(
							elementaryFlow.getType().getValue()))
						es2Compartment = compartment;
				}
			}

			// add new compartment if null
			if (es2Compartment == null) {
				es2Compartment = new ES2Compartment();
				compartmentList.getCompartments().add(es2Compartment);
				es2Compartment.id = UUID.randomUUID().toString();
				es2Compartment.getNames().add(
						new LangString(elementaryFlow.getType().getValue()));
			}

			// check if the subCompartment exist
			for (ES2SubCompartment subCompartment : es2Compartment
					.getSubCompartments()) {
				for (LangString langString : subCompartment.getNames()) {
					if (langString.equals(elementaryFlow.getSubCompartment()
							.getValue()))
						es2SubCompartment = subCompartment;

				}
			}

			// add new subCompartment if null
			if (es2SubCompartment == null) {
				es2SubCompartment = new ES2SubCompartment();
				es2Compartment.getSubCompartments().add(es2SubCompartment);
				compartmentList.getCompartments().add(es2Compartment);

				es2SubCompartment.id = UUID.randomUUID().toString();
				es2SubCompartment.getNames().add(
						new LangString(elementaryFlow.getSubCompartment()
								.getValue()));
			}

		}
		return es2Compartment;
	}

	private void addTagToMasterData(String tag, String comment) {
		if (!tagList.containsName(tag)) {
			ES2Tag es2Tag = new ES2Tag();
			tagList.getTags().add(es2Tag);
			es2Tag.name = tag;
			if (comment != null && !comment.equals(""))
				es2Tag.getComments().add(new LangString(comment));
		}
	}

	/**
	 * Converts the given {@link com.greendeltatc.simapro.csv.model.SPDataEntry} to an EcoSpold 02 activity.
	 * 
	 * @throws IllegalAccessException
	 */
	private DataSetReference convert(SPDataEntry dataEntry)
			throws IllegalAccessException {

		ES2EcoSpold ecoSpold = new ES2EcoSpold();
		ES2Dataset dataset = new ES2Dataset();
		ecoSpold.getDatasets().add(dataset);

		// convert parameters
		parameters.clear();
		parameters(dataEntry.getInputParameters(),
				dataEntry.getCalculatedParameters());
		dataset.getParameters().addAll(parameters.values());

		// convert dataset
		activity(dataEntry, dataset);
		activityTags(dataEntry, dataset);
		geography(dataEntry, dataset);
		technology(dataEntry, dataset);
		timePeriod(dataEntry, dataset);
		representativeness(dataEntry, dataset);

		// convert elementary flows
		for (SPElementaryFlow elementaryFlow : dataEntry.getElementaryFlows()) {
			elementaryFlow(elementaryFlow, dataset);
		}

		// convert product flows
		for (SPProductFlow productFlow : dataEntry.getProductFlows()) {
			productFlow(productFlow, dataset);
		}

		// Convert the data for the specific object
		if (dataEntry instanceof SPProcess) {
			process((SPProcess) dataEntry, dataset);
		} else if (dataEntry instanceof SPWasteTreatment) {
			wasteTreatment((SPWasteTreatment) dataEntry, dataset);
		}

		// write the file
		File outFile = new File(es2Folder.getActivityFolder(),
				dataset.description.id + ".spold");
		outputter = new XmlOutputter();
		outputter.output(ecoSpold, template, outFile, true);

		// create the data set reference of the created process
		DataSetReference reference = new DataSetReference();
		if (dataset.description != null)
			reference.setRefObjectId(dataset.description.activityNameId);
		reference.setType("process data set");

		// create the URI
		try {
			String uri = outFile.toURI().toString();
			reference.setUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// the (first) name of the created process
		if (dataset.description != null
				&& !dataset.description.getName().isEmpty()) {
			reference.setName(dataset.description.getName().get(0).getValue());
		}
		return reference;
	}

	private void parameters(SPInputParameter[] inputParameters,
			SPCalculatedParameter[] calculatedParameters)
			throws IllegalAccessException {
		Map<String, SPCalculatedParameter> index = new HashMap<String, SPCalculatedParameter>();

		for (SPInputParameter inputParameter : inputParameters) {
			index.put(inputParameter.getName(), null);
		}
		for (SPCalculatedParameter calculatedParameter : calculatedParameters) {
			index.put(calculatedParameter.getName(), calculatedParameter);
		}

		// convert input parameters
		for (SPInputParameter inputParameter : inputParameters) {
			inputParameter(inputParameter);
		}

		// convert calculated parameters
		for (SPCalculatedParameter calculatedParameter : calculatedParameters) {
			if (!parameters.containsKey(calculatedParameter.getName())) {
				calculatedParamter(calculatedParameter, index);
			}

		}
	}

	private void inputParameter(SPInputParameter parameter) {
		ES2Parameter es2Parameter = new ES2Parameter();
		parameters.put(parameter.getName(), es2Parameter);

		// @id
		es2Parameter.id = UUID.randomUUID().toString();

		// @name & @variableName
		if (parameter.getName() != null && !parameter.getName().equals("")) {
			es2Parameter.getName().add(new LangString(parameter.getName()));
			es2Parameter.variableName = parameter.getName();
		}

		// @amount
		es2Parameter.amount = parameter.getValue();

		// @comment
		if (parameter.getComment() != null
				&& !parameter.getComment().equals(""))
			es2Parameter.getComment().add(
					new LangString(parameter.getComment()));

		// @uncertainty
		if (parameter.getDistribution() != null) {
			es2Parameter.uncertainty = uncertainty(parameter.getDistribution());
		}
	}

	private void calculatedParamter(SPCalculatedParameter parameter,
			Map<String, SPCalculatedParameter> index)
			throws IllegalAccessException {

		ES2Parameter es2Parameter = new ES2Parameter();
		parameters.put(parameter.getName(), es2Parameter);

		// @id
		es2Parameter.id = UUID.randomUUID().toString();

		// @name & @variableName
		if (parameter.getName() != null && !parameter.getName().equals("")) {
			es2Parameter.getName().add(new LangString(parameter.getName()));
			es2Parameter.variableName = parameter.getName();
		}

		// @isCalculatedAmount
		es2Parameter.isCalculatedAmount = true;

		String expression = parameter.getExpression();
		// @mathematicalRelation
		es2Parameter.mathematicalRelation = expression;

		// convert formula
		try {
			FormulaInterpreter interpreter = new FormulaInterpreter();
			for (String name : index.keySet()) {
				if (expression.contains(name)) {
					if (parameters.containsKey(name)) {
						interpreter.bind(name, Double.toString(parameters
								.get(name).amount));
					} else {
						calculatedParamter(index.get(name), index);
						if (!parameters.containsKey(name))
							throw new IllegalAccessException(
									"Conversion formula error!");
						interpreter.bind(name, Double.toString(parameters
								.get(name).amount));
					}
				}
			}

			// @amount
			es2Parameter.amount = interpreter
					.eval(expression.replace(",", ";"));
		} catch (InterpreterException e) {
			e.printStackTrace();
		}
	}

	private void productFlow(SPProductFlow productFlow, ES2Dataset dataset) {

		ES2IntermediateExchange intermediateExchange = new ES2IntermediateExchange();
		dataset.getIntermediateExchanges().add(intermediateExchange);

		// @id
		intermediateExchange.id = UUID.randomUUID().toString();

		// @acitivityLinkId TODO test it
		Entry entry = index.get(productFlow.getName());
		if (entry != null) {
			String acitivityLinkId = entry.processId;

			if (acitivityLinkId == null)
				acitivityLinkId = UUID.randomUUID().toString();

			// @intermediateExchangeId
			intermediateExchange.intermediateExchangeId = entry.productId;

		}

		// @name
		if (productFlow.getName() != null)
			intermediateExchange.getName().add(
					new LangString(productFlow.getName()));

		// @unit
		if (productFlow.getUnit() != null
				&& !productFlow.getUnit().getName().equals("")) {
			ES2Unit unit = getUnit(productFlow.getUnit().getName());
			intermediateExchange.unitId = unit.id;
			intermediateExchange.getUnitNames().addAll(unit.getNames());
		}

		// @amount
		String amount = productFlow.getAmount();
		if (amount != null && !productFlow.getAmount().equals(""))
			try {
				intermediateExchange.amount = Double.parseDouble(productFlow
						.getAmount().replace(",", "."));
			} catch (NumberFormatException e) {
				if (parameters.containsKey(amount)) {
					ES2Parameter parameter = parameters.get(amount);
					intermediateExchange.amount = parameter.amount;

					// @isCalculatedAmount
					intermediateExchange.isCalculatedAmount = true;

					// @mathematicalRelation
					intermediateExchange.mathematicalRelation = parameter.variableName;
				}
			}

		// @uncertainty
		if (productFlow.getDistribution() != null)
			intermediateExchange.uncertainty = uncertainty(productFlow
					.getDistribution());

		// @comment
		if (productFlow.getComment() != null)
			intermediateExchange.getComment().add(
					new LangString(productFlow.getComment()));

		// @input -outputGroup
		if (productFlow.getType() != null) {
			switch (productFlow.getType()) {
			case AVOIDED_PRODUCT:
				intermediateExchange.outputGroup = 2;
				if (intermediateExchange.amount > 0)
					intermediateExchange.amount = intermediateExchange.amount
							* -1;
				break;

			case WASTE_TREATMENT:
				intermediateExchange.outputGroup = 3;
				break;

			case ELECTRICITY_INPUT:
				intermediateExchange.inputGroup = 2;
				break;

			case MATERIAL_INPUT:
				intermediateExchange.inputGroup = 1;
				break;
			}
		}

		// Add Master Data
		addIntermediateExchangeToMasterData(intermediateExchange);
	}

	private void elementaryFlow(SPElementaryFlow elementaryFlow,
			ES2Dataset dataset) {
		ES2ElementaryExchange elementaryExchange = new ES2ElementaryExchange();
		dataset.getElementaryExchanges().add(elementaryExchange);

		// @id
		elementaryExchange.id = UUID.randomUUID().toString();

		// @name
		if (elementaryFlow.getName() != null)
			elementaryExchange.getName().add(
					new LangString(elementaryFlow.getName()));

		// @unit
		if (elementaryFlow.getUnit() != null
				&& !elementaryFlow.getUnit().getName().equals("")) {
			ES2Unit unit = getUnit(elementaryFlow.getUnit().getName());
			elementaryExchange.unitId = unit.id;
			elementaryExchange.getUnitNames().addAll(unit.getNames());
		}
		// @comment
		if (elementaryFlow.getComment() != null
				&& !elementaryFlow.getComment().equals(""))
			elementaryExchange.getComment().add(
					new LangString(elementaryFlow.getComment()));

		// @amount
		String amount = elementaryFlow.getAmount();
		if (amount != null && !elementaryFlow.getAmount().equals(""))
			try {
				elementaryExchange.amount = Double.parseDouble(elementaryFlow
						.getAmount().replace(",", "."));
			} catch (NumberFormatException e) {
				if (parameters.containsKey(amount)) {
					ES2Parameter parameter = parameters.get(amount);
					elementaryExchange.amount = parameter.amount;

					// @isCalculatedAmount
					elementaryExchange.isCalculatedAmount = true;

					// @mathematicalRelation
					elementaryExchange.mathematicalRelation = parameter.variableName;
				}
			}

		// @uncertainty
		if (elementaryFlow.getDistribution() != null)
			elementaryExchange.uncertainty = uncertainty(elementaryFlow
					.getDistribution());

		// @input -outputGroup
		if (elementaryFlow.getType() != null) {
			if (elementaryFlow.getType() != ElementaryFlowType.RESOURCE) {
				elementaryExchange.outputGroup = 4;
			} else {
				elementaryExchange.inputGroup = 4;
			}
		}

		// @CAS number
		if (elementaryFlow.getSubstance() != null)
			elementaryExchange.casNumber = elementaryFlow.getSubstance()
					.getCASNumber();

		// @compartment
		ES2Compartment compartment = getCompartment(elementaryFlow);
		if (compartment != null) {
			elementaryExchange.compartmentId = compartment.id;
		}

		// add master data
		if (!elemFlowList.containsName(elementaryExchange.getName())) {
			ES2ElemFlow elemFlow = new ES2ElemFlow();
			elemFlowList.getElemFlows().add(elemFlow);
			elemFlow.id = elementaryExchange.id;
			elemFlow.unitId = elementaryExchange.unitId;
			elemFlow.getName().addAll(elementaryExchange.getName());
			elemFlow.getComment().addAll(elementaryExchange.getComment());
		}
	}

	private ES2Uncertainty uncertainty(IDistribution distribution) {
		ES2Uncertainty uncertainty = new ES2Uncertainty();

		if (distribution != null) {
			switch (distribution.getType()) {
			case LOG_NORMAL:
				SPLogNormalDistribution spLogNormalDistribution = (SPLogNormalDistribution) distribution;
				ES2LogNormalDistribution es2LogNormalDistribution = new ES2LogNormalDistribution();
				uncertainty.logNormalDistribution = es2LogNormalDistribution;

				// @variance TODO test it
				double squared = spLogNormalDistribution
						.getDistributionParameter(DistributionParameterType.SQUARED_STANDARD_DEVIATION);
				double variance = (Math.log(Math.sqrt(squared))) * 2; // TODO
																		// test
																		// it
				es2LogNormalDistribution.variance = variance;

				// @varianceWithPedigreeUncertainty // TODO test it
				es2LogNormalDistribution.varianceWithPedigreeUncertainty = squared
						+ variance;

				// @standardDeviation95 TODO test it
				es2LogNormalDistribution.standardDeviation95 = Math
						.sqrt(variance);

				// @pedigree matrix
				if (spLogNormalDistribution.getPedigreeMatrix() != null)
					uncertainty.pedigreeMatrix = pedigreeMatrix(spLogNormalDistribution
							.getPedigreeMatrix());

				// add SimaPro sampleSize to comment
				if (spLogNormalDistribution.getPedigreeMatrix() != null)
					uncertainty.getGeneralComment().add(
							new LangString("SampleSize: "
									+ spLogNormalDistribution
											.getPedigreeMatrix().sampleSize
											.getKey()
									+ " = "
									+ spLogNormalDistribution
											.getPedigreeMatrix().sampleSize
											.getValue()));

				break;

			case NORMAL:
				SPNormalDistribution spNormalDistribution = (SPNormalDistribution) distribution;
				ES2NormalDistribution es2NormalDistribution = new ES2NormalDistribution();
				uncertainty.normalDistribution = es2NormalDistribution;

				double standardDeviation = spNormalDistribution
						.getDistributionParameter(DistributionParameterType.DOUBLED_STANDARD_DEVIATION);

				// @variance TODO test it
				es2NormalDistribution.variance = standardDeviation;

				// standardDeviation95 TODO test it
				es2NormalDistribution.standardDeviation95 = Math
						.sqrt(standardDeviation);
				break;

			case TRIANGLE:
				SPTriangleDistribution spTriangleDistribution = (SPTriangleDistribution) distribution;
				ES2TriangularDistribution es2TriangularDistribution = new ES2TriangularDistribution();
				uncertainty.triangularDistribution = es2TriangularDistribution;

				// @min
				es2TriangularDistribution.minValue = spTriangleDistribution
						.getDistributionParameter(DistributionParameterType.MINIMUM);
				// @max
				es2TriangularDistribution.maxValue = spTriangleDistribution
						.getDistributionParameter(DistributionParameterType.MAXIMUM);

				break;

			case UNIFORM:
				SPUniformDistribution spUniformDistribution = (SPUniformDistribution) distribution;
				ES2UniformDistribution es2UniformDistribution = new ES2UniformDistribution();
				uncertainty.uniformDistribution = es2UniformDistribution;

				// @min
				es2UniformDistribution.minValue = spUniformDistribution
						.getDistributionParameter(DistributionParameterType.MINIMUM);

				// @max
				es2UniformDistribution.maxValue = spUniformDistribution
						.getDistributionParameter(DistributionParameterType.MAXIMUM);

				break;

			case UNDEFINED:
				uncertainty = null;
				break;
			}
		}

		return uncertainty;
	}

	private ES2PedigreeMatrix pedigreeMatrix(SPPedigreeMatrix pedigreeMatrix) {
		ES2PedigreeMatrix es2PedigreeMatrix = new ES2PedigreeMatrix();
		if (pedigreeMatrix.completeness != Completeness.NA)
			es2PedigreeMatrix.completeness = Integer
					.parseInt(pedigreeMatrix.completeness.getKey());

		if (pedigreeMatrix.furtherTechnologicalCorrelation != FurtherTechnologicalCorrelation.NA)
			es2PedigreeMatrix.furtherTechnologyCorrelation = Integer
					.parseInt(pedigreeMatrix.furtherTechnologicalCorrelation
							.getKey());

		if (pedigreeMatrix.geographicalCorrelation != GeographicalCorrelation.NA)
			es2PedigreeMatrix.geographicalCorrelation = Integer
					.parseInt(pedigreeMatrix.geographicalCorrelation.getKey());

		if (pedigreeMatrix.reliability != Reliability.NA) {
			es2PedigreeMatrix.reliability = Integer
					.parseInt(pedigreeMatrix.reliability.getKey());
		}

		if (pedigreeMatrix.temporalCorrelation != TemporalCorrelation.NA)
			es2PedigreeMatrix.temporalCorrelation = Integer
					.parseInt(pedigreeMatrix.temporalCorrelation.getKey());

		return es2PedigreeMatrix;
	}

	private void representativeness(SPDataEntry dataEntry, ES2Dataset dataset) {

		ES2Representativeness representativeness = new ES2Representativeness();
		dataset.representativeness = representativeness;

		if (dataEntry.getDocumentation().getRepresentativeness() != null)
			representativeness.getSamplingProcedure().add(
					new LangString(dataEntry.getDocumentation()
							.getRepresentativeness().getValue()));
	}

	/**
	 * Creates the activity description.
	 */
	private void activity(SPDataEntry dataEntry, ES2Dataset dataset) {
		ES2Description description = new ES2Description(true);
		dataset.description = description;

		SPDocumentation documentation = dataEntry.getDocumentation();

		// @ id TODO error message if null
		description.id = index
				.get(dataEntry.getDocumentation().getIdentifier()).processId;

		// @ activity id
		description.activityNameId = description.id;

		// @ name
		if (documentation.getName() != null)
			description.getName().add(
					new LangString(dataEntry.getDocumentation().getName()));

		// @ comment
		String generalComment = "";
		if (documentation.getComment() != null)
			generalComment = documentation.getComment();

		if (!generalComment.equals(""))
			description.generalComment = new TextAndImage(generalComment);

		// @ allocationComment
		if (documentation.getAllocationRules() != null)
			description.allocationComment = new TextAndImage(
					documentation.getAllocationRules());

		// @ type
		if (documentation.getProcessType() != null) {
			switch (documentation.getProcessType()) {
			case UNIT_PROCESS:
				description.type = 1;
				break;

			case SYSTEM:
				description.type = 2;
				break;
			}
		}

		// Set Master Data for index
		ES2ActivityEntry indexEntry = new ES2ActivityEntry();
		activityList.getEntries().add(indexEntry);
		indexEntry.specialActivityType = 0;

		// the index attributes
		indexEntry.activityNameId = dataset.description.activityNameId;
		indexEntry.id = dataset.description.id;
		indexEntry.getName().addAll(dataset.description.getName());

		// geography and time for the index attributes
		if (dataset.geography != null) {
			indexEntry.geographyId = dataset.geography.geographyId;
			indexEntry.getLocation().addAll(dataset.geography.getShortNames());
		}
		if (dataset.timePeriod != null) {
			indexEntry.startDate = dataset.timePeriod.startDate;
			indexEntry.endDate = dataset.timePeriod.endDate;
		}

		// Set Master Data for names
		ES2ActivityName nameEntry = new ES2ActivityName();
		activityNameList.getActivityNames().add(nameEntry);

		if (dataset.description != null) {

			// the name attributes
			nameEntry.id = dataset.description.activityNameId;
			nameEntry.getNames().addAll(dataset.description.getName());
		}

	}

	private void activityTags(SPDataEntry dataEntry, ES2Dataset dataset) {
		String tag;

		// @tag (Category type)
		ProcessCategory processCategory = dataEntry.getDocumentation()
				.getCategory();
		if (processCategory != null) {
			tag = "Category type: " + processCategory.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Cut off rules)
		CutOffRule cutOffRule = dataEntry.getDocumentation().getCutOffRule();
		if (cutOffRule != null) {
			tag = "Cut off rules: " + cutOffRule.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Boundary with nature)
		BoundaryWithNature boundaryWithNature = dataEntry.getDocumentation()
				.getBoundaryWithNature();
		if (boundaryWithNature != null) {
			tag = "Boundary with nature: " + boundaryWithNature.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (infrastructure)
		if (dataEntry.getDocumentation().isInfrastructureProcess()) {
			tag = "Infrastructure: Yes";
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Status)
		Status status = dataEntry.getDocumentation().getStatus();
		if (status != null) {
			tag = "Status: " + status.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Record)
		tag = dataEntry.getDocumentation().getRecord();
		if (tag != null) {
			tag = "Record: " + tag;
			dataset.description.getTags().add(tag);
			addTagToMasterData("Record", null);
		}

		// @tag (Record)
		tag = dataEntry.getDocumentation().getGenerator();
		if (tag != null) {
			tag = "Generator: " + tag;
			dataset.description.getTags().add(tag);
			addTagToMasterData("Generator", null);
		}

		// @tag (Literature references)
		for (SPLiteratureReferenceEntry liEntry : dataEntry.getDocumentation()
				.getLiteratureReferencesEntries()) {
			SPLiteratureReference literatureReference = liEntry
					.getLiteratureReference();
			if (literatureReference != null) {
				tag = "Literature references: " + literatureReference.getName();
				dataset.description.getTags().add(
						tag + ";" + liEntry.getComment());
				addTagToMasterData(tag, null);
			}
		}

		// @tag (Collection method)
		tag = dataEntry.getDocumentation().getCollectionMethod();
		if (tag != null) {
			tag = "Collection method: " + tag;
			dataset.description.getTags().add(tag);
			addTagToMasterData("Collection method", null);
		}

		// @tag (Data treatment)
		tag = dataEntry.getDocumentation().getDataTreatment();
		if (tag != null) {
			tag = "Data treatment: " + tag;
			dataset.description.getTags().add(tag);
			addTagToMasterData("Data treatment", null);
		}

		// @tag (Verification)
		tag = dataEntry.getDocumentation().getVerification();
		if (tag != null) {
			tag = "Verification: " + tag;
			dataset.description.getTags().add(tag);
			addTagToMasterData("Verification", null);
		}

	}

	private void geography(SPDataEntry dataEntry, ES2Dataset dataset) {
		if (dataEntry.getDocumentation().getGeography() != null) {
			ES2GeographyRef refGeography = new ES2GeographyRef();
			dataset.geography = refGeography;

			// @id
			refGeography.geographyId = dataEntry.getDocumentation()
					.getGeography().getES2Id();

			// @location
			refGeography.getShortNames().add(
					new LangString(dataEntry.getDocumentation().getGeography()
							.getValue()));

			// Set Master Data
			boolean geoExist = false;
			for (ES2Geography geo : geographyList.getGeographies()) {
				if (geo.id.equals(dataEntry.getDocumentation().getGeography()
						.getES2Id())) {
					geoExist = true;
				}
			}

			if (!geoExist) {
				ES2Geography geography = new ES2Geography();
				geography.id = dataEntry.getDocumentation().getGeography()
						.getES2Id();
				geography.getNames().add(
						new LangString(dataEntry.getDocumentation()
								.getGeography().getValue()));
				geography.getShortNames().add(
						new LangString(dataEntry.getDocumentation()
								.getGeography().getValue()));
				geographyList.getGeographies().add(geography);
			}
		}
	}

	private void technology(SPDataEntry dataEntry, ES2Dataset dataset) {
		if (dataEntry.getDocumentation().getTechnology() != null) {

			ES2Technology technology = new ES2Technology();
			dataset.technology = technology;

			// @ level
			int technologyLevel;
			switch (dataEntry.getDocumentation().getTechnology()) {
			case AVERAGE_TECHNOLOGY:
				technologyLevel = 3;
				break;

			case BEST_AVAILABLE_TECHNOLOGY:
				technologyLevel = 1;
				break;

			case FUTURE_TECHNOLOGY:
				technologyLevel = 1;
				break;

			case MIXED_DATA:
				technologyLevel = 3;
				break;

			case MODERN_TECHNOLOGY:
				technologyLevel = 2;
				break;

			case OUTDATED_TECHNOLOGY:
				technologyLevel = 5;
				break;

			case UNKNOWN:
				technologyLevel = 0;
				break;

			case UNSPECIFIED:
				technologyLevel = 0;
				break;

			case WORST_CASE:
				technologyLevel = 5;
				break;

			default:
				technologyLevel = 0;
			}
			technology.technologyLevel = technologyLevel;

		}

	}

	private void timePeriod(SPDataEntry dataEntry, ES2Dataset dataset) {
		if (dataEntry.getDocumentation().getTimePeriod() != null) {

			String startDate = null;
			String endDate = null;
			String comment = null;

			switch (dataEntry.getDocumentation().getTimePeriod()) {

			case P_1980_1984:
				startDate = "1980";
				endDate = "1984";
				break;

			case P_1985_1989:
				startDate = "1985";
				endDate = "1989";
				break;

			case P_1990_1994:
				startDate = "1990";
				endDate = "1994";
				break;

			case P_1995_1999:
				startDate = "1995";
				endDate = "1999";
				break;

			case P_2000_2004:
				startDate = "2000";
				endDate = "2004";
				break;

			case P_2005_2009:
				startDate = "2005";
				endDate = "2009";
				break;

			case P_2010_AND_AFTER:
				startDate = "2010";
				endDate = "after";
				break;

			case P_1980_AND_BEFORE:
				startDate = "before";
				endDate = "1980";
				break;

			case MIXED_DATA:
				comment = "mixed data";
				break;

			case UNKNOWN:
				comment = "unknown";
				break;

			case UNSPECIFIED:
				comment = "unspecified";
				break;
			}

			ES2TimePeriod timePeriod = new ES2TimePeriod();
			dataset.timePeriod = timePeriod;

			if (startDate != null)
				timePeriod.startDate = startDate;

			if (endDate != null)
				timePeriod.endDate = endDate;

			if (comment != null)
				timePeriod.comment = new TextAndImage(comment);

		}
	}

	private void process(SPProcess process, ES2Dataset dataset) {

		String tag;

		// @tag (Multiple output allocation)
		ProcessAllocation processAllocation = process.getDocumentation()
				.getProcessAllocation();
		if (processAllocation != null) {
			tag = "Multiple output allocation: " + processAllocation.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Substitution allocation)
		Substitution substitution = process.getDocumentation()
				.getSubstitution();
		if (substitution != null) {
			tag = "Substitution allocation: " + substitution.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// convert ref products
		refProducts(process, dataset);

	}

	private void refProducts(SPProcess process, ES2Dataset dataset) {

		for (SPReferenceProduct product : process.getReferenceProducts()) {
			ES2IntermediateExchange intermediateExchange = new ES2IntermediateExchange();
			dataset.getIntermediateExchanges().add(intermediateExchange);

			// @id
			intermediateExchange.id = UUID.randomUUID().toString();

			// @name
			if (product.getName() != null)
				intermediateExchange.getName().add(
						new LangString(product.getName()));

			// @amount
			String amount = product.getAmount();
			if (amount != null && !product.getAmount().equals(""))
				try {
					intermediateExchange.amount = Double.parseDouble(product
							.getAmount().replace(",", "."));
				} catch (NumberFormatException e) {
					if (parameters.containsKey(amount)) {
						ES2Parameter parameter = parameters.get(amount);
						intermediateExchange.amount = parameter.amount;

						// @isCalculatedAmount
						intermediateExchange.isCalculatedAmount = true;

						// @mathematicalRelation
						intermediateExchange.mathematicalRelation = parameter.variableName;
					}
				}

			// @unit
			if (product.getUnit() != null
					&& !product.getUnit().getName().equals("")) {
				ES2Unit unit = getUnit(product.getUnit().getName());
				intermediateExchange.unitId = unit.id;
				intermediateExchange.getUnitNames().addAll(unit.getNames());
			}

			// @comment
			if (product.getComment() != null)
				intermediateExchange.getComment().add(
						new LangString(product.getComment()));

			// @type
			int type = 2;
			if (product.equals(process.getReferenceProducts()[0])) {
				type = 0;
			}
			intermediateExchange.outputGroup = type;

			// @tag (category)
			String tag = product.getCategory();
			if (tag != null && !tag.equals("")) {
				tag = "Category: " + tag;
				intermediateExchange.getTags().add(tag);
				addTagToMasterData(tag, null);
			}

			// @tag (WasteType)
			if (product.getWasteType() != null) {
				tag = "Waste type: " + product.getWasteType();
				intermediateExchange.getTags().add(tag);
				addTagToMasterData(tag, null);
			}

			// @tag (Allocation)
			tag = String.valueOf(product.getAllocation());
			if (tag != null) {
				tag = "Allocation: " + tag;
				intermediateExchange.getTags().add(tag);
				addTagToMasterData("Allocation", null);
			}

			// add master data
			addIntermediateExchangeToMasterData(intermediateExchange);
		}
	}

	private void wasteTreatment(SPWasteTreatment treatment, ES2Dataset dataset) {

		// @tag (Waste treatment allocation)
		String tag;
		WasteTreatmentAllocation treatmentAllocation = treatment
				.getDocumentation().getWasteTreatmentAllocation();
		if (treatmentAllocation != null) {
			tag = "Waste treatment allocation: "
					+ treatmentAllocation.getValue();
			dataset.description.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// convert waste specification
		wasteSpecification(treatment.getWasteSpecification(), dataset);

	}

	private void wasteSpecification(SPWasteSpecification specification,
			ES2Dataset dataset) {
		ES2IntermediateExchange intermediateExchange = new ES2IntermediateExchange();
		dataset.getIntermediateExchanges().add(intermediateExchange);

		// @id
		intermediateExchange.id = UUID.randomUUID().toString();

		// @name
		if (specification.getName() != null)
			intermediateExchange.getName().add(
					new LangString(specification.getName()));

		// @amount
		String amount = specification.getAmount();
		if (amount != null && !specification.getAmount().equals(""))
			try {
				intermediateExchange.amount = Double.parseDouble(specification
						.getAmount().replace(",", "."));
			} catch (NumberFormatException e) {
				if (parameters.containsKey(amount)) {
					ES2Parameter parameter = parameters.get(amount);
					intermediateExchange.amount = parameter.amount;

					// @isCalculatedAmount
					intermediateExchange.isCalculatedAmount = true;

					// @mathematicalRelation
					intermediateExchange.mathematicalRelation = parameter.variableName;
				}
			}

		// @unit
		if (specification.getUnit() != null
				&& !specification.getUnit().equals("")) {
			ES2Unit unit = getUnit(specification.getUnit().getName());
			intermediateExchange.unitId = unit.id;
			intermediateExchange.getUnitNames().addAll(unit.getNames());
		}

		// @comment
		if (specification.getComment() != null
				& !specification.getComment().equals(""))
			intermediateExchange.getComment().add(
					new LangString(specification.getComment()));

		// @outputGroup
		intermediateExchange.outputGroup = 0;
		String tag;

		// @tag (WasteType)
		if (specification.getWasteType() != null) {
			tag = "Waste type: " + specification.getWasteType();
			intermediateExchange.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// @tag (Category)
		String category = specification.getCategory();
		if (category != null && !category.equals("")) {
			tag = "Category: " + category;
			intermediateExchange.getTags().add(tag);
			addTagToMasterData(tag, null);
		}

		// add master data
		addIntermediateExchangeToMasterData(intermediateExchange);
	}
}
