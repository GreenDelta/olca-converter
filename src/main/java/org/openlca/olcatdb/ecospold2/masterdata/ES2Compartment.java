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
 * A master data entry for a compartment.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "compartment", parentName = "validCompartments")
public class ES2Compartment extends ContextObject {

	@ContextField(name = "compartment", parentName = "validCompartments", attributeName = "id", isAttribute = true, isRequired = true, length = 36)
	public String id;

	@ContextField(name = "name", parentName = "compartment", isMultiple = true, isRequired = true, length = 40, type = Type.MultiLangText)
	private List<LangString> names = new ArrayList<LangString>();

	public List<LangString> getNames() {
		return names;
	}

	@ContextField(name = "comment", parentName = "compartment", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comments = new ArrayList<LangString>();

	public List<LangString> getComments() {
		return comments;
	}

	@SubContext(contextClass = ES2SubCompartment.class, isMultiple = true)
	private List<ES2SubCompartment> subCompartments = new ArrayList<ES2SubCompartment>();

	public List<ES2SubCompartment> getSubCompartments() {
		return subCompartments;
	}

}
