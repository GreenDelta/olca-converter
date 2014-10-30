package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * The EcoSpold 2 master data file for valid context.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validContext")
public class ES2Context extends ContextObject {

	@ContextField(name = "validContext", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 0;

	@ContextField(name = "validContext", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "validContext", attributeName = "majorRevision", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRevision = 0;

	@ContextField(name = "validContext", attributeName = "miniorRevision", isAttribute = true, isRequired = true, type = Type.Integer)
	public int miniorRevision = 0;

	@ContextField(name = "validContext", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "validContext", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "name", parentName = "validContext", isMultiple = true, isRequired = true, length = 40, type = Type.MultiLangText)
	private List<LangString> names = new ArrayList<LangString>();

	public List<LangString> getNames() {
		return names;
	}

	@SubContext(contextClass = ES2RequiredContext.class, isMultiple = true)
	private List<ES2RequiredContext> requiredContexts = new ArrayList<ES2RequiredContext>();

	public List<ES2RequiredContext> getRequiredContexts() {
		return requiredContexts;
	}

	@ContextField(name = "comment", parentName = "validContext", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

}
