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
 * A flow property entry in a flow data set.
 * 
 * @Element flowProperty
 */
@Context(name = "flowProperty", parentName = "flowProperties")
public class ILCDFlowPropertyValue extends ContextObject {

	/**
	 * The data set internal ID.
	 * 
	 * @Attribute dataSetInternalID
	 */
	@ContextField(
			name = "flowProperty", 
			parentName = "flowProperties", 
			isAttribute = true, 
			attributeName = "dataSetInternalID", 
			type = Type.Integer)
	public Integer id;

	/**
	 * Reference to the flow property data set.
	 * 
	 * @Element referenceToFlowPropertyDataSet
	 */
	@ContextField(
			name = "referenceToFlowPropertyDataSet", 
			parentName = "flowProperty", 
			type = Type.DataSetReference)
	public DataSetReference flowPropertyDataSet;

	/**
	 * The mean value of the relation to the reference flow property.
	 * 
	 * @Element meanValue
	 */
	@ContextField(
			name = "meanValue", 
			parentName = "flowProperty", 
			type = Type.Double)
	public Double meanValue;

	/**
	 * Minimum value of this flow property in case uncertainty distribution is
	 * uniform or triangular.
	 * 
	 * @Element minimumValue
	 */
	@ContextField(
			name = "minimumValue", 
			parentName = "flowProperty", 
			type = Type.Double)
	public Double minimumValue;

	/**
	 * Maximum value of this flow property in case uncertainty distribution is
	 * uniform or triangular.
	 * 
	 * @Element maximumValue
	 */
	@ContextField(
			name = "maximumValue", 
			parentName = "flowProperty", 
			type = Type.Double)
	public Double maximumValue;

	/**
	 * Defines the kind of uncertainty distribution that is valid for this
	 * particular object or parameter.
	 * 
	 * @Element uncertaintyDistributionType
	 */
	@ContextField(
			name = "uncertaintyDistributionType", 
			parentName = "flowProperty")
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
	 */
	@ContextField(
			name = "relativeStandardDeviation95In", 
			parentName = "flowProperty", 
			type = Type.Double)
	public Double relStdDeviation95In;

	/**
	 * Identifies the way by which the Flow property value was derived (e.g.
	 * measured, estimated etc.), respectively the status and relevancy of
	 * missing data.
	 * 
	 * @Element dataDerivationTypeStatus
	 */
	@ContextField(
			name = "dataDerivationTypeStatus", 
			parentName = "flowProperty")
	public String dataDerivationType;

	
	@ContextField(
			name = "generalComment", 
			parentName = "flowProperty", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> comment 
		= new ArrayList<LangString>();

	/**
	 * General comment on each single flow property (if necessary) referring to
	 * specifc data sources used, or for workflow purposes about status of
	 * "finalisation" of an entry etc.
	 *
	 * @Element generalComment
	 */
	public List<LangString> getComment() {
		return comment;
	}
}
