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
 * A set of formulas that allows to model the amount of single exchanges in the
 * input and output list in dependency of each other and/or in dependency of
 * parameters. Used to provide a process model ("parameterized process") for
 * calculation of inventories in dependency of user settings of e.g. yield,
 * efficiency of abatement measures, processing of different educts, etc.
 * 
 * @Element mathematicalRelations
 * @ContentModel : (modelDescription*, variableParameter*, other?)
 */
@Context(name = "mathematicalRelations", parentName = "processInformation")
public class ILCDMathSection extends ContextObject {

	/**
	 * Description of the model(s) represented in this section of mathematical
	 * relations. Can cover information on restrictions, model strenghts and
	 * weaknesses, etc. (Note: Also see information provided on the level of the
	 * individual formula in field "Comment" and in the general process
	 * description in the fields in section "Technology".)
	 * 
	 * @Element modelDescription
	 * @DataType FT
	 */
	@ContextField(isMultiple = true, name = "modelDescription", 
			parentName = "mathematicalRelations", type = Type.MultiLangText)
	private List<LangString> modelDescription = new ArrayList<LangString>();

	/**
	 * Description of the model(s) represented in this section of mathematical
	 * relations. Can cover information on restrictions, model strenghts and
	 * weaknesses, etc. (Note: Also see information provided on the level of the
	 * individual formula in field "Comment" and in the general process
	 * description in the fields in section "Technology".)
	 * 
	 * @Element modelDescription
	 * @DataType FT
	 */
	public List<LangString> getModelDescription() {
		return modelDescription;
	}

	/**
	 * Name of variable or parameter used as scaling factors for the
	 * "Mean amount" of individual inputs or outputs of the data set.
	 * 
	 * @Element variableParameter
	 * @ContentModel (formula?, meanValue?, minimumValue?, maximumValue?,
	 *               uncertaintyDistributionType?,
	 *               relativeStandardDeviation95In?, comment*, other?)
	 */
	@SubContext(contextClass = ILCDParameter.class, isMultiple = true)
	private List<ILCDParameter> parameters = new ArrayList<ILCDParameter>();
	
	/**
	 * Name of variable or parameter used as scaling factors for the
	 * "Mean amount" of individual inputs or outputs of the data set.
	 * 
	 * @Element variableParameter
	 * @ContentModel (formula?, meanValue?, minimumValue?, maximumValue?,
	 *               uncertaintyDistributionType?,
	 *               relativeStandardDeviation95In?, comment*, other?)
	 */
	public List<ILCDParameter> getParameters() {
		return parameters;
	}
	
}
