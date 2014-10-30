package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2Property;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold 02 master data list for valid properties.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validProperties")
public class ES2PropertyList extends ContextObject {

	@ContextField(name = "validProperties", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validProperties", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1; // default

	@ContextField(name = "validProperties", attributeName = "majorRevision", isAttribute = true, type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "validProperties", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0; // default

	@ContextField(name = "validProperties", attributeName = "minorRevision", isAttribute = true, type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "validProperties", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validProperties", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2PropertyRequiredContext.class, isMultiple = true)
	private List<ES2PropertyRequiredContext> requiredContexts = new ArrayList<ES2PropertyRequiredContext>();

	public List<ES2PropertyRequiredContext> getRequiredContexts() {
		return requiredContexts;
	}

	@SubContext(contextClass = ES2Property.class, isMultiple = true)
	private List<ES2Property> properties = new ArrayList<ES2Property>();

	public List<ES2Property> getProperties() {
		return properties;
	}
}
