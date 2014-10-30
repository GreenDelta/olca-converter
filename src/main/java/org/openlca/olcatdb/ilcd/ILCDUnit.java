package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * @Element unit
 * @ContentModel (name, meanValue, generalComment*, other?)
 */
@Context(name = "unit", parentName = "units")
public class ILCDUnit extends ContextObject {

	/**
	 * Automated entry: internal ID, used in the "Quantitative reference"
	 * section to identify the reference unit.
	 * 
	 * @Attribute dataSetInternalID
	 * @DataType Int5
	 */
	@ContextField(name = "unit", parentName = "units", attributeName = "dataSetInternalID", isAttribute = true, type = Type.Integer)
	public Integer dataSetInternalID;

	/**
	 * Name of the individual unit.
	 * 
	 * @Element name
	 * @DataType String
	 */
	@ContextField(name = "name", parentName = "unit")
	public String name;

	/**
	 * Mean value of this unit in relationship to the reference unit of this
	 * Unit group (see field "Reference unit" in the "Quantitative reference"
	 * section). [Notes and Examples: This vale is i.e. the linear conversion
	 * factor for this unit. E.g., if the Unit group would be "Units of mass"
	 * and the selected reference unit "kg", then the value stated here for an
	 * additional unit "g" would be 0.001, as 1 g is 0.001 times 1 kg. It is
	 * recommended to report only significant digits of the value.]
	 * 
	 * @Element meanValue
	 * @DataType Real
	 */
	@ContextField(name = "meanValue", parentName = "unit", type = Type.Double)
	public Double meanValue;

	/**
	 * General comment on each single unit, typically giving the long name and
	 * unit system from which this unit stems, and (if necessary) referring to
	 * specifc data sources used, or for workflow purposes about status of
	 * "finalisation" of an entry etc.
	 * 
	 * @Element generalComment
	 * @DataType String
	 */
	@ContextField(name = "generalComment", parentName = "unit", type = Type.MultiLangText, isMultiple = true)
	private List<LangString> generalComment = new ArrayList<LangString>();

	/**
	 * General comment on each single unit, typically giving the long name and
	 * unit system from which this unit stems, and (if necessary) referring to
	 * specifc data sources used, or for workflow purposes about status of
	 * "finalisation" of an entry etc.
	 * 
	 * @Element generalComment
	 * @DataType String
	 */
	public List<LangString> getGeneralComment() {
		return generalComment;
	}
}
