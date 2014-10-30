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
 * The EcoSpold 02 master data unit list. See the 
 * schema definition EcoSpold02Units.xsd.
 * 
 * @author Michael Srocka
 */
@Context(name="validUnits")
public class ES2UnitList extends ContextObject {

	@ContextField(
		name="validUnits",
		attributeName="contextId",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String contextId;	
	
	@ContextField(
		name="validUnits",
		attributeName="majorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int majorRelease = 1;
	
	@ContextField(
		name="validUnits",
		attributeName="majorRevision",
		isAttribute=true,
		type=Type.Integer)
	public Integer majorRevision;
	
	@ContextField(
		name="validUnits",
		attributeName="minorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int minorRelease = 0;
	
	@ContextField(
		name="validUnits",
		attributeName="minorRevision",
		isAttribute=true,
		type=Type.Integer)
	public Integer minorRevision;
	
	@ContextField(
		name="validUnits",
		attributeName="originalSource",
		isAttribute=true)
	public String originalSource;	
	
	@ContextField(
		name="contextName",
		parentName="validUnits",
		isRequired=true,
		length=80,
		isMultiple=true,
		type=Type.MultiLangText)
	private List<LangString> contextNames 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getContextNames() {
		return contextNames;
	}
	
	@SubContext(
		contextClass=ES2Unit.class,
		isMultiple=true)
	private List<ES2Unit> units 
		= new ArrayList<ES2Unit>();
	
	public List<ES2Unit> getUnits() {
		return units;
	}
	
	/**
	 * Returns the unit for the given ID, or null if there is no such unit.
	 */
	public ES2Unit getUnit(String id) {
		ES2Unit unit = null;
		for(ES2Unit u : this.units) {
			if(u.id != null && u.id.equals(id)) {
				unit = u;
				break;
			}
		}
		return unit;
	}
	
}
