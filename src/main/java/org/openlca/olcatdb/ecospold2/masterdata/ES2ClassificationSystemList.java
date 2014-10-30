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
 * The master data list with the classification systems.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "validClassificationSystems")
public class ES2ClassificationSystemList extends ContextObject {

	@ContextField(name = "validClassificationSystems", attributeName = "majorRelease", isAttribute = true, type = Type.Integer, isRequired = true)
	public int majorRelease = 1;

	@ContextField(name = "validClassificationSystems", attributeName = "minorRelease", isAttribute = true, type = Type.Integer, isRequired = true)
	public int minorRelease = 0;

	@ContextField(name = "validClassificationSystems", attributeName = "contextId", isAttribute = true, type = Type.Text, isRequired = true)
	public String contextId;

	@ContextField(name = "contextName", parentName = "validClassificationSystems", isMultiple = true, isRequired = true, length = 80, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2ClassificationSystem.class, isMultiple = true)
	private List<ES2ClassificationSystem> systems = new ArrayList<ES2ClassificationSystem>();

	public List<ES2ClassificationSystem> getSystems() {
		return systems;
	}

	/**
	 * As there is often only one classification system in the list, this is a
	 * helpful method to get the first entry.
	 */
	public ES2ClassificationSystem first() {
		ES2ClassificationSystem system = null;
		if(!systems.isEmpty()) {
			system = systems.get(0);
		}
		return system;
	}

}
