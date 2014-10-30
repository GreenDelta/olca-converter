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
 *               referenceToConvertedOriginalDataSetFrom?,
 *               ((referenceToPersonOrEntityEnteringTheData?)),
 *               referenceToDataSetUseApproval*, other?)
 */
@Context(name = "dataEntryBy", parentName = "administrativeInformation")
public class ILCDProcessEntry extends ContextObject {

	/**
	 * Date and time stamp of data set generation, typically an automated entry
	 * ("last saved").
	 * 
	 * @Element timeStamp
	 * @DataType dateTime
	 */
	@ContextField(name = "timeStamp", parentName = "dataEntryBy")
	public String timeStamp;

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
	private List<DataSetReference> dataFormatReferences = new ArrayList<DataSetReference>();

	// getters and setters
	
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
	public List<DataSetReference> getDataFormatReferences() {
		return dataFormatReferences;
	}

	/**
	 * "Source data set" of the database or data set publication from which this
	 * data set has been obtained by conversion. This can cover e.g. conversion
	 * to a different format, applying a different nomenclature, mapping of flow
	 * names, conversion of units, etc. This may however not have changed or
	 * re-modeled the Inputs and Outputs, i.e. obtaining the same LCIA results.
	 * This entry is required for converted data sets stemming originally from
	 * other LCA databases (e.g. when re-publishing data from IISI, ILCD etc.
	 * databases). [Note: Identically re-published data sets are identied in the
	 * field "Unchanged re-publication of:" in the section
	 * "Publication and Ownership".]
	 * 
	 * @Element referenceToConvertedOriginalDataSetFrom
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToConvertedOriginalDataSetFrom", parentName = "dataEntryBy", type = Type.DataSetReference)
	public DataSetReference originalFormatReference;

	/**
	 * "Contact data set" of the responsible person or entity that has
	 * documented this data set, i.e. entered the data and the descriptive
	 * information.
	 * 
	 * @Element referenceToPersonOrEntityEnteringTheData
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToPersonOrEntityEnteringTheData", parentName = "dataEntryBy", type = Type.DataSetReference)
	public DataSetReference dataSetEntryReference;

	/**
	 * "Source data set": Names exclusively the producer or operator of the
	 * good, service or technology represented by this data set, which
	 * officially has approved this data set in all its parts. In case of
	 * nationally or internationally averaged data sets, this will be the
	 * respective business association. If no official approval has been given,
	 * the entry "No official approval by producer or operator" is to be entered
	 * and the reference will point to an empty "Contact data set". [Notes: The
	 * producer or operator may only be named here, if a written approval of
	 * this data set was given. A recognition of this data set by any other
	 * organisation then the producer/operator of the good, service, or process
	 * is not to be stated here, but as a "review" in the validation section.]
	 * 
	 * @Element referenceToDataSetUseApproval
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToDataSetUseApproval", parentName = "dataEntryBy", isMultiple = true, type = Type.DataSetReference)
	private List<DataSetReference> useApprovalReferences = new ArrayList<DataSetReference>();

	/**
	 * "Source data set": Names exclusively the producer or operator of the
	 * good, service or technology represented by this data set, which
	 * officially has approved this data set in all its parts. In case of
	 * nationally or internationally averaged data sets, this will be the
	 * respective business association. If no official approval has been given,
	 * the entry "No official approval by producer or operator" is to be entered
	 * and the reference will point to an empty "Contact data set". [Notes: The
	 * producer or operator may only be named here, if a written approval of
	 * this data set was given. A recognition of this data set by any other
	 * organisation then the producer/operator of the good, service, or process
	 * is not to be stated here, but as a "review" in the validation section.]
	 * 
	 * @Element referenceToDataSetUseApproval
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	public List<DataSetReference> getUseApprovalReferences() {
		return useApprovalReferences;
	}
	
	
}
