package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.ecospold1.ES1Dataset;
import org.openlca.olcatdb.ecospold1.ES1EcoSpold;
import org.openlca.olcatdb.ecospold1.ES1Exchange;
import org.openlca.olcatdb.ecospold1.ES1Person;
import org.openlca.olcatdb.ecospold1.ES1Source;
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2Description;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2Entry;
import org.openlca.olcatdb.ecospold2.ES2FileAttributes;
import org.openlca.olcatdb.ecospold2.ES2GeographyRef;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2LogNormalDistribution;
import org.openlca.olcatdb.ecospold2.ES2MacroEconomicScenario;
import org.openlca.olcatdb.ecospold2.ES2NormalDistribution;
import org.openlca.olcatdb.ecospold2.ES2Publication;
import org.openlca.olcatdb.ecospold2.ES2Representativeness;
import org.openlca.olcatdb.ecospold2.ES2Review;
import org.openlca.olcatdb.ecospold2.ES2Technology;
import org.openlca.olcatdb.ecospold2.ES2TimePeriod;
import org.openlca.olcatdb.ecospold2.ES2TriangularDistribution;
import org.openlca.olcatdb.ecospold2.ES2Uncertainty;
import org.openlca.olcatdb.ecospold2.ES2UndefinedDistribution;
import org.openlca.olcatdb.ecospold2.ES2UniformDistribution;
import org.openlca.olcatdb.ecospold2.resources.ES2Folder;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts EcoSpold 01 to EcoSpold 02 data sets.
 * 
 * @author Michael Srocka
 * 
 */
class ES1ToES2Conversion extends AbstractConversionImpl {

	/**
	 * The directory where the created data sets are stored.
	 */
	private ES2Folder es2Folder;	
	
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
	 * Converts the given EcoSpold 01 data set to an EcoSpold 02 activity.
	 */
	private DataSetReference convert(ES1Dataset dataset) {

		ES2EcoSpold ecoSpold = new ES2EcoSpold();
		ES2Dataset e2Dataset = new ES2Dataset();
		ecoSpold.getDatasets().add(e2Dataset);
		description(dataset, e2Dataset);
		classification(dataset, e2Dataset);
		geography(dataset, e2Dataset);
		technology(dataset, e2Dataset);
		time(dataset, e2Dataset);
		economicScen(dataset, e2Dataset);

		// the exchanges
		for (ES1Exchange exchange : dataset.getExchanges()) {
			if (exchange.isElementaryFlow()) {
				elementaryExchange(exchange, e2Dataset);
			} else {
				intermediateExchange(exchange, e2Dataset);
			}
		}

		representativeness(dataset, e2Dataset);
		review(dataset, e2Dataset);
		dataEntry(dataset, e2Dataset);
		publication(dataset, e2Dataset);
		fileAttributes(dataset, e2Dataset);

		// write the file
		File outFile = new File(es2Folder.getActivityFolder(), UUID
				.randomUUID().toString()
				+ ".spold");
		XmlOutputter outputter = new XmlOutputter();
		outputter.output(ecoSpold, TemplateType.EcoSpold02, outFile, true);

		// create the data set reference of the created process
		DataSetReference reference = new DataSetReference();
		if (e2Dataset.description != null)
			reference.setRefObjectId(e2Dataset.description.activityNameId);
		reference.setType("process data set");
		// create the URI
		try {
			String uri = outFile.toURI().toString();
			reference.setUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// the (first) name of the created process
		if (e2Dataset.description != null
				&& !e2Dataset.description.getName().isEmpty()) {
			reference
					.setName(e2Dataset.description.getName().get(0).getValue());
		}
		return reference;
	}

	/**
	 * Creates the activity description.
	 */
	private void description(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		ES2Description e2Description = new ES2Description(true);
		e2Ds.description = e2Description;

		// @ id
		e2Description.id = UUID.randomUUID().toString();
		// @ activity id
		e2Description.activityNameId = UUID.randomUUID().toString();

		if (e1Ds.referenceFunction != null) {
			// @ name
			e2Description.getName().add(
					new LangString(e1Ds.referenceFunction.name));

			// @ included activities
			if (e1Ds.referenceFunction.includedProcesses != null) {
				e2Description
						.getIncludedActivitiesEnd()
						.add(
								new LangString(
										e1Ds.referenceFunction.includedProcesses));
			}

			// @ comment
			if (e1Ds.referenceFunction.generalComment != null) {
				e2Description.generalComment = new TextAndImage(
						e1Ds.referenceFunction.generalComment);
			}

		}

		// TODO: master data for activity

	}

	/**
	 * Creates the activity classification.
	 */
	private void classification(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.referenceFunction != null) {
			ES2ClassificationRef classification = new ES2ClassificationRef();
			e2Ds.getClassifications().add(classification);
			classification.classificationId = "5d8c60ab-17f8-4ba6-99ca-a72b75f66cf3";
			classification.getClassificationSystem().add(
					new LangString("EcoSpold01Categories"));
			String val = "" + e1Ds.referenceFunction.category + "/"
					+ e1Ds.referenceFunction.subCategory;
			classification.getClassificationValue().add(new LangString(val));
		}

		// TODO: master data for classifications

	}

	/**
	 * Creates the geography of the activity.
	 */
	private void geography(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.geography != null) {

			ES2GeographyRef es2Geography = new ES2GeographyRef();
			e2Ds.geography = es2Geography;

			// @id
			es2Geography.geographyId = UUID.randomUUID().toString();

			// @ location
			if (e1Ds.geography.location != null) {
				es2Geography.getShortNames().add(
						new LangString(e1Ds.geography.location));
			}

			// @ comment
			if (e1Ds.geography.text != null) {
				es2Geography.comment = new TextAndImage(e1Ds.geography.text);
			}
		}

		// TODO: create master data
	}

	/**
	 * Creates the technology element of the activity data set.
	 */
	private void technology(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.technology != null) {

			ES2Technology e2Technology = new ES2Technology();
			e2Ds.technology = e2Technology;

			// @ level
			e2Technology.technologyLevel = 3; // default

			// @ comment
			if (e1Ds.technology.text != null) {
				e2Technology.comment = new TextAndImage(e1Ds.technology.text);
			}
		}
	}

	/**
	 * Creates the time element of the data set.
	 */
	private void time(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.timePeriod != null) {

			ES2TimePeriod e2Time = new ES2TimePeriod();
			e2Ds.timePeriod = e2Time;

			// @ start year // TODO: support other time stamps
			if (e1Ds.timePeriod.startYear != null) {
				e2Time.startDate = e1Ds.timePeriod.startYear.toString()
						+ "-01-01";
			}

			// @ end year // TODO: support other time stamps
			if (e1Ds.timePeriod.endYear != null) {
				e2Time.endDate = e1Ds.timePeriod.endYear.toString() + "-01-01";
			}

			// @ time valid
			if (e1Ds.timePeriod.dataValidForEntirePeriod != null) {
				e2Time.dataValidForEntirePeriod = e1Ds.timePeriod.dataValidForEntirePeriod;
			}

			// @ comment
			if (e1Ds.timePeriod.text != null) {
				e2Time.comment = new TextAndImage(e1Ds.timePeriod.text);
			}
		}
	}

	/**
	 * Creates the economic scenario of the data set.
	 */
	private void economicScen(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		ES2MacroEconomicScenario scen = new ES2MacroEconomicScenario();
		e2Ds.macroEconomicScenario = scen;
		// @ id
		scen.scenarioId = "D9F57F0A-A01F-42eb-A57B-8F18D6635801";
		// @ name
		scen.getName().add(new LangString("Business-as-Usual"));

	}

	/**
	 * Creates a new intermediate exchange.
	 */
	private void intermediateExchange(ES1Exchange e1Exchange, ES2Dataset e2Ds) {

		ES2IntermediateExchange i2Exchange = new ES2IntermediateExchange();
		e2Ds.getIntermediateExchanges().add(i2Exchange);

		// @ id
		i2Exchange.id = UUID.randomUUID().toString();
		// TODO unitId

		// amount
		if (e1Exchange.meanValue != null)
			i2Exchange.amount = e1Exchange.meanValue;

		i2Exchange.isCalculatedAmount = false;

		// @ intermediate exchange id
		i2Exchange.intermediateExchangeId = UUID.randomUUID().toString();
		// TODO master data

		// @ name
		i2Exchange.getName().add(new LangString(e1Exchange.name));

		// @ unit name
		i2Exchange.getUnitNames().add(new LangString(e1Exchange.unit));

		// @ comment
		if (e1Exchange.generalComment != null) {
			i2Exchange.getComment().add(
					new LangString(e1Exchange.generalComment));
		}

		// uncertainty
		if (e1Exchange.uncertaintyType != null) {
			i2Exchange.uncertainty = new ES2Uncertainty();

			switch (e1Exchange.uncertaintyType) {
			case 0:
				// undefined
				ES2UndefinedDistribution udistr = new ES2UndefinedDistribution();
				i2Exchange.uncertainty.undefinedDistribution = udistr;
				if (e1Exchange.maxValue != null)
					udistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					udistr.minValue = e1Exchange.minValue;
				if (e1Exchange.standardDeviation95 != null)
					udistr.standardDeviation95 = e1Exchange.standardDeviation95;

				break;
			case 1:
				// log-normal
				ES2LogNormalDistribution ldistr = new ES2LogNormalDistribution();
				i2Exchange.uncertainty.logNormalDistribution = ldistr;
				if (e1Exchange.meanValue != null)
					ldistr.meanValue = e1Exchange.meanValue;
				if (e1Exchange.standardDeviation95 != null)
					ldistr.standardDeviation95 = e1Exchange.standardDeviation95;
				break;
			case 2:
				// normal
				ES2NormalDistribution ndistr = new ES2NormalDistribution();
				i2Exchange.uncertainty.normalDistribution = ndistr;
				if (e1Exchange.meanValue != null)
					ndistr.meanValue = e1Exchange.meanValue;
				if (e1Exchange.standardDeviation95 != null)
					ndistr.standardDeviation95 = e1Exchange.standardDeviation95;
				break;
			case 3:
				// triangular
				ES2TriangularDistribution tdistr = new ES2TriangularDistribution();
				i2Exchange.uncertainty.triangularDistribution = tdistr;
				if (e1Exchange.maxValue != null)
					tdistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					tdistr.minValue = e1Exchange.minValue;
				if (e1Exchange.mostLikelyValue != null)
					tdistr.mostLikelyValue = e1Exchange.mostLikelyValue;
				break;
			case 4:
				// uniform
				ES2UniformDistribution uniDistr = new ES2UniformDistribution();
				i2Exchange.uncertainty.uniformDistribution = uniDistr;
				if (e1Exchange.maxValue != null)
					uniDistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					uniDistr.minValue = e1Exchange.minValue;
				break;
			default:
				break;
			}

		}

		// @ input / output group
		i2Exchange.inputGroup = e1Exchange.inputGroup;
		i2Exchange.outputGroup = e1Exchange.outputGroup;

	}

	/**
	 * Creates a new elementary exchange.
	 */
	private void elementaryExchange(ES1Exchange e1Exchange, ES2Dataset e2Ds) {

		ES2ElementaryExchange e2Exchange = new ES2ElementaryExchange();
		e2Ds.getElementaryExchanges().add(e2Exchange);

		// @ id
		e2Exchange.id = UUID.randomUUID().toString();
		// TODO unitId

		// amount
		if (e1Exchange.meanValue != null)
			e2Exchange.amount = e1Exchange.meanValue;

		e2Exchange.isCalculatedAmount = false;

		// @ elementary exchange id
		e2Exchange.elementaryExchangeId = UUID.randomUUID().toString();
		// TODO master data

		// @ name
		e2Exchange.getName().add(new LangString(e1Exchange.name));

		// @ unit name
		e2Exchange.getUnitNames().add(new LangString(e1Exchange.unit));

		// @ comment
		if (e1Exchange.generalComment != null) {
			e2Exchange.getComment().add(
					new LangString(e1Exchange.generalComment));
		}

		// @ CAS
		if (e1Exchange.CASNumber != null) {
			e2Exchange.casNumber = e1Exchange.CASNumber;
		}

		// @ formula
		if (e1Exchange.formula != null) {
			e2Exchange.formula = e1Exchange.formula;
		}

		// TODO: compartments

		// uncertainty
		if (e1Exchange.uncertaintyType != null) {
			e2Exchange.uncertainty = new ES2Uncertainty();

			switch (e1Exchange.uncertaintyType) {
			case 0:
				// undefined
				ES2UndefinedDistribution udistr = new ES2UndefinedDistribution();
				e2Exchange.uncertainty.undefinedDistribution = udistr;
				if (e1Exchange.maxValue != null)
					udistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					udistr.minValue = e1Exchange.minValue;
				if (e1Exchange.standardDeviation95 != null)
					udistr.standardDeviation95 = e1Exchange.standardDeviation95;

				break;
			case 1:
				// log-normal
				ES2LogNormalDistribution ldistr = new ES2LogNormalDistribution();
				e2Exchange.uncertainty.logNormalDistribution = ldistr;
				if (e1Exchange.meanValue != null)
					ldistr.meanValue = e1Exchange.meanValue;
				if (e1Exchange.standardDeviation95 != null)
					ldistr.standardDeviation95 = e1Exchange.standardDeviation95;
				break;
			case 2:
				// normal
				ES2NormalDistribution ndistr = new ES2NormalDistribution();
				e2Exchange.uncertainty.normalDistribution = ndistr;
				if (e1Exchange.meanValue != null)
					ndistr.meanValue = e1Exchange.meanValue;
				if (e1Exchange.standardDeviation95 != null)
					ndistr.standardDeviation95 = e1Exchange.standardDeviation95;
				break;
			case 3:
				// triangular
				ES2TriangularDistribution tdistr = new ES2TriangularDistribution();
				e2Exchange.uncertainty.triangularDistribution = tdistr;
				if (e1Exchange.maxValue != null)
					tdistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					tdistr.minValue = e1Exchange.minValue;
				if (e1Exchange.mostLikelyValue != null)
					tdistr.mostLikelyValue = e1Exchange.mostLikelyValue;
				break;
			case 4:
				// uniform
				ES2UniformDistribution uniDistr = new ES2UniformDistribution();
				e2Exchange.uncertainty.uniformDistribution = uniDistr;
				if (e1Exchange.maxValue != null)
					uniDistr.maxValue = e1Exchange.maxValue;
				if (e1Exchange.minValue != null)
					uniDistr.minValue = e1Exchange.minValue;
				break;
			default:
				break;
			}

		}

		// @ input / output group
		e2Exchange.inputGroup = e1Exchange.inputGroup;
		e2Exchange.outputGroup = e1Exchange.outputGroup;
	}

	/**
	 * Creates the representativeness entry of the data set.
	 */
	private void representativeness(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.representativeness != null) {

			ES2Representativeness repri = new ES2Representativeness();
			e2Ds.representativeness = repri;

			// @ model id
			repri.systemModelId = "8B738EA0-F89E-4627-8679-433616064E82";

			// @ model name
			repri.getSystemModelName().add(new LangString("undefined"));

			// @ sampling procedure
			if (e1Ds.representativeness.samplingProcedure != null) {
				repri.getSamplingProcedure().add(
						new LangString(
								e1Ds.representativeness.samplingProcedure));
			}

			// @ extrapolations
			if (e1Ds.representativeness.extrapolations != null) {
				repri.getExtrapolations().add(
						new LangString(e1Ds.representativeness.extrapolations));
			}

		}
	}

	/**
	 * Creates the review element of the data set.
	 */
	private void review(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.validation != null) {

			ES2Review review = new ES2Review();
			e2Ds.getReviews().add(review);

			// the reviewer
			Integer ref = e1Ds.validation.proofReadingValidator;
			if (ref != null) {
				ES1Person person = e1Ds.getPerson(ref);
				if (person != null) {
					review.reviewerEmail = person.email;
					review.reviewerId = UUID.randomUUID().toString();
					review.reviewerName = person.name;
				}
			}

			// the review data
			if (e1Ds.dataSetInformation != null) {
				review.reviewDate = e1Ds.dataSetInformation.timestamp;
			}

			// review details
			if (e1Ds.validation.proofReadingDetails != null) {
				review.details = new TextAndImage(
						e1Ds.validation.proofReadingDetails);
			}

			// other details
			if (e1Ds.validation.otherDetails != null) {
				review.getOtherDetails().add(
						new LangString(e1Ds.validation.otherDetails));
			}
		}
	}

	/**
	 * Creates the data entry element.
	 */
	private void dataEntry(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.dataEntryBy != null) {

			ES2Entry dataEntry = new ES2Entry();
			e2Ds.dataEntryBy = dataEntry;

			Integer ref = e1Ds.dataEntryBy.person;
			if (ref != null) {
				ES1Person person = e1Ds.getPerson(ref);
				if (person != null) {
					dataEntry.personEmail = person.email;
					dataEntry.personId = UUID.randomUUID().toString();
					dataEntry.personName = person.name;
				}
			}
		}
	}

	/**
	 * Creates the publication element.
	 */
	private void publication(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		if (e1Ds.dataGeneratorAndPublication != null) {

			ES2Publication publication = new ES2Publication();
			e2Ds.publication = publication;

			publication.accessRestrictedTo = e1Ds.dataGeneratorAndPublication.accessRestrictedTo;
			publication.companyCode = e1Ds.dataGeneratorAndPublication.companyCode;
			publication.dataPublishedIn = e1Ds.dataGeneratorAndPublication.dataPublishedIn;
			publication.isCopyrightProtected = e1Ds.dataGeneratorAndPublication.copyright;
			publication.pageNumbers = e1Ds.dataGeneratorAndPublication.pageNumbers;

			// generator
			Integer ref = e1Ds.dataGeneratorAndPublication.person;
			if (ref != null) {

				ES1Person person = e1Ds.getPerson(ref);
				if (person != null) {
					publication.personEmail = person.email;
					publication.personId = UUID.randomUUID().toString();
					publication.personName = person.name;
				}
			}

			// source
			ref = e1Ds.dataGeneratorAndPublication.referenceToPublishedSource;
			if (ref != null) {

				ES1Source source = e1Ds.getSource(ref);
				if (source != null) {
					publication.publishedSourceFirstAuthor = source.firstAuthor;
					publication.publishedSourceId = UUID.randomUUID()
							.toString();
					publication.publishedSourceYear = source.year != null ? source.year
							.toString()
							: null;
				}
			}
		}
	}

	/**
	 * Creates the file attributes element.
	 */
	private void fileAttributes(ES1Dataset e1Ds, ES2Dataset e2Ds) {

		ES2FileAttributes atts = new ES2FileAttributes(true);
		e2Ds.fileAttributes = atts;

		atts.contextId = "DE659012-50C4-4e96-B54A-FC781BF987AB";
		atts.creationTimestamp = Time.now();
		atts.defaultLanguage = "en";
		atts.fileGenerator = "openLCA - data converter";
		atts.fileTimestamp = Time.now();
		atts.internalSchemaVersion = "2.0.8";
		atts.lastEditTimestamp = Time.now();
		atts.getContextNames().add(new LangString("ecoinvent"));
	}

}
