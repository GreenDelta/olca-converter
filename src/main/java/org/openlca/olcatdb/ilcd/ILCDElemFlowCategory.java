package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Identifying category/compartment information exclusively used for elementary
 * flows. E.g. "Emission to air", "Renewable resource", etc.
 * 
 * @Element elementaryFlowCategorization
 * @ContentModel (category+, other?)
 * 
 */
@Context(
		name = "category", 
		parentName = "elementaryFlowCategorization")
public class ILCDElemFlowCategory extends ContextObject implements Comparable<ILCDElemFlowCategory> {

	/**
	 * Unique identifier of the category. [Note: May be used by LCA software for
	 * it's category system. If used the identifer should be identical to the on
	 * defined in the referenced category file. Identifiers can be UUIDs, but
	 * also other forms are possible.]
	 * 
	 * @Attribute catId
	 * @DataType string
	 */
	@ContextField(
			name = "category", 
			parentName = "elementaryFlowCategorization", 
			attributeName = "catId", 
			isAttribute = true, 
			type = Type.Text)
	public String id;

	/**
	 * Hierarchy level (1,2,...), if the categorization system is hierachical,
	 * otherwise emtpy or not used.
	 * 
	 * @Attribute level
	 * @DataType LevelType
	 */
	@ContextField(
			name = "category", 
			parentName = "elementaryFlowCategorization", 
			attributeName = "level", 
			isAttribute = true, 
			type = Type.Integer)
	public int level;

	/**
	 * Name of the category of this elementary flow.
	 * 
	 * @Element category
	 * @DataType String
	 */
	@ContextField(
			name = "category", 
			parentName = "elementaryFlowCategorization", 
			isAttribute = false, 
			type = Type.Text)
	public String name;
	
	@Override
	public int compareTo(ILCDElemFlowCategory o) {
		int c = 0;
		if(o != null) {
			c = level - o.level;
		}		
		return c;
	}
}
