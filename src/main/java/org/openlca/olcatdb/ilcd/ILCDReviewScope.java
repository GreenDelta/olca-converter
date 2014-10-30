package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;

@Context(name="scope", parentName ="review")
public class ILCDReviewScope extends ContextObject {

	@ContextField(
			name = "scope", 
			parentName = "review",
			attributeName = "name",
			isAttribute = true)
	public String name;
	
	@ContextField(
			name = "method", 
			parentName = "scope",
			attributeName = "name",
			isAttribute = true,
			isMultiple = true)
	private List<String> methods = new ArrayList<String>();
	
	public List<String> getMethods() {
		return methods;
	}
}
