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
 * The EcoSpold 2 master data file for valid persons.
 * 
 * @author Michael Srocka 
 *
 */
@Context(name="validPersons")
public class ES2PersonList extends ContextObject{

	@ContextField(
		name="validPersons",
		attributeName="contextId",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String contextId;	
	
	@ContextField(
		name="validPersons",
		attributeName="majorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int majorRelease = 1;
	
	@ContextField(
		name="validPersons",
		attributeName="minorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int minorRelease = 0;
	
	@ContextField(
		name="contextName",
		parentName="validPersons",
		isRequired=true,
		length=80,
		isMultiple=true,
		type=Type.MultiLangText)
	private List<LangString> contextNames 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getContextNames() {
		return contextNames;
	}
	
	@SubContext(contextClass=ES2Person.class, 
			isMultiple=true)
	private List<ES2Person> persons
		= new ArrayList<ES2Person>();
	
	public List<ES2Person> getPersons() {
		return persons;
	}
	
	/**
	 * Returns the person with the given ID from this
	 * list, or <code>null</code> if there is no person
	 * with this ID in this list.
	 */
	public ES2Person getPerson(String id) {
		ES2Person person = null;
		for(ES2Person p : persons) {
			if(id == null && p.id == null) {
				person = p;
				break;
			} else if(id != null && p.id != null && id.equals(p.id)) {
				person = p;
				break;
			}
		}
		return person;
	}
	
}
