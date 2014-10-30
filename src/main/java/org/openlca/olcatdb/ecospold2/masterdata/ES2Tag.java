package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Master data entry of an EcoSpold 02 tag.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="tag", parentName="validTags")
public class ES2Tag {

	@ContextField(
		name="tag", 
		parentName="validTags",
		attributeName="name",
		isAttribute=true,
		isRequired=true,
		length=40
	)
	public String name;
	
	public String getName() {
		return name;
	}
		
	@ContextField(
		name="comment",
		parentName="tag",
		isMultiple=true,
		length=32000,
		type=Type.MultiLangText)
	private List<LangString> comments 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getComments() {
		return comments;
	}
	
}
