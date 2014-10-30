package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.SubContext;


@Context(name="CategorySystem")
public class ILCDFlowCategorization {

	@ContextField(
		name = "CategorySystem",
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