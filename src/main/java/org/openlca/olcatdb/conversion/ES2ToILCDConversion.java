package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.database.CompartmentMap;
import org.openlca.olcatdb.database.ES2ElemFlowRec;
import org.openlca.olcatdb.database.ElemFlowMap;
import org.openlca.olcatdb.database.ILCDCompartmentRec;
import org.openlca.olcatdb.database.ILCDElemFlowRec;
import org.openlca.olcatdb.database.UnitMap;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2Description;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.ecospold2.ES2GeographyRef;
import org.openlca.olcatdb.ecospold2.ES2IntermediateExchange;
import org.openlca.olcatdb.ecospold2.ES2Review;
import org.openlca.olcatdb.ecospold2.ES2TimePeriod;
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
import org.openlca.olcatdb.ilcd.ILCDSource;
import org.openlca.olcatdb.ilcd.ILCDSourceDescription;
import org.openlca.olcatdb.ilcd.ILCDTime;
import org.openlca.olcatdb.ilcd.resources.ILCDFolder;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * Converts EcoSpold 02 activity data sets to ILCD.
 * 
 * @author Michael Srocka
 */
public class ES2ToILCDConversion extends AbstractConversionImpl {

	private ILCDExchangeAccumulator accumulator = new ILCDExchangeAccumulator();
	private Map<String, ILCDExchange> exchangesByFlow = new HashMap<String, ILCDExchange>();

	/**
	 * The ILCD folder where the created files of the current conversion are
	 * stored.
	 */
	private ILCDFolder ilcdFolder;

	/**
	 * The XML writer of the created data sets (writes the data sets to an XML
	 * file using a template).
	 */
	private XmlOutputter outputter;

	@Override
	public void createFolder(File targetDir) {
		super.createFolder(targetDir);
		this.ilcdFolder = new ILCDFolder(targetDir);
		this.ilcdFolder.createContent();
		logFile(ilcdFolder);
	}

	@Override
	public ResourceFolder getResult() {
		return this.ilcdFolder;
	}

	@Override
	public void run() {

		// create the output folder
		createFolder(targetDir);

		// set up the conversion
		outputter = new XmlOutputter();

		// iterate over the data streams
		XmlContextParser parser = new XmlContextParser();
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

		// finish the conversion
		flush();

	}

	/**
	 * Converts a EcoSpold 02 data set to an ILCD data set and returns the
	 * reference to the created file.
	 */
	private DataSetReference convert(ES2Dataset dataset) {

		ILCDProcess iProcess = new ILCDProcess();

		dataSetInformation(dataset, iProcess);
		time(dataset, iProcess);
		geography(dataset, iProcess);
		technology(dataset, iProcess);
		lciMethod(dataset, iProcess);
		representativeness(dataset, iProcess);
		reviews(dataset, iProcess);
		adminInfo(dataset, iProcess);
		exchanges(dataset, iProcess);

		// save the conversion result
		String uuid = null;
		if (iProcess.description != null && iProcess.description.uuid != null) {
			uuid = iProcess.description.uuid;
		} else {
			uuid = UUID.randomUUID().toString();
		}

		String name = null;
		if (iProcess.description != null
				&& !(iProcess.description.getName().isEmpty())) {
			name = iProcess.description.getName().get(0).getValue();
		} else {
			name = "no name";
		}

		// create the data set reference of the created process
		DataSetReference reference = new DataSetReference();
		reference.setRefObjectId(uuid);
		reference.setName(name);
		reference.setType(ILCDDataSetType.Process.toString());
		reference.setVersion("01.00.000");

		File outFile = ilcdFolder.file(reference);
		if (outFile.exists()) {
			logger.severe("A process file for " + uuid + " already exists.");
			reference = null; // -> nothing is created
		} else {
			outputter.output(iProcess, TemplateType.ILCDProcess, outFile, true);
			String url = outFile.toURI().toString();
			reference.setUri(url);
		}
		return reference;
	}

	/**
	 * Creates the data set information.
	 */
	private void dataSetInformation(ES2Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessDescription iDescription = new ILCDProcessDescription();
		iProcess.description = iDescription;

		if (eDataset.description != null) {
			ES2Description eDescription = eDataset.description;

			// UUID
			iDescription.uuid = eDescription.id;
			// name
			iDescription.getName().addAll(eDescription.getName());

			// synonyms
			if (!eDescription.getSynonyms().isEmpty()) {
				String syns = "";
				Iterator<LangString> it = eDescription.getSynonyms().iterator();
				while (it.hasNext()) {
					LangString langString = it.next();
					syns += langString.getValue();
					if (it.hasNext()) {
						syns += "; ";
					}
				}
				iDescription.getSynonyms().add(new LangString(syns));
			}

			// general comment -> technological applicability
			if (eDescription.generalComment != null
					&& eDescription.generalComment.hasText()) {
				iDescription.getComment().add(
						eDescription.generalComment.getFirstLangString());
			}

		}

		// classifications
		for (ES2ClassificationRef eClassification : eDataset
				.getClassifications()) {
			ILCDClassification iClassification = new ILCDClassification();
			iDescription.getClassifications().add(iClassification);
			iClassification.fileURI = "../classifications.xml"; // DEFAULT

			iClassification.name = LangString.getFirstValue(eClassification
					.getClassificationSystem());
			int level = 0;
			for (LangString langString : eClassification
					.getClassificationValue()) {
				if (langString.getValue() != null) {
					String[] entries = langString.getValue().split("/");
					for (String e : entries) {
						if (e.length() > 0) {
							iClassification.getClasses().add(
									new ILCDClass(level, e));
							level++;
						}
					}
				}
			}
		}
	}

	/**
	 * Creates the time element.
	 */
	private void time(ES2Dataset eDataset, ILCDProcess iProcess) {

		if (eDataset.timePeriod != null) {
			ES2TimePeriod eTime = eDataset.timePeriod;
			ILCDTime iTime = new ILCDTime();
			iProcess.time = iTime;

			// start date
			if (eTime.startDate != null && eTime.startDate.length() > 3) {
				try {
					String val = eTime.startDate.substring(0, 4);
					iTime.referenceYear = Integer.parseInt(val);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// end date
			if (eTime.endDate != null && eTime.endDate.length() > 3) {
				try {
					String val = eTime.endDate.substring(0, 4);
					iTime.validUntil = Integer.parseInt(val);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// comment
			if (eTime.comment != null && eTime.comment.hasText()) {
				iTime.getDescription().add(eTime.comment.getFirstLangString());
			}

			// TODO: isValidForEntirePeriod
		}

	}

	/**
	 * Creates the geography element.
	 */
	private void geography(ES2Dataset eDataset, ILCDProcess iProcess) {

		if (eDataset.geography != null) {
			ES2GeographyRef eGeography = eDataset.geography;
			ILCDGeography iGeography = new ILCDGeography();
			iProcess.geography = iGeography;
			iGeography.location = LangString.getFirstValue(eGeography
					.getShortNames());
			if (eGeography.comment != null && eGeography.comment.hasText()) {
				iGeography.getDescription().add(
						eGeography.comment.getFirstLangString());
			}
		}
	}

	/**
	 * Creates the technology element.
	 */
	private void technology(ES2Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessTechnology iTechnology = new ILCDProcessTechnology();
		iProcess.technology = iTechnology;

		if (eDataset.description != null) {

			// included activities start & end -> included processes
			ES2Description eDescription = eDataset.description;
			String includedActivities = null;
			if (!eDescription.getIncludedActivitiesStart().isEmpty()) {
				includedActivities = eDescription.getIncludedActivitiesStart()
						.get(0).getValue();
			}
			if (!eDescription.getIncludedActivitiesEnd().isEmpty()) {
				if (includedActivities != null
						&& includedActivities.length() > 0) {
					includedActivities += " // "
							+ eDescription.getIncludedActivitiesEnd().get(0)
									.getValue();
				} else {
					includedActivities = eDescription
							.getIncludedActivitiesEnd().get(0).getValue();
				}
			}
			if (includedActivities != null) {
				iTechnology.getDescription().add(
						new LangString(includedActivities));
			}
		}

		if (eDataset.technology != null && eDataset.technology.comment != null
				&& eDataset.technology.comment.hasText()) {
			iTechnology.getApplicability().add(
					eDataset.technology.comment.getFirstLangString());
		}
	}

	// TODO: mathematical relations

	/**
	 * Creates the LCI method element.
	 */
	private void lciMethod(ES2Dataset eDataset, ILCDProcess iProcess) {

		ILCDProcessMethod iMethod = new ILCDProcessMethod();
		iProcess.method = iMethod;

		// the process type
		if (eDataset.description != null) {
			if (eDataset.description.type == 2) {
				iMethod.dataSetType = "LCI result";
			} else {
				iMethod.dataSetType = "Unit process, black box";
			}
		}

		// enumeration values
		iMethod.lciMethodPrinciple = "Other";
		iMethod.getLciMethodApproaches().add("Other");

		// deviations from ...
		LangString stdText = new LangString();
		stdText.setValue("See the methodology report.");
		iMethod.getDeviationsFromLCIMethodApproaches().add(stdText);
		iMethod.getDeviationsFromLCIMethodPrinciple().add(stdText);
		iMethod.getDeviationsFromModellingConstants().add(stdText);
		iMethod.getModellingConstants().add(stdText);

		// reference to the methodology data set
		iMethod.getMethodDetails().add(
				ReferenceFactory.ECOINVENT_METHODIC.createReference());

	}

	/**
	 * Creates the representativeness element.
	 */
	private void representativeness(ES2Dataset eDataset, ILCDProcess iProcess) {

		ILCDRepresentativeness iRepri = new ILCDRepresentativeness();
		iProcess.representativeness = iRepri;

		// direct mapping: extrapolations, sampling procedure, percent
		if (eDataset.representativeness != null) {
			iRepri.getSamplingProcedure().addAll(
					eDataset.representativeness.getSamplingProcedure());
			iRepri.getExtrapolationPrinciples().addAll(
					eDataset.representativeness.getExtrapolations());
			iRepri.percent = eDataset.representativeness.percent;
		}

		// default entries for empty fields
		LangString stdText = new LangString();
		stdText.setValue("See the methodology report.");
		if (iRepri.getCutOffPrinciples().isEmpty())
			iRepri.getCutOffPrinciples().add(stdText);

		if (iRepri.getDeviationsFromCutOffPrinciples().isEmpty())
			iRepri.getDeviationsFromCutOffPrinciples().add(stdText);

		if (iRepri.getDataSelectionPrinciples().isEmpty())
			iRepri.getDataSelectionPrinciples().add(stdText);

		if (iRepri.getDeviationsFromDataSelectionPrinciples().isEmpty())
			iRepri.getDeviationsFromDataSelectionPrinciples().add(stdText);

		if (iRepri.getExtrapolationPrinciples().isEmpty())
			iRepri.getExtrapolationPrinciples().add(stdText);

		if (iRepri.getDeviationsFromExtrapolationPrinciples().isEmpty())
			iRepri.getDeviationsFromExtrapolationPrinciples().add(stdText);

		// the use advice
		String use = "This data set was automatically converted "
				+ "with the openLCA data converter. Before you use "
				+ "this data set you should check the entries and the "
				+ "compliance with the ILCD conformity. You "
				+ "can edit this data set with the ILCD Editor "
				+ "freely available at "
				+ "http://lct.jrc.ec.europa.eu/assessment/tools.";
		iRepri.getUseAdviceForDataSet().add(new LangString(use));

	}

	/**
	 * Creates the review elements
	 */
	private void reviews(ES2Dataset eDataset, ILCDProcess iProcess) {
		for (ES2Review eReview : eDataset.getReviews()) {

			ILCDReview iReview = new ILCDReview();
			iProcess.getReviews().add(iReview);
			if (eReview.details != null && eReview.details.hasText())
				iReview.getDetails().add(eReview.details.getFirstLangString());
			if (!eReview.getOtherDetails().isEmpty()) {
				iReview.getOtherDetails().add(
						LangString.getFirst(eReview.getOtherDetails()));
			}

			// TODO: reviewers

		}
	}

	/**
	 * Creates the administrative information.
	 */
	private void adminInfo(ES2Dataset eDataset, ILCDProcess iProcess) {

		// data generator
		if (eDataset.publication != null) {
			if (eDataset.publication.personId != null
					&& eDataset.publication.personName != null) {
				DataSetReference ref = contact(eDataset.publication.personId,
						eDataset.publication.personName,
						eDataset.publication.personEmail);
				iProcess.getDataGenerators().add(ref);
			}
		}

		// data entry by
		ILCDProcessEntry iEntry = new ILCDProcessEntry();
		iProcess.entry = iEntry;
		iEntry.getDataFormatReferences().add(
				ReferenceFactory.ILCD_FORMAT.createReference());
		iEntry.getDataFormatReferences().add(
				ReferenceFactory.ECOSPOLD_FORMAT.createReference());
		if (eDataset.fileAttributes != null
				&& eDataset.fileAttributes.creationTimestamp != null) {
			iEntry.timeStamp = eDataset.fileAttributes.creationTimestamp;
		} else {
			iEntry.timeStamp = Time.now();
		}

		// reference to contact
		if (eDataset.dataEntryBy != null) {
			if (eDataset.dataEntryBy.personId != null
					&& eDataset.dataEntryBy.personName != null) {
				DataSetReference ref = contact(eDataset.dataEntryBy.personId,
						eDataset.dataEntryBy.personName,
						eDataset.dataEntryBy.personEmail);
				iEntry.dataSetEntryReference = ref;
			}
		}

		// publication and ownership
		ILCDProcessPublication publication = new ILCDProcessPublication();
		iProcess.publication = publication;

		if (eDataset.publication != null) {
			publication.copyright = eDataset.publication.isCopyrightProtected;
			publication.dataSetVersion = "01.00.000";
			String processId = iProcess.description.uuid;
			publication.permanentDataSetURI = "http://ecoinvent.org?processId="
					+ processId;
			if (eDataset.publication.dataPublishedIn != null) {
				switch (eDataset.publication.dataPublishedIn) {
				case 0:
					publication.workflowStatus = "Working draft";
					break;
				case 1:
					publication.workflowStatus = "Data set finalised; subsystems published";
					break;
				case 2:
					publication.workflowStatus = "Data set finalised; entirely published";
					break;
				default:
					break;
				}
			}

			// publication source
			if (eDataset.publication.publishedSourceId != null
					&& eDataset.publication.publishedSourceFirstAuthor != null) {
				DataSetReference sourceRef = source(
						eDataset.publication.publishedSourceId,
						eDataset.publication.publishedSourceFirstAuthor,
						eDataset.publication.publishedSourceYear);
				publication.republicationReference = sourceRef;
			}
		}

	}

	// exchanges start

	private void exchanges(ES2Dataset eDataSet, ILCDProcess iProcess) {

		ILCDQuantitativeReference qRef = new ILCDQuantitativeReference();
		qRef.type = "Reference flow(s)";
		iProcess.quantitativeReference = qRef;

		// the intermediate exchanges
		for (ES2IntermediateExchange eExchange : eDataSet
				.getIntermediateExchanges()) {

			ILCDExchange iExchange = new ILCDExchange();
			iExchange.id = iProcess.getExchanges().size() + 1;

			// map the general exchange attributes
			iExchange.variableReference = eExchange.variableName;
			if (!eExchange.getComment().isEmpty()) {
				iExchange.getComment().add(
						LangString.getFirst(eExchange.getComment()));
			}
			iExchange.direction = eExchange.outputGroup != null ? "Output"
					: "Input";

			// make flow (reference) and numeric values
			double factor = flowDispatch(eExchange, iExchange);
			numericValues(eExchange, iExchange, factor);

			// check for quantitative reference
			if (eExchange.outputGroup != null
					&& eExchange.outputGroup.equals(0)) {
				qRef.getReferenceFlows().add(iExchange.id);
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

		// the elementary exchanges
		for (ES2ElementaryExchange eExchange : eDataSet
				.getElementaryExchanges()) {

			ILCDExchange iExchange = new ILCDExchange();
			iExchange.id = iProcess.getExchanges().size() + 1;

			// map the general exchange attributes
			iExchange.variableReference = eExchange.variableName;
			if (!eExchange.getComment().isEmpty()) {
				iExchange.getComment().add(
						LangString.getFirst(eExchange.getComment()));
			}
			iExchange.direction = eExchange.outputGroup != null ? "Output"
					: "Input";

			// make flow (reference) and numeric values
			double factor = flowDispatch(eExchange, iExchange);
			numericValues(eExchange, iExchange, factor);

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
	private double flowDispatch(Object eExchange, ILCDExchange iExchange) {

		// the conversion factor
		double factor = 0;

		if (eExchange instanceof ES2IntermediateExchange) {

			// product flow
			factor = productFlow((ES2IntermediateExchange) eExchange, iExchange);

		} else if (eExchange instanceof ES2ElementaryExchange) {

			ES2ElementaryExchange _eExchange = (ES2ElementaryExchange) eExchange;

			// test if it is an assigned elementary flow
			ElemFlowMap.Entry flowEntry = ElemFlowMap
					.es2ToILCD(_eExchange.elementaryExchangeId);
			if (flowEntry != null) {

				// assigned elementary flow
				factor = assignedElemFlow(flowEntry, iExchange);

			} else {

				// test if it is a known but unassigned elementary flow
				ES2ElemFlowRec flowRec = ES2ElemFlowRec
						.forID(_eExchange.elementaryExchangeId);
				if (flowRec != null) {

					// unassigned but known elementary flow
					factor = unassignedElemFlow(flowRec, iExchange);

				} else {

					// unknown elementary flow
					factor = unknownElemFlow(_eExchange, iExchange);
				}
			}
		}

		return factor;
	}

	private double productFlow(ES2IntermediateExchange eExchange,
			ILCDExchange iExchange) {

		// create the flow reference
		String uuid = eExchange.intermediateExchangeId;
		String name = LangString.getFirstValue(eExchange.getName());
		DataSetReference flowRef = ILCDHelper.newFlowRef(uuid, name);
		iExchange.flowDataSet = flowRef;

		// entry for the unit - flow property conversion
		UnitMap.Entry unitEntry = UnitMap.es2ToILCD(eExchange.unitId);
		if (unitEntry == null) {
			logger.severe("No unit assignment for: " + eExchange.unitId);
			return 0;
		}

		// create the flow if required
		if (!ilcdFolder.exists(flowRef)) {

			ILCDFlow flow = ILCDHelper.makeFlow(uuid, name,
					eExchange.casNumber, null, "Product flow", unitEntry);

			// create the flow
			ILCDHelper.writeFlow(ilcdFolder, flowRef, flow, createdFiles);
		}

		return unitEntry.getFactor();
	}

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

	private double unassignedElemFlow(ES2ElemFlowRec flowRec,
			ILCDExchange iExchange) {

		// create the flow reference
		String uuid = flowRec.getId();
		String name = flowRec.getName();
		DataSetReference flowRef = ILCDHelper.newFlowRef(uuid, name);
		iExchange.flowDataSet = flowRef;

		// entry for the unit - flow property conversion
		UnitMap.Entry unitEntry = UnitMap.es2ToILCD(flowRec.getUnitId());
		if (unitEntry == null) {
			logger.severe("No unit assignment for: " + flowRec.getUnitId());
			return 0;
		}

		if (!ilcdFolder.exists(flowRef)) {

			// create the elementary flow
			ILCDFlow flow = ILCDHelper.makeFlow(uuid, name, flowRec.getCas(),
					flowRec.getFormula(), "Elementary flow", unitEntry);

			// the flow category
			String catId = CompartmentMap.es2ToILCD(flowRec.getCompartmentId());
			if (catId != null) {
				ILCDCompartmentRec rec = ILCDCompartmentRec.forID(catId);
				if (rec != null) {
					flow.description.getElemFlowCategorizations().add(
							rec.toCategorization());
				}
			}

			// create the flow file
			ILCDHelper.writeFlow(ilcdFolder, flowRef, flow, createdFiles);

		}

		return unitEntry.getFactor();

	}

	/**
	 * Creates a reference to an ILCD elementary flow for the given EcoSpold 02
	 * elementary exchange. If there is no elementary flow data set in the ILCD
	 * folder of this conversion, this flow data set is created.
	 */
	private double unknownElemFlow(ES2ElementaryExchange eExchange,
			ILCDExchange iExchange) {

		// create the flow reference
		String uuid = eExchange.elementaryExchangeId;
		String name = LangString.getFirstValue(eExchange.getName());
		DataSetReference flowRef = ILCDHelper.newFlowRef(uuid, name);
		iExchange.flowDataSet = flowRef;

		// entry for the unit - flow property conversion
		UnitMap.Entry unitEntry = UnitMap.es2ToILCD(eExchange.unitId);
		if (unitEntry == null) {
			logger.severe("No unit assignment for: " + eExchange.unitId);
			return 0;
		}

		if (!ilcdFolder.exists(flowRef)) {

			// create the elementary flow
			ILCDFlow flow = ILCDHelper.makeFlow(uuid, name,
					eExchange.casNumber, eExchange.formula, "Elementary flow",
					unitEntry);

			// the flow category
			String catId = CompartmentMap.es2ToILCD(eExchange.compartmentId);
			if (catId != null) {
				ILCDCompartmentRec rec = ILCDCompartmentRec.forID(catId);
				if (rec != null) {
					flow.description.getElemFlowCategorizations().add(
							rec.toCategorization());
				}
			}

			// create the flow file
			ILCDHelper.writeFlow(ilcdFolder, flowRef, flow, createdFiles);
		}

		return unitEntry.getFactor();
	}

	private void numericValues(Object eExchange, ILCDExchange iExchange,
			double factor) {

		if (factor == 0) {
			logger.warning("Conversion factor is 0: " + eExchange);
		}

		double amount = 0;
		if (eExchange instanceof ES2IntermediateExchange) {
			amount = ((ES2IntermediateExchange) eExchange).amount;
		} else if (eExchange instanceof ES2ElementaryExchange) {
			amount = ((ES2ElementaryExchange) eExchange).amount;
		}

		iExchange.resultingAmount = amount * factor;
		iExchange.meanAmount = amount * factor;

		// TODO: uncertainty...

	}

	// exchanges end

	/**
	 * Creates an ILCD contact data set reference for the given attributes. If
	 * there is no contact file for this reference in the ILCD folder, this file
	 * is created.
	 */
	private DataSetReference contact(String id, String name, String email) {

		DataSetReference ref = new DataSetReference();
		ref.setName(name);
		ref.setRefObjectId(id);
		ref.setType(ILCDDataSetType.Contact.toString());
		ref.setUri("../contacts/" + id + ".xml");
		ref.setVersion("01.00.000");
		ref.getDescription().add(new LangString(name));

		if (!ilcdFolder.exists(ref)) {

			// create the contact data set
			ILCDContact contact = new ILCDContact();

			// contact description
			contact.description = new ILCDContactDescription();
			contact.description.email = email;
			contact.description.uuid = id;
			contact.description.getName().add(new LangString(name));
			contact.description.getShortName().add(new LangString(name));

			// classification
			ILCDClassification classification = new ILCDClassification();
			ILCDClass clazz = new ILCDClass();
			clazz.level = 0;
			clazz.name = "Persons";
			classification.getClasses().add(clazz);
			contact.description.getClassifications().add(classification);

			// data entry
			ILCDEntry entry = new ILCDEntry();
			contact.entry = entry;
			entry.timestamp = Time.now();
			entry.getDataSetFormats().add(
					ReferenceFactory.ILCD_FORMAT.createReference());
			entry.getDataSetFormats().add(
					ReferenceFactory.ECOSPOLD_FORMAT.createReference());

			// publication
			ILCDPublication publication = new ILCDPublication();
			contact.publication = publication;
			publication.dataSetVersion = "01.00.000";
			publication.permanentDataSetURI = "http://ecoinvent.org?personId="
					+ id;

			// save the file
			File outFile = ilcdFolder.file(ref);
			outputter.output(contact, TemplateType.ILCDContact, outFile, false);
			// reference for created files
			DataSetReference refCopy = ref.copy();
			refCopy.setUri(outFile.toURI().toString());
			createdFiles.add(refCopy);
		}

		return ref;
	}

	/**
	 * Creates an ILCD source data set reference for the given attributes. If
	 * there is no source file for this reference in the ILCD folder, this file
	 * is created.
	 */
	private DataSetReference source(String id, String author, String year) {

		String cit = year == null ? author : author + " " + year;

		// the reference to the contact data set
		DataSetReference ref = new DataSetReference();
		ref.setName(cit);
		ref.setRefObjectId(id);
		ref.setType(ILCDDataSetType.Source.toString());
		ref.setUri("../sources/" + id + ".xml");
		ref.setVersion("01.00.000");
		ref.getDescription().add(new LangString(cit));

		if (!ilcdFolder.exists(ref)) {

			// create the source file

			ILCDSource source = new ILCDSource();

			// the source description
			ILCDSourceDescription iDescription = new ILCDSourceDescription();
			source.description = iDescription;
			iDescription.uuid = id;
			iDescription.sourceCitation = cit;
			iDescription.getShortName().add(new LangString(cit));

			// classification
			ILCDClassification classification = new ILCDClassification();
			iDescription.getClassifications().add(classification);
			ILCDClass clazz = new ILCDClass();
			classification.getClasses().add(clazz);
			clazz.level = 0;
			clazz.name = "Publications and communications";

			// data entry
			ILCDEntry entry = new ILCDEntry();
			source.entry = entry;
			entry.timestamp = Time.now();
			entry.getDataSetFormats().add(
					ReferenceFactory.ILCD_FORMAT.createReference());
			entry.getDataSetFormats().add(
					ReferenceFactory.ECOSPOLD_FORMAT.createReference());

			// publication
			ILCDPublication publication = new ILCDPublication();
			source.publication = publication;
			publication.dataSetVersion = "01.00.000";
			publication.permanentDataSetURI = "http://ecoinvent.org?sourceId="
					+ id;

			// store the source
			File outFile = ilcdFolder.file(ref);
			outputter.output(source, TemplateType.ILCDSource, outFile, false);
			// reference for created files
			DataSetReference refCopy = ref.copy();
			refCopy.setUri(outFile.toURI().toString());
			createdFiles.add(refCopy);
		}

		return ref;
	}
}
