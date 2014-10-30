package org.openlca.olcatdb.ecospold2.masterdata;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;

/**
 * An EcoSpold 02 master data entry for a person.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "person", parentName = "validPersons")
public class ES2Person extends ContextObject {

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "address",
			length=255)
	public String address;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "email",
			isRequired=true,
			length=80)
	public String email;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "id",
			isRequired=true,
			length=36)
	public String id;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "name",
			isRequired=true,
			length=40)
	public String name;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "organisationName",
			length=80)
	public String organisationName;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "organisationWebsite",
			length=255)
	public String organisationWebsite;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "telefax",
			length=40)
	public String telefax;

	@ContextField(
			name = "person", 
			parentName = "validPersons", 
			isAttribute = true, 
			attributeName = "telephone",
			length=40)
	public String telephone;

}
