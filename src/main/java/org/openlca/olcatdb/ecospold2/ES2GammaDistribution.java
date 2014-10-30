package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Gamma distribution using scale and shape parameter. Absolute values of the
 * values entered here will be used. The value of the minimum parameter will be
 * added to all samples.
 * 
 * @Element gamma
 */
@Context(name = "gamma", parentName = "uncertainty")
public class ES2GammaDistribution extends ContextObject {

	/**
	 * @Attribute shape
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "gamma", parentName = "uncertainty", isAttribute = true, attributeName = "shape", type = Type.Double)
	public Double shape;
	
	/**
	 * @Attribute minValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "gamma", parentName = "uncertainty", isAttribute = true, attributeName = "minValue", type = Type.Double)
	public Double minValue;
	
	/**
	 * @Attribute scale
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "gamma", parentName = "uncertainty", isAttribute = true, attributeName = "scale", type = Type.Double)
	public Double scale;
	
}
