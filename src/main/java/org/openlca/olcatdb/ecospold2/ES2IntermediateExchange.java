package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2PropertyList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2UnitList;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

@Context(name = "intermediateExchange", parentName = "flowData")
public class ES2IntermediateExchange extends ContextObject {

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "id")
	public String id;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "intermediateExchangeId")
	public String intermediateExchangeId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "activityLinkId", isMaster = true, classGroup = ES2ActivityList.class, overwrittenByChild = "activityLinkIdOverwrittenByChild")
	public String activityLinkId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "activityLinkContextId", isMaster = true)
	public String activityLinkContextId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "activityLinkIdOverwrittenByChild", classGroup = ES2ActivityList.class)
	public String activityLinkIdOverwrittenByChild;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeAmount", type = Type.Double)
	public Double productionVolumeAmount;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeVariableName")
	public String productionVolumeVariableName;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeSourceId", isMaster = true, classGroup = ES2SourceList.class, overwrittenByChild = "productionVolumeSourceIdOverwrittenByChild")
	public String productionVolumeSourceId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeSourceIdOverwrittenByChild", classGroup = ES2SourceList.class)
	public String productionVolumeSourceIdOverwrittenByChild;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeSourceContextId", isMaster = true)
	public String productionVolumeSourceContextId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeMathematicalRelation")
	public String productionVolumeMathematicalRelation;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeSourceYear", classGroup = ES2SourceList.class)
	public String productionVolumeSourceYear;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "productionVolumeSourceFirstAuthor", classGroup = ES2SourceList.class)
	public String productionVolumeSourceFirstAuthor;

	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty productionVolumeUncertainty;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "unitId", isMaster = true, classGroup = ES2UnitList.class)
	public String unitId;

	@ContextField(name = "unitName", parentName = "intermediateExchange", isMultiple = true, isRequired = true, length = 40, type = Type.MultiLangText, classGroup = ES2UnitList.class)
	private List<LangString> unitNames = new ArrayList<LangString>();

	public List<LangString> getUnitNames() {
		return unitNames;
	}

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "variableName")
	public String variableName;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "casNumber")
	public String casNumber;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "amount", type = Type.Double)
	public double amount;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "isCalculatedAmount", type = Type.Boolean)
	public Boolean isCalculatedAmount;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "mathematicalRelation")
	public String mathematicalRelation;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceId", isMaster = true, classGroup = ES2SourceList.class)
	public String sourceId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceYear", classGroup = ES2SourceList.class)
	public String sourceYear;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "sourceFirstAuthor", classGroup = ES2SourceList.class)
	public String sourceFirstAuthor;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "pageNumbers", classGroup = ES2SourceList.class)
	public String pageNumbers;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "specificAllocationPropertyId", isMaster = true, classGroup = ES2PropertyList.class, overwrittenByChild = "specificAllocationPropertyIdOverwrittenByChild")
	public String specificAllocationPropertyId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "specificAllocationPropertyContextId", isMaster = true)
	public String specificAllocationPropertyContextId;

	@ContextField(name = "intermediateExchange", parentName = "flowData", isAttribute = true, attributeName = "specificAllocationPropertyIdOverwrittenByChild", type = Type.Boolean, classGroup = ES2PropertyList.class)
	public Boolean specificAllocationPropertyIdOverwrittenByChild;

	@ContextField(name = "name", parentName = "intermediateExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	public List<LangString> getName() {
		return name;
	}

	@ContextField(name = "comment", parentName = "intermediateExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	public List<LangString> getComment() {
		return comment;
	}

	@SubContext(contextClass = ES2Uncertainty.class)
	public ES2Uncertainty uncertainty;

	@ContextField(name = "synonym", parentName = "intermediateExchange", isMultiple = true, type = Type.MultiLangText)
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

	@ContextField(name = "tag", parentName = "intermediateExchange", isMultiple = true)
	private List<String> tags = new ArrayList<String>();

	public List<String> getTags() {
		return tags;
	}

	@ContextField(name = "productionVolumeComment", parentName = "intermediateExchange", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> productionVolumeComment = new ArrayList<LangString>();

	public List<LangString> getProductionVolumeComment() {
		return productionVolumeComment;
	}

	@SubContext(contextClass = ES2ClassificationRef.class, isMultiple = true)
	private List<ES2ClassificationRef> productClassifications = new ArrayList<ES2ClassificationRef>();

	public List<ES2ClassificationRef> getProductClassifications() {
		return productClassifications;
	}

	@ContextField(name = "inputGroup", parentName = "intermediateExchange", type = Type.Integer)
	public Integer inputGroup;

	@ContextField(name = "outputGroup", parentName = "intermediateExchange", type = Type.Integer)
	public Integer outputGroup;

	@Override
	public String toString() {
		return "ES2IntermediateExchange [id=" + id
				+ ", intermediateExchangeId=" + intermediateExchangeId
				+ ", unitId=" + unitId + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ES2IntermediateExchange && obj != null) {
			ES2IntermediateExchange exchange = ES2IntermediateExchange.class
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
