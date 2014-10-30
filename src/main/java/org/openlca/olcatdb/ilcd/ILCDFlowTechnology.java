package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Provides information about the technological representativeness of the flow
 * in case it is a product or waste flow.
 * 
 * @Element technology
 * @ContentModel (technologicalApplicability*,
 *               referenceToTechnicalSpecification*, other?)
 */
@Context(name = "technology", parentName = "flowInformation")
public class ILCDFlowTechnology extends ContextObject {

	/**
	 * Description of the intended / possible applications of the good or
	 * service, or waste. E.g. for which type of products the material,
	 * represented by this data set, is used. Examples: "This high purity
	 * chemical is used for analytical laboratories only." or "This technical
	 * quality bulk chemical is used for large scale synthesis in chemical
	 * industry.". Or: "This type of biowaste is typically composted or
	 * biodigested as the water content is too high for efficient combustion".
	 * 
	 * @Element technologicalApplicability
	 * @DataType FT
	 */
	@ContextField(name = "technologicalApplicability", parentName = "technology", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> applicability = new ArrayList<LangString>();

	/**
	 * Description of the intended / possible applications of the good or
	 * service, or waste. E.g. for which type of products the material,
	 * represented by this data set, is used. Examples: "This high purity
	 * chemical is used for analytical laboratories only." or "This technical
	 * quality bulk chemical is used for large scale synthesis in chemical
	 * industry.". Or: "This type of biowaste is typically composted or
	 * biodigested as the water content is too high for efficient combustion".
	 * 
	 * @Element technologicalApplicability
	 * @DataType FT
	 */
	public List<LangString> getApplicability() {
		return applicability;
	}

	/**
	 * "Source data set(s)" of the product's or waste's technical specification,
	 * waste data sheet, safety data sheet, etc.
	 * 
	 * @Element referenceToTechnicalSpecification
	 */
	@ContextField(name = "referenceToTechnicalSpecification", parentName = "technology", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> technicalSpecifications = new ArrayList<DataSetReference>();

	/**
	 * "Source data set(s)" of the product's or waste's technical specification,
	 * waste data sheet, safety data sheet, etc.
	 * 
	 * @Element referenceToTechnicalSpecification
	 */
	public List<DataSetReference> getTechnicalSpecifications() {
		return technicalSpecifications;
	}
}
