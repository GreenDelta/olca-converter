package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Beta distribution using minValue (a), maxValue (b) and mostFrequent (m)
 * parameters to calculate the two shape parameters of the underlying Gamma
 * distributions.'+' The parameters must follow this condition: ((a <= m) and (m
 * <= b)) or (a = b). the shape values will be calculated by these formulas:
 * shape1 = 1 + 4 * ((m-a) / (b-a)). shape2 = 6 - shape1.
 * 
 * @Element beta
 */
@Context(name = "beta", parentName = "uncertainty")
public class ES2BetaDistribution extends ContextObject {

	/**
	 * @Attribute minValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "beta", parentName = "uncertainty", attributeName = "minValue", isAttribute = true, type = Type.Double)
	public Double minValue;
	
	/**
	 * @Attribute maxValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "beta", parentName = "uncertainty", attributeName = "maxValue", isAttribute = true, type = Type.Double)
	public Double maxValue;
	
	/**
	 * @Attribute mostFrequent
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "beta", parentName = "uncertainty", attributeName = "mostFrequent", isAttribute = true, type = Type.Double)
	public Double mostFrequent;
}
