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
 * The EcoSpold 02 master data list for valid system models.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validSystemModels")
public class ES2SystemModelList extends ContextObject {

	@ContextField(name = "validSystemModels", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validSystemModels", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1; // default

	@ContextField(name = "validSystemModels", attributeName = "majorRevision", isAttribute = true, type = Type.Integer)
	public Integer majorRevision;

	@ContextField(name = "validSystemModels", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0; // default

	@ContextField(name = "validSystemModels", attributeName = "minorRevision", isAttribute = true, type = Type.Integer)
	public Integer minorRevision;

	@ContextField(name = "validSystemModels", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validSystemModels", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2SystemModel.class, isMultiple = true)
	private List<ES2SystemModel> systemModels = new ArrayList<ES2SystemModel>();

	public List<ES2SystemModel> getSystemModels() {
		return systemModels;
	}
}
