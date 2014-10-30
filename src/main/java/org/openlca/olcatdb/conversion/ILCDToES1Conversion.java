package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.database.CompartmentMap;
import org.openlca.olcatdb.database.ES1CompartmentRec;
import org.openlca.olcatdb.database.ES1ElemFlowRec;
import org.openlca.olcatdb.database.ElemFlowMap;
import org.openlca.olcatdb.database.ILCDElemFlowRec;
import org.openlca.olcatdb.database.ILCDPropertyRec;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.datatypes.TypeCheck;
import org.openlca.olcatdb.ecospold1.ES1DataEntryBy;
import org.openlca.olcatdb.ecospold1.ES1DataGeneratorAndPublication;
import org.openlca.olcatdb.ecospold1.ES1DataSetInformation;
import org.openlca.olcatdb.ecospold1.ES1Dataset;
import org.openlca.olcatdb.ecospold1.ES1EcoSpold;
import org.openlca.olcatdb.ecospold1.ES1Exchange;
import org.openlca.olcatdb.ecospold1.ES1Geography;
import org.openlca.olcatdb.ecospold1.ES1Person;
import org.openlca.olcatdb.ecospold1.ES1ReferenceFunction;
import org.openlca.olcatdb.ecospold1.ES1Representativeness;
import org.openlca.olcatdb.ecospold1.ES1Source;
import org.openlca.olcatdb.ecospold1.ES1Technology;
import org.openlca.olcatdb.ecospold1.ES1TimePeriod;
import org.openlca.olcatdb.ecospold1.ES1Validation;
import org.openlca.olcatdb.ecospold1.resources.ES1Folder;
import org.openlca.olcatdb.ilcd.ILCDClass;
import org.openlca.olcatdb.ilcd.ILCDClassification;
import org.openlca.olcatdb.ilcd.ILCDContact;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDExchange;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDProcess;
import org.openlca.olcatdb.ilcd.ILCDReview;
import org.openlca.olcatdb.ilcd.ILCDSource;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts ILCD process data sets to EcoSpold 01.
 * 
 * @author Michael Srocka
 * 
 */
public class ILCDToES1Conversion extends AbstractConversionImpl {

	/**
	 * Indicates if the files should be sima pro conform. If true also a sima
	 * pro mapping file will be created
	 */
	private boolean simaProConform = false;

	/**
	 * Sima pro sub converter
	 */
	private ES1SimaProValidConversion simaProConverter;

	/**
	 * The target folder of the created files.
	 */
	private ES1Folder es1Folder;

	/**
	 * An UUID-ID map cache for the persons of one data set.
	 */
	private HashMap<String, Integer> personMap = new HashMap<String, Integer>();

	/**
	 * An UUID-ID map cache for the sources of one data set.
	 */
	private HashMap<String, Integer> sourceMap = new HashMap<String, Integer>();

	public ILCDToES1Conversion() {

	}

	public ILCDToES1Conversion(boolean simaProConform) {
		this.simaProConform = simaProConform;
		if (simaProConform) {
			simaProConverter = new ES1SimaProValidConversion();
		}
	}

	@Override
	public void createFolder(File targetDir) {
		super.createFolder(targetDir);
		this.es1Folder = new ES1Folder(targetDir);
		this.es1Folder.createContent();
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

					// parse and convert the data set
					ILCDProcess process = parser.getContext(ILCDProcess.class,
							is);
					is.close();
					if (process != null) {
						DataSetReference reference = convert(process);
						if (reference != null)
							createdFiles.add(reference);
					}

				} catch (Exception e) {
					logger.severe("Parse exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (simaProConform) {
			// SPMapping file
			try {
				simaProConverter.getMappingFile().writeToFile(
						targetDir.getAbsolutePath() + File.separator
								+ "mapping.SPMapping");
			} catch (IOException e) {
				logger.severe("IO exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// finish the conversion
		flush();
	}

	/**
	 * Converts the given ILCD process to an EcoSpold 01 data set. The EcoSpold
	 * data set is written to a file and a data set reference describing this
	 * file is returned.
	 */
	private DataSetReference convert(ILCDProcess process) {

		// initialization of the reference attributes
		String uuid = process.description != null
				&& process.description.uuid != null ? process.description.uuid
				: UUID.randomUUID().toString();

		// construct the name
		String name = "no name";
		if (process.description != null) {
			String v = LangString.getFirstValue(process.description.getName(),
					"en");
			if (v != null) {
				name = v;
			}
			v = LangString.getFirstValue(
					process.description.getTreatmentStandardsRoutes(), "en");
			if (v != null) {
				name += ("; " + v);
			}
			v = LangString.getFirstValue(
					process.description.getMixAndLocationTypes(), "en");
			if (v != null) {
				name += ("; " + v);
			}
			v = LangString
					.getFirstValue(process.description
							.getFunctionalUnitFlowProperties(), "en");
			if (v != null) {
				name += ("; " + v);
			}
		}

		// create the EcoSpold 01 data set.
		ES1EcoSpold ecoSpold = new ES1EcoSpold();

		// #> dataset
		ES1Dataset dataset = new ES1Dataset();
		ecoSpold.getDatasets().add(dataset);
		dataset.generator = "openLCA data converter 1.2"; // default
		dataset.number = 1; // default
		dataset.timestamp = Time.now();

		// TODO: set valid master data: valid units, ...

		referenceFunction(process, dataset);

		geography(process, dataset);

		technology(process, dataset);

		timePeriod(process, dataset);

		datasetInformation(process, dataset);

		representativeness(process, dataset);

		sources(process, dataset);

		validation(process, dataset);

		dataEntryBy(process, dataset);

		dataGeneratorAndPublication(process, dataset);

		exchanges(process, dataset);

		if (simaProConform) {
			simaProConverter.validate(dataset);
			// process for the SP Mapping File
			simaProConverter.processForMappingFile(dataset);
		}

		// write the file and create a data set reference
		DataSetReference ref = null;

		File outFile = new File(es1Folder.getProcessFolder(), uuid + ".xml");
		if (outFile.exists()) {
			logger.severe("The file " + outFile.getAbsolutePath()
					+ " already exists.");
		} else {
			XmlOutputter outputter = new XmlOutputter();
			outputter.output(ecoSpold, TemplateType.EcoSpold01, outFile, true);

			ref = new DataSetReference();
			ref.setName(name);
			ref.setUri(outFile.toURI().toString());
			ref.setRefObjectId(uuid);
			ref.setType(ILCDDataSetType.Process.toString());
			ref.setVersion("01.00.000");
		}

		// clear the conversion cache
		personMap.clear();
		sourceMap.clear();

		return ref;
	}

	/**
	 * @param process
	 * @param dataset
	 */
	private void sources(ILCDProcess process, ES1Dataset dataset) {
		if (process.representativeness != null) {

			for (DataSetReference ref : process.representativeness
					.getDataSources()) {
				String _uuid = ref.getRefObjectId();
				if (!sourceMap.keySet().contains(_uuid)) {
					ES1Source eSource = source(ref);
					if (eSource != null) {
						dataset.getSources().add(eSource);
						eSource.number = sourceMap.size() + 1;
						sourceMap.put(_uuid, eSource.number);
					}
				}
			}
		}
	}

	/**
	 * Creates the reference function element and assigns the process relevant
	 * information to it.
	 */
	private void referenceFunction(ILCDProcess process, ES1Dataset dataset) {
		// #> reference function
		ES1ReferenceFunction refFunction = new ES1ReferenceFunction();
		dataset.referenceFunction = refFunction;
		refFunction.datasetRelatesToProduct = true; // default
		refFunction.infrastructureProcess = false; // default
		refFunction.infrastructureIncluded = false; // default

		// @ general comment
		if (process.description != null) {
			String com = LangString.getFirstValue(
					process.description.getComment(), "en");
			if (com != null) {
				refFunction.generalComment = com;
			}
		}

		// @ included processes
		if (process.technology != null) {
			refFunction.includedProcesses = LangString.getFirstValue(
					process.technology.getDescription(), "en");
		}
	}

	/**
	 * This function assigns the flow relevant information to the reference
	 * function from the given (reference exchange) and vice versa.
	 */
	private void referenceFunction(ES1Exchange refExchange, ES1Dataset dataset) {
		if (dataset.referenceFunction == null)
			return;
		ES1ReferenceFunction refFunction = dataset.referenceFunction;
		refFunction.name = refExchange.name;
		refFunction.localName = refExchange.localName;
		refFunction.category = refExchange.category;
		refFunction.subCategory = refExchange.subCategory;
		refFunction.localCategory = refExchange.localCategory;
		refFunction.localSubCategory = refExchange.localSubCategory;
		refFunction.amount = refExchange.meanValue;
		refFunction.unit = refExchange.unit;
		refFunction.CASNumber = refExchange.CASNumber;
		refFunction.formula = refExchange.formula;
		refFunction.datasetRelatesToProduct = true;

		refExchange.location = dataset.geography != null ? dataset.geography.location
				: "GLO";
		refExchange.inputGroup = null;
		refExchange.outputGroup = 0; // = reference output
	}

	/**
	 * Creates the geography element.
	 */
	private void geography(ILCDProcess process, ES1Dataset dataset) {
		// #> geography
		ES1Geography eGeography = new ES1Geography();
		dataset.geography = eGeography;

		if (process.geography != null) {

			// @ location, text
			eGeography.location = process.geography.location;
			eGeography.text = LangString.getFirstValue(
					process.geography.getDescription(), "en");
		}
	}

	/**
	 * Creates the technology element.
	 */
	private void technology(ILCDProcess process, ES1Dataset dataset) {
		// #> technology
		ES1Technology eTechnology = new ES1Technology();
		dataset.technology = eTechnology;

		if (process.technology != null) {
			// @ text
			eTechnology.text = LangString.getFirstValue(
					process.technology.getApplicability(), "en");
		}
	}

	/**
	 * Creates the time element.
	 */
	private void timePeriod(ILCDProcess process, ES1Dataset dataset) {
		// #> time period
		ES1TimePeriod eTime = new ES1TimePeriod();
		dataset.timePeriod = eTime;
		eTime.dataValidForEntirePeriod = true; // default

		if (process.time != null) {

			// @ start / end year
			eTime.startYear = process.time.referenceYear;
			eTime.endYear = process.time.validUntil;

			// @ text
			eTime.text = LangString.getFirstValue(
					process.time.getDescription(), "en");

		}
	}

	/**
	 * Creates the data set information element.
	 */
	private void datasetInformation(ILCDProcess process, ES1Dataset dataset) {

		// #> data set information
		ES1DataSetInformation eDataInfo = new ES1DataSetInformation();
		dataset.dataSetInformation = eDataInfo;
		eDataInfo.energyValues = 0; // default
		eDataInfo.impactAssessmentResult = false; // default
		eDataInfo.internalVersion = 1.0; // default
		eDataInfo.languageCode = "en"; // default
		eDataInfo.localLanguageCode = "de"; // default
		eDataInfo.timestamp = Time.now(); // default
		eDataInfo.version = 1.0; // default

		// @ type
		// 0=System non-terminated. 1=Unit process. 2=System terminated.
		// 3=Elementary flow. 4=Impact category.5=Multioutput process
		if (process.method != null && process.method.dataSetType != null) {
			String type = process.method.dataSetType;
			if (type.equals("Unit process, single operation")) {
				eDataInfo.type = 1;
			} else if (type.equals("Unit process, black box")) {
				eDataInfo.type = 1;
			} else if (type.equals("LCI result")) {
				eDataInfo.type = 2;
			} else if (type.equals("Partly terminated system")) {
				eDataInfo.type = 0;
			} else {
				eDataInfo.type = 1; // default
			}
		}
	}

	/**
	 * Creates the representativeness element.
	 */
	private void representativeness(ILCDProcess process, ES1Dataset dataset) {
		// #> representativeness
		ES1Representativeness eRepri = new ES1Representativeness();
		dataset.representativeness = eRepri;

		if (process.representativeness != null) {

			// @ extrapolations
			eRepri.extrapolations = LangString.getFirstValue(
					process.representativeness.getExtrapolationPrinciples(),
					"en");

			// @ percent
			eRepri.percent = process.representativeness.percent;

			// @ production volume
			eRepri.productionVolume = TypeCheck.checkLength(LangString
					.getFirstValue(
							process.representativeness.getProductionVolume(),
							"en"), 80);

			// @ sampling procedure
			String s1 = LangString.getFirstValue(
					process.representativeness.getSamplingProcedure(), "en");
			String s2 = LangString.getFirstValue(
					process.representativeness.getDataSelectionPrinciples(),
					"en");
			if (s1 == null) {
				eRepri.samplingProcedure = s2;
			} else if (s2 == null) {
				eRepri.samplingProcedure = s1;
			} else if (s1 != null && s2 != null) {
				eRepri.samplingProcedure = s1 + " // " + s2;
			}

			// @ uncertainty adjustments
			eRepri.uncertaintyAdjustments = LangString.getFirstValue(
					process.representativeness.getUncertaintyAdjustments(),
					"en");

		}
	}

	/**
	 * Creates the validation element of the data set.
	 */
	private void validation(ILCDProcess process, ES1Dataset dataset) {
		if (process.getReviews().size() > 0) {

			ES1Validation eValidation = new ES1Validation();
			dataset.validation = eValidation;

			// can only take the first review
			ILCDReview iReview = process.getReviews().get(0);

			// @ other details
			eValidation.otherDetails = LangString.getFirstValue(
					iReview.getOtherDetails(), "en");

			// @ proof reading details
			eValidation.proofReadingDetails = LangString.getFirstValue(
					iReview.getDetails(), "en");
			if (eValidation.proofReadingDetails == null) {
				eValidation.proofReadingDetails = "no details";
			}

			// @ validator
			for (DataSetReference ref : iReview.getReviewers()) {

				String _uuid = ref.getRefObjectId();
				if (!personMap.keySet().contains(_uuid)) {
					ES1Person person = person(ref);
					if (person != null) {
						person.number = personMap.size() + 1;
						dataset.getPersons().add(person);
						personMap.put(_uuid, person.number);
					}
				}

				if (eValidation.proofReadingValidator == null) {
					eValidation.proofReadingValidator = personMap.get(_uuid);
				}
			}
		}
	}

	/**
	 * Creates the data entry by element.
	 */
	private void dataEntryBy(ILCDProcess process, ES1Dataset dataset) {
		if (process.entry != null) {

			DataSetReference ref = process.entry.dataSetEntryReference;
			if (ref != null) {

				String _uuid = ref.getRefObjectId();
				if (!personMap.keySet().contains(_uuid)) {
					ES1Person person = person(ref);
					if (person != null) {
						person.number = personMap.size() + 1;
						dataset.getPersons().add(person);
						personMap.put(_uuid, person.number);
					}
				}

				ES1DataEntryBy entryBy = new ES1DataEntryBy();
				dataset.dataEntryBy = entryBy;

				// @ person
				entryBy.person = personMap.get(_uuid);

			}
		}
	}

	/**
	 * Creates the element for the data generator and publication.
	 */
	private void dataGeneratorAndPublication(ILCDProcess process,
			ES1Dataset dataset) {
		ES1DataGeneratorAndPublication ePublication = new ES1DataGeneratorAndPublication();
		dataset.dataGeneratorAndPublication = ePublication;

		if (process.publication != null) {

			// @ access restricted to
			String license = process.publication.licenseType;
			if (license != null
					&& license.equals("Free of charge for all users and uses")) {
				ePublication.accessRestrictedTo = 0;
			}

			// @ copyright
			ePublication.copyright = process.publication.copyright;

			// @ reference to published source
			DataSetReference ref = process.publication.republicationReference;
			if (ref != null) {
				String _uuid = ref.getRefObjectId();
				if (!sourceMap.keySet().contains(_uuid)) {
					ES1Source eSource = source(ref);
					if (eSource != null) {
						dataset.getSources().add(eSource);
						eSource.number = sourceMap.size() + 1;
						sourceMap.put(_uuid, eSource.number);
					}
				}
				ePublication.referenceToPublishedSource = sourceMap.get(_uuid);
			}

			// @ data published in
			if (ePublication.referenceToPublishedSource != null)
				ePublication.dataPublishedIn = 2;
			else
				ePublication.dataPublishedIn = 0;
		}

		// @ person
		for (DataSetReference ref : process.getDataGenerators()) {

			String _uuid = ref.getRefObjectId();
			if (!personMap.keySet().contains(_uuid)) {
				ES1Person person = person(ref);
				if (person != null) {
					person.number = personMap.size() + 1;
					dataset.getPersons().add(person);
					personMap.put(_uuid, person.number);
				}
			}

			if (ePublication.person == null) {
				ePublication.person = personMap.get(_uuid);
			}
		}
	}

	/**
	 * Creates an EcoSpold 01 source from the given ILCD source reference.
	 */
	private ES1Source source(DataSetReference ref) {

		ES1Source eSource = null;

		Object o = fetch(ref);
		if (!(o instanceof ILCDSource)) {
			logger.severe("Cannot load source from reference: " + ref.getUri());
		} else {
			ILCDSource iSource = (ILCDSource) o;
			eSource = new ES1Source();
			eSource.placeOfPublications = "see title"; // default
			eSource.year = 9999; // default

			if (iSource.description != null) {

				// @ title
				eSource.title = iSource.description.sourceCitation;

				// @ first author
				eSource.firstAuthor = iSource.description.sourceCitation; // default

				// @ source type
				if (iSource.description.publicationType != null) {
					String pubType = iSource.description.publicationType;
					if (pubType.equals("Undefined")) {
						eSource.sourceType = 0;
					} else if (pubType.equals("Article in periodical")) {
						eSource.sourceType = 1;
					} else if (pubType.equals("Chapter in anthology")) {
						eSource.sourceType = 2;
					} else if (pubType.equals("Monograph")) {
						eSource.sourceType = 3;
					} else if (pubType.equals("Direct measurement")) {
						eSource.sourceType = 4;
					} else if (pubType.equals("Oral communication")) {
						eSource.sourceType = 5;
					} else if (pubType.equals("Personal written communication")) {
						eSource.sourceType = 6;
					} else if (pubType.equals("Questionnaire")) {
						eSource.sourceType = 7;
					} else {
						eSource.sourceType = 0;
					}
				}

				// @ text
				eSource.text = LangString.getFirstValue(
						iSource.description.getComment(), "en");
			}
			eSource.makeValid();
		}// else

		return eSource;
	}

	/**
	 * Creates an EcoSpold 01 person from the given ILCD contact reference.
	 */
	private ES1Person person(DataSetReference ref) {

		ES1Person person = null;

		Object o = fetch(ref);
		if (!(o instanceof ILCDContact)) {
			logger.severe("Cannot load contact from reference: " + ref.getUri());
		} else {
			ILCDContact contact = (ILCDContact) o;
			person = new ES1Person();
			person.companyCode = "-"; // default
			person.countryCode = "CH"; // default

			if (contact.description != null) {

				// @ address
				person.address = TypeCheck.checkLength(
						LangString.getFirstValue(
								contact.description.getContactAddress(), "en"),
						255);
				if (person.address == null) {
					person.address = "no address";
				}

				// @ email
				person.email = TypeCheck.checkLength(contact.description.email,
						80);

				// @ name
				person.name = TypeCheck.checkLength(LangString.getFirstValue(
						contact.description.getName(), "en"), 40);

				// @ fax
				person.telefax = TypeCheck.checkLength(
						contact.description.telefax, 40);

				// @ telephone
				person.telephone = TypeCheck.checkLength(
						contact.description.telephone, 40);
			}

		}

		return person;
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

	/**
	 * Creates the exchange elements.
	 */
	private void exchanges(ILCDProcess iProcess, ES1Dataset eDataset) {

		// the reference flow
		int refFlow = -1;
		if (iProcess.quantitativeReference != null) {
			if (!iProcess.quantitativeReference.getReferenceFlows().isEmpty()) {
				refFlow = iProcess.quantitativeReference.getReferenceFlows()
						.get(0);
			}
		}

		int nr = 200000;
		for (ILCDExchange iExchange : iProcess.getExchanges()) {

			// map the general exchange attributes
			ES1Exchange eExchange = new ES1Exchange();
			eDataset.getExchanges().add(eExchange);
			eExchange.number = nr++; // default
			eExchange.location = iExchange.location;
			eExchange.generalComment = LangString.getFirstValue(
					iExchange.getComment(), "en");

			// the flow dispatch
			double factor = flowDispatch(iExchange, eExchange);

			// numeric values
			if (factor == 0) {
				logger.severe("No conversion factor: " + iExchange.toString());
			} else {
				numericValues(iExchange, eExchange, factor);
			}

			// reference flow? -> reference function
			if (iExchange.id != null && iExchange.id == refFlow) {
				referenceFunction(eExchange, eDataset);
			}

		}

	}

	/**
	 * The dispatch function for assigning the respective flow information to
	 * the target exchange. There are four possibilities:
	 * <ul>
	 * <li>The source exchange describes an elementary flow which is stored in
	 * the database and there <b>IS</b> a mapping to a stored flow of the target
	 * format in the database: <code>assignedElemFlow(...)</code> is called.</li>
	 * <li>The source exchange describes an elementary flow which is stored in
	 * the database and there <b>IS NO</b> mapping to a stored flow of the
	 * target format in the database: <code>unassignedElemFlow(...)</code> is
	 * called.</li>
	 * <li>The source exchange describes an elementary flow which is <b>NOT</b>
	 * stored in the database: <code>unknownElemFlow(...)</code> is called.</li>
	 * <li>The source exchange describes a product flow:
	 * <code>productFlow(...)</code> is called</li>
	 * </ul>
	 * 
	 * This function is included in every conversion but, naturally, the
	 * implementation of the case differentiation differs depending on the
	 * respective source and target format.
	 * 
	 * As the reference unit in which a flow is stated in the source format may
	 * differs to the reference unit (of this flow) in the target format, this
	 * function returns a conversion factor which is then applied in the numeric
	 * values of the exchange (see the function <code>numericValues(..)</code>).
	 */
	private double flowDispatch(ILCDExchange iExchange, ES1Exchange eExchange) {

		double factor = 0;

		// check the flow reference
		String flowId = null;
		DataSetReference flowRef = iExchange.flowDataSet;
		if (flowRef != null) {
			flowId = flowRef.getRefObjectId();
		}
		if (flowId == null) {
			logger.severe("No valid flow reference in ILCD exchange: "
					+ flowRef);
			return 0;
		}

		// for the input/output group, see below
		boolean isProduct = false;

		ElemFlowMap.Entry mapEntry = ElemFlowMap.ilcdToES1(flowId);
		if (mapEntry != null) {

			// assigned elementary flow
			factor = assignedElemFlow(mapEntry, eExchange);

		} else {

			ILCDElemFlowRec flowRec = ILCDElemFlowRec.forID(flowId);
			if (flowRec != null) {

				// unassigned elementary flow
				factor = unassignedElemFlow(flowRec, eExchange);

			} else {

				Object obj = fetch(flowRef);
				if (!(obj instanceof ILCDFlow)) {
					logger.severe("Cannot load ILCD flow data set for reference: "
							+ flowRef);
				} else {
					ILCDFlow flow = (ILCDFlow) obj;

					if (flow.isElementary()) {

						// unknown elementary flow
						factor = unknownElemFlow(flow, eExchange);

					} else {

						factor = productFlow(flow, eExchange);
						isProduct = true;
					}
				}
			}
		}

		// definition of the input / output group
		if (isProduct) {
			if (iExchange.isInput()) {
				// from technosphere
				eExchange.inputGroup = 5;
			} else {
				// waste to treatment as default
				// reference product = 0 is set in the function 'exchanges'
				eExchange.outputGroup = 3;
			}
		} else {
			if (iExchange.isInput()) {
				// from nature
				eExchange.inputGroup = 4;
			} else {
				// to nature
				eExchange.outputGroup = 4;
			}
		}

		return factor;
	}

	/**
	 * This function is called when there is an elementary flow mapping stored
	 * in the database. The information of the respective flow of the target
	 * format is assigned to the given exchange.
	 */
	private double assignedElemFlow(ElemFlowMap.Entry mapEntry,
			ES1Exchange eExchange) {

		ES1ElemFlowRec rec = ES1ElemFlowRec.forID(mapEntry.getFlowId());
		if (rec == null) {
			logger.severe("Cannot load EcoSpold 01 flow for ID: "
					+ mapEntry.getFlowId());
			return 0;
		}

		eExchange.number = rec.getId();
		eExchange.CASNumber = formatCAS(rec.getCas());
		eExchange.formula = rec.getFormula();
		eExchange.name = rec.getName();
		eExchange.localName = rec.getName();
		eExchange.unit = rec.getUnit();

		// compartment
		ES1CompartmentRec compartment = ES1CompartmentRec.forID(rec
				.getCompartmentId());
		if (compartment != null) {
			eExchange.category = compartment.getCompartment();
			eExchange.localCategory = compartment.getCompartment();
			eExchange.subCategory = compartment.getSubCompartment();
			eExchange.localSubCategory = compartment.getSubCompartment();
		}

		return mapEntry.getFactor();
	}

	/**
	 * This function is called when there is the source elementary exchange
	 * stored in the database but there is no stored elementary flow of the
	 * target format assigned to this flow. The information of the source flow
	 * are transformed to the given exchange.
	 */
	private double unassignedElemFlow(ILCDElemFlowRec flowRec,
			ES1Exchange eExchange) {

		eExchange.name = TypeCheck.checkLength(flowRec.getName(), 80);
		eExchange.localName = TypeCheck.checkLength(flowRec.getName(), 80);
		eExchange.CASNumber = formatCAS(flowRec.getCas());
		eExchange.formula = flowRec.getFormula();

		// compartment
		int compId = CompartmentMap.ilcdToES1(flowRec.getCompartmentId());
		if (compId == -1) {
			logger.severe("No corresponding compartment for "
					+ "ILCD elementary flow category "
					+ flowRec.getCompartmentId());
		} else {
			ES1CompartmentRec compRec = ES1CompartmentRec.forID(compId);
			if (compRec != null) {
				eExchange.category = compRec.getCompartment();
				eExchange.localCategory = compRec.getCompartment();
				eExchange.subCategory = compRec.getSubCompartment();
				eExchange.localSubCategory = compRec.getSubCompartment();
			}
		}

		// unit assignment
		String unit = ILCDPropertyRec.unit(flowRec.getPropertyId());

		if (unit == null) {
			logger.severe("Cannot load unit for ILCD flow property: "
					+ flowRec.getPropertyId());
			return 0;
		} else {
			eExchange.unit = unit;
		}
		return 1;
	}

	/**
	 * This function is called if the source elementary flow is not stored in
	 * the database. The information of the source flow are transformed to the
	 * given exchange.
	 * 
	 */
	private double unknownElemFlow(ILCDFlow flow, ES1Exchange eExchange) {

		if (flow.description != null) {

			eExchange.name = TypeCheck.checkLength(
					LangString.getFirstValue(flow.description.getName(), "en"),
					80);
			eExchange.localName = eExchange.name;
			eExchange.CASNumber = formatCAS(flow.description.casNumber);
			eExchange.formula = flow.description.sumFormula;

			// flow classification
			// TODO elementary flow category
		}

		// convert the ILCD flow property to the EcoSpold 01 unit
		String unit = null;
		DataSetReference ref = flow.getReferenceProperty();
		if (ref != null) {
			unit = ILCDPropertyRec.unit(ref.getRefObjectId());
		}

		if (unit == null) {
			logger.severe("Cannot load flow property for " + ref);
			return 0;
		} else {
			eExchange.unit = unit;
		}

		return 1;

	}

	/**
	  * 
	  */
	private double productFlow(ILCDFlow flow, ES1Exchange eExchange) {

		if (flow.description != null) {

			eExchange.name = TypeCheck.checkLength(
					LangString.getFirstValue(flow.description.getName(), "en"),
					80);
			eExchange.localName = eExchange.name;
			eExchange.CASNumber = formatCAS(flow.description.casNumber);
			eExchange.formula = flow.description.sumFormula;
			eExchange.infrastructureProcess = false; // default
			eExchange.location = "GLO"; // default

			// flow classification
			if (!flow.description.getClassifications().isEmpty()) {
				ILCDClassification clasf = flow.description
						.getClassifications().get(0);
				for (ILCDClass clazz : clasf.getClasses()) {
					if (clazz.level == 0) {
						eExchange.category = clazz.name;
						eExchange.localCategory = eExchange.category;
					}
					if (clazz.level == 1) {
						eExchange.subCategory = clazz.name;
						eExchange.localSubCategory = eExchange.subCategory;
					}
				}
			}
		}

		// convert the ILCD flow property to the EcoSpold 01 unit
		String unit = null;
		DataSetReference ref = flow.getReferenceProperty();
		if (ref != null) {
			unit = ILCDPropertyRec.unit(ref.getRefObjectId());
		}

		if (unit == null) {
			logger.severe("Cannot load flow property for " + ref);
			return 0;
		} else {
			eExchange.unit = unit;
		}

		return 1;

	}

	/**
	 * This function converts the numeric values from an exchange in the source
	 * format to an exchange of the target format. The conversion factor which
	 * is determined in the flow dispatch (see function
	 * <code>flowDispatch(...):double</code> is applied.
	 */
	private void numericValues(ILCDExchange iExchange, ES1Exchange eExchange,
			double factor) {

		if (iExchange.uncertaintyDistribution == null
				|| iExchange.uncertaintyDistribution.equals("undefined")) {

			// no uncertainty
			eExchange.uncertaintyType = 0;

			if (iExchange.resultingAmount != null) {
				eExchange.meanValue = factor * iExchange.resultingAmount;
			}

		} else if (iExchange.uncertaintyDistribution.equals("log-normal")) {

			// log-normal
			eExchange.uncertaintyType = 1;

			if (iExchange.meanAmount != null) {
				eExchange.meanValue = factor * iExchange.meanAmount;
			}
			if (iExchange.relStdDeviation95In != null) {
				// no factor here!?
				eExchange.standardDeviation95 = iExchange.relStdDeviation95In;
			}

		} else if (iExchange.uncertaintyDistribution.equals("normal")) {

			// normal
			eExchange.uncertaintyType = 2;

			if (iExchange.meanAmount != null) {
				eExchange.meanValue = factor * iExchange.meanAmount;
			}
			if (iExchange.relStdDeviation95In != null) {
				eExchange.standardDeviation95 = factor
						* iExchange.relStdDeviation95In;
			}

		} else if (iExchange.uncertaintyDistribution.equals("triangular")) {

			// triangular
			eExchange.uncertaintyType = 3;

			if (iExchange.meanAmount != null) {
				eExchange.meanValue = factor * iExchange.meanAmount;
			}
			if (iExchange.minimumAmount != null) {
				eExchange.minValue = factor * iExchange.minimumAmount;
			}
			if (iExchange.maximumAmount != null) {
				eExchange.maxValue = factor * iExchange.maximumAmount;
			}

		} else if (iExchange.uncertaintyDistribution.equals("uniform")) {

			// uniform
			eExchange.uncertaintyType = 4;

			if (iExchange.meanAmount != null) {
				eExchange.meanValue = factor * iExchange.meanAmount;
			}
			if (iExchange.minimumAmount != null) {
				eExchange.minValue = factor * iExchange.minimumAmount;
			}
			if (iExchange.maximumAmount != null) {
				eExchange.maxValue = factor * iExchange.maximumAmount;
			}
		}
	}

	private String formatCAS(String ilcdCAS) {
		String cas = ilcdCAS;
		if (cas != null) {
			while (cas.length() < 11) {
				cas = "0" + cas;
			}
		}
		return cas;
	}
}
