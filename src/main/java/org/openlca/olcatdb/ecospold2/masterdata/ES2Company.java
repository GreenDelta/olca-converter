package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * A master data entry for a company.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "company", parentName = "validCompanies")
public class ES2Company extends ContextObject {

	@ContextField(name = "company", parentName = "validCompanies", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "company", parentName = "validCompanies", attributeName = "companyCode", isAttribute = true, isRequired = true, length = 7)
	public String companyCode;

	@ContextField(name = "company", parentName = "validCompanies", attributeName = "website", isAttribute = true, isRequired = true, length = 255)
	public String website;

	@ContextField(name = "name", parentName = "company", isMultiple = true, isRequired = true, length = 40, type = Type.MultiLangText)
	private List<LangString> names = new ArrayList<LangString>();

	public List<LangString> getNames() {
		return names;
	}

	@ContextField(name = "comment", parentName = "company", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

}
