package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
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
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2Review;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts EcoSpold 02 files back to EcoSpold 01 files.
 * 
 * @author Michael Srocka
 * 
 */
class ES2ToES1Conversion extends AbstractConversionImpl {

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
	 * The folder where the created EcoSpold 01 data sets are stored.
	 */
	private ES1Folder es1Folder = null;

	/**
	 * The created persons in the conversion process.
	 */
	private HashMap<String, ES1Person> createdPersons = new HashMap<String, ES1Person>();

	/**
	 * The created sources in the conversion process.
	 */
	private HashMap<String, ES1Source> createdSources = new HashMap<String, ES1Source>();

	public ES2ToES1Conversion() {

	}

	public ES2ToES1Conversion(boolean simaProConform) {
		this.simaProConform = simaProConform;
		if (simaProConform) {
			simaProConverter = new ES1SimaProValidConversion();
		}
	}

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
					ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class,
							is);
					is.close();
					if (ecoSpold != null) {
						for (ES2Dataset dataset : ecoSpold.getDatasets()) {
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

		// finish the conversion and clear cache
		flush();
		createdPersons.clear();
		createdSources.clear();

	}

	/**
	 * Converts the given EcoSpold 02 data set to an EcoSpold 01 data set.
	 */
	private DataSetReference convert(ES2Dataset dataset) {

		createdPersons.clear();
		createdSources.clear();

		ES1EcoSpold ecoSpold = new ES1EcoSpold();
		ES1Dataset e1Ds = new ES1Dataset();
		ecoSpold.getDatasets().add(e1Ds);

		// map the data
		dataSetAtts(dataset, e1Ds);
		referenceFunction(dataset, e1Ds);
		geography(dataset, e1Ds);
		technology(dataset, e1Ds);
		timePeriod(dataset, e1Ds);
		dataSetInfo(dataset, e1Ds);
		representativeness(dataset, e1Ds);
		// validation(dataset, e1Ds);
		dataEntry(dataset, e1Ds);
		publication(dataset, e1Ds);
		exchanges(dataset, e1Ds);

		// init. values for data set reference
		// uuid
		String uuid = null;
		if (dataset.description != null && dataset.description.id != null)
			uuid = dataset.description.id;
		else
			uuid = UUID.randomUUID().toString();

		// name
		String name = null;
		if (e1Ds.referenceFunction != null
				&& e1Ds.referenceFunction.name != null)
			name = e1Ds.referenceFunction.name;
		else
			name = "no name";

		if (simaProConform) {
			for (ES1Dataset es1DataSet : ecoSpold.getDatasets()) {
				simaProConverter.validate(es1DataSet);
				simaProConverter.processForMappingFile(es1DataSet);
			}
		}

		DataSetReference ref = null;
		File outFile = new File(es1Folder.getProcessFolder(), uuid + ".xml");

		if (outFile.exists()) {
			logger.severe("The file " + outFile.getAbsolutePath()
					+ " already exists.");
		} else {

			// write the file
			XmlOutputter outputter = new XmlOutputter();
			outputter.output(ecoSpold, TemplateType.EcoSpold01, outFile, true);

			// create the reference
			ref = new DataSetReference();
			ref.setName(name);
			ref.setUri(outFile.toURI().toString());
			ref.setRefObjectId(uuid);
			ref.setType(ILCDDataSetType.Process.toString());
			ref.setVersion("01.00.000");
		}

		return ref;
	}

	/**
	 * Creates the top data set attributes.
	 */
	private void dataSetAtts(ES2Dataset e2Ds, ES1Dataset e1Ds) {
		e1Ds.generator = "openLCA - data converter";
		e1Ds.number = 1;
		e1Ds.timestamp = Time.now();
	}

	/**
	 * Creates the reference function with the process related information.
	 */
	private void referenceFunction(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		ES1ReferenceFunction refFunction = new ES1ReferenceFunction();
		e1Ds.referenceFunction = refFunction;

		if (e2Ds.description != null) {

			refFunction.datasetRelatesToProduct = true; // default
			refFunction.infrastructureProcess = false; // default

			// included activities
			String val = LangString.getFirstValue(e2Ds.description
					.getIncludedActivitiesStart());
			String end = LangString.getFirstValue(e2Ds.description
					.getIncludedActivitiesEnd());
			;
			if (end != null) {
				if (val != null) {
					val = val + " | " + end;
				} else {
					val = end;
				}
			}
			refFunction.includedProcesses = val;

			//

		}
	}

	/**
	 * Fills the reference function with the product related information of the
	 * reference flow.
	 */
	private void referenceFunction(ES2IntermediateExchange e2Exchange,
			ES1Dataset e1Ds) {

		ES1ReferenceFunction refFunction = e1Ds.referenceFunction;
		if (refFunction == null)
			return;

		// categories
		if (e2Exchange.getProductClassifications().isEmpty()) {
			// create default values
			refFunction.category = "not available";
			refFunction.subCategory = "not available";
			refFunction.localCategory = "not available";
			refFunction.localSubCategory = "not available";
		} else {

			ES2ClassificationRef classRef = e2Exchange
					.getProductClassifications().get(0);

			// classification system is mapped to the category
			String system = LangString.getFirstValue(classRef
					.getClassificationSystem());
			system = system != null ? system : "not available";
			refFunction.category = system;
			refFunction.localCategory = system;

			// classification value is mapped to the sub-category
			String value = LangString.getFirstValue(classRef
					.getClassificationValue());
			value = value != null ? value : "not available";
			refFunction.subCategory = value;
			refFunction.localSubCategory = value;
		}

		// name
		String name = TypeCheck.checkLength(
				LangString.getFirstValue(e2Exchange.getName()), 80);
		refFunction.name = name;
		refFunction.localName = name;

		// amount
		refFunction.amount = e2Exchange.amount;

		// unit
		refFunction.unit = LangString.getFirstValue(e2Exchange.getUnitNames());

		// general comment
		refFunction.generalComment = LangString.getFirstValue(e2Exchange
				.getComment());

	}

	/**
	 * Creates the geography element.
	 */
	private void geography(ES2Dataset e2Ds, ES1Dataset e1Ds) {
		if (e2Ds.geography != null) {
			ES1Geography geography = new ES1Geography();
			e1Ds.geography = geography;
			geography.location = LangString.getFirstValue(e2Ds.geography
					.getShortNames());
			if (e2Ds.geography.comment != null)
				geography.text = e2Ds.geography.comment.getFirstText();
		}
	}

	/**
	 * Creates the technology element.
	 */
	private void technology(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (e2Ds.technology != null) {

			ES1Technology technology = new ES1Technology();
			e1Ds.technology = technology;

			if (e2Ds.technology.comment != null) {
				e1Ds.technology.text = e2Ds.technology.comment.getFirstText();
			}
		}
	}

	/**
	 * Create the time period element.
	 */
	private void timePeriod(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (e2Ds.timePeriod != null) {

			ES1TimePeriod time = new ES1TimePeriod();
			e1Ds.timePeriod = time;

			// start and end
			time.startDate = e2Ds.timePeriod.startDate;
			time.endDate = e2Ds.timePeriod.endDate;

			// comment
			if (e2Ds.timePeriod.comment != null)
				time.text = e2Ds.timePeriod.comment.getFirstText();

			// valid for period
			time.dataValidForEntirePeriod = e2Ds.timePeriod.dataValidForEntirePeriod;
			if (time.dataValidForEntirePeriod == null) {
				time.dataValidForEntirePeriod = true; // default
			}
		}
	}

	/**
	 * Creates the data set information element.
	 */
	private void dataSetInfo(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		ES1DataSetInformation dInfo = new ES1DataSetInformation();
		e1Ds.dataSetInformation = dInfo;

		// default values
		dInfo.energyValues = 0;
		dInfo.impactAssessmentResult = false;
		dInfo.languageCode = "en";
		dInfo.localLanguageCode = "de";
		dInfo.timestamp = Time.now();
		dInfo.version = 1.00;
		dInfo.internalVersion = 1.00;

		// data set type
		if (e2Ds.description != null) {
			dInfo.type = e2Ds.description.type;
		}
	}

	/**
	 * Creates the representativeness element.
	 */
	private void representativeness(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (e2Ds.representativeness != null) {

			ES1Representativeness repri = new ES1Representativeness();
			e1Ds.representativeness = repri;

			// sampling procedure
			repri.samplingProcedure = LangString
					.getFirstValue(e2Ds.representativeness
							.getSamplingProcedure());

			// extrapolations
			repri.extrapolations = LangString
					.getFirstValue(e2Ds.representativeness.getExtrapolations());

			// representativeness
			repri.percent = e2Ds.representativeness.percent;
		}
	}

	/**
	 * Creates the validation element.
	 */
	private void validation(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (!e2Ds.getReviews().isEmpty()) {

			ES2Review review = e2Ds.getReviews().get(0);
			ES1Validation validation = new ES1Validation();
			e1Ds.validation = validation;

			// the reviewer
			ES1Person person = new ES1Person();
			e1Ds.getPersons().add(person);
			int pNo = e1Ds.getPersons().size();
			person.number = pNo;
			person.name = review.reviewerName;
			person.email = review.reviewerEmail;

			validation.proofReadingValidator = pNo;
			if (review.details != null)
				validation.proofReadingDetails = review.details.getFirstText();

			validation.otherDetails = LangString.getFirstValue(review
					.getOtherDetails());

		}
	}

	/**
	 * Creates the data entry by element.
	 */
	private void dataEntry(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (e2Ds.dataEntryBy != null) {

			ES1DataEntryBy entry = new ES1DataEntryBy();
			e1Ds.dataEntryBy = entry;
			ES1Person person = getPerson(e2Ds.dataEntryBy.personEmail,
					e2Ds.dataEntryBy.personName, e1Ds);
			if (person != null) {
				// point from data entry to the (created) person
				entry.person = person.number;

			}
			// entry.qualityNetwork = 1; //for ecoinvent data sets
		}
	}

	/**
	 * Returns the cached source or creates a new one for the given attributes.
	 */
	private ES1Source getSource(String author, String year, ES1Dataset e1Ds) {
		String key = "" + author + "-" + year;
		ES1Source source = createdSources.get(key);
		if (source == null) {

			// create a new source
			source = new ES1Source();
			e1Ds.getSources().add(source);
			createdSources.put(key, source);

			// number
			int sNo = createdSources.size();
			source.number = sNo;

			// author
			source.firstAuthor = author;
			if (source.firstAuthor == null) {
				source.firstAuthor = "!NO AUTHOR!";
				logger.severe("No author in source.");
			}

			// year
			if (year != null) {
				try {
					source.year = Integer.parseInt(year);
				} catch (Exception e) {
					// o.k.
				}
			}
			if (source.year == null) {
				source.year = 9999; // default
			}

			// default attributes to get the data set valid
			source.placeOfPublications = "-";
			source.title = "see master data";

		}
		return source;
	}

	/**
	 * Returns the cached person or creates a new one for the given attributes.
	 */
	private ES1Person getPerson(String email, String name, ES1Dataset e1Ds) {

		String key = "" + email + "-" + name;

		ES1Person person = createdPersons.get(key);
		if (person == null) {

			// create a new person
			person = new ES1Person();
			e1Ds.getPersons().add(person);
			createdPersons.put(key, person);

			int pNo = createdPersons.size();
			person.number = pNo;
			person.email = email;
			person.name = name;

			// default values to get a valid person element
			person.address = "see master data";
			person.companyCode = "-";
			person.countryCode = "CH";
			person.telephone = "see master data";

		}

		return person;
	}

	/**
	 * Creates the data generator and publication element.
	 */
	private void publication(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		if (e2Ds.publication != null) {

			ES1DataGeneratorAndPublication publication = new ES1DataGeneratorAndPublication();
			e1Ds.dataGeneratorAndPublication = publication;

			// the person
			ES1Person person = getPerson(e2Ds.publication.personEmail,
					e2Ds.publication.personName, e1Ds);
			if (person != null) {
				publication.person = person.number;
			}

			// the source
			ES1Source source = getSource(
					e2Ds.publication.publishedSourceFirstAuthor,
					e2Ds.publication.publishedSourceYear, e1Ds);
			if (source != null) {
				publication.referenceToPublishedSource = source.number;
			}

			// copyright
			publication.copyright = e2Ds.publication.isCopyrightProtected;
			if (publication.copyright == null) {
				publication.copyright = false; // default
			}

			// page numbers
			publication.pageNumbers = e2Ds.publication.pageNumbers;

			// access restrictions
			publication.accessRestrictedTo = e2Ds.publication.accessRestrictedTo;

			// company
			publication.companyCode = e2Ds.publication.companyCode;
		}
	}

	/**
	 * Creates the exchange elements.
	 */
	private void exchanges(ES2Dataset e2Ds, ES1Dataset e1Ds) {

		// the intermediate exchanges
		for (ES2IntermediateExchange e2Exchange : e2Ds
				.getIntermediateExchanges()) {

			ES1Exchange e1Exchange = new ES1Exchange();
			e1Ds.getExchanges().add(e1Exchange);
			e1Exchange.number = e1Ds.getExchanges().size();

			e1Exchange.infrastructureProcess = false; // default
			e1Exchange.location = "GLO"; // default

			// name
			e1Exchange.name = TypeCheck.checkLength(
					LangString.getFirstValue(e2Exchange.getName()), 80);

			// unit
			e1Exchange.unit = LangString.getFirstValue(e2Exchange
					.getUnitNames());

			// comment
			e1Exchange.generalComment = LangString.getFirstValue(e2Exchange
					.getComment());

			// amount
			e1Exchange.meanValue = e2Exchange.amount;

			// CAS number
			e1Exchange.CASNumber = e2Exchange.casNumber;

			// classification
			if (!e2Exchange.getProductClassifications().isEmpty()) {
				ES2ClassificationRef classRef = e2Exchange
						.getProductClassifications().get(0);
				e1Exchange.category = LangString.getFirstValue(classRef
						.getClassificationSystem());
				e1Exchange.subCategory = LangString.getFirstValue(classRef
						.getClassificationValue());
			}

			// TODO: uncertainty types
			// Temporarily for SimaPro // by SGR
			e1Exchange.uncertaintyType = 0;

			// input / output group
			e1Exchange.inputGroup = e2Exchange.inputGroup;
			e1Exchange.outputGroup = e2Exchange.outputGroup;

			// map the reference function attributes
			if (e2Exchange.outputGroup != null && e2Exchange.outputGroup == 0) {
				referenceFunction(e2Exchange, e1Ds);
			}

			// location of the reference exchange
			if (e1Ds.geography != null && e1Ds.geography.location != null) {
				e1Exchange.location = e1Ds.geography.location;
			}

		}

		// the elementary exchanges
		for (ES2ElementaryExchange e2Exchange : e2Ds.getElementaryExchanges()) {

			ES1Exchange e1Exchange = new ES1Exchange();
			e1Ds.getExchanges().add(e1Exchange);
			e1Exchange.number = e1Ds.getExchanges().size();

			// name
			e1Exchange.name = TypeCheck.checkLength(
					LangString.getFirstValue(e2Exchange.getName()), 80);

			// unit
			e1Exchange.unit = LangString.getFirstValue(e2Exchange
					.getUnitNames());

			// general comment
			e1Exchange.generalComment = LangString.getFirstValue(e2Exchange
					.getComment());

			// amount
			e1Exchange.meanValue = e2Exchange.amount;

			// CAS Number
			e1Exchange.CASNumber = e2Exchange.casNumber;

			// compartment
			e1Exchange.category = LangString.getFirstValue(e2Exchange
					.getCompartment());

			// sub-compartment
			e1Exchange.subCategory = LangString.getFirstValue(e2Exchange
					.getSubCompartment());

			// TODO: uncertainty types

			// input / output group
			e1Exchange.inputGroup = e2Exchange.inputGroup;
			e1Exchange.outputGroup = e2Exchange.outputGroup;

		}

	}

}
