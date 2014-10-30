package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * One compliance declaration.
 * 
 * @Element compliance
 * @ContentModel (((referenceToComplianceSystem, approvalOfOverallCompliance?)),
 *               other?)
 * 
 */
@Context(name = "compliance", parentName = "complianceDeclarations")
public class ILCDCompliance extends ContextObject {

	/**
	 * "Source data set" of the "Compliance system" that is declared to be met
	 * by the data set.
	 * 
	 * @Element referenceToComplianceSystem
	 */
	@ContextField(name = "referenceToComplianceSystem", parentName = "compliance", type = Type.DataSetReference)
	public DataSetReference complianceSystem;

	/**
	 * Official approval whether or not and in how far the data set meets all
	 * the requirements of the "Compliance system" refered to. This approval
	 * should be issued/confirmed by the owner of that compliance system, who is
	 * identified via the respective "Contact data set".
	 * 
	 * @Element approvalOfOverallCompliance
	 * @DataType ComplianceValues
	 */
	@ContextField(name = "approvalOfOverallCompliance", parentName = "compliance")
	public String overallCompliance;
}
