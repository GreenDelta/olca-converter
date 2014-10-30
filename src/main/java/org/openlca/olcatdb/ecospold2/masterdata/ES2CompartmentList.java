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
 * The EcoSpold 2 master data file for valid compartments.
 * 
 * @author Michael Srocka 
 *
 */
@Context(name="validCompartments")
public class ES2CompartmentList extends ContextObject{

	// TODO: contains required attributes only 
	
	@ContextField(
		name="validCompartments",
		attributeName="majorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int majorRelease = 1;
	
	@ContextField(
		name="validCompartments",
		attributeName="minorRelease",
		isAttribute=true,
		type=Type.Integer,
		isRequired=true
	)
	public int minorRelease = 0;
	
	@ContextField(
		name="validCompartments",
		attributeName="contextId",
		isAttribute=true,
		type=Type.Text,
		isRequired=true
	)
	public String contextId;
		
	@ContextField(
		name="contextName",
		parentName="validCompartments",
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
	
	@SubContext(contextClass=ES2Compartment.class, isMultiple = true)
	private List<ES2Compartment> compartments
	 	= new ArrayList<ES2Compartment>();
	
	public List<ES2Compartment> getCompartments() {
		return compartments;
	}
	
	
}
