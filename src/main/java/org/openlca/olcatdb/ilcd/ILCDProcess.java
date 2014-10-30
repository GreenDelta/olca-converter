package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

@Context(name = "processDataSet")
public class ILCDProcess extends ContextObject {

	/**
	 * Indicates whether this data set contains only meta data (no exchanges
	 * section).
	 * 
	 * @Attribute metaDataOnly
	 * @DataType boolean
	 * @DefaultValue false
	 */
	@ContextField(attributeName = "metaDataOnly", isAttribute = true, name = "processDataSet", type = Type.Boolean)
	public boolean metaDataOnly = false;

	/**
	 * General data set information. Section covers all single fields in the
	 * ISO/TS 14048 "Process description", which are not part of the other
	 * sub-sections. In ISO/TS 14048 no own sub-section is foreseen for these
	 * entries.
	 * 
	 * @Element dataSetInformation
	 * @ContentModel (UUID, name?, identifierOfSubDataSet?, synonyms*,
	 *               complementingProcesses?, classificationInformation?,
	 *               generalComment*, referenceToExternalDocumentation*, other?)
	 */
	@SubContext(contextClass = ILCDProcessDescription.class)
	public ILCDProcessDescription description;

	/**
	 * This section names the quantitative reference used for this data set,
	 * i.e. the reference to which the inputs and outputs quantiatively relate.
	 * 
	 * @Element quantitativeReference
	 * @ContentModel (referenceToReferenceFlow*, functionalUnitOrOther*, other?)
	 */
	@SubContext(contextClass = ILCDQuantitativeReference.class)
	public ILCDQuantitativeReference quantitativeReference;

	/**
	 * Provides information about the geographical representativeness of the
	 * data set.
	 * 
	 * @Element geography
	 * @ContentModel (locationOfOperationSupplyOrProduction?,
	 *               subLocationOfOperationSupplyOrProduction*, other?)
	 */
	@SubContext(contextClass = ILCDGeography.class)
	public ILCDGeography geography;

	/**
	 * Provides information about the technological representativeness of the
	 * data set.
	 * 
	 * @Element technology
	 * @ContentModel (technologyDescriptionAndIncludedProcesses*,
	 *               referenceToIncludedProcesses*, technologicalApplicability*,
	 *               referenceToTechnologyPictogramme?,
	 *               referenceToTechnologyFlowDiagrammOrPicture*, other?)
	 */
	@SubContext(contextClass = ILCDProcessTechnology.class)
	public ILCDProcessTechnology technology;

	/**
	 * A set of formulas that allows to model the amount of single exchanges in
	 * the input and output list in dependency of each other and/or in
	 * dependency of parameters. Used to provide a process model
	 * ("parameterized process") for calculation of inventories in dependency of
	 * user settings of e.g. yield, efficiency of abatement measures, processing
	 * of different educts, etc.
	 * 
	 * @Element mathematicalRelations
	 * @ContentModel : (modelDescription*, variableParameter*, other?)
	 */
	@SubContext(contextClass = ILCDMathSection.class)
	public ILCDMathSection mathSection;

	/**
	 * LCI methodological modelling aspects including allocation / substitution
	 * information.
	 * 
	 * @Element LCIMethodAndAllocation
	 * @ContentModel (typeOfDataSet?, LCIMethodPrinciple?,
	 *               deviationsFromLCIMethodPrinciple*, LCIMethodApproaches*,
	 *               deviationsFromLCIMethodApproaches*, modellingConstants*,
	 *               deviationsFromModellingConstants*,
	 *               referenceToLCAMethodDetails*, other?)
	 */
	@SubContext(contextClass = ILCDProcessMethod.class)
	public ILCDProcessMethod method;

	/**
	 * Data selection, completeness, and treatment principles and procedures,
	 * data sources and market coverage information.
	 * 
	 * @Element dataSourcesTreatmentAndRepresentativeness
	 * @ContentModel (dataCutOffAndCompletenessPrinciples*,
	 *               deviationsFromCutOffAndCompletenessPrinciples*,
	 *               dataSelectionAndCombinationPrinciples*,
	 *               deviationsFromSelectionAndCombinationPrinciples*,
	 *               dataTreatmentAndExtrapolationsPrinciples*,
	 *               deviationsFromTreatmentAndExtrapolationPrinciples*,
	 *               referenceToDataHandlingPrinciples*, referenceToDataSource*,
	 *               percentageSupplyOrProductionCovered?,
	 *               annualSupplyOrProductionVolume*, samplingProcedure*,
	 *               dataCollectionPeriod*, uncertaintyAdjustments*,
	 *               useAdviceForDataSet*, other?)
	 */
	@SubContext(contextClass = ILCDRepresentativeness.class)
	public ILCDRepresentativeness representativeness;

	/**
	 * Provides information about the time representativeness of the data set.
	 * 
	 * @Element time
	 * @ContentModel (referenceYear?, dataSetValidUntil?,
	 *               timeRepresentativenessDescription*, other?)
	 */
	@SubContext(contextClass = ILCDTime.class)
	public ILCDTime time;

	/**
	 * Data completeness aspects for this specific data set.
	 * 
	 * @Element completeness
	 * @ContentModel (completenessProductModel?,
	 *               referenceToSupportedImpactAssessmentMethods*,
	 *               completenessElementaryFlows*,
	 *               completenessOtherProblemField*, other?)
	 */
	@SubContext(contextClass = ILCDCompleteness.class)
	public ILCDCompleteness completeness;

	@ContextField(
			name = "referenceToPersonOrEntityGeneratingTheDataSet",
			parentName = "dataGenerator",
			isMultiple = true,
			type = Type.DataSetReference
	)
	private List<DataSetReference> dataGenerators
		= new ArrayList<DataSetReference>();
		
	public List<DataSetReference> getDataGenerators() {
		return dataGenerators;
	}
	
	
	/**
	 * Staff or entity, that documented the generated data set, entering the
	 * information into the database; plus administrative information linked to
	 * the data entry activity.
	 * 
	 * @Element dataEntryBy
	 * @ContentModel (((timeStamp?, referenceToDataSetFormat*)),
	 *               referenceToConvertedOriginalDataSetFrom?,
	 *               ((referenceToPersonOrEntityEnteringTheData?)),
	 *               referenceToDataSetUseApproval*, other?)
	 */
	@SubContext(contextClass = ILCDProcessEntry.class)
	public ILCDProcessEntry entry;

	/**
	 * Information related to publication and version management of the data set
	 * including copyright and access restrictions.
	 * 
	 * @Element publicationAndOwnership
	 * @ContentModel (dateOfLastRevision?, ((dataSetVersion,
	 *               referenceToPrecedingDataSetVersion*,
	 *               permanentDataSetURI?)), ((workflowAndPublicationStatus?,
	 *               referenceToUnchangedRepublication?)),
	 *               referenceToRegistrationAuthority?, registrationNumber?,
	 *               referenceToOwnershipOfDataSet?, ((copyright?,
	 *               referenceToEntitiesWithExclusiveAccess*, licenseType?,
	 *               accessRestrictions*)), other?)
	 */
	@SubContext(contextClass = ILCDProcessPublication.class)
	public ILCDProcessPublication publication;

	
	@SubContext(contextClass = ILCDReview.class, isMultiple = true)
	private List<ILCDReview> reviews = new ArrayList<ILCDReview>();
	
	public List<ILCDReview> getReviews() {
		return reviews;
	}
	
	/**
	 * Input/Output list of exchanges with the quantitative inventory data, as
	 * well as pre-calculated LCIA results.
	 * 
	 * @Element exchanges
	 * @ContentModel (exchange*, other?)
	 */
	@SubContext(contextClass = ILCDExchange.class, isMultiple = true)
	private List<ILCDExchange> exchanges = new ArrayList<ILCDExchange>();

	/**
	 * Input/Output list of exchanges with the quantitative inventory data, as
	 * well as pre-calculated LCIA results.
	 * 
	 * @Element exchanges
	 * @ContentModel (exchange*, other?)
	 */
	public List<ILCDExchange> getExchanges() {
		return exchanges;
	}
	
}
