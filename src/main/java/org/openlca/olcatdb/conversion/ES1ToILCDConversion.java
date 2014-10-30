package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.database.CompartmentMap;
import org.openlca.olcatdb.database.ES1CompartmentRec;
import org.openlca.olcatdb.database.ES1ElemFlowRec;
import org.openlca.olcatdb.database.ElemFlowMap;
import org.openlca.olcatdb.database.ILCDCompartmentRec;
import org.openlca.olcatdb.database.ILCDElemFlowRec;
import org.openlca.olcatdb.database.UnitMap;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.SourceType;
import org.openlca.olcatdb.datatypes.Time;
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
import org.openlca.olcatdb.ilcd.ILCDClass;
import org.openlca.olcatdb.ilcd.ILCDClassification;
import org.openlca.olcatdb.ilcd.ILCDContact;
import org.openlca.olcatdb.ilcd.ILCDContactDescription;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDEntry;
import org.openlca.olcatdb.ilcd.ILCDExchange;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDGeography;
import org.openlca.olcatdb.ilcd.ILCDProcess;
import org.openlca.olcatdb.ilcd.ILCDProcessDescription;
import org.openlca.olcatdb.ilcd.ILCDProcessEntry;
import org.openlca.olcatdb.ilcd.ILCDProcessMethod;
import org.openlca.olcatdb.ilcd.ILCDProcessPublication;
import org.openlca.olcatdb.ilcd.ILCDProcessTechnology;
import org.openlca.olcatdb.ilcd.ILCDPublication;
import org.openlca.olcatdb.ilcd.ILCDQuantitativeReference;
import org.openlca.olcatdb.ilcd.ILCDRepresentativeness;
import org.openlca.olcatdb.ilcd.ILCDReview;
import org.openlca.olcatdb.ilcd.ILCDReviewScope;
import org.openlca.olcatdb.ilcd.ILCDSource;
import org.openlca.olcatdb.ilcd.ILCDSourceDescription;
import org.openlca.olcatdb.ilcd.ILCDTime;
import org.openlca.olcatdb.ilcd.resources.ILCDFolder;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts a list of EcoSpold 01 process files to ILCD processes.
 */
class ES1ToILCDConversion extends AbstractConversionImpl {

	private ILCDExchangeAccumulator accumulator = new ILCDExchangeAccumulator();
	private Map<String, ILCDExchange> exchangesByFlow = new HashMap<String, ILCDExchange>();

	// the language code of the current data set in conversion
	private String langCode = "en";

	// the local language code of the current converted data set in conversion
	private String localLangCode = "de";

	// references to the sources of the current data set
	private HashMap<Integer, DataSetReference> sourceRefs = new HashMap<Integer, DataSetReference>();

	// references to the contacts of the current data set
	private HashMap<Integer, DataSetReference> contactRefs = new HashMap<Integer, DataSetReference>();

	private ILCDFolder ilcdFolder;

	@Override
	protected void createFolder(File targetDir) {
		super.createFolder(targetDir);
		ilcdFolder = new ILCDFolder(targetDir);
		ilcdFolder.createContent();
		logFile(ilcdFolder);
	}

	@Override
	public ResourceFolder getResult() {
		return ilcdFolder;
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

	/**
	 * Converts the given EcoSpold data set into the ILCD format.
	 * 
	 * @param dataset
	 *            the EcoSpold data set
	 * @param targetDir
	 *            the ILCD directory
	 */
	private DataSetReference convert(ES1Dataset dataset) {

		// extract the language codes
		pullLanguageCodes(dataset);

		// create the contact / source files
		sources(dataset);
		contacts(dataset);

		ILCDProcess process = new ILCDProcess();

		dataSetInfo(dataset, process);

		quantReferefence(dataset, process);

		time(dataset, process);

		geography(dataset, process);

		technology(dataset, process);

		method(dataset, process);

		representativeness(dataset, process);

		validation(dataset, process);

		// TODO: compliance section

		adminInfo(dataset, process);

		publication(dataset, process);

		exchanges(dataset, process);

		// clear the source / contact cache
		sourceRefs.clear();
		contactRefs.clear();

		// create the data set reference for the created data set
		DataSetReference reference = new DataSetReference();
		reference.setRefObjectId(process.description.uuid);
		reference.setType(ILCDDataSetType.Process.toString());
		reference.setVersion("01.00.000");

		// look-up for the file
		File f = ilcdFolder.file(reference);
		if (f.exists()) {
			logger.severe("The file " + f.getAbsoluteFile()
					+ " already exists.");
			return null;
		} else {

			// create the file
			XmlOutputter outputter = new XmlOutputter();
			outputter.output(process, TemplateType.ILCDProcess, f, true);
			reference.setUri(f.toURI().toString());
			reference.setName(process.description.getName().get(0).getValue());
			return reference;
		}
	}

	/**
	 * Create the contact data sets.
	 */
	private void contacts(ES1Dataset dataset) {

		for (ES1Person ePerson : dataset.getPersons()) {

			// no person without a number !
			if (ePerson.number == null) {
				continue;
			}

			// calculate a pseudo-UUID
			String[] uuidArgs = new String[5];
			uuidArgs[0] = ePerson.address;
			uuidArgs[1] = ePerson.name;
			uuidArgs[2] = ePerson.companyCode;
			uuidArgs[3] = ePerson.countryCode;
			uuidArgs[4] = Integer.toString(ePerson.number);
			String pUUID = KeyGen.getPseodoUUID(uuidArgs);

			String name = ePerson.name != null ? ePerson.name : "no name";

			// create and cache the data set reference
			DataSetReference ref = new DataSetReference();
			ref.setRefObjectId(pUUID);
			ref.setName(name);
			ref.setType(ILCDDataSetType.Contact.toString());
			ref.setUri("../contacts/" + pUUID + ".xml");
			ref.setVersion("01.00.000");
			ref.getDescription().add(new LangString(name));
			contactRefs.put(ePerson.number, ref);

			// create the source if it not yet exists
			if (!ilcdFolder.exists(ref)) {
				contact(ePerson, ref);
			}

		}

	}

	/**
	 * Creates an ILCD contact file for the given person.
	 */
	private void contact(ES1Person ePerson, DataSetReference ref) {

		ILCDContact contact = new ILCDContact();
		ILCDContactDescription description = new ILCDContactDescription();
		contact.description = description;
		description.uuid = ref.getRefObjectId();

		// name & short name
		description.getShortName().add(new LangString(langCode, ePerson.name));
		String name = ePerson.name + " (" + ePerson.companyCode + ")";
		description.getName().add(new LangString(langCode, name));

		// classification
		ILCDClassification classification = new ILCDClassification();
		ILCDClass clazz = new ILCDClass();
		clazz.id = ("4");
		clazz.level = (0);
		clazz.name = ("Persons");
		classification.getClasses().add(clazz);
		description.getClassifications().add(classification);

		// address, telephone, ...
		description.getContactAddress().add(
				new LangString(langCode, ePerson.address + ", "
						+ ePerson.countryCode));
		description.email = ePerson.email;
		description.telefax = ePerson.telefax;
		description.telephone = ePerson.telephone;

		// data entry
		ILCDEntry entry = new ILCDEntry();
		contact.entry = entry;
		entry.timestamp = Time.now();
		entry.getDataSetFormats().add(
				ReferenceFactory.ILCD_FORMAT.createReference());
		entry.getDataSetFormats().add(
				ReferenceFactory.ECOSPOLD_FORMAT.createReference());

		// publication and ownership
		ILCDPublication publication = new ILCDPublication();
		contact.publication = publication;
		publication.dataSetVersion = ref.getVersion();
		publication.permanentDataSetURI = ref.getUri();

		// save the conversion result
		File outFile = ilcdFolder.file(ref);
		XmlOutputter outputter = new XmlOutputter();
		outputter.output(contact, TemplateType.ILCDContact, outFile, true);

		DataSetReference cRef = ref.copy();
		cRef.setUri(outFile.toURI().toString());
		createdFiles.add(cRef);

	}

	/**
	 * Create source data sets.
	 */
	private void sources(ES1Dataset dataset) {

		for (ES1Source eSource : dataset.getSources()) {

			// no source without a number !
			if (eSource.number == null) {
				continue;
			}

			// calculate a pseudo-UUID
			String[] uuidArgs = new String[5];
			uuidArgs[0] = eSource.title;
			uuidArgs[1] = eSource.firstAuthor;
			uuidArgs[2] = eSource.additionalAuthors;
			uuidArgs[3] = eSource.placeOfPublications;
			uuidArgs[4] = Integer.toString(eSource.number);
			String pUUID = KeyGen.getPseodoUUID(uuidArgs);

			// create a short name of the source
			String shortName = "";
			if (eSource.firstAuthor != null) {
				shortName += eSource.firstAuthor;
			}
			if (eSource.additionalAuthors != null) {
				shortName += " et. al.";
			}
			if (eSource.year != null) {
				shortName += (" (" + eSource.year + ")");
			}

			// create and cache the data set reference
			DataSetReference ref = new DataSetReference();
			ref.setRefObjectId(pUUID);
			ref.setName(shortName);
			ref.setType(ILCDDataSetType.Source.toString());
			ref.setUri("../sources/" + pUUID + ".xml");
			ref.setVersion("01.00.000");
			ref.getDescription().add(new LangString(shortName));
			sourceRefs.put(eSource.number, ref);

			// create the source if it not yet exists
			if (!ilcdFolder.exists(ref)) {
				source(eSource, ref);
			}
		}
	}

	/**
	 * Creates an ILCD source file
	 */
	private void source(ES1Source eSource, DataSetReference ref) {

		ILCDSource iSource = new ILCDSource();
		ILCDSourceDescription iDescription = new ILCDSourceDescription();
		iSource.description = iDescription;

		// map the source type, the EcoSpold codes are:
		// 0=Undefined (default) 1=Article
		// 2=Chapters in anthology 3=Separate publication
		// 4=Measurement on site 5=Oral communication
		// 6=Personal written communication 7=Questionnaires
		if (eSource.sourceType != null) {
			String sourceType = null; // the default
			switch (eSource.sourceType) {
			case 0:
				sourceType = "Undefined";
				break;
			case 1:
				sourceType = "Article in periodical";
				break;
			case 2:
				sourceType = "Chapter in anthology";
				break;
			case 3:
				sourceType = "Monograph";
				break;
			case 4:
				sourceType = "Direct measurement";
				break;
			case 5:
				sourceType = "Oral communication";
				break;
			case 6:
				sourceType = "Personal written communication";
				break;
			case 7:
				sourceType = "Questionnaire";
				break;
			default:
				break;
			}
			iDescription.publicationType = sourceType;
		}

		// UUID
		iDescription.uuid = ref.getRefObjectId();

		// short name
		String shortName = "";
		if (eSource.firstAuthor != null) {
			shortName += eSource.firstAuthor;
		}
		if (eSource.additionalAuthors != null) {
			shortName += " et. al.";
		}
		if (eSource.year != null) {
			shortName += (" (" + eSource.year + ")");
		}
		iDescription.getShortName().add(new LangString(shortName));

		// category
		ILCDClassification classification = new ILCDClassification();
		ILCDClass clazz = new ILCDClass();
		clazz.id = ("5");
		clazz.level = (0);
		clazz.name = ("Publications and communications");
		classification.getClasses().add(clazz);
		iDescription.getClassifications().add(classification);

		// source citation
		String citation = eSource.firstAuthor == null ? ""
				: eSource.firstAuthor;
		if (eSource.additionalAuthors != null) {
			citation += ", " + eSource.additionalAuthors;
		}
		if (eSource.title != null) {
			citation += ": " + eSource.title + ". ";
		}
		if (eSource.placeOfPublications != null) {
			citation += eSource.placeOfPublications;
		}
		if (eSource.year != null) {
			citation += " " + eSource.year;
		}
		iDescription.sourceCitation = citation;

		// publication type
		if (eSource.sourceType != null) {
			String publType = SourceType.fromEcoSpoldCode(eSource.sourceType)
					.getILCDName();
			iDescription.publicationType = publType;
		}

		// data entry
		ILCDEntry entry = new ILCDEntry();
		iSource.entry = entry;
		entry.timestamp = Time.now();
		entry.getDataSetFormats().add(
				ReferenceFactory.ILCD_FORMAT.createReference());
		entry.getDataSetFormats().add(
				ReferenceFactory.ECOSPOLD_FORMAT.createReference());

		// publication and ownership
		ILCDPublication publication = new ILCDPublication();
		iSource.publication = publication;
		publication.dataSetVersion = ref.getVersion();
		publication.permanentDataSetURI = ref.getUri();

		// save the conversion result
		File outFile = ilcdFolder.file(ref);
		XmlOutputter outputter = new XmlOutputter();
		outputter.output(iSource, TemplateType.ILCDSource, outFile, true);

		DataSetReference cRef = ref.copy();
		cRef.setUri(outFile.toURI().toString());
		createdFiles.add(cRef);
	}

	/**
	 * Creates the data set information element.
	 */
	private void dataSetInfo(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessDescription iDescription = new ILCDProcessDescription();
		iProcess.description = iDescription;

		// @ UUID -> a pseudo UUID from the data set arguments
		String[] args = new String[5];
		if (eDataset.referenceFunction != null) {
			String loc = "GLO";
			if (eDataset.geography != null
					&& eDataset.geography.location != null) {
				loc = eDataset.geography.location;
			}

			ES1ReferenceFunction refFun = eDataset.referenceFunction;
			args[0] = loc + refFun.name;
			args[1] = refFun.category;
			args[2] = refFun.subCategory;
			args[3] = refFun.unit;
			args[4] = refFun.infrastructureProcess == null ? "false"
					: refFun.infrastructureProcess.toString();
		}
		iDescription.uuid = KeyGen.getPseodoUUID(args);

		// @ base names
		if (eDataset.referenceFunction != null) {
			ES1ReferenceFunction refFun = eDataset.referenceFunction;
			if (refFun.name != null) {
				iDescription.getName().add(
						new LangString(langCode, refFun.name));
			}
			if (refFun.localName != null) {
				iDescription.getName().add(
						new LangString(localLangCode, refFun.localName));
			}
		}

		// @ synonyms
		if (eDataset.referenceFunction != null) {
			ES1ReferenceFunction refFun = eDataset.referenceFunction;
			if (!refFun.getSynonyms().isEmpty()) {
				String synonyms = "";
				Iterator<String> it = refFun.getSynonyms().iterator();
				while (it.hasNext()) {
					String s = it.next();
					synonyms += s;
					if (it.hasNext()) {
						synonyms += "; ";
					}
				}
				iDescription.getSynonyms().add(
						new LangString(langCode, synonyms));
			}
		}

		// @ classification <- category / sub-category
		if (eDataset.referenceFunction != null
				&& eDataset.referenceFunction.category != null) {

			ES1ReferenceFunction refFun = eDataset.referenceFunction;

			ILCDClassification classification = new ILCDClassification();

			ILCDClass ilcdClass = new ILCDClass();
			ilcdClass.id = (UUID.randomUUID().toString());
			ilcdClass.level = (0);
			ilcdClass.name = refFun.category;
			classification.getClasses().add(ilcdClass);

			// the ILCD class for the EcoSpold sub-category
			if (refFun.subCategory != null) {
				ilcdClass = new ILCDClass();
				ilcdClass.id = UUID.randomUUID().toString();
				ilcdClass.level = 1;
				ilcdClass.name = refFun.subCategory;
				classification.getClasses().add(ilcdClass);
			}

			iDescription.getClassifications().add(classification);
		}

		// @ general comment
		if (eDataset.referenceFunction != null) {
			ES1ReferenceFunction refFun = eDataset.referenceFunction;
			if (refFun.generalComment != null) {
				iDescription.getComment().add(
						new LangString(langCode, refFun.generalComment));
			}
		}
	}

	/**
	 * Creates the element for the quantitative reference.
	 */
	private void quantReferefence(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDQuantitativeReference qRef = new ILCDQuantitativeReference();
		iProcess.quantitativeReference = qRef;

		// check if there is a reference exchange
		Integer refExchange = null;
		for (ES1Exchange eExchange : eDataset.getExchanges()) {
			if (eExchange.outputGroup != null && eExchange.outputGroup == 0) {
				refExchange = eExchange.number;
			}
		}

		if (refExchange != null) {
			qRef.type = "Reference flow(s)";
			qRef.getReferenceFlows().add(refExchange);
		} else {
			qRef.type = "Functional unit";
			String fu = "";
			if (eDataset.referenceFunction != null) {
				fu += eDataset.referenceFunction.name + ", ";
				fu += eDataset.referenceFunction.amount + " ";
				fu += eDataset.referenceFunction.unit;
			}
			qRef.getFunctionalUnits().add(new LangString(langCode, ""));
		}

	}

	/**
	 * Creates the time element
	 */
	private void time(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDTime iTime = new ILCDTime();
		iProcess.time = iTime;

		// @ reference year
		if (eDataset.timePeriod != null) {
			ES1TimePeriod eTime = eDataset.timePeriod;
			if (eTime.startYear != null) {
				iTime.referenceYear = eTime.startYear;
			} else {
				String timeString = null;
				if (eTime.startYearMonth != null) {
					timeString = eTime.startYearMonth;
				} else {
					timeString = eTime.startDate;
				}
				if (timeString != null) {
					try {
						XMLGregorianCalendar calendar = DatatypeFactory
								.newInstance().newXMLGregorianCalendar(
										timeString);
						iTime.referenceYear = calendar.getYear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// @ valid until
		if (eDataset.timePeriod != null) {
			ES1TimePeriod eTime = eDataset.timePeriod;
			if (eTime.endYear != null) {
				iTime.validUntil = eTime.endYear;
			} else {
				String timeString = null;
				if (eTime.endYearMonth != null) {
					timeString = eTime.endYearMonth;
				} else {
					timeString = eTime.endDate;
				}
				if (timeString != null) {
					try {
						XMLGregorianCalendar calendar = DatatypeFactory
								.newInstance().newXMLGregorianCalendar(
										timeString);
						iTime.validUntil = calendar.getYear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// @ description
		if (eDataset.timePeriod != null && eDataset.timePeriod.text != null) {
			ES1TimePeriod eTime = eDataset.timePeriod;
			iTime.getDescription().add(new LangString(langCode, eTime.text));
		}

	}

	/**
	 * Creates the geography element.
	 */
	private void geography(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDGeography iGeography = new ILCDGeography();
		iProcess.geography = iGeography;

		if (eDataset.geography != null) {
			ES1Geography eGeography = eDataset.geography;
			if (eGeography.location != null) {
				iGeography.location = eGeography.location;
			}

			if (eGeography.text != null) {
				iGeography.getDescription().add(
						new LangString(langCode, eGeography.text));
			}
		}
	}

	/**
	 * Creates the technology element.
	 */
	private void technology(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessTechnology iTechnology = new ILCDProcessTechnology();
		iProcess.technology = iTechnology;

		// @ included processes
		if (eDataset.referenceFunction != null) {
			ES1ReferenceFunction refFun = eDataset.referenceFunction;
			if (refFun.includedProcesses != null) {
				iTechnology.getDescription().add(
						new LangString(langCode, refFun.includedProcesses));
			}
		}

		// @ technology description
		if (eDataset.technology != null) {
			ES1Technology eTech = eDataset.technology;
			if (eTech.text != null) {
				iTechnology.getApplicability().add(
						new LangString(langCode, eTech.text));
			}
		}
	}

	/**
	 * Creates the LCI method and allocation element.
	 */
	private void method(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessMethod iMethod = new ILCDProcessMethod();
		iProcess.method = iMethod;

		// default entries
		iMethod.lciMethodPrinciple = "Other";
		iMethod.getDeviationsFromLCIMethodApproaches().add(
				new LangString("None"));
		iMethod.getDeviationsFromLCIMethodPrinciple().add(
				new LangString("None"));
		iMethod.getDeviationsFromModellingConstants().add(
				new LangString("None"));
		iMethod.getLciMethodApproaches().add("Other");

		if (isFromEcoinvent(eDataset)) {
			iMethod.getModellingConstants().add(
					new LangString("See the ecoinvent reports at "
							+ "http://www.ecoinvent.org."));
			iMethod.getMethodDetails().add(
					ReferenceFactory.ECOINVENT_METHODIC.createReference());
		}

		// @ the data set type
		if (eDataset.dataSetInformation != null) {
			ES1DataSetInformation eInformation = eDataset.dataSetInformation;
			if (eInformation.type != null) {
				String ilcdType = null;
				switch (eInformation.type) {
				case 1:
					ilcdType = "Unit process, single operation";
					break;
				case 2:
					ilcdType = "LCI result";
					break;
				case 5:
					ilcdType = "Unit process, black box";
					break;
				default:
					break;
				}
				iMethod.dataSetType = ilcdType;
			}
		}
	}

	/**
	 * Creates the element data sources and representativeness.
	 */
	private void representativeness(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDRepresentativeness repri = new ILCDRepresentativeness();
		iProcess.representativeness = repri;

		// default entries
		LangString none = new LangString("None");
		repri.getDeviationsFromCutOffPrinciples().add(none);
		repri.getDeviationsFromDataSelectionPrinciples().add(none);
		repri.getDeviationsFromExtrapolationPrinciples().add(none);

		if (isFromEcoinvent(eDataset)) {
			LangString report = new LangString(
					"See the ecoinvent methodology report.");
			repri.getCutOffPrinciples().add(report);
			repri.getDataHandlingPrinciples().add(
					ReferenceFactory.ECOINVENT_METHODIC.createReference());
			repri.getDataSelectionPrinciples().add(report);
		}

		if (eDataset.representativeness != null) {
			ES1Representativeness eRepresentativeness = eDataset.representativeness;

			// percentage
			if (eRepresentativeness.percent != null) {
				repri.percent = eRepresentativeness.percent;
			}

			// annual production volume
			if (eRepresentativeness.productionVolume != null) {
				repri.getProductionVolume().add(
						new LangString(langCode,
								eRepresentativeness.productionVolume));
			}

			// sampling procedure
			if (eRepresentativeness.samplingProcedure != null) {
				repri.getSamplingProcedure().add(
						new LangString(langCode,
								eRepresentativeness.samplingProcedure));
			}

			// extrapolations
			if (eRepresentativeness.extrapolations != null) {
				repri.getExtrapolationPrinciples().add(
						new LangString(langCode,
								eRepresentativeness.extrapolations));
			}

			// uncertainty adjustments
			if (eRepresentativeness.uncertaintyAdjustments != null) {
				repri.getUncertaintyAdjustments().add(
						new LangString(langCode,
								eRepresentativeness.uncertaintyAdjustments));
			}
		}

		// data sources
		for (DataSetReference ref : sourceRefs.values()) {
			repri.getDataSources().add(ref);
		}

		// the use advice
		String use = "This data set was automatically converted "
				+ "with the openLCA data converter. Before you use "
				+ "this data set you should check the entries and the "
				+ "compliance with the ILCD conformity. You "
				+ "can edit this data set with the ILCD Editor "
				+ "freely available at "
				+ "http://lct.jrc.ec.europa.eu/assessment/tools.";
		repri.getUseAdviceForDataSet().add(new LangString(use));
	}

	/**
	 * Creates the validation element with review information.
	 */
	private void validation(ES1Dataset eDataset, ILCDProcess iProcess) {

		if (eDataset.validation != null) {

			ILCDReview review = new ILCDReview();
			iProcess.getReviews().add(review);

			if (isFromEcoinvent(eDataset)) {

				// when the data set is from ecoinvent it is assumed
				// that the data set passed the review procedure
				// described in the ecoinvent methodology report

				review.type = "Independent internal review";

				// documentation
				ILCDReviewScope scope = new ILCDReviewScope();
				scope.name = "Documentation";
				scope.getMethods().add("Expert judgement");
				review.getScopes().add(scope);

				// LCI method
				scope = new ILCDReviewScope();
				scope.name = "Life cycle inventory methods";
				scope.getMethods().add("Expert judgement");
				scope.getMethods().add("Sample tests on calculations");
				review.getScopes().add(scope);

				// set the ecoinvent center as reviewer
				review.getReviewers().add(
						ReferenceFactory.ECOINVENT_CENTER.createReference());
			}

			// @ review details
			if (eDataset.validation.proofReadingDetails != null) {
				review.getDetails()
						.add(new LangString(
								eDataset.validation.proofReadingDetails));
			}

			// @ other details
			if (eDataset.validation.otherDetails != null) {
				review.getOtherDetails().add(
						new LangString(eDataset.validation.otherDetails));
			}

			// @ reviewer
			Integer val = eDataset.validation.proofReadingValidator;
			if (val != null) {
				DataSetReference ref = contactRefs.get(val);
				if (ref != null) {
					review.getReviewers().add(ref);
				}
			}
		}
	}

	/**
	 * Creates the element with the administrative information.
	 */
	private void adminInfo(ES1Dataset eDataset, ILCDProcess iProcess) {

		// TODO: commissioner and goal

		// the data generator
		if (eDataset.dataGeneratorAndPublication != null) {
			Integer val = eDataset.dataGeneratorAndPublication.person;
			if (val != null) {
				DataSetReference ref = contactRefs.get(val);
				if (ref != null) {
					iProcess.getDataGenerators().add(ref);
				}
			}
		}

		ILCDProcessEntry iEntry = new ILCDProcessEntry();
		iProcess.entry = iEntry;

		// @ timestamp
		if (eDataset.dataSetInformation != null) {
			iEntry.timeStamp = eDataset.dataSetInformation.timestamp;
		}

		// @ data entry by
		if (eDataset.dataEntryBy != null) {
			Integer val = eDataset.dataEntryBy.person;
			if (val != null) {
				DataSetReference ref = contactRefs.get(val);
				if (ref != null) {
					iEntry.dataSetEntryReference = ref;
				}
			}
		}

		// @ data set formats
		iEntry.getDataFormatReferences().add(
				ReferenceFactory.ILCD_FORMAT.createReference());
		iEntry.getDataFormatReferences().add(
				ReferenceFactory.ECOSPOLD_FORMAT.createReference());

	}

	/**
	 * Creates the publication and ownership element.
	 */
	private void publication(ES1Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessPublication iPublication = new ILCDProcessPublication();
		iProcess.publication = iPublication;

		// @ version
		if (eDataset.dataSetInformation != null
				&& eDataset.dataSetInformation.version != null) {
			Double vers = eDataset.dataSetInformation.version;
			iPublication.dataSetVersion = new DecimalFormat("00.00",
					DecimalFormatSymbols.getInstance(Locale.US)).format(vers);
		}

		// @ permanent URI
		if (isFromEcoinvent(eDataset)) {
			iPublication.permanentDataSetURI = "http://ecoinvent.org";
		}

		// @ last revision
		iPublication.lastRevision = eDataset.timestamp;

		// @ ownership
		if (isFromEcoinvent(eDataset)) {
			iPublication.ownershipReference = ReferenceFactory.ECOINVENT_CENTER
					.createReference();
		}

		// @ publication
		if (eDataset.dataGeneratorAndPublication != null) {
			Integer val = eDataset.dataGeneratorAndPublication.referenceToPublishedSource;
			if (val != null) {
				DataSetReference ref = sourceRefs.get(val);
				if (ref != null) {
					iPublication.republicationReference = ref;
				}
			}
		}

		// @ copyright
		if (eDataset.dataGeneratorAndPublication != null) {
			iPublication.copyright = eDataset.dataGeneratorAndPublication.copyright;
		}

		// @ registration authority & number
		if (isFromEcoinvent(eDataset)) {
			iPublication.registrationAuthorityReference = ReferenceFactory.ECOINVENT_CENTER
					.createReference();
			if (eDataset.number != null) {
				iPublication.registrationNumber = Integer
						.toString(eDataset.number);
			}
		}

		// TODO: iPublication.workflowStatus

	}

	/**
	 * Creates the exchange elements.
	 */
	private void exchanges(ES1Dataset eDataset, ILCDProcess iProcess) {

		for (ES1Exchange eExchange : eDataset.getExchanges()) {

			ILCDExchange iExchange = new ILCDExchange();

			// standard mappings
			iExchange.id = eExchange.number; // ! see quantitative reference
			iExchange.dataDerivationType = "Unknown derivation"; // default
			iExchange.dataSourceType = "Mixed primary / secondary"; // default
			iExchange.direction = eExchange.inputGroup != null ? "Input"
					: "Output";
			iExchange.location = eExchange.location;
			if (eExchange.generalComment != null) {
				iExchange.getComment().add(
						new LangString(langCode, eExchange.generalComment));
			}

			// check if this is the reference flow

			// the conversion factor
			double factor = flowDispatch(eExchange, iExchange);

			// numeric values
			if (factor == 0) {
				logger.severe("No conversion factor: " + eExchange.toString());
			} else {
				numericValues(eExchange, iExchange, factor);
			}

			// accumulate exchanges with equal flow
			ILCDExchange existing = exchangesByFlow.get(iExchange.flowDataSet
					.getRefObjectId());
			if (existing == null) {
				iProcess.getExchanges().add(iExchange);
				exchangesByFlow.put(iExchange.flowDataSet.getRefObjectId(),
						iExchange);
			} else {
				accumulator.accumulate(existing, iExchange);
			}
		}

	}

	private double flowDispatch(ES1Exchange eExchange, ILCDExchange iExchange) {
		double factor = 0;
		if (!eExchange.isElementaryFlow()) {

			// product flow
			factor = productFlow(eExchange, iExchange);

		} else {

			// check if there is an elementary flow mapping
			ElemFlowMap.Entry flowEntry = ElemFlowMap.es1ToILCD(eExchange.name,
					eExchange.category, eExchange.subCategory, eExchange.unit);

			if (flowEntry != null) {

				// there is an assignment in the database
				factor = assignedElemFlow(flowEntry, iExchange);

			} else {

				// check if this is a known flow
				ES1ElemFlowRec flowRec = ES1ElemFlowRec.forAtts(eExchange.name,
						eExchange.category, eExchange.subCategory,
						eExchange.unit);

				if (flowRec != null) {

					// there is an unassigned flow in the database
					factor = unassignedElemFlow(flowRec, iExchange);

				} else {

					// an unknown elementary flow
					factor = unknownElemFlow(eExchange, iExchange);
				}
			}
		}
		return factor;
	}

	/**
	 * Maps the information of the stored ILCD elementary flow to the exchange.
	 * 
	 */
	private double assignedElemFlow(ElemFlowMap.Entry mapEntry,
			ILCDExchange iExchange) {

		ILCDElemFlowRec flowRec = ILCDElemFlowRec.forID(mapEntry.getFlowId());
		if (flowRec == null) {
			logger.severe("Cannot load ILCD flow for ID: "
					+ mapEntry.getFlowId());
			return 0;
		}

		// set the flow reference
		DataSetReference ref = flowRec.toReference();
		iExchange.flowDataSet = ref;

		// create the flow data set if required
		if (!ilcdFolder.exists(ref)) {
			ILCDHelper.writeFlow(ilcdFolder, ref, flowRec.toFlow(),
					createdFiles);
		}

		return mapEntry.getFactor();
	}

	/**
	 * Maps the information of the stored EcoSpold 1 elementary flow to the ILCD
	 * exchange.
	 */
	private double unassignedElemFlow(ES1ElemFlowRec flowRec,
			ILCDExchange iExchange) {

		// create a pseudo-key
		String[] keyArgs = new String[5];
		keyArgs[0] = flowRec.getName();
		keyArgs[1] = flowRec.getCas();
		keyArgs[2] = flowRec.getFormula();
		keyArgs[3] = Integer.toString(flowRec.getId());
		keyArgs[4] = Integer.toString(flowRec.getCompartmentId());
		String pUUID = KeyGen.getPseodoUUID(keyArgs);

		// create the flow reference
		DataSetReference ref = ILCDHelper.newFlowRef(pUUID, flowRec.getName());
		iExchange.flowDataSet = ref;

		// get the entry for the unit conversion
		UnitMap.Entry unitEntry = UnitMap.es1ToILCD(flowRec.getUnit());
		if (unitEntry == null) {
			logger.severe("Unknown unit " + flowRec.getUnit());
			return 0;
		}

		// create the flow if required
		if (!ilcdFolder.exists(ref)) {

			ILCDFlow flow = ILCDHelper.makeFlow(pUUID, flowRec.getName(),
					flowRec.getCas(), flowRec.getFormula(), "Elementary flow",
					unitEntry);

			// elementary flow category
			ILCDCompartmentRec rec = CompartmentMap.es1ToILCD(flowRec
					.getCompartmentId());
			if (rec == null) {
				logger.severe("No correspondig ILCD compartment for "
						+ flowRec.getCompartmentId());
			} else {
				flow.description.getElemFlowCategorizations().add(
						rec.toCategorization());
			}

			// create the flow
			ILCDHelper.writeFlow(ilcdFolder, ref, flow, createdFiles);
		}

		return unitEntry.getFactor();
	}

	private double unknownElemFlow(ES1Exchange eExchange, ILCDExchange iExchange) {

		// create the pseudo UUID
		String[] keyArgs = new String[5];
		keyArgs[0] = eExchange.name;
		keyArgs[1] = eExchange.category;
		keyArgs[2] = eExchange.subCategory;
		keyArgs[3] = eExchange.unit;
		keyArgs[4] = eExchange.CASNumber;
		String pUUID = KeyGen.getPseodoUUID(keyArgs);

		// create the flow reference
		DataSetReference ref = ILCDHelper.newFlowRef(pUUID, eExchange.name);
		iExchange.flowDataSet = ref;

		// get the entry for the unit conversion
		UnitMap.Entry unitEntry = UnitMap.es1ToILCD(eExchange.unit);
		if (unitEntry == null) {
			logger.severe("Unknown unit " + eExchange.unit);
			return 0;
		}

		// create the flow if required
		if (!ilcdFolder.exists(ref)) {

			ILCDFlow flow = ILCDHelper.makeFlow(pUUID, eExchange.name,
					eExchange.CASNumber, eExchange.formula, "Elementary flow",
					unitEntry);

			// elementary flow category
			ES1CompartmentRec eRec = ES1CompartmentRec.forNames(
					eExchange.category, eExchange.subCategory);
			ILCDCompartmentRec iRec = null;
			if (eRec != null)
				iRec = CompartmentMap.es1ToILCD(eRec.getId());

			if (iRec == null) {
				logger.severe("No correspondig ILCD compartment for "
						+ eExchange.category + "/" + eExchange.subCategory);
			} else {
				flow.description.getElemFlowCategorizations().add(
						iRec.toCategorization());
			}

			// create the flow
			ILCDHelper.writeFlow(ilcdFolder, ref, flow, createdFiles);

		}

		return unitEntry.getFactor();
	}

	private double productFlow(ES1Exchange eExchange, ILCDExchange iExchange) {

		// create the pseudo UUID
		String[] keyArgs = new String[5];
		keyArgs[0] = eExchange.name + eExchange.infrastructureProcess;
		keyArgs[1] = eExchange.category;
		keyArgs[2] = eExchange.subCategory;
		keyArgs[3] = eExchange.unit;
		keyArgs[4] = eExchange.location;
		String pUUID = KeyGen.getPseodoUUID(keyArgs);

		// create the flow reference
		DataSetReference ref = ILCDHelper.newFlowRef(pUUID, eExchange.name);
		iExchange.flowDataSet = ref;

		// get the entry for the unit conversion
		UnitMap.Entry unitEntry = UnitMap.es1ToILCD(eExchange.unit);
		if (unitEntry == null) {
			logger.severe("Unknown unit " + eExchange.unit);
			return 0;
		}

		// create the flow if required
		if (!ilcdFolder.exists(ref)) {

			ILCDFlow flow = ILCDHelper.makeFlow(pUUID, eExchange.name,
					eExchange.CASNumber, eExchange.formula, "Product flow",
					unitEntry);

			// product classification
			if (eExchange.category != null) {
				ILCDClassification classification = new ILCDClassification();
				flow.description.getClassifications().add(classification);

				ILCDClass clazz = new ILCDClass();
				classification.getClasses().add(clazz);
				clazz.id = UUID.randomUUID().toString(); // default
				clazz.level = 0;
				clazz.name = eExchange.category;

				if (eExchange.subCategory != null) {
					clazz = new ILCDClass();
					classification.getClasses().add(clazz);
					clazz.id = UUID.randomUUID().toString(); // default
					clazz.level = 1;
					clazz.name = eExchange.subCategory;
				}
			}

			// create the flow
			ILCDHelper.writeFlow(ilcdFolder, ref, flow, createdFiles);
		}

		return unitEntry.getFactor();
	}

	/**
	 * Conversion of the numeric values from the given EcoSpold 01 exchange to
	 * the ILCD exchange.
	 * 
	 * @param eExchange
	 *            the EcoSpold 01 exchange
	 * @param iExchange
	 *            the ILCD exchange
	 * @param factor
	 *            the conversion factor from the unit of the EcoSpold exchange
	 *            to the reference unit / flow property of the ILCD exchange.
	 */
	private void numericValues(ES1Exchange eExchange, ILCDExchange iExchange,
			double factor) {

		if (eExchange.uncertaintyType == null || eExchange.uncertaintyType == 0) {

			// no uncertainty distribution
			if (eExchange.meanValue != null) {
				iExchange.meanAmount = factor * eExchange.meanValue;
				iExchange.resultingAmount = iExchange.meanAmount;
			}

		} else if (eExchange.uncertaintyType == 1) {

			// log-normal
			iExchange.uncertaintyDistribution = "log-normal";

			if (eExchange.meanValue != null) {
				iExchange.meanAmount = factor * eExchange.meanValue;
				iExchange.resultingAmount = iExchange.meanAmount;
			}
			// no factor for stdev95 here
			if (eExchange.standardDeviation95 != null)
				iExchange.relStdDeviation95In = eExchange.standardDeviation95;

		} else if (eExchange.uncertaintyType == 2) {

			// normal
			iExchange.uncertaintyDistribution = "normal";

			if (eExchange.meanValue != null) {
				iExchange.meanAmount = factor * eExchange.meanValue;
				iExchange.resultingAmount = iExchange.meanAmount;
			}
			if (eExchange.standardDeviation95 != null)
				iExchange.relStdDeviation95In = factor
						* eExchange.standardDeviation95;

		} else if (eExchange.uncertaintyType == 3) {

			// triangular
			iExchange.uncertaintyDistribution = "triangular";

			// most-likely value?
			if (eExchange.meanValue != null) {
				iExchange.meanAmount = factor * eExchange.meanValue;
				iExchange.resultingAmount = iExchange.meanAmount;
			}
			if (eExchange.maxValue != null)
				iExchange.maximumAmount = factor * eExchange.maxValue;
			if (eExchange.minValue != null)
				iExchange.minimumAmount = factor * eExchange.minValue;

		} else if (eExchange.uncertaintyType == 4) {

			// uniform
			iExchange.uncertaintyDistribution = "uniform";

			if (eExchange.meanValue != null) {
				iExchange.meanAmount = factor * eExchange.meanValue;
				iExchange.resultingAmount = iExchange.meanAmount;
			}
			if (eExchange.maxValue != null)
				iExchange.maximumAmount = factor * eExchange.maxValue;
			if (eExchange.minValue != null)
				iExchange.minimumAmount = factor * eExchange.minValue;

		}
	}

	/**
	 * Get the language code and the local language code from the given EcoSpold
	 * data set. The default values are "en" for the language code and "de" for
	 * the local language code.
	 * 
	 * @param dataset
	 *            The EcoSpold data set
	 */
	private void pullLanguageCodes(ES1Dataset dataset) {

		// initialization of the default values
		langCode = "en";
		localLangCode = "de";

		ES1DataSetInformation dataSetInfo = dataset.dataSetInformation;
		if (dataSetInfo != null) {

			if (dataSetInfo.languageCode != null) {
				langCode = dataSetInfo.languageCode.toLowerCase();
			}

			if (dataSetInfo.localLanguageCode != null) {
				localLangCode = dataSetInfo.localLanguageCode.toLowerCase();
			}

		}

	}

	/**
	 * Returns true if the given data set comes from the ecoinvent center.
	 */
	private boolean isFromEcoinvent(ES1Dataset dataset) {
		return dataset != null && dataset.dataEntryBy != null
				&& dataset.dataEntryBy.qualityNetwork != null
				&& dataset.dataEntryBy.qualityNetwork == 1;
	}

}
