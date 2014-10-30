package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * Completeness of the elementary flows in the Inputs and Outputs section of
 * this data set from impact perspective, regarding addressing the individual
 * mid-point problem field / impact category given. The completeness refers to
 * the state-of-the-art of scientific knowledge whether or not an individual
 * elementary flow contributes to the respective mid-point topic in a relevant
 * way, which is e.g. the basis for the ILCD reference elementary flows. [Note:
 * The "Completeness" statement does not automatically mean that related LCIA
 * methods exist or reference the elementary flows of this data set. Hence for
 * direct applicability of existing LCIA methods, check the field
 * "Supported LCIA method data sets".]
 * 
 * 
 * @Element completenessElementaryFlows
 */
@Context(name = "completenessElementaryFlows", parentName = "completeness")
public class ILCDElemFlowCompleteness extends ContextObject {

	/**
	 * Impact category for which the completeness information is stated.
	 * 
	 * @Attribute type
	 * @DataType CompletenessTypeValues
	 */
	@ContextField(name = "completenessElementaryFlows", parentName = "completeness", isAttribute = true, attributeName = "type")
	public String type;

	/**
	 * Completeness value for the given impact category.
	 * 
	 * @Attribute value
	 * @DataType CompletenessValues
	 */
	@ContextField(name = "completenessElementaryFlows", parentName = "completeness", isAttribute = true, attributeName = "value")
	public String value;
}
