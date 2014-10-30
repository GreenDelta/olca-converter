package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * Exchange of an elementary flow.
 * 
 * @author Michael Srocka
 * 
 */
@Context(name = "elementaryExchange", parentName = "flowData")
public class ES2ElementaryExchange extends ContextObject {

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "id")
	public String id;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "elementaryExchangeId", isMaster = true)
	public String elementaryExchangeId;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "formula")
	public String formula;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "unitId")
	public String unitId;

	@ContextField(name = "unitName", parentName = "elementaryExchange", isMultiple = true, isRequired = true, length = 40, type = Type.MultiLangText)
	private List<LangString> unitNames = new ArrayList<LangString>();

	public List<LangString> getUnitNames() {
		return unitNames;
	}

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "variableName")
	public String variableName;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "casNumber")
	public String casNumber;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "amount", type = Type.Double)
	public double amount;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "isCalculatedAmount", type = Type.Boolean)
	public Boolean isCalculatedAmount;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "mathematicalRelation")
	public String mathematicalRelation;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceId")
	public String sourceId;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceYear")
	public String sourceYear;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceFirstAuthor")
	public String sourceFirstAuthor;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "pageNumbers")
	public String pageNumbers;

	@ContextField(name = "elementaryExchange", parentName = "flowData", isAttribute = true, attributeName = "specificAllocationPropertyId")
	public String specificAllocationPropertyId;

	@ContextField(name = "name", parentName = "elementaryExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "comment", parentName = "elementaryExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty uncertainty;

	@ContextField(name = "compartment", parentName = "elementaryExchange", isAttribute = true, attributeName = "subcompartmentId")
	public String compartmentId;

	@ContextField(name = "compartment", parentName = "compartment", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> compartment = new ArrayList<LangString>();

	public List<LangString> getCompartment() {
		return compartment;
	}

	@ContextField(name = "subcompartment", parentName = "compartment", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> subCompartment = new ArrayList<LangString>();

	public List<LangString> getSubCompartment() {
		return subCompartment;
	}

	@ContextField(name = "synonym", parentName = "elementaryExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> synonym = new ArrayList<LangString>();

	public List<LangString> getSynonym() {
		return synonym;
	}

	@SubContext(contextClass = ES2Property.class, isMultiple = true)
	private List<ES2Property> properties = new ArrayList<ES2Property>();

	public List<ES2Property> getProperties() {
		return properties;
	}

	@SubContext(contextClass = ES2TransferCoefficient.class, isMultiple = true)
	private List<ES2TransferCoefficient> transferCoefficients = new ArrayList<ES2TransferCoefficient>();

	public List<ES2TransferCoefficient> getTransferCoefficients() {
		return transferCoefficients;
	}

	@ContextField(name = "tag", parentName = "elementaryExchange", isMultiple = true)
	private List<String> tags = new ArrayList<String>();

	public List<String> getTags() {
		return tags;
	}

	@ContextField(name = "inputGroup", parentName = "elementaryExchange", type = Type.Integer)
	public Integer inputGroup;

	@ContextField(name = "outputGroup", parentName = "elementaryExchange", type = Type.Integer)
	public Integer outputGroup;

	/**
	 * Return true if the id is equal, otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ES2ElementaryExchange && obj != null) {
			ES2ElementaryExchange exchange = ES2ElementaryExchange.class
					.cast(obj);
			if (exchange.id != null && this.id != null
					&& exchange.id.equals(this.id))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
