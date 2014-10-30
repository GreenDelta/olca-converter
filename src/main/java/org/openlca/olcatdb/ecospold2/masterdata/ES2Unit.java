package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold 02 master-data entry for valid units.
 * 
 * @author Michael Srocka
 */
@Context(name="unit", parentName="validUnits")
public class ES2Unit extends ContextObject {

	@ContextField(
		name="unit", 
		parentName="validUnits",
		attributeName="id",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String id;
	
	@ContextField(
		name="name",
		parentName="unit",
		isMultiple=true,
		isRequired=true,
		length=40,
		type=Type.MultiLangText)
	private List<LangString> names 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getNames() {
		return names;
	}
	
	@ContextField(
		name="comment",
		parentName="unit",
		isMultiple=true,
		length=32000,
		type=Type.MultiLangText)
	private List<LangString> comments 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getComments() {
		return comments;
	}
}
