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
 * The EcoSpold 2 master data file for valid activity names.
 * 
 * @author Michael Srocka 
 *
 */
@Context(name="validActivityNames")
public class ES2ActivityNameList extends ContextObject {

	// TODO: contains required attributes only 
	
	@ContextField(
		name="validActivityNames",
		attributeName="majorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int majorRelease = 1;
	
	@ContextField(
		name="validActivityNames",
		attributeName="minorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int minorRelease = 0;
	
	@ContextField(
		name="validActivityNames",
		attributeName="contextId",
		isAttribute=true,
		type=Type.Text,
		isRequired=true
	)
	public String contextId;
		
	@ContextField(
		name="contextName",
		parentName="validActivityNames",
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
			contextClass=ES2ActivityName.class, 
			isMultiple=true)
	private List<ES2ActivityName> activityNames 
		= new ArrayList<ES2ActivityName>();
	
	public List<ES2ActivityName> getActivityNames() {
		return activityNames;
	} 
	
}
