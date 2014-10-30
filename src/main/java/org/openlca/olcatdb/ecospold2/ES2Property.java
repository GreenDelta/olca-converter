package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2UnitList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * 
 * Properties of the exchange, e.g. dry mass, water content, price, content of
 * specific elements or substances. For the format definition see the complex
 * type section below.
 * 
 * @Element property
 * @ContentModel (name+, uncertainty?, generalComment*, namespace:uri="##other")
 * 
 */
@Context(name = "property", parentNonStrict = true)
public class ES2Property extends ContextObject {

	/**
	 * The value of the property.
	 * 
	 * @Attribute amount
	 * @DataType TFloatNumber
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "amount", type = Type.Double)
	public double amount;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "isCalculatedAmount", type = Type.Boolean)
	public Boolean isCalculatedAmount;

	/**
	 * If this field is true, the value of this property defines the exchanges
	 * it is assigned to and should not be changed later.
	 * 
	 * @Attribute definingValue
	 * @DataType boolean Enumerated Values : - true - false
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "isDefiningValue", type = Type.Boolean)
	public Boolean isDefiningValue;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "mathematicalRelation")
	public String mathematicalRelation;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "propertyContextId")
	public String propertyContextId;

	/**
	 * The unique identifier for the property.
	 * 
	 * @Attribute id
	 * @DataType TUuid
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "propertyId")
	public String propertyId;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "sourceContextId", isMaster = true)
	public String sourceContextId;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "sourceFirstAuthor", classGroup = ES2SourceList.class)
	public String sourceFirstAuthor;

	/**
	 * An ID used in the area 'sources' of the respective dataset is required.
	 * It indicates the publication where the property is documented.
	 * 
	 * @Attribute sourceId
	 * @DataType TUuid
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "sourceId", isMaster = true, classGroup = ES2SourceList.class)
	public String sourceId;

	/**
	 * 
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "sourceYear", classGroup = ES2SourceList.class)
	public String sourceYear;

	/**

	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "unitContextId", isMaster = true)
	public String unitContextId;

	/**
	 * Unit id.
	 * 
	 * @Attribute unit
	 * @DataType TUnit
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "unitId", isMaster = true, classGroup = ES2UnitList.class)
	public String unitId;

	/**
	 * Defines a variable name here for referencing the property amount in a
	 * mathematicalrelation.
	 * 
	 * @Attribute variableName
	 * @DataType TVariableName
	 */
	@ContextField(name = "property", parentNonStrict = true, isAttribute = true, attributeName = "variableName")
	public String variableName;

	/**
	 * Descriptive name of the property.
	 * 
	 * Element name DataType TBaseString80
	 */
	@ContextField(name = "name", parentName = "property", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * Descriptive name of the property.
	 * 
	 * Element name DataType TBaseString80
	 */
	public List<LangString> getName() {
		return this.name;
	}

	/**
	 * Descriptive name of the property.
	 * 
	 * Element name DataType TBaseString80
	 */
	@ContextField(name = "name", parentName = "property", isMultiple = true, type = Type.MultiLangText, classGroup = ES2UnitList.class)
	private List<LangString> unitName = new ArrayList<LangString>();

	public List<LangString> getUnitName() {
		return unitName;
	}

	/**
	 * Uncertainty of the property value.
	 * 
	 * @Element uncertainty
	 * @ContentModel ((lognormal | normal | triangular | uniform | beta | gamma
	 *               | erlang | undefined), pedigreeMatrix?, generalComment*,
	 *               namespace:uri="##other")
	 */
	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty uncertainty;

	/**
	 * Descriptive name of the property.
	 * 
	 * Element name DataType TBaseString80
	 */
	@ContextField(name = "name", parentName = "property", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ES2Property && obj != null) {
			ES2Property property = ES2Property.class.cast(obj);
			if (property.propertyId != null && this.propertyId != null
					&& property.propertyId.equals(this.propertyId))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
