package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Uniform distribution of values between the minValue and the maxValue
 * parameter. If the maxValue parameter is smaller than the minValue parameter
 * their values will be swapped.
 * 
 * @Element uniform
 * 
 */
@Context(name = "uniform", parentName = "uncertainty")
public class ES2UniformDistribution extends ContextObject {

	/**
	 * @Attribute minValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "uniform", parentName = "uncertainty", isAttribute = true, attributeName = "minValue", type = Type.Double)
	public Double minValue;

	/**
	 * @Attribute maxValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "uniform", parentName = "uncertainty", isAttribute = true, attributeName = "maxValue", type = Type.Double)
	public Double maxValue;
	
	/**
	 * @Attribute mostFrequentValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "uniform", parentName = "uncertainty", isAttribute = true, attributeName = "mostFrequentValue", type = Type.Double)
	public Double mostFrequentValue;


	
	
	
}
