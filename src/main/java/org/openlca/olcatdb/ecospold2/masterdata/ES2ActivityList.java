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
 * The the master data list for activities of the
 * EcoSpold 02 format.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="activityIndex")
public class ES2ActivityList extends ContextObject {

	@ContextField(
		name="activityIndex",
		attributeName="majorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int majorRelease = 1;
	
	@ContextField(
		name="activityIndex",
		attributeName="minorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int minorRelease = 0;
	
	@ContextField(
		name="activityIndex",
		attributeName="contextId",
		isAttribute=true,
		type=Type.Text,
		isRequired=true
	)
	public String contextId;
		
	@ContextField(
		name="contextName",
		parentName="activityIndex",
		isMultiple=true,
		isRequired=true,
		length=80,
		type=Type.MultiLangText
	)	
	private List<LangString> contextNames
		= new ArrayList<LangString>();
	
	public List<LangString> getContextNames() {
		return contextNames;
	}
		
	@SubContext(
		contextClass=ES2ActivityEntry.class,
		isMultiple=true
	)
	private List<ES2ActivityEntry> entries
		= new ArrayList<ES2ActivityEntry>();
	
	public List<ES2ActivityEntry> getEntries() {
		return entries;
	}
	
}
