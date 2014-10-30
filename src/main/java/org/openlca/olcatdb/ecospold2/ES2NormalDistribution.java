package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Normal (also known as "Gaussian") distribution. It is a family of
 * distributions of the same general form, differing in their location and scale
 * parameters: the mean ("MeanValue") and standard deviation ("Deviation"),
 * respectively.
 * 
 * @Element normal
 * 
 */
@Context(name = "normal", parentName = "uncertainty")
public class ES2NormalDistribution extends ContextObject {

	/**
	 * @Attribute meanValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "normal", parentName = "uncertainty", isAttribute = true, attributeName = "meanValue", type = Type.Double)
	public Double meanValue;
	
	/**
	 * @Attribute standardDeviation95
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "normal", parentName = "uncertainty", isAttribute = true, attributeName = "standardDeviation95", type = Type.Double)
	public Double standardDeviation95;
	
	/**
	 * @Attribute variance
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "normal", parentName = "uncertainty", isAttribute = true, attributeName = "variance", type = Type.Double)
	public Double variance;
	
}
