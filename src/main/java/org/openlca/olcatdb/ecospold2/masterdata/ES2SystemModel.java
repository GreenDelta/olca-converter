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
@Context(name = "systemModel", parentName = "validSystemModels")
public class ES2SystemModel extends ContextObject {

	@ContextField(name = "systemModel", parentName = "validSystemModels", isAttribute = true, attributeName = "id", length = 36)
	public String id;

	@ContextField(name = "name", parentName = "systemModel", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "shortname", parentName = "systemModel", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> shortname = new ArrayList<LangString>();

	public List<LangString> getShortname() {
		return shortname;
	}

	@ContextField(name = "comment", parentName = "systemModel", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}
}
