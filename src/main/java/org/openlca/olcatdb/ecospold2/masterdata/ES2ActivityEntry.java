package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * A master data entry for an EcoSpold 02 activity index.
 * 
 * @author Michael Srocka
 *
 */
@Context(
		name="activityIndexEntry", 
		parentName="activityIndex")
public class ES2ActivityEntry extends ContextObject {

	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="id",
		isRequired=true,
		length=36		
	)
	public String id;
	
	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="activityNameId",
		isRequired=true,
		length=36		
	)
	public String activityNameId;
	
	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="geographyId",
		isRequired=true,
		length=36		
	)
	public String geographyId;
	
	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="startDate",
		isRequired=true
	)
	public String startDate;
	
	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="endDate",
		isRequired=true
	)
	public String endDate;
	
	@ContextField(
		name="activityIndexEntry", 
		parentName="activityIndex",
		isAttribute=true,
		attributeName="specialActivityType",
		isRequired=true,
		type=Type.Integer
	)
	public int specialActivityType = 0;
	
	@ContextField(
		name="name",
		parentName="activityIndexEntry",
		isMultiple=true,
		isRequired=true,
		length=120,
		type=Type.MultiLangText
	)
	private List<LangString> name 
		= new ArrayList<LangString>();
	
	public List<LangString> getName() {
		return name;
	}
	
	@ContextField(
		name="shortname",
		parentName="activityIndexEntry",
		isMultiple=true,
		isRequired=true,
		length=120,
		type=Type.MultiLangText
	)
	private List<LangString> location 
		= new ArrayList<LangString>();
	
	public List<LangString> getLocation() {
		return location;
	}
}
