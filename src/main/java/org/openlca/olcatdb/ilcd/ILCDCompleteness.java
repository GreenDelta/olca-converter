package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Data completeness aspects for this specific data set.
 * 
 * @Element completeness
 * @ContentModel (completenessProductModel?,
 *               referenceToSupportedImpactAssessmentMethods*,
 *               completenessElementaryFlows*, completenessOtherProblemField*,
 *               other?)
 */
@Context(name = "completeness", parentName = "modellingAndValidation")
public class ILCDCompleteness extends ContextObject {

	/**
	 * Completeness of coverage of relevant product, waste and elementary flows.
	 * [Notes: For LCI results and Partly terminated systems this means
	 * throughout the underlying product system model. "Relevant" refers to the
	 * overall environmental relevance, i.e. for unit processes including the
	 * upstream and downstream burdens of product and waste flows.]
	 * 
	 * @Element completenessProductModel
	 * @DataType CompletenessValues
	 */
	@ContextField(name = "completenessProductModel", parentName = "completeness")
	public String completenessProductModel;

	/**
	 * "LCIA methods data sets" that can be applied to the elementary flows in
	 * the Inputs and Outputs section, i.e. ALL these flows are referenced by
	 * the respective LCIA method data set (if they are of environmental
	 * relevance and a characterisation factor is defined for the respective
	 * flow). [Note: Applicability is not given if the inventoty contains some
	 * elementary flows with the same meaning as referenced in the LCIA method
	 * data set but in a different nomenclature (and hence carry no
	 * characterisation factor), or if the flows are sum indicators or flow
	 * groups that are addressed differently in the LCIA method data set.]
	 * 
	 * @Element referenceToSupportedImpactAssessmentMethods
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToSupportedImpactAssessmentMethods", parentName = "completeness", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> supportedImpactAssessmentMethods = new ArrayList<DataSetReference>();

	/**
	 * "LCIA methods data sets" that can be applied to the elementary flows in
	 * the Inputs and Outputs section, i.e. ALL these flows are referenced by
	 * the respective LCIA method data set (if they are of environmental
	 * relevance and a characterisation factor is defined for the respective
	 * flow). [Note: Applicability is not given if the inventoty contains some
	 * elementary flows with the same meaning as referenced in the LCIA method
	 * data set but in a different nomenclature (and hence carry no
	 * characterisation factor), or if the flows are sum indicators or flow
	 * groups that are addressed differently in the LCIA method data set.]
	 * 
	 * @Element referenceToSupportedImpactAssessmentMethods
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getSupportedImpactAssessmentMethods() {
		return supportedImpactAssessmentMethods;
	}

	/**
	 * Completeness of the elementary flows in the Inputs and Outputs section of
	 * this data set from impact perspective, regarding addressing the
	 * individual mid-point problem field / impact category given. The
	 * completeness refers to the state-of-the-art of scientific knowledge
	 * whether or not an individual elementary flow contributes to the
	 * respective mid-point topic in a relevant way, which is e.g. the basis for
	 * the ILCD reference elementary flows. [Note: The "Completeness" statement
	 * does not automatically mean that related LCIA methods exist or reference
	 * the elementary flows of this data set. Hence for direct applicability of
	 * existing LCIA methods, check the field
	 * "Supported LCIA method data sets".]
	 * 
	 * 
	 * @Element completenessElementaryFlows
	 */
	@SubContext(contextClass = ILCDElemFlowCompleteness.class, isMultiple = true)
	private List<ILCDElemFlowCompleteness> elemFlowCompleteness = new ArrayList<ILCDElemFlowCompleteness>();

	/**
	 * Completeness of the elementary flows in the Inputs and Outputs section of
	 * this data set from impact perspective, regarding addressing the
	 * individual mid-point problem field / impact category given. The
	 * completeness refers to the state-of-the-art of scientific knowledge
	 * whether or not an individual elementary flow contributes to the
	 * respective mid-point topic in a relevant way, which is e.g. the basis for
	 * the ILCD reference elementary flows. [Note: The "Completeness" statement
	 * does not automatically mean that related LCIA methods exist or reference
	 * the elementary flows of this data set. Hence for direct applicability of
	 * existing LCIA methods, check the field
	 * "Supported LCIA method data sets".]
	 * 
	 * 
	 * @Element completenessElementaryFlows
	 */
	public List<ILCDElemFlowCompleteness> getElemFlowCompleteness() {
		return elemFlowCompleteness;
	}

	/**
	 * Completeness of coverage of elementary flows that contribute to other
	 * problem fields that are named here as free text, preferably using the
	 * same terminology as for the specified environmental problems.
	 * 
	 * @Element completenessOtherProblemField
	 * @DataType FT
	 */
	@ContextField(name = "completenessOtherProblemField", parentName = "completeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> completenessOtherProblemFields = new ArrayList<LangString>();

	/**
	 * Completeness of coverage of elementary flows that contribute to other
	 * problem fields that are named here as free text, preferably using the
	 * same terminology as for the specified environmental problems.
	 * 
	 * @Element completenessOtherProblemField
	 * @DataType FT
	 */
	public List<LangString> getCompletenessOtherProblemFields() {
		return completenessOtherProblemFields;
	}
}
