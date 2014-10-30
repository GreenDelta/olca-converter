package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2ClassificationRef;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The EcoSpold 02 master data entry for an intermediate exchange.
 * 
 * 
 * @author Michael Srocka
 * 
 */
@Context(name="intermediateExchange", parentName="validIntermediateExchanges")
public class ES2ProductFlow extends ContextObject {

	@ContextField(
		name="intermediateExchange", 
		parentName="validIntermediateExchanges",
		attributeName="id",
		isAttribute=true,
		isRequired=true,
		length=36)
	public String id;
	
	@ContextField(
		name="intermediateExchange", 
		parentName="validIntermediateExchanges",
		attributeName="unitId",
		isAttribute=true,
		length=36)
	public String unitId;

	@ContextField(
		name="intermediateExchange", 
		parentName="validIntermediateExchanges",
		attributeName="unitContextId",
		isAttribute=true,
		length=36)
	public String unitContextId;
			
	@ContextField(
		name="intermediateExchange", 
		parentName="validIntermediateExchanges",
		attributeName="casNumber",
		isAttribute=true,
		length=11)
	public String casNumber;
	
	@ContextField(
		name="intermediateExchange", 
		parentName="validIntermediateExchanges",
		attributeName="defaultVariableName",
		isAttribute=true,
		length=40)
	public String defaultVariableName;
	
	@ContextField(
		name="name",
		parentName="intermediateExchange",
		isMultiple=true,
		isRequired=true,
		length=120,
		type=Type.MultiLangText)
	private List<LangString> name 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getName() {
		return name;
	}
	
	@ContextField(
		name="unitName",
		parentName="intermediateExchange",
		isMultiple=true,
		length=40,
		type=Type.MultiLangText)
	private List<LangString> unitName 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getUnitName() {
		return unitName;
	}
	
	@SubContext(
			contextClass = ES2ClassificationRef.class,
			isMultiple = true)
	private List<ES2ClassificationRef> productClassifications  
		= new ArrayList<ES2ClassificationRef>();
	
	public List<ES2ClassificationRef> getProductClassifications() {
		return productClassifications;
	}
	
	@ContextField(
		name="comment",
		parentName="intermediateExchange",
		isMultiple=true,
		length=32000,
		type=Type.MultiLangText)
	private List<LangString> comment 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getComment() {
		return comment;
	}
	
	@ContextField(
		name="synonym",
		parentName="intermediateExchange",
		isMultiple=true,
		length=80,
		type=Type.MultiLangText)
	private List<LangString> synonyms 
		= new ArrayList<LangString>(); 
	
	public List<LangString> getSynonyms() {
		return synonyms;
	}
	
	
}
