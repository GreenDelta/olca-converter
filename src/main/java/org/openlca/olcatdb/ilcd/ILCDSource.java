package org.openlca.olcatdb.ilcd;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;

/**
 * Data set for bibliographical references to sources used, but also for
 * reference to data set formats, databases, conformity systems etc.
 * 
 * @Element sourceDataSet
 * @ContentModel (sourceInformation, administrativeInformation?, other?)
 */
@Context(name = "sourceDataSet")
public class ILCDSource extends ContextObject {

	/**
	 * @Element dataSetInformation
	 * @ContentModel (UUID, shortName*, classificationInformation?, sourceCitation?,
	 *               publicationType?, sourceDescriptionOrComment*,
	 *               referenceToDigitalFile*, referenceToContact*, referenceToLogo?,
	 *               other?)
	 */
	@SubContext(contextClass = ILCDSourceDescription.class)
	public ILCDSourceDescription description;
	
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
