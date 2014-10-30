package org.openlca.olcatdb.ecospold2;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains information about the representativeness of the unit process data
 * (meta information and flow data).
 * 
 * @Element representativeness
 * @ContentModel (productionVolumeText*, samplingProcedure*, extrapolations*,
 *               namespace:uri="##other")
 * 
 */
@Context(name = "representativeness", parentName = "modellingAndValidation")
public class ES2Representativeness extends ContextObject {

	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "percent", type = Type.Double)
	public Double percent;

	@ContextField(name = "representativeness", parentName = "modellingAndValidation", isAttribute = true, attributeName = "systemModelId")
	public String systemModelId;

	@ContextField(name = "systemModelName", parentName = "representativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> systemModelName = new ArrayList<LangString>();

	public List<LangString> getSystemModelName() {
		return systemModelName;
	}

	@ContextField(name = "samplingProcedure", parentName = "representativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> samplingProcedure = new ArrayList<LangString>();

	public List<LangString> getSamplingProcedure() {
		return samplingProcedure;
	}

	@ContextField(name = "extrapolations", parentName = "representativeness", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> extrapolations = new ArrayList<LangString>();

	public List<LangString> getExtrapolations() {
		return extrapolations;
	}

}
