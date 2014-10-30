package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2Description;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * The EcoSpold 02 master-data entry for elementary flows.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "elementaryExchange", parentName = "validElementaryExchanges")
public class ES2ElemFlow extends ContextObject {

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "unitId", isAttribute = true, length = 36)
	public String unitId;

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "unitContextId", isAttribute = true, length = 36)
	public String unitContextId;

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "formula", isAttribute = true, length = 40)
	public String formula;

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "casNumber", isAttribute = true, length = 11)
	public String casNumber;

	@ContextField(name = "elementaryExchange", parentName = "validElementaryExchanges", attributeName = "defaultVariableName", isAttribute = true, length = 40)
	public String defaultVariableName;

	@ContextField(name = "name", parentName = "elementaryExchange", isMultiple = true, isRequired = true, length = 120, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "unitName", parentName = "elementaryExchange", isMultiple = true, length = 40, type = Type.MultiLangText)
	private List<LangString> unitName = new ArrayList<LangString>();

	public List<LangString> getUnitName() {
		return unitName;
	}

	@ContextField(name = "compartment", parentName = "elementaryExchange", attributeName = "subcompartmentContextId", isAttribute = true, isRequired = false, length = 36)
	public String subCompartmentContextId;

	@ContextField(name = "compartment", parentName = "elementaryExchange", attributeName = "subcompartmentId", isAttribute = true, isRequired = true, length = 36)
	public String subCompartmentId;

	@ContextField(name = "compartment", parentName = "compartment", isMultiple = true, length = 40, type = Type.MultiLangText)
	private List<LangString> compartment = new ArrayList<LangString>();

	public List<LangString> getCompartment() {
		return compartment;
	}

	@ContextField(name = "subcompartment", parentName = "compartment", isMultiple = true, length = 40, type = Type.MultiLangText)
	private List<LangString> subCompartment = new ArrayList<LangString>();

	public List<LangString> getSubCompartment() {
		return subCompartment;
	}

	@ContextField(name = "comment", parentName = "elementaryExchange", isMultiple = true, length = 32000, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

	@ContextField(name = "synonym", parentName = "elementaryExchange", isMultiple = true, length = 80, type = Type.MultiLangText)
	private List<LangString> synonyms = new ArrayList<LangString>();

	public List<LangString> getSynonyms() {
		return synonyms;
	}

}
