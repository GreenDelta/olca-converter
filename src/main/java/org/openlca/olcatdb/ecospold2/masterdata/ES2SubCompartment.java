package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * A master data entry for a EcoSpold 02 sub-compartment.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="subcompartment", parentName="compartment")
public class ES2SubCompartment {

	@ContextField(
		name="subcompartment", 
		parentName="compartment",
		attributeName="id",
		isAttribute=true,
		isRequired=true,
		length=36
	)
	public String id;	
	
	@ContextField(
		name="name",
		parentName="subcompartment",
		isMultiple=true,
		isRequired=true,
		length=40,
		type=Type.MultiLangText
	)	
	private List<LangString> names
		= new ArrayList<LangString>();
	
	public List<LangString> getNames() {
		return names;
	}
	
	@ContextField(
		name="comment",
		parentName="subcompartment",
		isMultiple=true,
		type=Type.MultiLangText
	)	
	private List<LangString> comments
		= new ArrayList<LangString>();
	
	public List<LangString> getComments() {
		return comments;
	}

}
