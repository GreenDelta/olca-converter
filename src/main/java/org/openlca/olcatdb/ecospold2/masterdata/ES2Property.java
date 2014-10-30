package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "property", parentName = "validProperties")
public class ES2Property extends ContextObject{

	@ContextField(name = "property", parentName = "validProperties", isAttribute = true, attributeName = "id", length = 36)
	public String id;

	@ContextField(name = "property", parentName = "validProperties", isAttribute = true, attributeName = "unitId", length = 36)
	public String unitId;

	@ContextField(name = "property", parentName = "validProperties", isAttribute = true, attributeName = "unitContextId", length = 36)
	public String unitContextId;

	@ContextField(name = "property", parentName = "validProperties", isAttribute = true, attributeName = "defaultVariableName", length = 40)
	public String defaultVariableName;

	@ContextField(name = "name", parentName = "property", isMultiple = true, length = 80, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "unitName", parentName = "property", isMultiple = true, length = 40, type = Type.MultiLangText)
	private List<LangString> unitName = new ArrayList<LangString>();

	public List<LangString> getUnitName() {
		return unitName;
	}

	@ContextField(name = "comment", parentName = "property", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}
}
