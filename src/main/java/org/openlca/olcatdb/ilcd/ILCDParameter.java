package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Name of variable or parameter used as scaling factors for the "Mean amount"
 * of individual inputs or outputs of the data set.
 * 
 * @Element variableParameter
 * @ContentModel (formula?, meanValue?, minimumValue?, maximumValue?,
 *               uncertaintyDistributionType?, relativeStandardDeviation95In?,
 *               comment*, other?)
 */
@Context(name = "variableParameter", parentName = "mathematicalRelations")
public class ILCDParameter extends ContextObject {

	/**
	 * Name of variable or parameter used as scaling factors for the
	 * "Mean amount" of individual inputs or outputs of the data set.
	 * 
	 * @Attribute name
	 * @DataType MatV
	 */
	@ContextField(
			name = "variableParameter", 
			parentName = "mathematicalRelations", 
			attributeName = "name", 
			isAttribute = true)
	public String name;

	/**
	 * Mathematical expression that determines the value of a variable. [Note: A
	 * parameter is defined by entering the value manually into the field
	 * "Mean value" and this field can be left empty.]
	 * 
	 * @Element formula
	 * @DataType MatR
	 */
	@ContextField(
			name = "formula", 
			parentName = "variableParameter")
	public String formula;

	/**
	 * Parameter value entered by user OR in case a formula is given in the
	 * "Formula" field, the result of the formula for the variable is displayed
	 * here.
	 * 
	 * @Element meanValue
	 * @DataType Real
	 */
	@ContextField(
			name = "meanValue", 
			parentName = "variableParameter", 
			type = Type.Double)
	public Double meanValue;

	/**
	 * Minimum value permissible for this parameter. For variables this field is
	 * empty.
	 * 
	 * @Element minimumValue
	 * @DataType Real
	 */
	@ContextField(
			name = "minimumValue", 
			parentName = "variableParameter", 
			type = Type.Double)
	public Double minimumValue;

	/**
	 * Maximum value permissible for this parameter. For variables this field is
	 * empty.
	 * 
	 * @Element maximumValue
	 * @DataType Real
	 */
	@ContextField(
			name = "maximumValue", 
			parentName = "variableParameter", 
			type = Type.Double)
	public Double maximumValue;

	/**
	 * Defines the kind of uncertainty distribution that is valid for this
	 * particular object or parameter.
	 * 
	 * @Element uncertaintyDistributionType
	 * @DataType UncertaintyDistributionTypeValues
	 */
	@ContextField(
			name = "uncertaintyDistributionType", 
			parentName = "variableParameter")
	public String uncertaintyType;

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
	@ContextField(name = "relativeStandardDeviation95In", parentName = "variableParameter", type = Type.Double)
	public Double relStdDeviation95In;

	/**
	 * Comment or description of variable or parameter. Typically including its
	 * unit and default values, e.g. in the pattern .
	 * 
	 * @Element comment
	 * @DataType String
	 */
	@ContextField(name = "comment", parentName = "variableParameter", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * Comment or description of variable or parameter. Typically including its
	 * unit and default values, e.g. in the pattern .
	 * 
	 * @Element comment
	 * @DataType String
	 */
	public List<LangString> getComment() {
		return comment;
	}
}
