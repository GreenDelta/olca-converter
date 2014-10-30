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
 * 
 * @Element dataSetInformation
 * @ContentModel (UUID, name*, synonyms*, classificationInformation?,
 *               generalComment*, other?)
 */
@Context(name = "dataSetInformation", parentName = "flowPropertiesInformation")
public class ILCDFlowPropertyDescription extends ContextObject {

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
	 * Name of flow property.
	 * 
	 * @Element name
	 * @DataType String
	 */
	@ContextField(name = "name", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * Name of flow property.
	 * 
	 * @Element name
	 * @DataType String
	 */
	public List<LangString> getName() {
		return name;
	}

	/**
	 * Synonyms are alternative names for the "Name" of the Flow property.
	 * 
	 * @Element synonyms
	 * @DataType FT
	 */
	@ContextField(name = "synonyms", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> synonyms = new ArrayList<LangString>();

	/**
	 * Synonyms are alternative names for the "Name" of the Flow property.
	 * 
	 * @Element synonyms
	 * @DataType FT
	 */
	public List<LangString> getSynonyms() {
		return synonyms;
	}

	/**
	 * Hierachical classification of the Flow property foreseen to be used to
	 * structure the Flow property content of the database. (Note: This entry is
	 * NOT required for the identification of the Flow property data set. It
	 * should nevertheless be avoided to use identical names for Flow properties
	 * in the same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	@SubContext(contextClass = ILCDClassification.class, isMultiple = true)
	private List<ILCDClassification> classifications = new ArrayList<ILCDClassification>();

	/**
	 * Hierachical classification of the Flow property foreseen to be used to
	 * structure the Flow property content of the database. (Note: This entry is
	 * NOT required for the identification of the Flow property data set. It
	 * should nevertheless be avoided to use identical names for Flow properties
	 * in the same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	/**
	 * Free text for general information about the data set. It may contain
	 * comments on e.g. information sources used as well as general (internal,
	 * not reviewed) quality statements.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	@ContextField(name = "generalComment", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

		
	/**
	 * Free text for general information about the data set. It may contain
	 * comments on e.g. information sources used as well as general (internal,
	 * not reviewed) quality statements.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	public List<LangString> getComment() {
		return comment;
	}
}
