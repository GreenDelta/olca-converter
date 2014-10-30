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
 * The EcoSpold 02 master-data list for product flows (intermediate exchanges).
 * 
 * @author Michael Srocka
 *
 */
@Context(name="validIntermediateExchanges")
public class ES2ProductFlowList extends ContextObject{

	@ContextField(
		name="validIntermediateExchanges",
		attributeName="contextId",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String contextId;	
	
	@ContextField(
		name="validIntermediateExchanges",
		attributeName="majorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int majorRelease = 1; // default
	
	@ContextField(
		name="validIntermediateExchanges",
		attributeName="majorRevision",
		isAttribute=true,
		type=Type.Integer)
	public Integer majorRevision;
	
	@ContextField(
		name="validIntermediateExchanges",
		attributeName="minorRelease",
		isAttribute=true,
		isRequired=true,
		type=Type.Integer)
	public int minorRelease = 0; // default
	
	@ContextField(
		name="validIntermediateExchanges",
		attributeName="minorRevision",
		isAttribute=true,
		type=Type.Integer)
	public Integer minorRevision;
	
	@ContextField(
		name="validIntermediateExchanges",
		attributeName="originalSource",
		isAttribute=true)
	public String originalSource;	
	
	@ContextField(
		name="contextName",
		parentName="validIntermediateExchanges",
		isRequired=true,
		length=80,
		isMultiple=true,
		type=Type.MultiLangText)
	private List<LangString> contextName 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getContextName() {
		return contextName;
	}
	
	@SubContext(
		contextClass=ES2ProductFlow.class,
		isMultiple=true
	)
	private List<ES2ProductFlow> productFlows
		= new ArrayList<ES2ProductFlow>();
	
	public List<ES2ProductFlow> getProductFlows() {
		return productFlows;
	}
	
	/**
	 * Returns true if this list contains an product
	 * flow with the given ID, otherwise false.
	 */
	public boolean contains (String id) {
		boolean b = false;
		if(id != null)
		for(ES2ProductFlow f : this.productFlows) {
			if(id.equals(f.id)) {
				b = true;
				break;
			}
		}
		return b;
	}
	
}
