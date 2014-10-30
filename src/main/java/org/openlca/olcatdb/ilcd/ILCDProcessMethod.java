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
@Context(name = "LCIMethodAndAllocation", parentName = "modellingAndValidation")
public class ILCDProcessMethod extends ContextObject{

	/**
	 * Type of the data set regarding systematic inclusion/exclusion of upstream
	 * or downstream processes, transparency and internal (hidden)
	 * multi-functionality, and the completeness of modelling.
	 * 
	 * @Element typeOfDataSet
	 * @DataType TypeOfProcessValues
	 */
	@ContextField(
			name = "typeOfDataSet", 
			parentName = "LCIMethodAndAllocation")
	public String dataSetType;

	/**
	 * LCI method principle followed in the product system modelling, i.e.
	 * regarding using average data (= attributional = non-marginal) or
	 * modelling effects in a change-oriented way (= consequential = marginal).
	 * 
	 * @Element LCIMethodPrinciple
	 * @DataType LCIMethodPrincipleValues
	 */
	@ContextField(
			name = "LCIMethodPrinciple", 
			parentName = "LCIMethodAndAllocation")
	public String lciMethodPrinciple;

	/**
	 * Short description of any deviations from the general
	 * "LCI method principles" and additional explanations. Refers especially to
	 * specific processes/cases where the stated "attributional" or
	 * "consequential" approach was not applied. Or where deviations were made
	 * from any specific rules for applying the
	 * "Consequential with attributional components" approach. A reference to
	 * the "Intended application" of the data collection can be made here, too.
	 * Allocated co-products may be reported here as well. In case of no
	 * (quantitatively relevant) deviations from the LCI method principle,
	 * "none" should be stated.
	 * 
	 * @Element deviationsFromLCIMethodPrinciple
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromLCIMethodPrinciple", parentName = "LCIMethodAndAllocation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromLCIMethodPrinciple = new ArrayList<LangString>();

	/**
	 * Short description of any deviations from the general
	 * "LCI method principles" and additional explanations. Refers especially to
	 * specific processes/cases where the stated "attributional" or
	 * "consequential" approach was not applied. Or where deviations were made
	 * from any specific rules for applying the
	 * "Consequential with attributional components" approach. A reference to
	 * the "Intended application" of the data collection can be made here, too.
	 * Allocated co-products may be reported here as well. In case of no
	 * (quantitatively relevant) deviations from the LCI method principle,
	 * "none" should be stated.
	 * 
	 * @Element deviationsFromLCIMethodPrinciple
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromLCIMethodPrinciple() {
		return deviationsFromLCIMethodPrinciple;
	}

	/**
	 * Names briefly the specific approach(es) used in LCI modeling, e.g.
	 * allocation, substitution etc. In case of LCI results and Partly
	 * terminated system data sets this also covers those applied in the
	 * included background system.
	 * 
	 * @Element LCIMethodApproaches
	 * @DataType LCIMethodApproachesValues
	 */
	@ContextField(name = "LCIMethodApproaches", parentName = "LCIMethodAndAllocation", isMultiple = true)
	private List<String> lciMethodApproaches = new ArrayList<String>();

	/**
	 * Names briefly the specific approach(es) used in LCI modeling, e.g.
	 * allocation, substitution etc. In case of LCI results and Partly
	 * terminated system data sets this also covers those applied in the
	 * included background system.
	 * 
	 * @Element LCIMethodApproaches
	 * @DataType LCIMethodApproachesValues
	 */
	public List<String> getLciMethodApproaches() {
		return lciMethodApproaches;
	}

	/**
	 * Description of relevant deviations from the applied approaches as well as
	 * of the relevant specific approaches that were applied, including in an
	 * possibly included background system. Further explanations and details of
	 * the allocation, substitution and other consequential approaches applied
	 * for relevant processes, e.g. how the marginal substitute was identified,
	 * year and region of which market prices were used in market allocation,
	 * i.e. method, procedure, data/information details. In case of no (result
	 * relevant) deviations from the before stated LCI method approaches, and in
	 * case of no need for further explanations, "none" is entered.
	 * 
	 * @Element deviationsFromLCIMethodApproaches
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromLCIMethodApproaches", parentName = "LCIMethodAndAllocation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromLCIMethodApproaches = new ArrayList<LangString>();

	/**
	 * Description of relevant deviations from the applied approaches as well as
	 * of the relevant specific approaches that were applied, including in an
	 * possibly included background system. Further explanations and details of
	 * the allocation, substitution and other consequential approaches applied
	 * for relevant processes, e.g. how the marginal substitute was identified,
	 * year and region of which market prices were used in market allocation,
	 * i.e. method, procedure, data/information details. In case of no (result
	 * relevant) deviations from the before stated LCI method approaches, and in
	 * case of no need for further explanations, "none" is entered.
	 * 
	 * @Element deviationsFromLCIMethodApproaches
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromLCIMethodApproaches() {
		return deviationsFromLCIMethodApproaches;
	}

	/**
	 * Short identification and description of constants applied in LCI
	 * modelling other than allocation / substitution, e.g. systematic setting
	 * of recycling quota, use of gross or net calorific value, etc.
	 * 
	 * @Element modellingConstants
	 * @DataType FT
	 */
	@ContextField(name = "modellingConstants", parentName = "LCIMethodAndAllocation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> modellingConstants = new ArrayList<LangString>();

	/**
	 * Short identification and description of constants applied in LCI
	 * modelling other than allocation / substitution, e.g. systematic setting
	 * of recycling quota, use of gross or net calorific value, etc.
	 * 
	 * @Element modellingConstants
	 * @DataType FT
	 */
	public List<LangString> getModellingConstants() {
		return modellingConstants;
	}

	/**
	 * Short description of data set specific deviations from the
	 * "Modelling constants" if any, including in the possibly included
	 * background system.
	 * 
	 * @Element deviationsFromModellingConstants
	 * @DataType FT
	 */
	@ContextField(name = "deviationsFromModellingConstants", parentName = "LCIMethodAndAllocation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> deviationsFromModellingConstants = new ArrayList<LangString>();

	/**
	 * Short description of data set specific deviations from the
	 * "Modelling constants" if any, including in the possibly included
	 * background system.
	 * 
	 * @Element deviationsFromModellingConstants
	 * @DataType FT
	 */
	public List<LangString> getDeviationsFromModellingConstants() {
		return deviationsFromModellingConstants;
	}

	/**
	 * "Source data set"(s) where the generally used LCA methods including the
	 * LCI method principles and specific approaches, the modelling constants
	 * details, as well as any other applied methodological conventions are
	 * described.
	 * 
	 * @Element referenceToLCAMethodDetails
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToLCAMethodDetails", parentName = "LCIMethodAndAllocation", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> methodDetails = new ArrayList<DataSetReference>();
	
	
	/**
	 * "Source data set"(s) where the generally used LCA methods including the
	 * LCI method principles and specific approaches, the modelling constants
	 * details, as well as any other applied methodological conventions are
	 * described.
	 * 
	 * @Element referenceToLCAMethodDetails
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getMethodDetails() {
		return methodDetails;
	}
}
