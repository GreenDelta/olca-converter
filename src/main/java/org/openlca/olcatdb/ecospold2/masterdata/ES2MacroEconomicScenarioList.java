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
@Context(name = "validMacroEconomicScenarios")
public class ES2MacroEconomicScenarioList extends ContextObject{

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1; // default

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "majorRevision", isAttribute = true, type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0; // default

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "minorRevision", isAttribute = true, type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "validMacroEconomicScenarios", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validMacroEconomicScenarios", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2MacroEconomicScenario.class, isMultiple = true)
	private List<ES2MacroEconomicScenario> macroEconomicScenarios = new ArrayList<ES2MacroEconomicScenario>();

	public List<ES2MacroEconomicScenario> getMacroEconomicScenarios() {
		return macroEconomicScenarios;
	}
}
