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
 * The EcoSpold 2 master data file for valid languages.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validLanguages")
public class ES2LanguageList extends ContextObject {

	@ContextField(name = "validLanguages", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 0;

	@ContextField(name = "validLanguages", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "validLanguages", attributeName = "majorRevision", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRevision = 0;

	@ContextField(name = "validLanguages", attributeName = "miniorRevision", isAttribute = true, isRequired = true, type = Type.Integer)
	public int miniorRevision = 0;

	@ContextField(name = "validLanguages", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "validLanguages", attributeName = "originalSource", isAttribute = true)
	public String originalSource;

	@ContextField(name = "contextName", parentName = "validLanguages", isMultiple = true, isRequired = true, length = 80, type = Type.MultiLangText)
	private List<LangString> contextName = new ArrayList<LangString>();

	public List<LangString> getContextName() {
		return contextName;
	}

	@SubContext(contextClass = ES2Language.class, isMultiple = true)
	private List<LangString> language = new ArrayList<LangString>();

	public List<LangString> getLanguage() {
		return language;
	}

}
