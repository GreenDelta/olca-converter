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
 * The EcoSpold 02 master data list for valid MacroEconomicScenarios.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validParameters")
public class ES2ParameterList extends ContextObject {

	@ContextField(name = "validParameters", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validParameters", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1; // default

	@ContextField(name = "validParameters", attributeName = "majorRevision", isAttribute = true, type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "validParameters", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0; // default

	@ContextField(name = "validParameters", attributeName = "minorRevision", isAttribute = true, type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "validParameters", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validParameters", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2ParameterRequiredContext.class, isMultiple = true)
	private List<ES2ParameterRequiredContext> requiredContexts = new ArrayList<ES2ParameterRequiredContext>();

	public List<ES2ParameterRequiredContext> getRequiredContexts() {
		return requiredContexts;
	}

	@SubContext(contextClass = ES2Parameter.class, isMultiple = true)
	private List<ES2Parameter> parameter = new ArrayList<ES2Parameter>();

	public List<ES2Parameter> getParameter() {
		return parameter;
	}
}
