package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;

@Context(name = "dataQualityIndicator", parentName = "dataQualityIndicators")
public class ILCDQualityIndicator {

	@ContextField(
			name = "dataQualityIndicator", 
			parentName = "dataQualityIndicators",
			attributeName = "name",
			isAttribute = true)
	public String name;


	@ContextField(
			name = "dataQualityIndicator", 
			parentName = "dataQualityIndicators",
			attributeName = "value",
			isAttribute = true)
	public String value;


	@Override
	public String toString() {
		return "ILCDQualityIndicator ["
				+ (name != null ? "name=" + name + ", " : "")
				+ (value != null ? "value=" + value : "") + "]";
	}	
}
