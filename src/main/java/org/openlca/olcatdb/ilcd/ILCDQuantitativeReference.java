package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * This section names the quantitative reference used for this data set, i.e.
 * the reference to which the inputs and outputs quantiatively relate.
 * 
 * @Element quantitativeReference
 * @ContentModel (referenceToReferenceFlow*, functionalUnitOrOther*, other?)
 */
@Context(name = "quantitativeReference", parentName = "processInformation")
public class ILCDQuantitativeReference extends ContextObject {

	/**
	 * Type of quantitative reference of this data set.
	 * 
	 * @Attribute type
	 * @DataType TypeOfQuantitativeReferenceValues
	 */
	@ContextField(name = "quantitativeReference", parentName = "processInformation", attributeName = "type", isAttribute = true)
	public String type;

	/**
	 * One or more of the Inputs or Outputs in case
	 * "Type of quantitative reference" is of type "Reference flow(s)". (Data
	 * set internal reference.)
	 * 
	 * @Element referenceToReferenceFlow
	 * @DataType Int6
	 */
	@ContextField(name = "referenceToReferenceFlow", parentName = "quantitativeReference", isMultiple = true, type = Type.Integer)
	private List<Integer> referenceFlows = new ArrayList<Integer>();

	/**
	 * One or more of the Inputs or Outputs in case
	 * "Type of quantitative reference" is of type "Reference flow(s)". (Data
	 * set internal reference.)
	 * 
	 * @Element referenceToReferenceFlow
	 * @DataType Int6
	 */
	public List<Integer> getReferenceFlows() {
		return referenceFlows;
	}

	/**
	 * Quantity, name, property/quality, and measurement unit of the Functional
	 * unit, Production period, or Other parameter, in case
	 * "Type of quantitative reference" is of one of these types. [Note: One or
	 * more functional units can also be given in addition to a reference flow.]
	 * 
	 * @Element functionalUnitOrOther
	 * @DataType String
	 */
	@ContextField(name = "functionalUnitOrOther", parentName = "quantitativeReference", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> functionalUnits = new ArrayList<LangString>();

	/**
	 * Quantity, name, property/quality, and measurement unit of the Functional
	 * unit, Production period, or Other parameter, in case
	 * "Type of quantitative reference" is of one of these types. [Note: One or
	 * more functional units can also be given in addition to a reference flow.]
	 * 
	 * @Element functionalUnitOrOther
	 * @DataType String
	 */
	public List<LangString> getFunctionalUnits() {
		return functionalUnits;
	}
}
