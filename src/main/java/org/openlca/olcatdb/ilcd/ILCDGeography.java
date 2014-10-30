package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Provides information about the geographical representativeness of the data
 * set.
 * 
 * @Element geography
 * @ContentModel (locationOfOperationSupplyOrProduction?,
 *               subLocationOfOperationSupplyOrProduction*, other?)
 */
@Context(name = "geography", parentName = "processInformation")
public class ILCDGeography extends ContextObject {

	/**
	 * Geographical latitude and longitude reference of "Location" /
	 * "Sub-location". For area-type locations (e.g. countries, continents) the
	 * field is empty.
	 * 
	 * @Attribute latitudeAndLongitude
	 * @DataType GIS
	 */
	@ContextField(name = "locationOfOperationSupplyOrProduction", parentName = "geography", attributeName = "latitudeAndLongitude", isAttribute = true)
	public String latitudeAndLongitude;

	/**
	 * Location, country or region the data set represents. [Note 1: This field
	 * does not refer to e.g. the country in which a specific site is located
	 * that is represented by this data set but to the actually represented
	 * country, region, or site. Note 2: Entry can be of type "two-letter ISO
	 * 3166 country code" for countries, "seven-letter regional
	 * codes" for regions or continents, or "market areas and market
	 * organisations", as predefined for the ILCD. Also a name for e.g. a
	 * specific plant etc. can be given here (e.g.
	 * "FR, Lyon, XY Company, Z Site"; user defined). Note 3: The fact whether
	 * the entry refers to production or to consumption / supply has to be
	 * stated in the name-field "Mix and location types" e.g. as "Production
	 * mix".]
	 * 
	 * @Attribute location
	 * @DataType NullableString
	 */
	@ContextField(name = "locationOfOperationSupplyOrProduction", parentName = "geography", attributeName = "location", isAttribute = true)
	public String location;

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
	@ContextField(name = "descriptionOfRestrictions", parentName = "locationOfOperationSupplyOrProduction", isMultiple = true, type = Type.MultiLangText)
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

	/**
	 * One or more geographical sub-unit(s) of the stated "Location". Such
	 * sub-units can be e.g. the sampling sites of a company-average data set,
	 * the countries of a region-average data set, or specific sites in a
	 * country-average data set. [Note: For single site data sets this field is
	 * empty and the site is named in the "Location" field.]
	 * 
	 * @Element subLocationOfOperationSupplyOrProduction
	 * @ContentModel (descriptionOfRestrictions*, other?)
	 */
	@SubContext(contextClass = ILCDSubLocation.class, isMultiple = true)
	private List<ILCDSubLocation> subLocations = new ArrayList<ILCDSubLocation>();
	
	
	/**
	 * One or more geographical sub-unit(s) of the stated "Location". Such
	 * sub-units can be e.g. the sampling sites of a company-average data set,
	 * the countries of a region-average data set, or specific sites in a
	 * country-average data set. [Note: For single site data sets this field is
	 * empty and the site is named in the "Location" field.]
	 * 
	 * @Element subLocationOfOperationSupplyOrProduction
	 * @ContentModel (descriptionOfRestrictions*, other?)
	 */
	public List<ILCDSubLocation> getSubLocations() {
		return subLocations;
	}
}
