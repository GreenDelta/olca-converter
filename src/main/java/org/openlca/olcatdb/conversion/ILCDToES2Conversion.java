package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.database.CompartmentMap;
import org.openlca.olcatdb.database.ES2CompartmentRec;
import org.openlca.olcatdb.database.ES2ElemFlowRec;
import org.openlca.olcatdb.database.ES2GeographyRec;
import org.openlca.olcatdb.database.ES2UnitRec;
import org.openlca.olcatdb.database.ElemFlowMap;
import org.openlca.olcatdb.database.ILCDElemFlowRec;
import org.openlca.olcatdb.database.UnitMap;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.datatypes.TypeCheck;
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2Description;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2Entry;
import org.openlca.olcatdb.ecospold2.ES2FileAttributes;
import org.openlca.olcatdb.ecospold2.ES2GeographyRef;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2MacroEconomicScenario;
import org.openlca.olcatdb.ecospold2.ES2Parameter;
import org.openlca.olcatdb.ecospold2.ES2Publication;
import org.openlca.olcatdb.ecospold2.ES2Representativeness;
import org.openlca.olcatdb.ecospold2.ES2Technology;
import org.openlca.olcatdb.ecospold2.ES2TimePeriod;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityEntry;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityNameList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityName;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Classification;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ClassificationSystem;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ClassificationSystemList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ElemFlow;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ElemFlowList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Person;
import org.openlca.olcatdb.ecospold2.masterdata.ES2PersonList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ProductFlow;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ProductFlowList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Source;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList;
import org.openlca.olcatdb.ecospold2.resources.ES2Folder;
import org.openlca.olcatdb.ilcd.ILCDClass;
import org.openlca.olcatdb.ilcd.ILCDClassification;
import org.openlca.olcatdb.ilcd.ILCDContact;
import org.openlca.olcatdb.ilcd.ILCDContactDescription;
import org.openlca.olcatdb.ilcd.ILCDExchange;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDGeography;
import org.openlca.olcatdb.ilcd.ILCDMathSection;
import org.openlca.olcatdb.ilcd.ILCDParameter;
import org.openlca.olcatdb.ilcd.ILCDProcess;
import org.openlca.olcatdb.ilcd.ILCDProcessDescription;
import org.openlca.olcatdb.ilcd.ILCDProcessEntry;
import org.openlca.olcatdb.ilcd.ILCDProcessMethod;
import org.openlca.olcatdb.ilcd.ILCDProcessPublication;
import org.openlca.olcatdb.ilcd.ILCDProcessTechnology;
import org.openlca.olcatdb.ilcd.ILCDRepresentativeness;
import org.openlca.olcatdb.ilcd.ILCDSource;
import org.openlca.olcatdb.ilcd.ILCDSourceDescription;
import org.openlca.olcatdb.ilcd.ILCDTime;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts ILCD to EcoSpold 02 data sets.
 * 
 * @author Michael Srocka
 * 
 */
class ILCDToES2Conversion extends AbstractConversionImpl {

	/**
	 * The folder where the created files are stored.
	 */
	private ES2Folder ecoSpoldFolder;

	/**
	 * The list with the created activity names for the master data entries.
	 * This list is saved as a master data file.
	 */
	private ES2ActivityNameList activityNameList = null;

	/**
	 * The list with the created activities for the master data entries. This
	 * list is saved as a master data file.
	 */
	private ES2ActivityList activityList = null;

	/**
	 * The list with the created classification entries for the master data.
	 */
	private ES2ClassificationSystemList classificationSystems = null;

	/**
	 * The list with the sources of the converted data sets. This list is saved
	 * as a master data file.
	 */
	private ES2SourceList sourceList = null;

	/**
	 * The list with the persons of the converted data sets. This list is saved
	 * as a master data file.
	 */
	private ES2PersonList personList = null;

	/**
	 * The master data for the created elementary flows.
	 */
	private ES2ElemFlowList elemFlows = null;

	/**
	 * The master data for the created product flows.
	 */
	private ES2ProductFlowList productFlows = null;

	private static final String CONTEXT_ID = "DE659012-50C4-4e96-B54A-FC781BF987AB";
	private static final LangString CONTEXT_NAME = new LangString("ecoinvent");

	@Override
	public void createFolder(File targetDir) {
		super.createFolder(targetDir);
		ecoSpoldFolder = new ES2Folder(targetDir);
		ecoSpoldFolder.createContent();
		logFile(ecoSpoldFolder);
	}

	@Override
	public ResourceFolder getResult() {
		return ecoSpoldFolder;
	}

	@Override
	public void run() {

		// create the output folder
		createFolder(targetDir);

		initMasterData();

		// set up the parser
		XmlContextParser parser = new XmlContextParser();

		// convert the source data sets
		while (this.hasNext()) {
			try {
				InputStream is = this.next();
				if (is != null) {
					ILCDProcess process = parser.getContext(ILCDProcess.class,
							is);
					is.close();
					if (process != null) {
						DataSetReference ref = convert(process);
						if (ref != null) {
							createdFiles.add(ref);
						}
					}
				}
			} catch (Exception e) {
				logger.severe("Parse exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

		flushMasterData();

		// finish the conversion
		flush();

	}

	/**
	 * Initializes the master data which are filled during the conversion
	 * process.
	 */
	private void initMasterData() {

		// the activity index
		activityList = new ES2ActivityList();
		activityList.contextId = CONTEXT_ID;
		activityList.getContextNames().add(CONTEXT_NAME);

		// the activity names
		activityNameList = new ES2ActivityNameList();
		activityNameList.contextId = CONTEXT_ID;
		activityNameList.getContextNames().add(CONTEXT_NAME);

		// the classifications
		classificationSystems = new ES2ClassificationSystemList();
		classificationSystems.contextId = CONTEXT_ID;
		classificationSystems.getContextNames().add(CONTEXT_NAME);

		// create the system for the ILCD classifications
		ES2ClassificationSystem ilcdSystem = new ES2ClassificationSystem();
		classificationSystems.getSystems().add(ilcdSystem);
		ilcdSystem.id = "10d9abd2-7366-4996-a0d7-549baef3c773";
		ilcdSystem.type = 1;
		ilcdSystem.getName().add(new LangString("ILCD Classification"));

		// the elementary flows
		elemFlows = new ES2ElemFlowList();
		elemFlows.contextId = CONTEXT_ID;
		elemFlows.getContextName().add(CONTEXT_NAME);

		// the product flows
		productFlows = new ES2ProductFlowList();
		productFlows.contextId = CONTEXT_ID;
		productFlows.getContextName().add(CONTEXT_NAME);

		// the sources
		sourceList = new ES2SourceList();
		sourceList.contextId = CONTEXT_ID;
		sourceList.getContextNames().add(CONTEXT_NAME);

		// the persons
		personList = new ES2PersonList();
		personList.contextId = CONTEXT_ID;
		personList.getContextNames().add(CONTEXT_NAME);
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

		// the classifications
		flushMasterData("Classifications.xml", classificationSystems,
				TemplateType.ES2Classifications);
		classificationSystems = null;

		// the elementary flows
		flushMasterData("ElementaryExchanges.xml", elemFlows,
				TemplateType.ES2ElemFlowList);
		elemFlows = null;

		// the product flows
		flushMasterData("IntermediateExchanges.xml", productFlows,
				TemplateType.ES2ProductFlowList);
		productFlows = null;

		// the sources
		flushMasterData("Sources.xml", sourceList, TemplateType.ES2SourceList);
		sourceList = null;

		// the persons
		flushMasterData("Persons.xml", personList, TemplateType.ES2PersonList);
		personList = null;
	}

	/**
	 * Writes the given content to the specified file.
	 */
	private void flushMasterData(String file, Object content,
			TemplateType template) {
		File mdDir = ecoSpoldFolder.getMasterDataFolder();
		File outFile = new File(mdDir, file);
		if (outFile.exists()) {
			logger.severe("Cannot create master data file '" + file
					+ "' because it already exists.");
		} else {
			XmlOutputter outputter = new XmlOutputter();
			outputter.output(content, template, outFile, true);
		}
	}

	private DataSetReference convert(ILCDProcess process) {

		ES2EcoSpold ecoSpold = new ES2EcoSpold();
		ES2Dataset eDataset = ecoSpold.makeDataset();

		// map the process description
		ILCDProcessDescription iDescription = process.description;
		if (iDescription != null) {
			map(iDescription, eDataset);
		}

		// ILCDProcessMethod -> eDataSet
		ILCDProcessMethod iMethod = process.method;
		if (iMethod != null && eDataset.description != null) {

			int type = 1; // = Unit process as default type
			if (iMethod.dataSetType != null
					&& iMethod.dataSetType.equals("LCI result")) {
				type = 2;
			}
			eDataset.description.type = type;

			if (!iMethod.getDeviationsFromLCIMethodApproaches().isEmpty()) {
				int idx = 1;
				eDataset.description.allocationComment = new TextAndImage();
				TextAndImage commment = eDataset.description.allocationComment;
				for (LangString lString : iMethod
						.getDeviationsFromLCIMethodApproaches()) {
					commment.getText().add(lString.setIndex(idx));
				}
			}
		}

		ILCDTime iTime = process.time;
		if (iTime != null) {
			map(iTime, eDataset);
		}

		ILCDGeography iGeography = process.geography;
		if (iGeography != null) {
			geography(iGeography, eDataset);
		}

		ILCDProcessTechnology iTechnology = process.technology;
		if (iTechnology != null) {
			map(iTechnology, eDataset);
		}

		ILCDMathSection iMathSection = process.mathSection;
		if (iMathSection != null) {
			map(iMathSection, eDataset);
		}

		exchanges(process, eDataset);

		// representativeness and data sources
		if (process.representativeness != null) {
			map(process.representativeness, eDataset);
		}

		// the default macro-economic scenario
		ES2MacroEconomicScenario eScenario = new ES2MacroEconomicScenario();
		eScenario.scenarioId = "d9f57f0a-a01f-42eb-a57b-8f18d6635801";
		eScenario.getName().add(new LangString("Business-as-Usual"));
		eDataset.macroEconomicScenario = eScenario;

		// map the data entry by information
		dataEntryBy(process, eDataset);

		// map the data generator and publication
		generatorAndPublication(process, eDataset);

		// create the file attributes
		fileAttributes(eDataset);

		// master data entry for the activity index
		activityEntry(eDataset);

		// write the file
		File outFile = new File(ecoSpoldFolder.getActivityFolder(), UUID
				.randomUUID().toString()
				+ ".spold");
		XmlOutputter outputter = new XmlOutputter();
		outputter.output(ecoSpold, TemplateType.EcoSpold02, outFile, true);

		// create the data set reference of the created process
		DataSetReference reference = new DataSetReference();
		reference.setRefObjectId(process.description.uuid);
		reference.setType("process data set");
		// create the URI
		try {
			String uri = outFile.toURI().toString();
			reference.setUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// the (first) name of the created process
		if (eDataset.description != null
				&& !eDataset.description.getName().isEmpty()) {
			reference.setName(eDataset.description.getName().get(0).getValue());
		}
		return reference;
	}

	/**
	 * Creates the master data index and name entry for the activity of the
	 * created data set.
	 */
	private void activityEntry(ES2Dataset eDataset) {

		// the index entry
		ES2ActivityEntry indexEntry = new ES2ActivityEntry();
		activityList.getEntries().add(indexEntry);
		indexEntry.specialActivityType = 0;

		// the name entry
		ES2ActivityName nameEntry = new ES2ActivityName();
		activityNameList.getActivityNames().add(nameEntry);

		if (eDataset.description != null) {

			// the index attributes
			indexEntry.activityNameId = eDataset.description.activityNameId;
			indexEntry.id = eDataset.description.id;
			indexEntry.getName().addAll(eDataset.description.getName());

			// the name attributes
			nameEntry.id = eDataset.description.activityNameId;
			nameEntry.getNames().addAll(eDataset.description.getName());
		}

		// geography and time for the index attributes
		if (eDataset.geography != null) {
			indexEntry.geographyId = eDataset.geography.geographyId;
			indexEntry.getLocation().addAll(eDataset.geography.getShortNames());
		}
		if (eDataset.timePeriod != null) {
			indexEntry.startDate = eDataset.timePeriod.startDate;
			indexEntry.endDate = eDataset.timePeriod.endDate;
		}

	}

	private void map(ILCDRepresentativeness iRepresentativeness,
			ES2Dataset eDataset) {

		ES2Representativeness eRepresentativeness = eDataset.representativeness;
		if (eRepresentativeness == null) {
			eRepresentativeness = new ES2Representativeness();
			eDataset.representativeness = eRepresentativeness;
		}

		// the default market model ID and name
		eRepresentativeness.systemModelId = "8b738ea0-f89e-4627-8679-433616064e82";
		eRepresentativeness.getSystemModelName().add(
				new LangString("undefined"));

		// percentage of supply or production
		eRepresentativeness.percent = iRepresentativeness.percent;

		// extrapolations
		for (LangString langString : iRepresentativeness
				.getExtrapolationPrinciples()) {
			eRepresentativeness.getExtrapolations().add(langString);
		}
		// deviation from selection / extrapolation principles
		LangString.concat(iRepresentativeness
				.getDeviationsFromExtrapolationPrinciples(),
				eRepresentativeness.getExtrapolations(), " // ");

		// sampling procedure
		for (LangString langString : iRepresentativeness.getSamplingProcedure()) {
			eRepresentativeness.getSamplingProcedure().add(langString);
		}
		// data selection and combination principles
		LangString.concat(iRepresentativeness.getDataSelectionPrinciples(),
				eRepresentativeness.getSamplingProcedure(), " // ");
	}

	/**
	 * Creates the EcoSpold 02 exchanges.
	 */
	private void exchanges(ILCDProcess iProcess, ES2Dataset eDataset) {

		// the quantitative reference
		int refFlow = -1;
		if (iProcess.quantitativeReference != null
				&& !iProcess.quantitativeReference.getReferenceFlows()
						.isEmpty()) {
			refFlow = iProcess.quantitativeReference.getReferenceFlows().get(0);
		}

		for (ILCDExchange iExchange : iProcess.getExchanges()) {

			if (iExchange.flowDataSet == null
					|| iExchange.flowDataSet.getRefObjectId() == null) {
				logger.severe("No valid flow data set referenced in exchange: "
						+ iExchange.id);
			} else {

				String flowId = iExchange.flowDataSet.getRefObjectId();

				// 1) try to map the flow to a known EcoSpold 02 exchange
				String es2Id = ElemFlowMap.ilcdToES2(flowId);
				if (es2Id != null) {

					ES2ElemFlowRec elemFlowRec = ES2ElemFlowRec.forID(es2Id);
					ES2ElementaryExchange eExchange = elemFlowRec.toExchange();
					eDataset.getElementaryExchanges().add(eExchange);

					// TODO: flow property - unit map for conversion factor

					eExchange.amount = iExchange.resultingAmount;
					if (iExchange.isInput()) {
						eExchange.inputGroup = 4;
					} else {
						eExchange.outputGroup = 4;
					}
					eExchange.isCalculatedAmount = iExchange.variableReference != null;
					// eExchange.variableName = iExchange.variableReference;
					// TODO: fix variable issue
				} else {

					ILCDElemFlowRec iElemFlowRec = ILCDElemFlowRec
							.forID(flowId);
					if (iElemFlowRec != null) {
						elementaryExchange(iExchange, iElemFlowRec, eDataset);
					} else {
						intermediateExchange(iExchange, refFlow, flowId,
								eDataset);
					}
				}
			}
		}
	}

	/**
	 * Create an elementary exchange with respective master data entry.
	 */
	private void elementaryExchange(ILCDExchange iExchange,
			ILCDElemFlowRec iElemFlowRec, ES2Dataset eDataset) {
		ES2ElementaryExchange eExchange = new ES2ElementaryExchange();
		eDataset.getElementaryExchanges().add(eExchange);

		eExchange.amount = iExchange.resultingAmount;
		eExchange.casNumber = iElemFlowRec.getCas();
		eExchange.elementaryExchangeId = iElemFlowRec.getId();
		eExchange.formula = iElemFlowRec.getFormula();
		eExchange.id = UUID.randomUUID().toString();
		if (iExchange.isInput()) {
			eExchange.inputGroup = 4;
		} else {
			eExchange.outputGroup = 4;
		}
		eExchange.isCalculatedAmount = iExchange.variableReference != null;
		// TODO: fix variable issue
		// eExchange.variableName = iExchange.variableReference;
		eExchange.getName().add(new LangString(iElemFlowRec.getName()));

		// flow property unit map
		UnitMap.Entry entry = UnitMap.ildcToES2(iElemFlowRec.getPropertyId());
		if (entry != null) {
			eExchange.amount = entry.getFactor() * iExchange.resultingAmount;
			eExchange.unitId = entry.getId();
			ES2UnitRec uRec = ES2UnitRec.forID(entry.getId());
			if (uRec != null) {
				eExchange.getUnitNames().add(new LangString(uRec.getName()));
			}
		}

		// compartment map
		String compId = CompartmentMap.ilcdToES2(iElemFlowRec.getCompartmentId());
		if (compId != null) {
			eExchange.compartmentId = compId;
			ES2CompartmentRec cRec = ES2CompartmentRec.forID(compId);
			if (cRec != null) {
				eExchange.getCompartment().add(
						new LangString(cRec.getCompartment()));
				if (cRec.getSubCompartment() != null) {
					eExchange.getSubCompartment().add(
							new LangString(cRec.getSubCompartment()));
				}
			}
		}

		// create the master data entry
		if (!elemFlows.contains(eExchange.elementaryExchangeId)) {
			ES2ElemFlow eFlow = new ES2ElemFlow();
			elemFlows.getElemFlows().add(eFlow);
			eFlow.casNumber = eExchange.casNumber;
			eFlow.formula = eExchange.formula;
			eFlow.id = eExchange.elementaryExchangeId;
			eFlow.subCompartmentId = eExchange.compartmentId;
			eFlow.unitId = eExchange.unitId;
			eFlow.getName().addAll(eExchange.getName());
			eFlow.getCompartment().addAll(eExchange.getCompartment());
			eFlow.getSubCompartment().addAll(eExchange.getSubCompartment());
			eFlow.getSynonyms().addAll(eExchange.getSynonym());
			eFlow.getUnitName().addAll(eExchange.getUnitNames());
		}
	}

	/**
	 * Create an intermediate exchange with the respective master data entry.
	 */
	private void intermediateExchange(ILCDExchange iExchange, int refFlow,
			String flowId, ES2Dataset eDataset) {
		// 3) create a new intermediate exchange

		// try to load the flow data set
		Object _flow = fetch(iExchange.flowDataSet);
		if (!(_flow instanceof ILCDFlow)) {
			logger.severe("Cannot load flow for reference: "
					+ iExchange.flowDataSet.getUri());
		} else {

			ILCDFlow iFlow = (ILCDFlow) _flow;

			// create an intermediate exchange
			ES2IntermediateExchange eExchange = new ES2IntermediateExchange();
			eDataset.getIntermediateExchanges().add(eExchange);

			eExchange.amount = iExchange.resultingAmount;
			eExchange.id = UUID.randomUUID().toString();
			eExchange.intermediateExchangeId = flowId;

			// the flow description
			if (iFlow.description != null) {
				eExchange.getName().addAll(iFlow.description.getName());
				eExchange.casNumber = iFlow.description.casNumber;
			}

			// the input / output group
			if (iExchange.isInput()) {
				// from technosphere
				eExchange.inputGroup = 5;
			} else {

				if (iExchange.id != null && iExchange.id == refFlow) {
					// reference product
					eExchange.outputGroup = 0;
				} else if (iFlow.isWaste()) {
					// MaterialForTreatment
					eExchange.outputGroup = 3;
				} else if (iFlow.isProduct()) {
					// By-product
					eExchange.outputGroup = 2;
				} else {
					// stock addition
					eExchange.outputGroup = 5;
				}

			}

			// product classification
			List<ES2ClassificationRef> eClassifications = fetchClassifications(iFlow.description
					.getClassifications());
			if (eClassifications != null) {
				for (ES2ClassificationRef eClassification : eClassifications) {
					eExchange.getProductClassifications().add(eClassification);
				}
			}

			String propertyId = iFlow.getReferenceProperty() == null ? null
					: iFlow.getReferenceProperty().getRefObjectId();
			if (propertyId != null) {
				UnitMap.Entry entry = UnitMap.ildcToES2(propertyId);
				if (entry != null) {
					eExchange.amount = entry.getFactor()
							* iExchange.resultingAmount;
					eExchange.unitId = entry.getId();
					ES2UnitRec uRec = ES2UnitRec.forID(entry.getId());
					if (uRec != null) {
						eExchange.getUnitNames().add(
								new LangString(uRec.getName()));
					}
				}
			}

			// create the master data entry
			if (!productFlows.contains(eExchange.intermediateExchangeId)) {

				ES2ProductFlow eFlow = new ES2ProductFlow();
				productFlows.getProductFlows().add(eFlow);

				eFlow.casNumber = eExchange.casNumber;
				eFlow.id = eExchange.intermediateExchangeId;
				eFlow.unitId = eExchange.unitId;
				eFlow.getName().addAll(eExchange.getName());
				eFlow.getProductClassifications().addAll(
						eExchange.getProductClassifications());
				eFlow.getSynonyms().addAll(eExchange.getSynonym());
				eFlow.getUnitName().addAll(eExchange.getUnitNames());

			}

		}// else
	}

	/**
	 * Maps the ILCD process description to the EcoSpold 2 activity data set.
	 * 
	 * @param iDescription
	 *            the ILCD process description
	 * @param eDataset
	 *            the EcoSpold 02 data set
	 */
	private void map(ILCDProcessDescription iDescription, ES2Dataset eDataset) {

		ES2Description eDescription = eDataset.description;
		if (eDescription == null) {
			eDescription = new ES2Description(true);
			eDataset.description = eDescription;
		}

		// the ILCD UUID is mapped to the activity.name.id because
		// this ID is stored in the master data
		// the activity ID is generated
		eDescription.id = UUID.randomUUID().toString();

		// UUID -> activity name ID
		eDescription.activityNameId = iDescription.uuid != null ? iDescription.uuid
				: UUID.randomUUID().toString();

		// the names
		for (LangString langString : iDescription.getName()) {

			// the base name
			String langCode = langString.getLangCode();
			String name = langString.getValue();

			// add the other ILCD name elements to the base name
			LangString toAdd = LangString.getFirst(iDescription
					.getTreatmentStandardsRoutes(), langCode);
			if (toAdd != null) {
				name += ", " + toAdd.getValue();
			}
			toAdd = LangString.getFirst(iDescription.getMixAndLocationTypes(),
					langCode);
			if (toAdd != null) {
				name += ", " + toAdd.getValue();
			}
			toAdd = LangString.getFirst(iDescription
					.getFunctionalUnitFlowProperties(), langCode);
			if (toAdd != null) {
				name += ", " + toAdd.getValue();
			}

			name = TypeCheck.checkLength(name, 120);
			LangString nameString = new LangString(langCode, name);
			eDescription.getName().add(nameString);
		}

		// synonyms
		for (LangString langString : iDescription.getSynonyms()) {
			String synString = langString.getValue();
			if (synString != null) {
				String[] syns = synString.split(";");
				if (syns.length == 1) {
					eDescription.getSynonyms().add(
							TypeCheck.checkLength(langString, 80));
				} else {
					for (String syn : syns) {
						String synonym = TypeCheck.checkLength(syn.trim(), 80);
						eDescription.getSynonyms().add(
								new LangString(langString.getLangCode(),
										synonym));
					}
				}
			}
		}

		// classification
		List<ES2ClassificationRef> eClassifications = fetchClassifications(iDescription
				.getClassifications());
		for (ES2ClassificationRef eClassification : eClassifications) {
			eDataset.getClassifications().add(eClassification);
		}

		// general comment
		if (!iDescription.getComment().isEmpty()) {
			TextAndImage textAndImage = new TextAndImage();
			eDescription.generalComment = textAndImage;
			int idx = 1;
			for (LangString langString : iDescription.getComment()) {
				LangString mls = new LangString(langString.getLangCode(),
						langString.getValue());
				mls.setIndex(idx);
				textAndImage.getText().add(mls);
				idx++;
			}
		}
	}

	/**
	 * Creates a list of EcoSpold classifications from the given list of ILCD
	 * classifications.
	 */
	private List<ES2ClassificationRef> fetchClassifications(
			List<ILCDClassification> iClassifications) {

		List<ES2ClassificationRef> eClassifications = new ArrayList<ES2ClassificationRef>();

		for (ILCDClassification iClassification : iClassifications) {

			if (classificationSystems.first() != null) {

				ES2ClassificationRef eClassifiaction = new ES2ClassificationRef();
				eClassifications.add(eClassifiaction);

				// system name and ID
				eClassifiaction.classificationId = classificationSystems
						.first().id;
				eClassifiaction.getClassificationSystem().addAll(
						classificationSystems.first().getName());

				// create the classification value
				String val = iClassification.getPath("/");

				ES2Classification clazz = classificationSystems.first()
						.forName(val);
				if (clazz == null) {
					clazz = new ES2Classification();
					classificationSystems.first().getClassifications().add(
							clazz);
					clazz.id = UUID.randomUUID().toString();
					clazz.getName().add(new LangString(val));
				}

				eClassifiaction.getClassificationValue()
						.addAll(clazz.getName());
				eClassifiaction.classificationId = clazz.id;

			}

		}

		return eClassifications;
	}

	/**
	 * Maps the ILCD time type to the given EcoSpold 02 activity data set.
	 * 
	 * @param iTime
	 *            the ILCD time type
	 * @param eDataset
	 *            the EcoSpold 02 activity data set
	 */
	private void map(ILCDTime iTime, ES2Dataset eDataset) {

		ES2TimePeriod eTime = eDataset.timePeriod;
		if (eTime == null) {
			eTime = new ES2TimePeriod();
			eDataset.timePeriod = eTime;
		}

		Integer refYear = iTime.referenceYear;
		if (refYear != null) {
			eTime.startDate = refYear.toString() + "-01-01";
		} else {
			eTime.startDate = "9999-01-01";
		}

		Integer validUntil = iTime.validUntil;
		if (validUntil != null) {
			eTime.endDate = validUntil.toString() + "-12-31";
		} else {
			eTime.endDate = "9999-12-31";
		}

		eTime.dataValidForEntirePeriod = true;

		if (!iTime.getDescription().isEmpty()) {
			TextAndImage comment = eTime.comment;
			if (comment == null) {
				comment = new TextAndImage();
				eTime.comment = comment;
			}
			int i = 1;
			for (LangString langString : iTime.getDescription()) {
				LangString commentText = new LangString(langString
						.getLangCode(), langString.getValue());
				commentText.setIndex(i++);
				comment.getText().add(commentText);
			}
		}
	}

	/**
	 * Maps the ILCD geography type to the given EcoSpold 02 activity data set.
	 * 
	 * @param iGeography
	 *            the ILCD geography type
	 * @param eDataset
	 *            the EcoSpold 02 activity data set
	 */
	private void geography(ILCDGeography iGeography, ES2Dataset eDataset) {

		ES2GeographyRef eGeography = new ES2GeographyRef();
		eDataset.geography = eGeography;

		if (iGeography.location != null) {
			ES2GeographyRec geoRec = ES2GeographyRec
					.forShortName(iGeography.location);
			if (geoRec != null) {
				eGeography.geographyId = geoRec.getId();
				eGeography.getShortNames().add(
						new LangString(geoRec.getShortName()));
			} else {
				logger.severe("No EcoSpold location '" + iGeography.location
						+ "' found.");
				// default: GLO = generic geography
				eGeography.geographyId = "34dbbff8-88ce-11de-ad60-0019e336be3a";
				eGeography.getShortNames().add(new LangString("GLO"));
			}
		}

		// the (process) geography description
		if (!iGeography.getDescription().isEmpty()) {
			TextAndImage comment = eGeography.comment;
			if (comment == null) {
				comment = new TextAndImage();
				eGeography.comment = comment;
			}

			int i = 1;
			for (LangString langString : iGeography.getDescription()) {
				LangString text = new LangString(langString.getLangCode(),
						langString.getValue());
				text.setIndex(i++);
				comment.getText().add(text);
			}
		}
	}

	/**
	 * Maps the ILCD technology type to the given EcoSpold 02 activity data set.
	 * 
	 * @param iTechnology
	 *            the ILCD technology type
	 * @param eDataset
	 *            the EcoSpold 02 activity data set
	 */
	private void map(ILCDProcessTechnology iTechnology, ES2Dataset eDataset) {

		ES2Technology eTechnology = eDataset.technology;
		if (eTechnology == null) {
			eTechnology = new ES2Technology();
			eDataset.technology = eTechnology;
		}

		int index = 1;

		// technology description
		if (!iTechnology.getDescription().isEmpty()) {
			TextAndImage comment = eTechnology.comment;
			if (comment == null) {
				comment = new TextAndImage();
				eTechnology.comment = comment;
			}

			for (LangString langString : iTechnology.getDescription()) {
				LangString text = new LangString(langString.getLangCode(),
						langString.getValue());
				text.setIndex(index++);
				comment.getText().add(text);
			}
		}

		// technology applicability
		if (!iTechnology.getApplicability().isEmpty()) {
			TextAndImage comment = eTechnology.comment;
			if (comment == null) {
				comment = new TextAndImage();
				eTechnology.comment = comment;
			}

			for (LangString langString : iTechnology.getApplicability()) {
				LangString text = new LangString(langString.getLangCode(),
						langString.getValue());
				text.setIndex(index++);
				comment.getText().add(text);
			}
		}

		eTechnology.technologyLevel = 0; // default value = "undefined"

		// TODO: fetch image URIS from source files

	}

	/**
	 * Maps the ILCD math section to the EcoSpold activity data set.
	 * 
	 */
	private void map(ILCDMathSection iMathSection, ES2Dataset eDataset) {

		for (ILCDParameter iParameter : iMathSection.getParameters()) {
			ES2Parameter eParameter = new ES2Parameter();
			eParameter.id = UUID.randomUUID().toString();
			if (iParameter.name != null) {
				eParameter.variableName = iParameter.name;
				eParameter.getName().add(new LangString(iParameter.name));
			}
			if (iParameter.meanValue != null) {
				eParameter.amount = iParameter.meanValue;
			}
			eParameter.mathematicalRelation = iParameter.formula;

			// TODO uncertainty

			for (LangString comment : iParameter.getComment()) {
				eParameter.getComment().add(comment);
			}

			eDataset.getParameters().add(eParameter);
		}

	}

	/**
	 * Makes an EcoSpold 02 source from the given ILCD source data set.
	 */
	private ES2Source source(ILCDSource iSource) {

		ES2Source eSource = new ES2Source();

		ILCDSourceDescription iDescription = iSource.description;
		if (iDescription != null) {

			// source ID
			eSource.id = iDescription.uuid;

			// short name
			if (!iDescription.getShortName().isEmpty()) {
				LangString langString = iDescription.getShortName().get(0);
				eSource.shortName = langString.getValue();
				if (eSource.shortName != null
						&& eSource.shortName.length() > 80) {
					eSource.shortName = eSource.shortName.substring(0, 80);
				}
			}

			// source citation
			eSource.title = iDescription.sourceCitation;

			// first author
			if (iDescription.sourceCitation != null) {
				if (iDescription.sourceCitation.length() > 40) {
					eSource.firstAuthor = iDescription.sourceCitation
							.substring(0, 38)
							+ "#";
				} else {
					eSource.firstAuthor = iDescription.sourceCitation;
				}

			}

			// source type
			if (iDescription.publicationType != null) {
				String pubType = iDescription.publicationType;
				int ePubType = -1; // default
				if (pubType.equals("Undefined")) {
					ePubType = 0;
				} else if (pubType.equals("Article in periodical")) {
					ePubType = 1;
				} else if (pubType.equals("Chapter in anthology")) {
					ePubType = 2;
				} else if (pubType.equals("Monograph")) {
					ePubType = 3;
				} else if (pubType.equals("Direct measurement")) {
					ePubType = 4;
				} else if (pubType.equals("Oral communication")) {
					ePubType = 5;
				} else if (pubType.equals("Personal written communication")) {
					ePubType = 6;
				} else if (pubType.equals("Questionnaire")) {
					ePubType = 7;
				}

				if (ePubType == -1) {
					eSource.sourceType = 0;
				} else {
					eSource.sourceType = ePubType;
				}
			}

			// source description
			for (LangString langString : iDescription.getComment()) {
				eSource.getComment().add(langString);
			}
		}
		return eSource;
	}

	/**
	 * Creates the element for the data generator and publication.
	 */
	private void generatorAndPublication(ILCDProcess iProcess,
			ES2Dataset eDataset) {

		ILCDProcessPublication iPublication = iProcess.publication;
		ES2Publication ePublication = new ES2Publication();
		eDataset.publication = ePublication;

		// the publication status
		if (iPublication != null && iPublication.workflowStatus != null) {

			if (iPublication.workflowStatus
					.equals("Data set finalised; entirely published")) {
				ePublication.dataPublishedIn = 2;
			} else if (iPublication.workflowStatus
					.equals("Data set finalised; subsystems published")) {
				ePublication.dataPublishedIn = 1;
			} else {
				ePublication.dataPublishedIn = 0;
			}
		}

		// the publication source
		if (iPublication != null && iPublication.republicationReference != null) {

			String sourceId = iPublication.republicationReference
					.getRefObjectId();
			ePublication.publishedSourceId = sourceId;
			ePublication.publishedSourceContextId = CONTEXT_ID;

			ES2Source source = sourceList.getSource(sourceId);
			if (source == null) {
				Object iSource = fetch(iPublication.republicationReference);
				if (iSource instanceof ILCDSource) {
					source = source((ILCDSource) iSource);
					if (source != null) {
						sourceList.getSources().add(source);
					}
				}
			}
			if (source != null) {
				ePublication.publishedSourceFirstAuthor = source.firstAuthor;
				ePublication.publishedSourceYear = source.year != null ? source.year
						.toString()
						: null;
			}
		}

		// the data generator
		if (!iProcess.getDataGenerators().isEmpty()) {
			DataSetReference generatorRef = iProcess.getDataGenerators().get(0);
			ePublication.personId = generatorRef.getRefObjectId();
			ePublication.personContextId = CONTEXT_ID;
			ES2Person person = personList.getPerson(ePublication.personId);
			if (person == null) {
				// try to load the contact data set
				Object iContact = fetch(generatorRef);
				if (iContact instanceof ILCDContact) {
					person = person((ILCDContact) iContact);
					if (person != null) {
						personList.getPersons().add(person);
					}
				}
			}
			if (person != null) {
				ePublication.personEmail = person.email;
				ePublication.personName = person.name;
			}
		}

		// copyright
		if (iPublication.copyright != null) {
			ePublication.isCopyrightProtected = iPublication.copyright;
		}
	}

	/**
	 * Makes an EcoSpold 02 person from the given ILCD contact data set.
	 */
	private ES2Person person(ILCDContact iContact) {

		ES2Person ePerson = new ES2Person();
		ILCDContactDescription iDescription = iContact.description;
		if (iDescription != null) {

			// check if the
			boolean isOrganisation = false;
			for (ILCDClassification classification : iDescription
					.getClassifications()) {
				for (ILCDClass clazz : classification.getClasses()) {
					if (clazz.name != null
							&& clazz.name.equals("Organisations")) {
						isOrganisation = true;
						break;
					}
				}
			}

			ePerson.id = iDescription.uuid;
			ePerson.name = iDescription.getShortName().isEmpty() ? "no name"
					: iDescription.getShortName().get(0).getValue();
			if (isOrganisation) {
				ePerson.organisationName = ePerson.name;
			}
			ePerson.address = iDescription.getContactAddress().isEmpty() ? null
					: iDescription.getContactAddress().get(0).getValue();
			ePerson.telephone = iDescription.telephone;
			ePerson.telefax = iDescription.telefax;
			ePerson.email = iDescription.email != null ? iDescription.email
					: "no email";
			ePerson.organisationWebsite = iDescription.webAddress;
		}
		return ePerson;
	}

	/**
	 * Creates the element 'dataEntryBy' (the reference to the person who
	 * entered the data).
	 */
	private void dataEntryBy(ILCDProcess iProcess, ES2Dataset eDataSet) {

		ES2Entry entryBy = new ES2Entry();
		eDataSet.dataEntryBy = entryBy;

		ILCDProcessEntry iEntry = iProcess.entry;
		if (iEntry == null || iEntry.dataSetEntryReference == null) {
			logger.severe("No contact information for 'data entry by'.");
			return;
		}

		entryBy.personContextId = CONTEXT_ID;
		entryBy.personId = iEntry.dataSetEntryReference.getRefObjectId();

		ES2Person person = personList.getPerson(entryBy.personId);
		if (person == null) {
			// try to load the contact data set
			Object iContact = fetch(iEntry.dataSetEntryReference);
			if (!(iContact instanceof ILCDContact)) {
				logger.severe("Cannot load the contact for the "
						+ "'data entry by' reference.");
			} else {
				person = person((ILCDContact) iContact);
				if (person != null) {
					personList.getPersons().add(person);
				}
			}
		}

		if (person != null) {
			entryBy.personEmail = person.email;
			entryBy.personName = person.name;
		}

	}

	/**
	 * Creates the file attributes of the data set.
	 */
	private void fileAttributes(ES2Dataset eDataSet) {

		ES2FileAttributes fileAtts = new ES2FileAttributes(true);
		eDataSet.fileAttributes = fileAtts;
		fileAtts.creationTimestamp = Time.now();
		fileAtts.fileTimestamp = Time.now();
		fileAtts.fileGenerator = "openLCA data converter";
		fileAtts.contextId = CONTEXT_ID;
		fileAtts.getContextNames().add(CONTEXT_NAME);

	}

	/**
	 * Overrides the match function for ZIP entries: only the ILCD process data
	 * sets are selected for the conversion.
	 */
	@Override
	protected boolean matchEntry(String entryName) {
		boolean match = super.matchEntry(entryName); // an XML file
		if (match) {
			match = entryName.contains("processes");
		}
		return match;
	}
}
