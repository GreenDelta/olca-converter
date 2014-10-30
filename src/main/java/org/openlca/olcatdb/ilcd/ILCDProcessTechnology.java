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
 * Provides information about the technological representativeness of the data
 * set.
 * 
 * @Element technology
 * @ContentModel (technologyDescriptionAndIncludedProcesses*,
 *               referenceToIncludedProcesses*, technologicalApplicability*,
 *               referenceToTechnologyPictogramme?,
 *               referenceToTechnologyFlowDiagrammOrPicture*, other?)
 */
@Context(name = "technology", parentName = "processInformation")
public class ILCDProcessTechnology extends ContextObject {

	/**
	 * Description of the technological characteristics including operating
	 * conditions of the process or product system. For the latter this includes
	 * the relevant upstream and downstream processes included in the data set.
	 * Professional terminology should be used.
	 * 
	 * @Element technologyDescriptionAndIncludedProcesses
	 * @DataType FT
	 */
	@ContextField(name = "technologyDescriptionAndIncludedProcesses", parentName = "technology", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> description = new ArrayList<LangString>();

	/**
	 * Description of the technological characteristics including operating
	 * conditions of the process or product system. For the latter this includes
	 * the relevant upstream and downstream processes included in the data set.
	 * Professional terminology should be used.
	 * 
	 * @Element technologyDescriptionAndIncludedProcesses
	 * @DataType FT
	 */
	public List<LangString> getDescription() {
		return description;
	}

	/**
	 * "Process data set(s)" included in this data set, if any and available as
	 * separate data set(s).
	 * 
	 * @Element referenceToIncludedProcesses
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToIncludedProcesses", parentName = "technology", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> includedProcesses = new ArrayList<DataSetReference>();

	/**
	 * "Process data set(s)" included in this data set, if any and available as
	 * separate data set(s).
	 * 
	 * @Element referenceToIncludedProcesses
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getIncludedProcesses() {
		return includedProcesses;
	}

	/**
	 * Description of the intended / possible applications of the good, service,
	 * or process. E.g. for which type of products the material, represented by
	 * this data set, is used. Examples: "This high purity chemical is used for
	 * analytical laboratories only." or "This technical quality bulk chemical
	 * is used for large scale synthesis in chemical industry.". Or: "This truck
	 * is used only for long-distance transport of liquid bulk chemicals".
	 * 
	 * @Element technologicalApplicability
	 * @DataType : FT
	 */
	@ContextField(name = "technologicalApplicability", parentName = "technology", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> applicability = new ArrayList<LangString>();

	/**
	 * Description of the intended / possible applications of the good, service,
	 * or process. E.g. for which type of products the material, represented by
	 * this data set, is used. Examples: "This high purity chemical is used for
	 * analytical laboratories only." or "This technical quality bulk chemical
	 * is used for large scale synthesis in chemical industry.". Or: "This truck
	 * is used only for long-distance transport of liquid bulk chemicals".
	 * 
	 * @Element technologicalApplicability
	 * @DataType : FT
	 */
	public List<LangString> getApplicability() {
		return applicability;
	}

	/**
	 * "Source data set" of the pictogramme of the good, service, technogy,
	 * plant etc. represented by this data set. For use in graphical user
	 * interfaces of LCA software.
	 * 
	 * @Element referenceToTechnologyPictogramme
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToTechnologyPictogramme", parentName = "technology", type = Type.DataSetReference)
	public DataSetReference pictogramme;

	/**
	 * "Source data set" of the flow diagramm(s) and/or photo(s) of the good,
	 * service, technology, plant etc represented by this data set. For clearer
	 * illustration and documentation of data set.
	 * 
	 * @Element referenceToTechnologyFlowDiagrammOrPicture
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToTechnologyFlowDiagrammOrPicture", parentName = "technology", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> flowDiagrammsAndPictures = new ArrayList<DataSetReference>();
	
	
	/**
	 * "Source data set" of the flow diagramm(s) and/or photo(s) of the good,
	 * service, technology, plant etc represented by this data set. For clearer
	 * illustration and documentation of data set.
	 * 
	 * @Element referenceToTechnologyFlowDiagrammOrPicture
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getFlowDiagrammsAndPictures() {
		return flowDiagrammsAndPictures;
	}
}
