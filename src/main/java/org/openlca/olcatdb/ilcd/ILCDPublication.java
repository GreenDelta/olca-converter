package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Information related to publication and version management of the data set
 * including copyright and access restrictions.
 * 
 * @Element publicationAndOwnership
 * @ContentModel (((dataSetVersion, referenceToPrecedingDataSetVersion*,
 *               permanentDataSetURI?)), referenceToOwnershipOfDataSet?, other?)
 */
@Context(name = "publicationAndOwnership", parentName = "administrativeInformation")
public class ILCDPublication extends ContextObject {

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
	private List<DataSetReference> precedingDataSets = new ArrayList<DataSetReference>();

	/**
	 * Last preceding data set, which was replaced by this version. Either a URI
	 * of that data set (i.e. an internet address) or its UUID plus version
	 * number is given (or both).
	 * 
	 * @Element referenceToPrecedingDataSetVersion
	 */
	public List<DataSetReference> getPrecedingDataSets() {
		return precedingDataSets;
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
	 * "Contact data set" of the person or entity who owns this data set. (Note:
	 * this is not necessarily the publisher of the data set.)
	 * 
	 * @Element referenceToOwnershipOfDataSet
	 */
	@ContextField(name = "referenceToOwnershipOfDataSet", parentName = "publicationAndOwnership", type = Type.DataSetReference)
	public DataSetReference ownership;
	
}
