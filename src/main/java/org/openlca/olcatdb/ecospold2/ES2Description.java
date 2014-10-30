package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.ecospold2.masterdata.ES2Context;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Comprises information which identifies and characterises one particular
 * dataset (=unit process or system terminated).
 * 
 * @Element activityDescription
 * @ContentModel (activityName+, synonym*, includedActivitiesStart*,
 *               includedActivitiesEnd*, allocationComment?, generalComment?,
 *               tag*, namespace:uri="##other")
 */
@Context(name = "activity", parentName = "activityDescription")
public class ES2Description extends ContextObject {

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "id", isAttribute = true)
	public String id;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "activityNameId", isAttribute = true)
	public String activityNameId;

	@ContextField(name = "allocationComment", parentName = "activity", type = Type.TextAndImage)
	public TextAndImage allocationComment;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "parentActivityId", isAttribute = true)
	public String parentActivityId;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "inheritanceDepth", isAttribute = true, type = Type.Integer)
	public Integer inheritanceDepth;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "type", isAttribute = true, type = Type.Integer)
	public int type;

	@ContextField(name = "activity", attributeName = "specialActivityType", isAttribute = true, parentName = "activityDescription", type = Type.Integer)
	public int specialActivityType;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "energyValues", isAttribute = true, type = Type.Integer)
	public int energyValues;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "masterAllocationPropertyContextId", isAttribute = true, classGroup = ES2Context.class)
	public String masterAllocationPropertyContextId;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "masterAllocationPropertyId", isAttribute = true, isMaster = true, classGroup = ES2Context.class, overwrittenByChild = "masterAllocationPropertyIdOverwrittenByChild")
	public String masterAllocationPropertyId;

	@ContextField(name = "activity", parentName = "activityDescription", isAttribute = true, attributeName = "masterAllocationPropertyIdOverwrittenByChild", type = Type.Boolean)
	public boolean masterAllocationPropertyIdOverwrittenByChild;

	@ContextField(name = "activity", parentName = "activityDescription", attributeName = "datasetIcon", isAttribute = true)
	public String datasetIcon;

	@ContextField(name = "activityName", parentName = "activity", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * 
	 * @param setDefault
	 */
	public ES2Description() {

	}

	public ES2Description(boolean setDefaults) {
		energyValues = 0;
		specialActivityType = 0;
		type = 1;
	}

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "synonym", parentName = "activity", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> synonyms = new ArrayList<LangString>();

	public List<LangString> getSynonyms() {
		return synonyms;
	}

	@ContextField(name = "includedActivitiesStart", parentName = "activity", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> includedActivitiesStart = new ArrayList<LangString>();

	public List<LangString> getIncludedActivitiesStart() {
		return includedActivitiesStart;
	}

	@ContextField(name = "includedActivitiesEnd", parentName = "activity", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> includedActivitiesEnd = new ArrayList<LangString>();

	public List<LangString> getIncludedActivitiesEnd() {
		return includedActivitiesEnd;
	}

	@ContextField(name = "generalComment", parentName = "activity", isMultiple = false, type = Type.TextAndImage)
	public TextAndImage generalComment;

	@ContextField(name = "tag", parentName = "activity", isMultiple = true)
	private List<String> tags = new ArrayList<String>();

	public List<String> getTags() {
		return tags;
	}
}
