package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * Contains information about the person and the quality network the person
 * belongs to.
 * 
 * @Element dataEntryBy
 */
@Context(name = "dataEntryBy", parentName = "administrativeInformation")
public class ES2Entry extends ContextObject {
	
	@ContextField(
			name = "dataEntryBy", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "personId")
	public String personId;

	@ContextField(
			name = "dataEntryBy", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "personContextId")
	public String personContextId;
	
	@ContextField(
			name = "dataEntryBy", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "personEmail")
	public String personEmail;
	
	@ContextField(
			name = "dataEntryBy", 
			parentName = "administrativeInformation", 
			isAttribute = true, 
			attributeName = "personName")
	public String personName;
	
	@ContextField(
			name = "dataEntryBy", 
			parentName = "isActiveAuthor", 
			isAttribute = true, 
			attributeName = "personName", type = Type.Boolean)
	public boolean isActiveAuthor;
	
}
