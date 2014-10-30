package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold master data entry for a classification.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="classificationValue", parentName="classificationSystem")
public class ES2Classification extends ContextObject {

	@ContextField(
		name="classificationValue", 
		parentName="classificationSystem",
		isAttribute=true,
		attributeName="id",
		length=36,
		isRequired=true
	)
	public String id;
	
	@ContextField(
		name="name",
		parentName="classificationValue",
		isMultiple=true,
		isRequired=true,
		type=Type.MultiLangText
	)
	private List<LangString> name = 
		new ArrayList<LangString>();
	
	public List<LangString> getName() {
		return name;
	}
	
}
