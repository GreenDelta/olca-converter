package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * 
 * @author Michael Srocka
 *
 */
// TODO: The recursion of this class does not work
// in the XML context parser 
@Context(name="category", parentNonStrict=true)
public class ILCDFlowCategory {

	@ContextField(
		name = "category",
		parentNonStrict = true,
		isAttribute = true,
		attributeName = "id"
	)
	public String id;
	
	@ContextField(
		name = "category",
		parentNonStrict = true,
		isAttribute = true,
		attributeName = "name"
	)
	public String name;
	
	@SubContext(
		contextClass = ILCDFlowCategory.class,
		isMultiple = true
	)
	private List<ILCDFlowCategory> categories
		= new ArrayList<ILCDFlowCategory>();
	
	public List<ILCDFlowCategory> getCategories() {
		return categories;
	}
}