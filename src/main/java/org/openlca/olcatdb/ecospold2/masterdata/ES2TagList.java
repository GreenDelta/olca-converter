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
 * The EcoSpold 02 master data tag list.
 * 
 * @author Michael Srocka
 */
@Context(name = "validTags")
public class ES2TagList extends ContextObject {

	@ContextField(name = "validTags", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validTags", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1;

	@ContextField(name = "validUnits", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "contextName", parentName = "validUnits", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2Tag.class, isMultiple = true)
	private List<ES2Tag> tags = new ArrayList<ES2Tag>();

	public List<ES2Tag> getTags() {
		return tags;
	}

	public boolean containsName(String name) {
		boolean result = false;

		for (int i = 0; i < getTags().size() && !result; i++) {
			if (getTags().get(i).name.equals(name))
				result = true;
		}

		return result;
	}
}
