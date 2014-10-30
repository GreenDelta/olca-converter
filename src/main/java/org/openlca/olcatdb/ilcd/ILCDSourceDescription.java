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
 * @Element dataSetInformation
 * @ContentModel (UUID, shortName*, classificationInformation?, sourceCitation?,
 *               publicationType?, sourceDescriptionOrComment*,
 *               referenceToDigitalFile*, referenceToContact*, referenceToLogo?,
 *               other?)
 */
@Context(name = "dataSetInformation", parentName = "sourceInformation")
public class ILCDSourceDescription extends ContextObject {

	/**
	 * Automatically generated Universally Unique Identifier of this data set.
	 * Together with the "Data set version", the UUID uniquely identifies each
	 * data set.
	 * 
	 * @Element UUID
	 * @DataType UUID
	 */
	@ContextField(name = "UUID", parentName = "dataSetInformation")
	public String uuid;

	/**
	 * Short name for the "Source citation", i.e. for the bibliographical
	 * reference or reference to internal data sources used.
	 * 
	 * @Element shortName
	 * @DataType String
	 */
	@ContextField(name = "shortName", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> shortName = new ArrayList<LangString>();

	/**
	 * Short name for the "Source citation", i.e. for the bibliographical
	 * reference or reference to internal data sources used.
	 * 
	 * @Element shortName
	 * @DataType String
	 */
	public List<LangString> getShortName() {
		return shortName;
	}

	/**
	 * Hierarchical classification of the Source foreseen to be used to
	 * structure the Source content of the database. (Note: This entry is NOT
	 * required for the identification of a Source. It should nevertheless be
	 * avoided to use identical names for Source in the same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	@SubContext(contextClass = ILCDClassification.class, isMultiple = true)
	private List<ILCDClassification> classifications = new ArrayList<ILCDClassification>();

	/**
	 * Hierarchical classification of the Source foreseen to be used to
	 * structure the Source content of the database. (Note: This entry is NOT
	 * required for the identification of a Source. It should nevertheless be
	 * avoided to use identical names for Source in the same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	/**
	 * Bibliographical reference or reference to internal data source. Also used
	 * in order to reference to databases and tools, data set formats,
	 * conformity systems, pictures etc..
	 * 
	 * @Element sourceCitation
	 * @DataType ST
	 */
	@ContextField(name = "sourceCitation", parentName = "dataSetInformation")
	public String sourceCitation;

	/**
	 * Bibliographic publication type of the source.
	 * 
	 * @Element publicationType
	 * @DataType PublicationTypeValues
	 */
	@ContextField(name = "publicationType", parentName = "dataSetInformation")
	public String publicationType;

	/**
	 * Free text for additional description of the source. In case of use of
	 * published data it may contain a brief summary of the publication and the
	 * kind of medium used (e.g. CD-ROM, hard copy).
	 * 
	 * @Element sourceDescriptionOrComment
	 * @DataType FT
	 */
	@ContextField(name = "sourceDescriptionOrComment", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * Free text for additional description of the source. In case of use of
	 * published data it may contain a brief summary of the publication and the
	 * kind of medium used (e.g. CD-ROM, hard copy).
	 * 
	 * @Element sourceDescriptionOrComment
	 * @DataType FT
	 */
	public List<LangString> getComment() {
		return comment;
	}

	/**
	 * Link to a digital file of the source (www-address or intranet-path;
	 * relative or absolue path). (Info: Allows direct access to e.g. complete
	 * reports of further documentation, which may also be digitally attached to
	 * this data set and exchanged jointly with the XML file.)
	 * 
	 * @Element referenceToDigitalFile
	 */
	@ContextField(name = "referenceToDigitalFile", parentName = "dataSetInformation", isAttribute = true, attributeName = "uri", isMultiple = true)
	private List<String> digitalFileUri = new ArrayList<String>();

	/**
	 * Link to a digital file of the source (www-address or intranet-path;
	 * relative or absolue path). (Info: Allows direct access to e.g. complete
	 * reports of further documentation, which may also be digitally attached to
	 * this data set and exchanged jointly with the XML file.)
	 * 
	 * @Element referenceToDigitalFile
	 */
	public List<String> getDigitalFileUri() {
		return digitalFileUri;
	}

	/**
	 * "Contact data set"s of working groups, organisations or database networks
	 * to which EITHER this person or entity OR this database, data set format,
	 * or compliance system belongs. [Note: This does not necessarily imply a
	 * legally binding relationship, but may also be a voluntary membership.]
	 * 
	 * @Element referenceToContact
	 */
	@ContextField(name = "referenceToContact", parentName = "dataSetInformation", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> contactReferences = new ArrayList<DataSetReference>();

	/**
	 * "Contact data set"s of working groups, organisations or database networks
	 * to which EITHER this person or entity OR this database, data set format,
	 * or compliance system belongs. [Note: This does not necessarily imply a
	 * legally binding relationship, but may also be a voluntary membership.]
	 * 
	 * @Element referenceToContact
	 */
	public List<DataSetReference> getContactReferences() {
		return contactReferences;
	}

	/**
	 * "Source data set" of the logo of the organisation or source to be used in
	 * reports etc.
	 * 
	 * @Element referenceToLogo
	 */
	@ContextField(name = "referenceToLogo", parentName = "dataSetInformation", type = Type.DataSetReference)
	public DataSetReference logoReference;
}
