package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The Erlang-distribution is a continuos distribution often used in queueing
 * theory. Mean parameter determines the average value, Order parameter is the
 * order of the Erlang distribution and must be >= 1.
 * 
 * @Element erlang
 */
@Context(name = "erlang", parentName = "uncertainty")
public class ES2ErlangDistribution extends ContextObject {

	/**
	 * @Attribute order
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "erlang", parentName = "uncertainty", isAttribute = true, attributeName = "order", type = Type.Double)
	public Double order;
	
	/**
	 * @Attribute meanValue
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "erlang", parentName = "uncertainty", isAttribute = true, attributeName = "meanValue", type = Type.Double)
	public Double meanValue;
}
