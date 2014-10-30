package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Transfer coefficients relate specific inputs to specific outputs and record
 * the share of this specific input that contributes to this specific output.
 * For the format definition see the complex type section below.
 * 
 * @Element transferCoefficient
 * @ContentModel (uncertainty?, generalComment*, namespace:uri="##other")
 * 
 */
@Context(name = "transferCoefficient", parentNonStrict = true)
public class ES2TransferCoefficient extends ContextObject {

	/**
	 * The amount of the transfer coefficient is to be multiplied with the
	 * amount of this referenced exchange.
	 * 
	 * @Attribute amount
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "amount")
	public String amount;

	/**
	 * Reference to the UUID of an exchange.
	 * 
	 * @Attribute exchangeId
	 * @DataType TUuid
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "exchangeId", isMaster = true)
	public String exchangeId;

	/**
	 * defines a mathematical formula with references to values of flows,
	 * parameters or properties by variable names or REF function the result of
	 * the formula with a specific set of variable values is written into the
	 * amount field
	 * 
	 * @Attribute mathematicalRelation
	 * @DataType TBaseString32000
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "mathematicalRelation")
	public String mathematicalRelation;

	/**
	 * 
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "isCalculatedAmount", type = Type.Boolean)
	public boolean isCalculatedAmount;

	/**
	 * An ID used in the area 'sources' of the respective dataset is required.
	 * 
	 * @Attribute sourceId
	 * @DataType TUuid
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "sourceId", isMaster = true, classGroup = ES2SourceList.class, overwrittenByChild = "sourceIdOverwrittenByChild")
	public String sourceId;

	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "sourceIdOverwrittenByChild", type = Type.Boolean)
	public boolean sourceIdOverwrittenByChild;

	/**
	 * 
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "sourceYear", classGroup = ES2SourceList.class)
	public String sourceYear;

	/**
	 * 
	 */
	@ContextField(name = "transferCoefficient", parentNonStrict = true, isAttribute = true, attributeName = "sourceFirstAuthor", classGroup = ES2SourceList.class)
	public String sourceFirstAuthor;

	/**
	 * Uncertainty of the transfer coefficient amount.
	 * 
	 * @Element uncertainty
	 * @ContentModel ((lognormal | normal | triangular | uniform | beta | gamma
	 *               | erlang | undefined), pedigreeMatrix?, generalComment*,
	 *               namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty uncertainty;

	/**
	 * A general comment can be made about each individual transfer coefficient.
	 * 
	 * @Element generalComment
	 * @DataType TBaseString32000
	 */
	@ContextField(name = "generalComment", parentName = "transferCoefficient", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> generalComment = new ArrayList<LangString>();

	/**
	 * A general comment can be made about each individual transfer coefficient.
	 * 
	 * @Element generalComment
	 * @DataType TBaseString32000
	 */
	public List<LangString> getGeneralComment() {
		return this.generalComment;
	}

	@Override
	public boolean equals(Object obj) {
		System.err.println("TODO");
		// TODO
		return super.equals(obj);
	}

}
