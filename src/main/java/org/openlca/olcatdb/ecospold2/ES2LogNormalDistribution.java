package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The Lognormal-distribution with average value µ (MeanValue parameter) and
 * variance s (Variance parameter) is a Normal-distribution, shaping the natural
 * logarithm of the characteristic values ln(x) instead of x-values.
 * 
 * @Element lognormal
 */
@Context(name = "lognormal", parentName = "uncertainty")
public class ES2LogNormalDistribution extends ContextObject {

	/**
	 * @Attribute meanValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "lognormal", parentName = "uncertainty", isAttribute = true, attributeName = "meanValue", type = Type.Double)
	public Double meanValue;

	/**
	 * @Attribute standardDeviation95
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "lognormal", parentName = "uncertainty", isAttribute = true, attributeName = "standardDeviation95", type = Type.Double)
	public Double standardDeviation95;

	/**
	 * @Attribute mu
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "lognormal", parentName = "uncertainty", isAttribute = true, attributeName = "mu", type = Type.Double)
	public Double mu;

	/**
	 * @Attribute variance
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "lognormal", parentName = "uncertainty", isAttribute = true, attributeName = "variance", type = Type.Double)
	public Double variance;
	
	/**
	 * @Attribute varianceWithPedigreeUncertainty
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "lognormal", parentName = "uncertainty", isAttribute = true, attributeName = "varianceWithPedigreeUncertainty", type = Type.Double)
	public Double varianceWithPedigreeUncertainty;

}
