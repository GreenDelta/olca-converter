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
 * Optional statistical or other classification of the data set. Typically also
 * used for structuring LCA databases.
 * 
 * @Element classification
 * @ContentModel (class+, other?)
 */
@Context(name = "classification", parentName = "classificationInformation")
public class ILCDClassification extends ContextObject {

	/**
	 * URL or file name of a file listing all classes of this classification
	 * system. [Notes: the referenced file has to be in form of the
	 * "ILCDClassification.xml" format. If a classification file is specified,
	 * the "class" entry should correspond to the classes defined in the
	 * classification file.]
	 * 
	 * @Attribute classes
	 * @DataType anyURI
	 */
	@ContextField(name = "classification", attributeName = "classes", isAttribute = true, parentName = "classificationInformation")
	public String fileURI;

	/**
	 * Name of the classification system.
	 * 
	 * @Attribute name
	 * @DataType string
	 * @DefaultValue ILCD
	 */
	@ContextField(name = "classification", attributeName = "name", isAttribute = true, parentName = "classificationInformation")
	public String name = "ILCD";

	/**
	 * Name of the class.
	 * 
	 * @Element class
	 * @DataType string
	 */
	@SubContext(contextClass = ILCDClass.class, isMultiple = true)
	private List<ILCDClass> classes = new ArrayList<ILCDClass>();

	/**
	 * Name of the class.
	 * 
	 * @Element class
	 * @DataType string
	 */
	public List<ILCDClass> getClasses() {
		return classes;
	}

	/**
	 * Returns a single string with the classes in this classification
	 * separated by the given separator. The classes are sorted by their
	 * respective level.
	 */
	public String getPath(String separator) {
		String path = "";
		Collections.sort(classes);
		Iterator<ILCDClass> it = classes.iterator();
		while (it.hasNext()) {
			ILCDClass cat = it.next();
			path += cat.name;
			if(it.hasNext())
				path += separator;
		}
		return path;
	}
	
}
