package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * This "distribution" can be used to hold legacy data of the EcoSpold01 format
 * which reused the minValue, maxValue and standardDeviation95 fields to store
 * undefined distribution data.
 * 
 * @Element undefined
 */
@Context(name = "undefined", parentName = "uncertainty")
public class ES2UndefinedDistribution extends ContextObject {

	/**
	 * @Attribute standardDeviation95
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "undefined", parentName = "uncertainty", isAttribute = true, attributeName = "standardDeviation95", type = Type.Double)
	public Double standardDeviation95;

	/**
	 * @Attribute minValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "undefined", parentName = "uncertainty", isAttribute = true, attributeName = "minValue", type = Type.Double)
	public Double minValue;

	/**
	 * @Attribute maxValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "undefined", parentName = "uncertainty", isAttribute = true, attributeName = "maxValue", type = Type.Double)
	public Double maxValue;
}
