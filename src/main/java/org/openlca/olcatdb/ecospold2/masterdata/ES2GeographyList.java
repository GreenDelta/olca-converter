package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold 02 master data list for valid 
 * geographies.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="validGeographies")
public class ES2GeographyList {

	@ContextField(
		name="validGeographies",
		attributeName="contextId",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String contextId;	
	
	@ContextField(
		name="validGeographies",
		attributeName="majorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int majorRelease = 1;
	
	@ContextField(
		name="validGeographies",
		attributeName="minorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int minorRelease = 0;
	
	@ContextField(
		name="contextName",
		parentName="validGeographies",
		isRequired=true,
		length=80,
		isMultiple=true,
		type=Type.MultiLangText)
	private List<LangString> contextNames 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getContextNames() {
		return contextNames;
	}
	
	@SubContext(contextClass=ES2Geography.class, 
			isMultiple=true)
	private List<ES2Geography> geographies
		= new ArrayList<ES2Geography>();
	
	public List<ES2Geography> getGeographies() {
		return geographies;
	}
	
}
