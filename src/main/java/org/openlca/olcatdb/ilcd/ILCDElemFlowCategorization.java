package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * Identifying category/compartment information exclusively used for elementary
 * flows. E.g. "Emission to air", "Renewable resource", etc.
 * 
 * @Element elementaryFlowCategorization
 * @ContentModel (category+, other?)
 * 
 */
@Context(name = "elementaryFlowCategorization", parentName = "classificationInformation")
public class ILCDElemFlowCategorization extends ContextObject {

	/**
	 * URL or file name of a file containing all categories of this
	 * categorization system. [Note: The file is to be in form of the
	 * "ILCDCategories.xml" format. If a category file is specified, only
	 * categories of the referenced categories file should be used.]
	 * 
	 * @Attribute categories
	 * @DataType anyURI
	 */
	@ContextField(name = "elementaryFlowCategorization", attributeName = "categories", isAttribute = true, parentName = "classificationInformation")
	public String fileURI;

	/**
	 * Name of the categorization system. E.g. "ILCD 1.1" or another elementary
	 * flow categorization/compartment scheme applied, as defined e.g. in other
	 * LCA database (systems).
	 * 
	 * @Attribute name
	 * @DataType string
	 * @DefaultValue ILCD
	 */
	@ContextField(name = "elementaryFlowCategorization", attributeName = "name", isAttribute = true, parentName = "classificationInformation")
	public String name = "ILCD";

	/**
	 * Identifying category/compartment information exclusively used for
	 * elementary flows. E.g. "Emission to air", "Renewable resource", etc.
	 * 
	 * @Element elementaryFlowCategorization
	 * @ContentModel (category+, other?)
	 * 
	 */
	@SubContext(contextClass = ILCDElemFlowCategory.class, isMultiple = true)
	private List<ILCDElemFlowCategory> categories = new ArrayList<ILCDElemFlowCategory>();

	/**
	 * Identifying category/compartment information exclusively used for
	 * elementary flows. E.g. "Emission to air", "Renewable resource", etc.
	 * 
	 * @Element elementaryFlowCategorization
	 * @ContentModel (category+, other?)
	 * 
	 */
	public List<ILCDElemFlowCategory> getCategories() {
		return categories;
	}

	/**
	 * Returns a single string with the categories in this categorization
	 * separated by the given separator. The categories are sorted by their
	 * respective level.
	 */
	public String getPath(String separator) {
		String path = "";
		Collections.sort(categories);
		Iterator<ILCDElemFlowCategory> it = categories.iterator();
		while (it.hasNext()) {
			ILCDElemFlowCategory cat = it.next();
			path += cat.name;
			if(it.hasNext())
				path += separator;
		}
		return path;
	}
}
