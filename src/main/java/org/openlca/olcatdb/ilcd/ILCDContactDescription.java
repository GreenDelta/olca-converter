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
 * @ContentModel (UUID, shortName*, name*, classificationInformation?,
 *               contactAddress*, telephone?, telefax?, email?, WWWAddress?,
 *               centralContactPoint*, contactDescriptionOrComment*,
 *               referenceToContact*, referenceToLogo?, other?)
 */
@Context(name = "dataSetInformation", parentName = "contactInformation")
public class ILCDContactDescription extends ContextObject {

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
	 * Short name for the contact, that is used for display e.g. of links to
	 * this data set (especially in case the full name of the contact is rather
	 * long, e.g. "FAO" for "Food and Agriculture Organization").
	 * 
	 * @Element shortName
	 * @DataType String
	 */
	@ContextField(name = "shortName", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> shortName = new ArrayList<LangString>();

	/**
	 * Short name for the contact, that is used for display e.g. of links to
	 * this data set (especially in case the full name of the contact is rather
	 * long, e.g. "FAO" for "Food and Agriculture Organization").
	 * 
	 * @Element shortName
	 * @DataType String
	 */
	public List<LangString> getShortName() {
		return shortName;
	}

	/**
	 * Name of the person, working group, organisation, or database network,
	 * which is represented by this contact data set.
	 * 
	 * @Element name
	 * @DataType String
	 */
	@ContextField(name = "name", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * Name of the person, working group, organisation, or database network,
	 * which is represented by this contact data set.
	 * 
	 * @Element name
	 * @DataType String
	 */
	public List<LangString> getName() {
		return name;
	}

	/**
	 * Hierarchical classification of the contact foreseen to be used to
	 * structure the contact content of the database. (Note: This entry is NOT
	 * required for the identification of the contact data set. It should
	 * nevertheless be avoided to use identical names for contacts in the same
	 * class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	@SubContext(contextClass = ILCDClassification.class, isMultiple = true)
	private List<ILCDClassification> classifications = new ArrayList<ILCDClassification>();

	/**
	 * Hierarchical classification of the contact foreseen to be used to
	 * structure the contact content of the database. (Note: This entry is NOT
	 * required for the identification of the contact data set. It should
	 * nevertheless be avoided to use identical names for contacts in the same
	 * class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	/**
	 * Mail address of the contact; specific for the person, working group, or
	 * department. [Note: A general contact point to the organisation is to be
	 * given in "General contact point".]
	 * 
	 * @Element contactAddress
	 * @DataType ST
	 */
	@ContextField(name = "contactAddress", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> contactAddress = new ArrayList<LangString>();

	/**
	 * Mail address of the contact; specific for the person, working group, or
	 * department. [Note: A general contact point to the organisation is to be
	 * given in "General contact point".]
	 * 
	 * @Element contactAddress
	 * @DataType ST
	 */
	public List<LangString> getContactAddress() {
		return contactAddress;
	}

	/**
	 * Contact's phone number(s) including country and regional codes.
	 * 
	 * @Element telephone
	 * @DataType String
	 */
	@ContextField(name = "telephone", parentName = "dataSetInformation")
	public String telephone;

	/**
	 * Contact's fax number(s) including country and regional codes.
	 * 
	 * @Element telefax
	 * @DataType String
	 */
	@ContextField(name = "telefax", parentName = "dataSetInformation")
	public String telefax;

	/**
	 * Contact's e-mail address.
	 * 
	 * @Element email
	 * @DataType String
	 */
	@ContextField(name = "email", parentName = "dataSetInformation")
	public String email;

	/**
	 * Web-address of the person, working group, organisation or database
	 * network.
	 * 
	 * @Element WWWAddress
	 * @DataType ST
	 */
	@ContextField(name = "WWWAddress", parentName = "dataSetInformation")
	public String webAddress;

	/**
	 * Alternative address / contact details for the contact. Provides contact
	 * information in case e.g. the person or group represented by this contact
	 * has left the organisation or changed office/telephone. This alternative
	 * contact point can hence contain also a central telephone number, e-mail,
	 * www- address etc. of the organisation.
	 * 
	 * @Element centralContactPoint
	 * @DataType ST
	 */
	@ContextField(name = "centralContactPoint", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> centralContactPoint = new ArrayList<LangString>();

	/**
	 * Alternative address / contact details for the contact. Provides contact
	 * information in case e.g. the person or group represented by this contact
	 * has left the organisation or changed office/telephone. This alternative
	 * contact point can hence contain also a central telephone number, e-mail,
	 * www- address etc. of the organisation.
	 * 
	 * @Element centralContactPoint
	 * @DataType ST
	 */
	public List<LangString> getCentralContactPoint() {
		return centralContactPoint;
	}

	/**
	 * Free text for additional description of the organisation or person of the
	 * contact, such as organisational profile, person responsibilities, etc.
	 * 
	 * @Element contactDescriptionOrComment
	 * @DataType ST
	 */
	@ContextField(name = "contactDescriptionOrComment", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * Free text for additional description of the organisation or person of the
	 * contact, such as organisational profile, person responsibilities, etc.
	 * 
	 * @Element contactDescriptionOrComment
	 * @DataType ST
	 */
	public List<LangString> getComment() {
		return comment;
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
