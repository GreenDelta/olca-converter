package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * @Element contactDataSet
 * @ContentModel (contactInformation, administrativeInformation?, other?)
 */
@Context(name = "contactDataSet")
public class ILCDContact extends ContextObject {

	/**
	 * @Element dataSetInformation
	 * @ContentModel (UUID, shortName*, name*, classificationInformation?,
	 *               contactAddress*, telephone?, telefax?, email?, WWWAddress?,
	 *               centralContactPoint*, contactDescriptionOrComment*,
	 *               referenceToContact*, referenceToLogo?, other?)
	 */
	@SubContext(contextClass = ILCDContactDescription.class)
	public ILCDContactDescription description;
	
	/**
	 * Staff or entity, that documented the generated data set, entering the
	 * information into the database; plus administrative information linked to the
	 * data entry activity.
	 * 
	 * @Element dataEntryBy
	 * @ContentModel (((timeStamp?, referenceToDataSetFormat*)), other?)
	 */
	@SubContext(contextClass = ILCDEntry.class)
	public ILCDEntry entry;
	
	/**
	 * Information related to publication and version management of the data set
	 * including copyright and access restrictions.
	 * 
	 * @Element publicationAndOwnership
	 * @ContentModel (((dataSetVersion, referenceToPrecedingDataSetVersion*,
	 *               permanentDataSetURI?)), referenceToOwnershipOfDataSet?, other?)
	 */
	@SubContext(contextClass = ILCDPublication.class)
	public ILCDPublication publication;
	
}
