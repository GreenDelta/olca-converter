package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The master data entry of an EcoSpold 02 activity (name).
 * 
 * @author Michael Srocka
 *
 */
@Context(name = "activityName", 
		parentName="validActivityNames")
public class ES2ActivityName extends ContextObject {

	@ContextField(
		name="activityName",
		parentName="validActivityNames",
		attributeName="id",
		isAttribute=true,
		isRequired=true,
		length=36,
		type=Type.Text
	)
	public String id;
	
	@ContextField(
		name="name",
		parentName="activityName",
		isMultiple=true,
		isRequired=true,
		length=120,
		type=Type.MultiLangText
	)
	private List<LangString> names =
		new ArrayList<LangString>();
	
	public List<LangString> getNames() {
		return names;
	}	
}
