package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * #missing.# Parameter are minValue, mostLikelyValue, maxValue. In case of
 * triangular uncertainty distribution, the meanValue shall be calculated from
 * the mostLikelyValue. The field mostLikelyValue (#3797) shall not be used in
 * the ecoinvent quality network.
 * 
 * @Element triangular
 */
@Context(name = "triangular", parentName = "uncertainty")
public class ES2TriangularDistribution extends ContextObject {

	/**
	 * @Attribute mostLikelyValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "triangular", parentName = "uncertainty", isAttribute = true, attributeName = "mostLikelyValue", type = Type.Double)
	public Double mostLikelyValue;
	
	/**
	 * @Attribute minValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "triangular", parentName = "uncertainty", isAttribute = true, attributeName = "minValue", type = Type.Double)
	public Double minValue;
	
	/**
	 * @Attribute maxValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "triangular", parentName = "uncertainty", isAttribute = true, attributeName = "maxValue", type = Type.Double)
	public Double maxValue;
	
	
}
