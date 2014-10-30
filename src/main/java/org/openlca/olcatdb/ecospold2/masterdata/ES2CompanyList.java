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
 * The EcoSpold 02 master data list for valid companies.
 * 
 * @author Imo Graf
 * 
 */
@Context(name = "validCompanies")
public class ES2CompanyList extends ContextObject{

	@ContextField(name = "validCompanies", attributeName = "contextId", isAttribute = true, isRequired = true, length = 36)
	public String contextId;

	@ContextField(name = "validCompanies", attributeName = "majorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int majorRelease = 1;

	@ContextField(name = "validCompanies", attributeName = "minorRelease", isAttribute = true, isRequired = true, type = Type.Integer)
	public int minorRelease = 0;

	@ContextField(name = "contextName", parentName = "validCompanies", isRequired = true, length = 80, isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contextNames = new ArrayList<LangString>();

	public List<LangString> getContextNames() {
		return contextNames;
	}

	@SubContext(contextClass = ES2Company.class, isMultiple = true)
	private List<ES2Company> companies = new ArrayList<ES2Company>();

	public List<ES2Company> getCompanies() {
		return companies;
	}
}
