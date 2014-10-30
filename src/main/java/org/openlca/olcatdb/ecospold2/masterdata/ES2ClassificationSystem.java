package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * A EcoSpold 02 master data entry for a classification system.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "classificationSystem", parentName = "validClassificationSystems")
public class ES2ClassificationSystem extends ContextObject {

	@ContextField(name = "classificationSystem", parentName = "validClassificationSystems", isAttribute = true, attributeName = "type", isRequired = true, type = Type.Integer)
	public int type = 1; // default

	@ContextField(name = "classificationSystem", parentName = "validClassificationSystems", isAttribute = true, attributeName = "id", isRequired = true)
	public String id;

	@ContextField(name = "name", parentName = "classificationSystem", isMultiple = true, isRequired = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@SubContext(contextClass = ES2Classification.class, isMultiple = true)
	private List<ES2Classification> classifications = new ArrayList<ES2Classification>();

	public List<ES2Classification> getClassifications() {
		return classifications;
	}

	/**
	 * Returns the first classification with the given name, or
	 * <code>null</code> if no such classification is in this system.
	 */
	public ES2Classification forName(String name) {
		ES2Classification clazz = null;
		for (ES2Classification c : classifications) {
			if (LangString.contains(c.getName(), name)) {
				clazz = c;
				break;
			}
		}
		return clazz;
	}
}
