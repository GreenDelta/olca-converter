package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Specifies one allocation of this exchange (see the attributes of this tag
 * below)
 * 
 * @Element allocation
 * 
 */
@Context(name = "allocation", parentName = "allocations")
public class ILCDAllocationFactor extends ContextObject {

	/**
	 * Fraction (expressed in %) of this Input or Output flow that is foreseen
	 * to be allocated to this co-product (recommended allocation). The numbers
	 * across the co-products should sum up to 100%.
	 * 
	 * @Attribute allocatedFraction
	 * @DataType Perc
	 */
	@ContextField(name = "allocation", parentName = "allocations",
			attributeName = "allocatedFraction", isAttribute = true,
			type = Type.Double)
	public Double fraction;

	/**
	 * Reference to one of the co-products. The applied allocation approach(es),
	 * details and and explanations are documented in the fields
	 * "LCI method approaches" and "Deviations from LCI method approaches /
	 * explanations". [Notes: Applicable only to multifunctional processes. The
	 * documented allocated fractions are only applicable when using the data
	 * set for attributional modelling and are to be ignored for consequential
	 * modeling.]
	 * 
	 * @Attribute internalReferenceToCoProduct
	 * @DataType Int6
	 */
	@ContextField(name = "allocation", parentName = "allocations",
			attributeName = "internalReferenceToCoProduct", isAttribute = true,
			type = Type.Integer)
	public Integer coProductId;
}
