package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Data selection, completeness, and treatment principles and procedures, data
 * sources and market coverage information.
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
@Context(name = "dataSourcesTreatmentAndRepresentativeness", parentName = "modellingAndValidation")
public class ILCDRepresentativeness extends ContextObject {

	/**
	 * Principles applied in data collection regarding completeness of (also
	 * intermediate) product and waste flows and of elementary flows. Examples
	 * are: cut-off rules, systematic exclusion of infrastructure, services or
	 * auxiliaries, etc. systematic exclusion of air in incineration processes,
	 * coling water, etc.
	 * 
	 * @Element dataCutOffAndCompletenessPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "dataCutOffAndCompletenessPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> cutOffPrinciples = new ArrayList<LangString>();

	/**
	 * Principles applied in data collection regarding completeness of (also
	 * intermediate) product and waste flows and of elementary flows. Examples
	 * are: cut-off rules, systematic exclusion of infrastructure, services or
	 * auxiliaries, etc. systematic exclusion of air in incineration processes,
	 * coling water, etc.
	 * 
	 * @Element dataCutOffAndCompletenessPrinciples
	 * @DataType FT
	 */
	public List<LangString> getCutOffPrinciples() {
		return cutOffPrinciples;
	}

	/**
	 * Short description of any deviations from the
	 * "Data completeness principles". In case of no (result relevant)
	 * deviations, "none" is entered.
	 * 
	 * @Element deviationsFromCutOffAndCompletenessPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromCutOffAndCompletenessPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromCutOffPrinciples = new ArrayList<LangString>();

	/**
	 * Short description of any deviations from the
	 * "Data completeness principles". In case of no (result relevant)
	 * deviations, "none" is entered.
	 * 
	 * @Element deviationsFromCutOffAndCompletenessPrinciples
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromCutOffPrinciples() {
		return deviationsFromCutOffPrinciples;
	}

	/**
	 * Principles applied in data selection and in combination of data from
	 * different sources. Includes brief discussion of consistency of data
	 * sources regarding data itself, modelling, appropriateness. In case of
	 * averaging: Principles and data selection applied in horizontal and / or
	 * vertical averaging.
	 * 
	 * @Element dataSelectionAndCombinationPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "dataSelectionAndCombinationPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> dataSelectionPrinciples = new ArrayList<LangString>();

	/**
	 * Principles applied in data selection and in combination of data from
	 * different sources. Includes brief discussion of consistency of data
	 * sources regarding data itself, modelling, appropriateness. In case of
	 * averaging: Principles and data selection applied in horizontal and / or
	 * vertical averaging.
	 * 
	 * @Element dataSelectionAndCombinationPrinciples
	 * @DataType FT
	 */
	public List<LangString> getDataSelectionPrinciples() {
		return dataSelectionPrinciples;
	}

	/**
	 * Short description of any deviations from the
	 * "Data selection and combination principles". In case of no (result
	 * relevant) deviations, "none" is entered.
	 * 
	 * @Element deviationsFromSelectionAndCombinationPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromSelectionAndCombinationPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromDataSelectionPrinciples = new ArrayList<LangString>();

	/**
	 * Short description of any deviations from the
	 * "Data selection and combination principles". In case of no (result
	 * relevant) deviations, "none" is entered.
	 * 
	 * @Element deviationsFromSelectionAndCombinationPrinciples
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromDataSelectionPrinciples() {
		return deviationsFromDataSelectionPrinciples;
	}

	/**
	 * Short description of any deviations from the
	 * " Data treatment and extrapolations principles". In case of no (result
	 * relevant) deviations, "none" is entered. (Note: If data representative
	 * for one "Location" is used for another "Location", its original
	 * representativity can be indicated here; see field "Percentage supply or
	 * production covered".)
	 * 
	 * @Element deviationsFromTreatmentAndExtrapolationPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "dataTreatmentAndExtrapolationsPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> extrapolationPrinciples = new ArrayList<LangString>();

	/**
	 * Short description of any deviations from the
	 * " Data treatment and extrapolations principles". In case of no (result
	 * relevant) deviations, "none" is entered. (Note: If data representative
	 * for one "Location" is used for another "Location", its original
	 * representativity can be indicated here; see field "Percentage supply or
	 * production covered".)
	 * 
	 * @Element deviationsFromTreatmentAndExtrapolationPrinciples
	 * @DataType FT
	 */
	public List<LangString> getExtrapolationPrinciples() {
		return extrapolationPrinciples;
	}

	/**
	 * Short description of any deviations from the
	 * " Data treatment and extrapolations principles". In case of no (result
	 * relevant) deviations, "none" is entered. (Note: If data representative
	 * for one "Location" is used for another "Location", its original
	 * representativity can be indicated here; see field "Percentage supply or
	 * production covered".)
	 * 
	 * @Element deviationsFromTreatmentAndExtrapolationPrinciples
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromTreatmentAndExtrapolationPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromExtrapolationPrinciples = new ArrayList<LangString>();

	/**
	 * Short description of any deviations from the
	 * " Data treatment and extrapolations principles". In case of no (result
	 * relevant) deviations, "none" is entered. (Note: If data representative
	 * for one "Location" is used for another "Location", its original
	 * representativity can be indicated here; see field "Percentage supply or
	 * production covered".)
	 * 
	 * @Element deviationsFromTreatmentAndExtrapolationPrinciples
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromExtrapolationPrinciples() {
		return deviationsFromExtrapolationPrinciples;
	}

	/**
	 * "Source data set"(s) of the source(s) in which the data completeness,
	 * selection, combination, treatment, and extrapolations principles' details
	 * are described
	 * 
	 * @Element referenceToDataHandlingPrinciples
	 */
	@ContextField(name = "referenceToDataHandlingPrinciples", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> dataHandlingPrinciples = new ArrayList<DataSetReference>();

	/**
	 * "Source data set"(s) of the source(s) in which the data completeness,
	 * selection, combination, treatment, and extrapolations principles' details
	 * are described
	 * 
	 * @Element referenceToDataHandlingPrinciples
	 */
	public List<DataSetReference> getDataHandlingPrinciples() {
		return dataHandlingPrinciples;
	}

	/**
	 * "Source data set"(s) of the source(s) used for deriving/compiling the
	 * inventory of this data set e.g. questionnaires, monographies, plant
	 * operation protocols, etc. For LCI results and Partly terminated systems
	 * the sources for relevant background system data are to be given, too. For
	 * parameterised data sets the sources used for the parameterisation /
	 * mathematical relations in the section "Mathematical model" are referenced
	 * here as well. [Note: If the data set stems from another database or data
	 * set publication and is only re-published: identify the origin of a
	 * converted data set in "Converted original data set from:" field in
	 * section "Data entry by" and its unchanged re-publication in
	 * "Unchanged re-publication of:" in the section
	 * "Publication and ownership". The data sources used to model a converted
	 * or re-published data set are nevertheless to be given here in this field,
	 * for transparency reasons.]
	 * 
	 * @Element referenceToDataSource
	 */
	@ContextField(name = "referenceToDataSource", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> dataSources = new ArrayList<DataSetReference>();

	/**
	 * "Source data set"(s) of the source(s) used for deriving/compiling the
	 * inventory of this data set e.g. questionnaires, monographies, plant
	 * operation protocols, etc. For LCI results and Partly terminated systems
	 * the sources for relevant background system data are to be given, too. For
	 * parameterised data sets the sources used for the parameterisation /
	 * mathematical relations in the section "Mathematical model" are referenced
	 * here as well. [Note: If the data set stems from another database or data
	 * set publication and is only re-published: identify the origin of a
	 * converted data set in "Converted original data set from:" field in
	 * section "Data entry by" and its unchanged re-publication in
	 * "Unchanged re-publication of:" in the section
	 * "Publication and ownership". The data sources used to model a converted
	 * or re-published data set are nevertheless to be given here in this field,
	 * for transparency reasons.]
	 * 
	 * @Element referenceToDataSource
	 */
	public List<DataSetReference> getDataSources() {
		return dataSources;
	}

	/**
	 * Percentage of the overall supply, consumption, or production of the
	 * specific good, service, or technology represented by this data set, in
	 * the region/market of the stated "Location". For multi- functional
	 * processes the market share of the specific technology is stated. If data
	 * that is representative for a process operated in one "Location" is used
	 * for another "Location", the entry is '0'. The representativity for the
	 * original "Location" is documented in the field "Deviation from data
	 * treatment and extrapolation principles, explanations".
	 * 
	 * @Element percentageSupplyOrProductionCovered
	 * @DataType Perc
	 */
	@ContextField(name = "percentageSupplyOrProductionCovered", parentName = "dataSourcesTreatmentAndRepresentativeness", type = Type.Double)
	public Double percent;

	/**
	 * Supply / consumption or production volume of the specific good, service,
	 * or technology in the region/ market of the stated "Location". The market
	 * volume is given in absolute numbers per year, in common units, for the
	 * stated "Reference year". For multi-fucntional processes the data should
	 * be given for all co-functions (good and services).
	 * 
	 * @Element annualSupplyOrProductionVolume
	 * @DataType String
	 */
	@ContextField(name = "annualSupplyOrProductionVolume", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> productionVolume = new ArrayList<LangString>();

	/**
	 * Supply / consumption or production volume of the specific good, service,
	 * or technology in the region/ market of the stated "Location". The market
	 * volume is given in absolute numbers per year, in common units, for the
	 * stated "Reference year". For multi-fucntional processes the data should
	 * be given for all co-functions (good and services).
	 * 
	 * @Element annualSupplyOrProductionVolume
	 * @DataType String
	 */
	public List<LangString> getProductionVolume() {
		return productionVolume;
	}

	/**
	 * Sampling procedure used for quantifying the amounts of Inputs and
	 * Outputs. Possible problems in combining different sampling procedures
	 * should be mentioned.
	 * 
	 * @Element samplingProcedure
	 * @DataType FT
	 */
	@ContextField(name = "samplingProcedure", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> samplingProcedure = new ArrayList<LangString>();

	/**
	 * Sampling procedure used for quantifying the amounts of Inputs and
	 * Outputs. Possible problems in combining different sampling procedures
	 * should be mentioned.
	 * 
	 * @Element samplingProcedure
	 * @DataType FT
	 */
	public List<LangString> getSamplingProcedure() {
		return samplingProcedure;
	}

	/**
	 * Date(s) or time period(s) when the data was collected. Note that this
	 * does NOT refer to e.g. the publication dates of papers or books from
	 * which the data may stem, but to the original data collection period.
	 * 
	 * @Element dataCollectionPeriod
	 * @DataType String
	 */
	@ContextField(name = "dataCollectionPeriod", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> dataCollectionPeriod = new ArrayList<LangString>();

	/**
	 * Date(s) or time period(s) when the data was collected. Note that this
	 * does NOT refer to e.g. the publication dates of papers or books from
	 * which the data may stem, but to the original data collection period.
	 * 
	 * @Element dataCollectionPeriod
	 * @DataType String
	 */
	public List<LangString> getDataCollectionPeriod() {
		return dataCollectionPeriod;
	}

	/**
	 * Description of methods, sources, and assumptions made in uncertainty
	 * adjustment. [Note: For data sets where the additional uncertainty due to
	 * lacking representativeness has been included in the quantified
	 * uncertainty values, this field also reports the original
	 * representativeness, the additional uncertainty, and the procedure by
	 * which the overall uncertainty was assessed or calculated.]
	 * 
	 * @Element uncertaintyAdjustments
	 * @DataType FT
	 */
	@ContextField(name = "uncertaintyAdjustments", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> uncertaintyAdjustments = new ArrayList<LangString>();

	/**
	 * Description of methods, sources, and assumptions made in uncertainty
	 * adjustment. [Note: For data sets where the additional uncertainty due to
	 * lacking representativeness has been included in the quantified
	 * uncertainty values, this field also reports the original
	 * representativeness, the additional uncertainty, and the procedure by
	 * which the overall uncertainty was assessed or calculated.]
	 * 
	 * @Element uncertaintyAdjustments
	 * @DataType FT
	 */
	public List<LangString> getUncertaintyAdjustments() {
		return uncertaintyAdjustments;
	}

	/**
	 * Specific methodological advice for data set users that requires
	 * attention. E.g. on inclusion/exclusion of recycling e.g. in material data
	 * sets, specific use phase behavior to be modelled, and other
	 * methodological advices. See also field "Technological applicability".
	 * 
	 * @Element useAdviceForDataSet
	 * @DataType FT
	 */
	@ContextField(name = "useAdviceForDataSet", parentName = "dataSourcesTreatmentAndRepresentativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> useAdviceForDataSet = new ArrayList<LangString>();

	/**
	 * Specific methodological advice for data set users that requires
	 * attention. E.g. on inclusion/exclusion of recycling e.g. in material data
	 * sets, specific use phase behavior to be modelled, and other
	 * methodological advices. See also field "Technological applicability".
	 * 
	 * @Element useAdviceForDataSet
	 * @DataType FT
	 */
	public List<LangString> getUseAdviceForDataSet() {
		return useAdviceForDataSet;
	}
}
