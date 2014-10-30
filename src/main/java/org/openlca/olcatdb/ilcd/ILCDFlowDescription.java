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
 * General data set information. Covers the ISO/TS 14048 fields 1.2.1, 1.2.3,
 * 1.2.4, 1.2.5, (1.2.6), (1.2.7),1.2.10.1, 1.2.10.2, 1.2.10.3, and references
 * to 1.2.11 (Flow property) and 1.2.11.2 (Unit).
 * 
 * @Element dataSetInformation
 * @ContentModel (UUID, name?, synonyms*, classificationInformation?,
 *               CASNumber?, sumFormula?, generalComment*, other?)
 */
@Context(name = "dataSetInformation", parentName = "flowInformation")
public class ILCDFlowDescription extends ContextObject {

	/**
	 * Automatically generated Universally Unique Identifier of this data set.
	 * Together with the "Data set version", the UUID uniquely identifies each
	 * data set.
	 * 
	 * @Element UUID
	 * @DataType UUID
	 */
	@ContextField(name = "UUID", parentName = "dataSetInformation", type = Type.Text)
	public String uuid;

	/**
	 * @Element baseName General descriptive name of the elementary, waste or
	 *          product flow, for the latter including it's level of processing.
	 * @DataType String
	 */
	@ContextField(name = "baseName", parentName = "name", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * @Element baseName General descriptive name of the elementary, waste or
	 *          product flow, for the latter including it's level of processing.
	 * @DataType String
	 */
	public List<LangString> getName() {
		return name;
	}

	/**
	 * Specifying information on the (product or waste) flow in technical
	 * term(s): treatment received, standard fulfilled, product quality, use
	 * information, production route name, educt name, primary / secondary etc.
	 * Separated by commata..
	 * 
	 * @Element treatmentStandardsRoutes
	 * @DataType String
	 */
	@ContextField(name = "treatmentStandardsRoutes", parentName = "name", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> treatmentStandardsRoutes = new ArrayList<LangString>();

	/**
	 * Specifying information on the (product or waste) flow in technical
	 * term(s): treatment received, standard fulfilled, product quality, use
	 * information, production route name, educt name, primary / secondary etc.
	 * Separated by commata..
	 * 
	 * @Element treatmentStandardsRoutes
	 * @DataType String
	 */
	public List<LangString> getTreatmentStandardsRoutes() {
		return treatmentStandardsRoutes;
	}

	/**
	 * Specifying information on the good, service, or process whether being a
	 * production mix or consumption mix, location type of availability (such as
	 * e.g. "to consumer" or "at plant"). Separated by commata.
	 * 
	 * @Element mixAndLocationTypes
	 * @DataType String
	 */
	@ContextField(name = "mixAndLocationTypes", parentName = "name", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> mixAndLocationTypes = new ArrayList<LangString>();

	/**
	 * Specifying information on the good, service, or process whether being a
	 * production mix or consumption mix, location type of availability (such as
	 * e.g. "to consumer" or "at plant"). Separated by commata.
	 * 
	 * @Element mixAndLocationTypes
	 * @DataType String
	 */
	public List<LangString> getMixAndLocationTypes() {
		return mixAndLocationTypes;
	}

	/**
	 * Further, quantitative specifying information on the (product or waste)
	 * flow, in technical term(s): qualifying constituent(s)-content and / or
	 * energy-content per unit etc. as appropriate. Separated by commata. (Note:
	 * non-qualifying flow properties, CAS No, Synonyms, Chemical formulas etc.
	 * are documented exclusively in the respective fields.)
	 * 
	 * @Element flowProperties
	 * @DataType String
	 */
	@ContextField(name = "flowProperties", parentName = "name", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> flowProperties = new ArrayList<LangString>();

	/**
	 * Further, quantitative specifying information on the (product or waste)
	 * flow, in technical term(s): qualifying constituent(s)-content and / or
	 * energy-content per unit etc. as appropriate. Separated by commata. (Note:
	 * non-qualifying flow properties, CAS No, Synonyms, Chemical formulas etc.
	 * are documented exclusively in the respective fields.)
	 * 
	 * @Element flowProperties
	 * @DataType String
	 */
	public List<LangString> getFlowProperties() {
		return flowProperties;
	}

	/**
	 * Synonyms / alternative names / brands of the good, service, or process.
	 * Separated by semicolon.
	 * 
	 * @Element synonyms
	 * @DataType FT
	 */
	@ContextField(name = "synonyms", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> synonyms = new ArrayList<LangString>();

	/**
	 * Synonyms / alternative names / brands of the good, service, or process.
	 * Separated by semicolon.
	 * 
	 * @Element synonyms
	 * @DataType FT
	 */
	public List<LangString> getSynonyms() {
		return synonyms;
	}

	/**
	 * Identifying category/compartment information exclusively used for
	 * elementary flows. E.g. "Emission to air", "Renewable resource", etc.
	 * 
	 * @Element elementaryFlowCategorization
	 * @ContentModel (category+, other?)
	 * 
	 */
	@SubContext(contextClass = ILCDElemFlowCategorization.class, isMultiple = true)
	private List<ILCDElemFlowCategorization> elemFlowCategorizations = new ArrayList<ILCDElemFlowCategorization>();

	/**
	 * Identifying category/compartment information exclusively used for
	 * elementary flows. E.g. "Emission to air", "Renewable resource", etc.
	 * 
	 * @Element elementaryFlowCategorization
	 * @ContentModel (category+, other?)
	 * 
	 */
	public List<ILCDElemFlowCategorization> getElemFlowCategorizations() {
		return elemFlowCategorizations;
	}

	/**
	 * Optional statistical or other classification of the data set. Typically
	 * also used for structuring LCA databases.
	 * 
	 * @Element classification
	 */
	@SubContext(contextClass = ILCDClassification.class, isMultiple = true)
	private List<ILCDClassification> classifications = new ArrayList<ILCDClassification>();

	/**
	 * Optional statistical or other classification of the data set. Typically
	 * also used for structuring LCA databases.
	 * 
	 * @Element classification
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	/**
	 * Chemical Abstract Systems Number of the substance. [Note: Should only be
	 * given for (virtually) pure substances, but NOT also for the main
	 * constituent of a material or product etc.]
	 * 
	 * @Element CASNumber
	 * @DataType CASNumber
	 */
	@ContextField(name = "CASNumber", parentName = "dataSetInformation", type = Type.Text)
	public String casNumber;

	/**
	 * Chemical sum formula of the substance.
	 * 
	 * @Element sumFormula
	 * @DataType String
	 */
	@ContextField(name = "sumFormula", parentName = "dataSetInformation", type = Type.Text)
	public String sumFormula;

	/**
	 * Free text for general information about the Flow data set. It may contain
	 * information about e.g. the use of the substance, good, service or process
	 * in a specific technology or industry-context, information sources used,
	 * data selection principles etc.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	@ContextField(name = "generalComment", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();
	
	/**
	 * Free text for general information about the Flow data set. It may contain
	 * information about e.g. the use of the substance, good, service or process
	 * in a specific technology or industry-context, information sources used,
	 * data selection principles etc.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	public List<LangString> getComment() {
		return comment;
	}
}
