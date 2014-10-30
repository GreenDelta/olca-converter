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
 * @Element dataSetInformation
 * @ContentModel (UUID, name*, classificationInformation?, generalComment*,
 *               other?)
 */
@Context(name = "dataSetInformation", parentName = "unitGroupInformation")
public class ILCDUnitGroupDescription extends ContextObject {

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
	 * Name of the unit group, typically indicating for which flow property or
	 * group of flow properties it is used. The individual units are named in
	 * the "Units" section of the "Unit group data set"
	 * 
	 * @Element name
	 * @DataType String
	 */
	@ContextField(name = "name", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> name = new ArrayList<LangString>();

	/**
	 * Name of the unit group, typically indicating for which flow property or
	 * group of flow properties it is used. The individual units are named in
	 * the "Units" section of the "Unit group data set"
	 * 
	 * @Element name
	 * @DataType String
	 */
	public List<LangString> getName() {
		return name;
	}

	/**
	 * Hierachical classification of the Unit groups; foreseen to be used to
	 * structure the Unit group content of the database. (Note: This entry is
	 * NOT required for the identification of the Unit group data set. It should
	 * nevertheless be avoided to use identical names for Unit groups in the
	 * same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	@SubContext(contextClass = ILCDClassification.class, isMultiple = true)
	private List<ILCDClassification> classifications = new ArrayList<ILCDClassification>();

	/**
	 * Hierachical classification of the Unit groups; foreseen to be used to
	 * structure the Unit group content of the database. (Note: This entry is
	 * NOT required for the identification of the Unit group data set. It should
	 * nevertheless be avoided to use identical names for Unit groups in the
	 * same class.
	 * 
	 * @Element classificationInformation
	 * @ContentModel (classification*)
	 */
	public List<ILCDClassification> getClassifications() {
		return classifications;
	}

	/**
	 * Free text for general information about the data set. E.g. coverage of
	 * different unit systems, information sources used, etc.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	@ContextField(name = "generalComment", parentName = "dataSetInformation", isMultiple = true, type = Type.MultiLangText)
	private List<LangString> comment = new ArrayList<LangString>();

	/**
	 * Free text for general information about the data set. E.g. coverage of
	 * different unit systems, information sources used, etc.
	 * 
	 * @Element generalComment
	 * @DataType FT
	 */
	public List<LangString> getComment() {
		return comment;
	}

}
