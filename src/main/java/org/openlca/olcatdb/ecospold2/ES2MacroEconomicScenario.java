package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2MacroEconomicScenarioList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * 
 * @Element macroEconomicScenario
 * @ContentModel (name+, generalComment*)
 */
@Context(name = "macroEconomicScenario", parentName = "activityDescription")
public class ES2MacroEconomicScenario extends ContextObject {

	@ContextField(name = "macroEconomicScenario", parentName = "activityDescription", isAttribute = true, attributeName = "macroEconomicScenarioId", isMaster = true, classGroup = ES2MacroEconomicScenarioList.class)
	public String scenarioId;

	@ContextField(name = "macroEconomicScenario", parentName = "activityDescription", isAttribute = true, attributeName = "macroEconomicScenarioContextId", isMaster = true)
	public String macroEconomicScenarioContextId;

	@ContextField(name = "name", parentName = "macroEconomicScenario", isMultiple = true, type = Type.MultiLangText, classGroup = ES2MacroEconomicScenarioList.class)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return this.name;
	}

	@ContextField(name = "comment", parentName = "macroEconomicScenario", isMultiple = true, type = Type.MultiLangText, classGroup = ES2MacroEconomicScenarioList.class)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return this.comment;
	}
}
