package org.openlca.olcatdb.ecospold2;

import org.openlca.olcatdb.parsing.Context;

/**
 * @Element childActivityDataset
 * @ContentModel (activityDescription, flowData, modellingAndValidation,
 *               administrativeInformation, namespace:uri="##other")
 */
@Context(name = "childActivityDataset", parentName = "ecoSpold")
public class ES2ChildDataset extends ES2Dataset {

}
