package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * General data set information. Section covers all single fields in the ISO/TS
 * 14048 "Process description", which are not part of the other sub-sections. In
 * ISO/TS 14048 no own sub-section is foreseen for these entries.
 * 
 * @Element dataSetInformation
 * @ContentModel (UUID, name?, identifierOfSubDataSet?, synonyms*,
 *               complementingProcesses?, classificationInformation?,
 *               generalComment*, referenceToExternalDocumentation*, other?)
 */
@Context(name = "dataSetInformation", parentName = "processInformation")
public class ILCDProcessDescription extends ContextObject{

	/**
	 * Automatically generated Universally Unique Identifier of this data set.
	 * Together with the "Data set version", the UUID uniquely identifies each
	 * data set.
	 * 
	 * @Element UUID
	 * @DataType UUID
	 */
	@ContextField(
			name = "UUID", 
			parentName = "dataSetInformation", 
			type = Type.Text)
	public String uuid;

	
	/**
	 * Identifier of a sub-set of a complete process data set. This can be the
	 * life cycle stage that a data set covers (such as used in EPDs for modular
	 * LCI reporting, with the inventory split up into "resource extraction
	 * stage", "production stage", "use stage" and "end-of-life stage"). Or it
	 * can be e.g. the type of emission source from which the elementary flows
	 * of the Inputs and Outputs stems (e.g. "incineration-related",
	 * "transport-related", etc.). Together with the field
	 * "Complementing processes" this allows to split up a process data set into
	 * a number of clearly identified data sets, each carrying only a part of
	 * the inventory and that together represent the complete inventory. Care
	 * has to be taken when naming the reference flow, to avoid
	 * misinterpretation.
	 * 
	 * @Element identifierOfSubDataSet
	 * @DataType String
	 */
	@ContextField(
			name = "identifierOfSubDataSet", 
			parentName="dataSetInformation")
	public String identifierOfSubDataSet = null;


	@ContextField(
			name = "baseName", 
			parentName = "name", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * General descriptive name of the process and/or its main good(s) or
	 * service(s) and/or it's level of processing.
	 * 
	 * @Element baseName*
	 * @DataType String
	 */
	public List<LangString> getName() {
		return name;
	}

	
	@ContextField(
			name = "treatmentStandardsRoutes", 
			parentName = "name", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> treatmentStandardsRoutes 
		= new ArrayList<LangString>();

	/**
	 * Specifying information on the good, service, or process in technical
	 * term(s): treatment received, standard fulfilled, product quality, use
	 * information, production route name, educt name, primary / secondary etc.
	 * Separated by commata.
	 * 
	 * @Element treatmentStandardsRoutes
	 * @DataType String
	 */
	public List<LangString> getTreatmentStandardsRoutes() {
		return treatmentStandardsRoutes;
	}
	
	@ContextField(
			name = "mixAndLocationTypes", 
			parentName = "name", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> mixAndLocationTypes 
		= new ArrayList<LangString>();

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

	
	@ContextField(
			name = "functionalUnitFlowProperties", 
			parentName = "name", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> functionalUnitFlowProperties 
		= new ArrayList<LangString>();

	/**
	 * Further, quantitative specifying information on the good, service or
	 * process in technical term(s): qualifying constituent(s)-content and / or
	 * energy-content per unit etc. as appropriate. Separated by commata. (Note:
	 * non-qualifying flow properties, CAS No, Synonyms, Chemical formulas etc.
	 * are documented exclusively in the "Flow data set".)
	 * 
	 * @Element functionalUnitFlowProperties
	 * @DataType String
	 */
	public List<LangString> getFunctionalUnitFlowProperties() {
		return functionalUnitFlowProperties;
	}

	@ContextField(
			name = "synonyms", 
			parentName = "dataSetInformation", 
			isMultiple = true, 
			type = Type.MultiLangText)
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
	
	@ContextField(
			name = "referenceToComplementingProcess", 
			parentName = "complementingProcesses", 
			isMultiple = true, 
			type = Type.DataSetReference)
	private List<DataSetReference> complementingProcesses 
		= new ArrayList<DataSetReference>();

	/**
	 * "Process data set(s)" that complement this partial / sub-set of a
	 * complete process data set, if any and available as separate data set(s).
	 * The identifying name of this sub-set should be stated in the field
	 * "Identifier of sub-data set".
	 * 
	 * @Element complementingProcesses
	 * @ContentModel : (referenceToComplementingProcess+)
	 */
	public List<DataSetReference> getComplementingProcesses() {
		return complementingProcesses;
	}


	@SubContext(
			contextClass = ILCDClassification.class, 
			isMultiple = true)
	private List<ILCDClassification> classifications 
		= new ArrayList<ILCDClassification>();

	/**
	 * Hierarchical classification of the good, service, or process. (Note: This
	 * entry is NOT required for the identification of a Process. It should
	 * nevertheless be avoided to use identical names for Processes in the same
	 * category.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	@ContextField(
			name = "generalComment", 
			parentName = "dataSetInformation", 
			isMultiple = true, 
			type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * General information about the data set, including e.g. general (internal,
	 * not reviewed) quality statements as well as information sources used.
	 * (Note: Please also check the more specific fields e.g. on
	 * "Intended application", "Advice on data set use" and the fields in the
	 * "Modelling and validation" section to avoid overlapping entries.)
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	public List<LangString> getComment() {
		return comment;
	}

	@ContextField(
			name = "referenceToExternalDocumentation", 
			parentName = "dataSetInformation", 
			isMultiple = true, 
			type = Type.DataSetReference)
	private List<DataSetReference> externalDocumentations 
		= new ArrayList<DataSetReference>();

	/**
	 * "Source data set(s)" of detailed LCA study on the process or product
	 * represented by this data set, as well as documents / files with
	 * overarching documentative information on technology, geographical and /
	 * or time aspects etc. (e.g. basic engineering studies, process simulation
	 * results, patents, plant documentation, model behind the parameterisation
	 * of the "Mathematical model" section, etc.) (Note: can indirectly
	 * reference to digital file.)
	 * 
	 * @Element referenceToExternalDocumentation
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getExternalDocumentations() {
		return externalDocumentations;
	}

}
