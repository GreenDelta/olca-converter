package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Staff or entity, that documented the generated data set, entering the
 * information into the database; plus administrative information linked to the
 * data entry activity.
 * 
 * @Element dataEntryBy
 * @ContentModel (((timeStamp?, referenceToDataSetFormat*)),
 *               ((referenceToPersonOrEntityEnteringTheData?)), other?)
 */
@Context(name = "dataEntryBy", parentName = "administrativeInformation")
public class ILCDFlowEntry extends ContextObject {

	/**
	 * Date and time stamp of data set generation, typically an automated entry
	 * ("last saved").
	 * 
	 * @Element timeStamp
	 * @DataType dateTime
	 */
	@ContextField(name = "timeStamp", parentName = "dataEntryBy")
	public String timestamp;

	/**
	 * "Source data set" of the used version of the ILCD format. If additional
	 * data format fields have been integrated into the data set file, using the
	 * "namespace" option, the used format namespace(s) are to be given. This is
	 * the case if the data sets carries additional information as specified by
	 * other, particular LCA formats, e.g. of other database networks or LCA
	 * softwares.
	 * 
	 * @Element referenceToDataSetFormat
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToDataSetFormat", parentName = "dataEntryBy", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> dataSetFormats = new ArrayList<DataSetReference>();

	/**
	 * "Source data set" of the used version of the ILCD format. If additional
	 * data format fields have been integrated into the data set file, using the
	 * "namespace" option, the used format namespace(s) are to be given. This is
	 * the case if the data sets carries additional information as specified by
	 * other, particular LCA formats, e.g. of other database networks or LCA
	 * softwares.
	 * 
	 * @Element referenceToDataSetFormat
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getDataSetFormats() {
		return dataSetFormats;
	}

	/**
	 * "Contact data set" of the responsible person or entity that has
	 * documented this data set, i.e. entered the data and the descriptive
	 * information.
	 * 
	 * @Element referenceToPersonOrEntityEnteringTheData
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToPersonOrEntityEnteringTheData", parentName = "dataEntryBy", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> personsOrEntities = new ArrayList<DataSetReference>();

	/**
	 * "Contact data set" of the responsible person or entity that has
	 * documented this data set, i.e. entered the data and the descriptive
	 * information.
	 * 
	 * @Element referenceToPersonOrEntityEnteringTheData
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getPersonsOrEntities() {
		return personsOrEntities;
	}
}
