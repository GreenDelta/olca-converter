package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * One or more geographical sub-unit(s) of the stated "Location". Such sub-units
 * can be e.g. the sampling sites of a company-average data set, the countries
 * of a region-average data set, or specific sites in a country-average data
 * set. [Note: For single site data sets this field is empty and the site is
 * named in the "Location" field.]
 * 
 * @Element subLocationOfOperationSupplyOrProduction
 * @ContentModel (descriptionOfRestrictions*, other?)
 */
@Context(name = "subLocationOfOperationSupplyOrProduction", parentName = "geography")
public class ILCDSubLocation extends ContextObject {

	/**
	 * Geographical latitude and longitude reference of "Location" /
	 * "Sub-location". For area-type locations (e.g. countries, continents) the
	 * field is empty.
	 * 
	 * @Attribute latitudeAndLongitude
	 * @DataType GIS
	 */
	@ContextField(name = "subLocationOfOperationSupplyOrProduction", parentName = "geography", attributeName = "latitudeAndLongitude", isAttribute = true)
	public String latitudeAndLongitude;

	/**
	 * One or more geographical sub-unit(s) of the stated "Location". Such
	 * sub-units can be e.g. the sampling sites of a company-average data set,
	 * the countries of a region-average data set, or specific sites in a
	 * country-average data set. [Note: For single site data sets this field is
	 * empty and the site is named in the "Location" field.]
	 * 
	 * @Attribute subLocation
	 * @DataType String
	 */
	@ContextField(name = "subLocationOfOperationSupplyOrProduction", parentName = "geography", attributeName = "subLocation", isAttribute = true)
	public String subLocation;

	/**
	 * Further explanations about additional aspects of the location: e.g. a
	 * company and/or site description and address, whether for certain
	 * sub-areas within the "Location" the data set is not valid, whether data
	 * is only valid for certain regions within the location indicated, or
	 * whether certain elementary flows or intermediate product flows are
	 * extrapolated from another geographical area.
	 * 
	 * @Element descriptionOfRestrictions
	 * @DataType FT
	 */
	@ContextField(name = "descriptionOfRestrictions", parentName = "subLocationOfOperationSupplyOrProduction", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> description = new ArrayList<LangString>();

	/**
	 * Further explanations about additional aspects of the location: e.g. a
	 * company and/or site description and address, whether for certain
	 * sub-areas within the "Location" the data set is not valid, whether data
	 * is only valid for certain regions within the location indicated, or
	 * whether certain elementary flows or intermediate product flows are
	 * extrapolated from another geographical area.
	 * 
	 * @Element descriptionOfRestrictions
	 * @DataType FT
	 */
	public List<LangString> getDescription() {
		return description;
	}

}
