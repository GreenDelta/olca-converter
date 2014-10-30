package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * A master data entry for a MacroEconomicScenario.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "macroEconomicScenario", parentName = "validMacroEconomicScenarios")
public class ES2MacroEconomicScenario extends ContextObject {

	@ContextField(name = "macroEconomicScenario", parentName = "validCompanies", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "name", parentName = "macroEconomicScenario", isMultiple = true, isRequired = true, length = 80, type = Type.MultiLangText)
	private List<LangString> names = new ArrayList<LangString>();

	public List<LangString> getNames() {
		return names;
	}

	@ContextField(name = "comment", parentName = "macroEconomicScenario", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

}
