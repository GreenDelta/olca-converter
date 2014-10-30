package org.openlca.olcatdb.ecospold2.masterdata;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * The master data entry of an EcoSpold 02 geography.
 * 
 * @author Michael Srocka
 *
 */
@Context(name="geography", parentName="validGeographies")
public class ES2Geography extends ContextObject {


	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "id",
			isRequired=true,
			length=36)
	public String id;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "longitude",
			type = Type.Double)
	public Double longitude;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "latitude",
			type = Type.Double)
	public Double latitude;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "ISOTwoLetterCode",
			length=2)
	public String isoTwoLetterCode;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "ISOThreeLetterCode",
			length=3)
	public String isoThreeLetterCode;
		
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "UNCode",
			type = Type.Integer)
	public Integer unCode;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "UNRegionCode",
			type = Type.Integer)
	public Integer unRegionCode;
	
	@ContextField(
			name="geography", 
			parentName="validGeographies",
			isAttribute = true,
			attributeName = "UNSubregionCode",
			type = Type.Integer)
	public Integer unSubregionCode;
	
	@ContextField(
			name = "name",
			parentName = "geography",
			isMultiple = true,
			type = Type.MultiLangText,
			isRequired=true,
			length=255)
	private List<LangString> names 
		= new ArrayList<LangString>();
	
	public List<LangString> getNames() {
		return names;
	}
	
	@ContextField(
			name = "shortname",
			parentName = "geography",
			isMultiple = true,
			type = Type.MultiLangText,
			isRequired=true,
			length=40)
	private List<LangString> shortnames 
		= new ArrayList<LangString>();
	
	public List<LangString> getShortNames() {
		return shortnames;
	}
		
	@ContextField(
			name = "comment",
			parentName = "geography",			
			type = Type.TextAndImage)
	public TextAndImage comment;
	
}
