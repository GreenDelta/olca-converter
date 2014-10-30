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
 * Information related to publication and version management of the data set
 * including copyright and access restrictions.
 * 
 * @Element publicationAndOwnership
 * @ContentModel (dateOfLastRevision?, ((dataSetVersion,
 *               referenceToPrecedingDataSetVersion*, permanentDataSetURI?)),
 *               ((workflowAndPublicationStatus?,
 *               referenceToUnchangedRepublication?)),
 *               referenceToRegistrationAuthority?, registrationNumber?,
 *               referenceToOwnershipOfDataSet?, ((copyright?,
 *               referenceToEntitiesWithExclusiveAccess*, licenseType?,
 *               accessRestrictions*)), other?)
 */
@Context(name = "publicationAndOwnership", parentName = "administrativeInformation")
public class ILCDProcessPublication extends ContextObject {

	/**
	 * Date when the data set was revised for the last time, typically manually
	 * set.
	 * 
	 * @Element dateOfLastRevision
	 * @DataType dateTime
	 */
	@ContextField(name = "dateOfLastRevision", parentName = "publicationAndOwnership")
	public String lastRevision;

	/**
	 * Version number of data set. First two digits refer to major updates, the
	 * second two digits to minor revisions and error corrections etc. The third
	 * three digits are intended for automatic and internal counting of versions
	 * during data set development. Together with the data set's UUID, the "Data
	 * set version" uniquely identifies each data set.
	 * 
	 * @Element dataSetVersion
	 * @DataType Version
	 */
	@ContextField(name = "dataSetVersion", parentName = "publicationAndOwnership")
	public String dataSetVersion;

	/**
	 * Last preceding data set, which was replaced by this version. Either a URI
	 * of that data set (i.e. an internet address) or its UUID plus version
	 * number is given (or both).
	 * 
	 * @Element referenceToPrecedingDataSetVersion
	 */
	@ContextField(name = "referenceToPrecedingDataSetVersion", parentName = "publicationAndOwnership", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> precedingDataSetReferences = new ArrayList<DataSetReference>();

	// getters and setters
	
	/**
	 * Last preceding data set, which was replaced by this version. Either a URI
	 * of that data set (i.e. an internet address) or its UUID plus version
	 * number is given (or both).
	 * 
	 * @Element referenceToPrecedingDataSetVersion
	 */
	public List<DataSetReference> getPrecedingDataSetReferences() {
		return precedingDataSetReferences;
	}

	/**
	 * URI (i.e. an internet address) of the original of this data set. [Note:
	 * This equally globally unique identifier supports users and software tools
	 * to identify and retrieve the original version of a data set via the
	 * internet or to check for available updates. The URI must not represent an
	 * existing WWW address, but it should be unique and point to the data
	 * access point, e.g. by combining the data owner's www path with the data
	 * set's UUID, e.g. http://www.mycompany.com/lca/
	 * processes/50f12420-8855-12db-b606-0900210c9a66.]
	 * 
	 * @Element permanentDataSetURI
	 * @DataType anyURI
	 */
	@ContextField(name = "permanentDataSetURI", parentName = "publicationAndOwnership")
	public String permanentDataSetURI;

	/**
	 * Workflow or publication status of data set. Details e.g. of foreseen
	 * publication dates should be provided on request by the "Data set owner".
	 * 
	 * @Element workflowAndPublicationStatus
	 * @DataType WorkflowAndPublicationStatusValues
	 */
	@ContextField(name = "workflowAndPublicationStatus", parentName = "publicationAndOwnership")
	public String workflowStatus;

	/**
	 * "Source data set" of the publication, in which this data set was
	 * published for the first time. [Note: This refers to exactly this data set
	 * as it is, without any format conversion, adjustments, flow name mapping,
	 * etc. In case this data set was modified/converted, the original source is
	 * documented in "Converted original data set from:" in section
	 * "Data entry by".]
	 * 
	 * @Element referenceToUnchangedRepublication
	 */
	@ContextField(name = "referenceToUnchangedRepublication", parentName = "publicationAndOwnership", type = Type.DataSetReference)
	public DataSetReference republicationReference;

	/**
	 * "Contact data set" of the authority that has registered this data set.
	 * 
	 * @Element referenceToRegistrationAuthority
	 */
	@ContextField(name = "referenceToRegistrationAuthority", parentName = "publicationAndOwnership", type = Type.DataSetReference)
	public DataSetReference registrationAuthorityReference;

	/**
	 * A unique identifying number for this data set issued by the registration
	 * authority.
	 * 
	 * @Element registrationNumber
	 * @DataType String
	 */
	@ContextField(name = "registrationNumber", parentName = "publicationAndOwnership")
	public String registrationNumber;

	/**
	 * "Contact data set" of the person or entity who owns this data set. (Note:
	 * this is not necessarily the publisher of the data set.)
	 * 
	 * @Element referenceToOwnershipOfDataSet
	 */
	@ContextField(name = "referenceToOwnershipOfDataSet", parentName = "publicationAndOwnership", type = Type.DataSetReference)
	public DataSetReference ownershipReference;

	/**
	 * Indicates whether or not a copyright on the data set exists. Decided upon
	 * by the "Owner of data set". [Note: See also field "Access and use
	 * restrictions".]
	 * 
	 * @Element copyright
	 * @DataType boolean
	 */
	@ContextField(name = "copyright", parentName = "publicationAndOwnership", type = Type.Boolean)
	public Boolean copyright;

	/**
	 * "Contact data set" of those entities or persons (or groups of these), to
	 * which an exclusive access to this data set is granted. Mainly intended to
	 * be used in confidentiality management in projects. [Note: See also field
	 * "Access and use restrictions".]
	 * 
	 * @Element referenceToEntitiesWithExclusiveAccess
	 */
	@ContextField(name = "referenceToEntitiesWithExclusiveAccess", parentName = "publicationAndOwnership", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> exclusiveAccessReferences = new ArrayList<DataSetReference>();

	// getters and setters
	
	/**
	 * "Contact data set" of those entities or persons (or groups of these), to
	 * which an exclusive access to this data set is granted. Mainly intended to
	 * be used in confidentiality management in projects. [Note: See also field
	 * "Access and use restrictions".]
	 * 
	 * @Element referenceToEntitiesWithExclusiveAccess
	 */
	public List<DataSetReference> getExclusiveAccessReferences() {
		return exclusiveAccessReferences;
	}

	/**
	 * Type of license that applies to the access and use of this data set.
	 * 
	 * @Element licenseType
	 * @DataType LicenseTypeValues
	 */
	@ContextField(name = "licenseType", parentName = "publicationAndOwnership")
	public String licenseType;

	/**
	 * Access restrictions / use conditions for this data set as free text or
	 * referring to e.g. license conditions. In case of no restrictions "None"
	 * is entered.
	 * 
	 * @Element accessRestrictions
	 * @DataType FT
	 */
	@ContextField(name = "accessRestrictions", parentName = "publicationAndOwnership", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> accessRestrictions = new ArrayList<LangString>();

	// getters and setters

	/**
	 * Access restrictions / use conditions for this data set as free text or
	 * referring to e.g. license conditions. In case of no restrictions "None"
	 * is entered.
	 * 
	 * @Element accessRestrictions
	 * @DataType FT
	 */
	public List<LangString> getAccessRestrictions() {
		return accessRestrictions;
	}

}
