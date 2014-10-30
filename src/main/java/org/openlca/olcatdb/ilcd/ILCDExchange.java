package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Input/Output list of exchanges with the quantitative inventory data as well
 * as pre-calculated LCIA results.
 * 
 * @Element exchange
 * @ContentModel (referenceToFlowDataSet, location?, functionType?,
 *               exchangeDirection?, referenceToVariable?, meanAmount,
 *               resultingAmount?, minimumAmount?, maximumAmount?,
 *               uncertaintyDistributionType?, relativeStandardDeviation95In?,
 *               allocations?, dataSourceType?, dataDerivationTypeStatus?,
 *               referencesToDataSource?, generalComment*, other?)
 */
@Context(name = "exchange", parentName = "exchanges")
public class ILCDExchange extends ContextObject {

	/**
	 * Automated entry: internal ID, used in the "Quantitative reference"
	 * section to identify the "Reference flow(s)" in case the quantitative
	 * reference of this Process data set is of this type.
	 * 
	 * @Attribute dataSetInternalID
	 * @DataType Int6
	 */
	@ContextField(name = "exchange", parentName = "exchanges", attributeName = "dataSetInternalID", isAttribute = true, type = Type.Integer)
	public Integer id;

	/**
	 * "Flow data set" of this Input or Output.
	 * 
	 * @Element referenceToFlowDataSet
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToFlowDataSet", parentName = "exchange", type = Type.DataSetReference)
	public DataSetReference flowDataSet;

	/**
	 * Location, where the Input/Output flow takes place. In case of
	 * location-specific product/waste flows, entry should be automatically
	 * transferred from field "Location" in that "Flow data set"; otherwise
	 * empty (by default). In case of location-dependent LCA the entry for
	 * elementary flows in LCI result data sets should be automatically
	 * transferred from "Location" in section "Geography" of all included
	 * processes during LCI result aggregation; otherwise empty (by default and
	 * also for all exchanges in unit processes, unless these are horizontally
	 * averaged across different locations). [Note: Location information is used
	 * for DISPLAY and TRANSFER purposes only, i.e. typically no direct manual
	 * use of this field.]
	 * 
	 * @Element location
	 * @DataType String
	 */
	@ContextField(name = "location", parentName = "exchange")
	public String location;

	/**
	 * Names the functional type of the input or output, especially whether or
	 * not it is a reminder flow and if so of which type. Reminder flows
	 * represent products and wastes which have already been fully considered in
	 * the inventory; they are used exclusively for additional documentation
	 * purposes. For normal input and putput flows the field is empty.
	 * 
	 * @Element functionType
	 * @DataType ExchangeFunctionTypeValues
	 */
	@ContextField(name = "functionType", parentName = "exchange")
	public String functionType;

	/**
	 * Direction of Input or Output flow.
	 * 
	 * @Element exchangeDirection
	 * @DataType ExchangeDirectionValues
	 */
	@ContextField(name = "exchangeDirection", parentName = "exchange")
	public String direction;

	/**
	 * Data set internal reference to a variable or parameter name as defined in
	 * the section "Mathematical model". The value of this variable or parameter
	 * acts as linear multiplier to the value given in the field "Mean amount"
	 * to yield the "Resulting amount", which is the final value in the
	 * inventory.
	 * 
	 * @Element referenceToVariable
	 * @DataType string
	 */
	@ContextField(name = "referenceToVariable", parentName = "exchange")
	public String variableReference;

	/**
	 * Mean amount of the Input or Output. Only significant digits of the amount
	 * should be stated.
	 * 
	 * @Element meanAmount
	 * @DataType Real
	 */
	@ContextField(name = "meanAmount", parentName = "exchange", type = Type.Double)
	public Double meanAmount;

	/**
	 * Final value to be used for calculation of the LCI results and in the
	 * product system: It is calculated as the product of the "Mean amount"
	 * value times the value of the "Variable". In case that no "Variable" entry
	 * is given, the "Resulting amount" is identical to the "Mean amount", i.e.
	 * a factor "1" is applied.
	 * 
	 * @Element resultingAmount
	 * @DataType Real
	 */
	@ContextField(name = "resultingAmount", parentName = "exchange", type = Type.Double)
	public Double resultingAmount;

	/**
	 * Minimum amount of the Input or Output in case the uncertainty
	 * distribution is uniform or triangular. In case of calculated LCI results
	 * and for the aggregated flows in Partly terminated system data sets, the
	 * lower end of the 95% likelihood range, i.e. the "2.5% value" can be
	 * reported in this field.
	 * 
	 * @Element minimumAmount
	 * @DataType Real
	 */
	@ContextField(name = "minimumAmount", parentName = "exchange", type = Type.Double)
	public Double minimumAmount;

	/**
	 * Maximum amount of the Input or Output in case the uncertainty
	 * distribution is uniform or triangular. In case of calculated LCI results
	 * and for the aggregated flows in Partly terminated system data sets, the
	 * upper end of the 95% likelihood range, i.e. the "97.5% value" can be
	 * reported in this field.
	 * 
	 * @Element maximumAmount
	 * @DataType Real
	 */
	@ContextField(name = "maximumAmount", parentName = "exchange", type = Type.Double)
	public Double maximumAmount;

	/**
	 * Defines the kind of uncertainty distribution that is valid for this
	 * particular object or parameter.
	 * 
	 * @Element uncertaintyDistributionType
	 * @DataType UncertaintyDistributionTypeValues
	 */
	@ContextField(name = "uncertaintyDistributionType", parentName = "exchange")
	public String uncertaintyDistribution;

	/**
	 * The resulting overall uncertainty of the calculated variable value
	 * considering uncertainty of measurements, modelling, appropriateness etc.
	 * [Notes: For log-normal distribution the square of the geometric standard
	 * deviation (SDg^2) is stated. Mean value times SDg^2 equals the 97.5%
	 * value (= Maximum value), Mean value divided by SDg^2 equals the 2.5%
	 * value (= Minimum value). For normal distribution the doubled standard
	 * deviation value (2*SD) is entered. Mean value plus 2*SD equals 97.5%
	 * value (= Maximum value), Mean value minus 2*SD equals 2.5% value (=
	 * Minimum value). This data field remains empty when uniform or triangular
	 * uncertainty distribution is applied.]
	 * 
	 * @Element relativeStandardDeviation95In
	 * @DataType Perc
	 */
	@ContextField(name = "relativeStandardDeviation95In", parentName = "exchange", type = Type.Double)
	public Double relStdDeviation95In;

	@SubContext(contextClass = ILCDAllocationFactor.class, isMultiple = true)
	private List<ILCDAllocationFactor> allocationFactors = new ArrayList<ILCDAllocationFactor>();

	/**
	 * Container tag for the specification of allocations if process has more
	 * than one reference product. Use only for multifunctional processes.
	 * 
	 * @Element allocations
	 * @ContentModel (allocation+)
	 */
	public List<ILCDAllocationFactor> getAllocationFactors() {
		return allocationFactors;
	}

	/**
	 * Identifies the data source type of each single Input or Output as being
	 * "Primary", "Secondary", or "Mixed primary/secondary".
	 * 
	 * @Element dataSourceType
	 * @DataType DataSourceTypeValues
	 */
	@ContextField(name = "dataSourceType", parentName = "exchange")
	public String dataSourceType;

	/**
	 * Identifies the way by which the individual Input / Output amount was
	 * derived (e.g. measured, estimated etc.), respectively the status and
	 * relevance of missing data.
	 * 
	 * @Element dataDerivationTypeStatus
	 * @DataType DataDerivationTypeStatusValues
	 */
	@ContextField(name = "dataDerivationTypeStatus", parentName = "exchange")
	public String dataDerivationType;

	@ContextField(name = "referenceToDataSource", parentName = "referencesToDataSource", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> dataSources = new ArrayList<DataSetReference>();

	/**
	 * "Source data set" of data source(s) used for the value of this specific
	 * Input or Output, especially if differing from the general data source
	 * used for this data set.
	 * 
	 * @Element referencesToDataSource
	 * @ContentModel (referenceToDataSource*, other?)
	 */
	public List<DataSetReference> getDataSources() {
		return dataSources;
	}

	/**
	 * General comment on this specific Input or Output, e.g. commenting on the
	 * data sources used and their specific representatuveness etc., on the
	 * status of "finalisation" of an entry as workflow information, etc.
	 * 
	 * @Element generalComment
	 * @DataType String
	 */
	@ContextField(name = "generalComment", parentName = "exchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * General comment on this specific Input or Output, e.g. commenting on the
	 * data sources used and their specific representatuveness etc., on the
	 * status of "finalisation" of an entry as workflow information, etc.
	 * 
	 * @Element generalComment
	 * @DataType String
	 */
	public List<LangString> getComment() {
		return comment;
	}

	/**
	 * Returns true if this exchange is an input (direction = "Input"),
	 * otherwise <code>false</code>.
	 */
	public boolean isInput() {
		return this.direction != null && this.direction.equals("Input");
	}

	@Override
	public String toString() {
		return "ILCDExchange [flowDataSet=" + flowDataSet + ", id=" + id + "]";
	}
	
	
}
