package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2UnitList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * comprises all referenceToInputOutput.
 * 
 * @Element parameter
 * @ContentModel (name*, uncertainty?, generalComment*, namespace:uri="##other")
 */
@Context(name = "parameter", parentName = "flowData")
public class ES2Parameter extends ContextObject {

	/**
	 * The current value of the parameter.
	 * 
	 * @Attribute amount
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "amount", type = Type.Double)
	public double amount;

	/**
	 * The unique identifier for the parameter.
	 * 
	 * @Attribute id
	 * @DataType TUuid
	 */
	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "parameterId")
	public String id;

	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "parameterContextId", isMaster = true)
	public String parameterContextId;

	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "unitId", isMaster = true, classGroup = ES2UnitList.class)
	public String unitId;

	/**
	 * Defines a mathematical formula with references to values of flows,
	 * parameters or properties by variable names or REF function. The result of
	 * the formula with a specific set of variable values is written into the
	 * amount field.
	 * 
	 * @Attribute mathematicalRelation
	 * @DataType TBaseString32000
	 */
	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "mathematicalRelation")
	public String mathematicalRelation;

	/**
	 * Defines a variable name here for referencing the parameter amount in a
	 * mathematicalrelation.
	 * 
	 * @Attribute variableName
	 * @DataType TVariableName
	 */
	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "variableName")
	public String variableName;

	/**
	 * If true the value of the amount field is the calculated value of the
	 * mathematicalRelation.
	 * 
	 */
	@ContextField(name = "parameter", parentName = "flowData", isAttribute = true, attributeName = "isCalculatedAmount", type = Type.Boolean)
	public boolean isCalculatedAmount;

	/**
	 * name
	 */
	@ContextField(name = "name", parentName = "parameter", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * Descriptive name of the parameter.
	 * 
	 * @Element name
	 * @DataType TBaseString80
	 */
	public List<LangString> getName() {
		return this.name;
	}

	/**
	 * name
	 */
	@ContextField(name = "unitName", parentName = "parameter", isMultiple = true, type = Type.MultiLangText, classGroup = ES2UnitList.class)
	private List<LangString> unitName = new ArrayList<LangString>();

	public List<LangString> getUnitName() {
		return unitName;
	}

	/**
	 * Uncertainty of the parameter amount.
	 */
	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty uncertainty;

	@ContextField(name = "comment", parentName = "parameter", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * A general comment can be made about each individual parameter.
	 * 
	 * @Element generalComment
	 * @DataType TBaseString32000
	 */
	public List<LangString> getComment() {
		return this.comment;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ES2Parameter) {
			ES2Parameter parameter = ES2Parameter.class.cast(obj);
			if (parameter.id != null && this.id != null
					&& parameter.id.equals(this.id))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
