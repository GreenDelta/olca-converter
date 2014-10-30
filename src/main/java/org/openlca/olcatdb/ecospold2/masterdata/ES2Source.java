package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Lists and describes the sources where the dataset is documented (e.g. final
 * report in the ECOINVENT quality network series).
 * 
 * @Element source
 * @ContentModel (generalComment*, namespace:uri="##other")
 * 
 */
@Context(name = "source", parentName = "modellingAndValidation")
public class ES2Source extends ContextObject {

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "id")
	public String id;
	
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "sourceType", 
			type = Type.Integer)
	public Integer sourceType;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "year",
			type = Type.Integer)
	public Integer year;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "volumeNo")
	public String volumeNo;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "firstAuthor")
	public String firstAuthor;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "additionalAuthors")
	public String additionalAuthors;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "title")
	public String title;

	
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "shortName")
	public String shortName;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "pageNumbers")
	public String pageNumbers;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "namesOfEditors")
	public String namesOfEditors;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "titleOfAnthology")
	public String titleOfAnthology;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "issueNo")
	public String issueNo;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "placeOfPublications")
	public String placeOfPublications;
	
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "publisher")
	public String publisher;

	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "journal")
	public String journal;

	@ContextField(
			name = "comment", 
			parentName = "source", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return this.comment;
	}

}
