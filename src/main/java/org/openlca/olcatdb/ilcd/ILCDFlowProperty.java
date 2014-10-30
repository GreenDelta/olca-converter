package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

@Context(name = "flowPropertyDataSet")
public class ILCDFlowProperty extends ContextObject {

	/**
	 * 
	 * @Element dataSetInformation
	 * @ContentModel (UUID, name*, synonyms*, classificationInformation?,
	 *               generalComment*, other?)
	 */
	@SubContext(contextClass = ILCDFlowPropertyDescription.class)
	public ILCDFlowPropertyDescription description;

	/**
	 * "Unit group data set" and its reference unit, in which the Flow property
	 * is measured.
	 * 
	 * @Element referenceToReferenceUnitGroup
	 * @ContentModel (subReference*, shortDescription*, other?)
	 */
	@ContextField(name = "referenceToReferenceUnitGroup", parentName = "quantitativeReference", type = Type.DataSetReference)
	public DataSetReference unitGroup;

	/**
	 * "Source data set" of data source(s) used for the data set e.g. a paper, a
	 * questionnaire, a monography etc. The main raw data sources should be
	 * named, too. [Note: relevant especially for market price data.]
	 * 
	 * @Element referenceToDataSource
	 */
	@ContextField(name = "referenceToDataSource", parentName = "dataSourcesTreatmentAndRepresentativeness", type = Type.DataSetReference, isMultiple = true)
	private List<DataSetReference> dataSources = new ArrayList<DataSetReference>();

	/**
	 * "Source data set" of data source(s) used for the data set e.g. a paper, a
	 * questionnaire, a monography etc. The main raw data sources should be
	 * named, too. [Note: relevant especially for market price data.]
	 * 
	 * @Element referenceToDataSource
	 */
	public List<DataSetReference> getDataSources() {
		return dataSources;
	}

	/**
	 * Statements on compliance of several data set aspects with compliance
	 * requirements as defined by the referenced compliance system (e.g. an EPD
	 * scheme, handbook of a national or international data network such as the
	 * ILCD, etc.).
	 * 
	 * @Element complianceDeclarations
	 * @ContentModel (compliance+, other?)
	 */
	@SubContext(contextClass = ILCDCompliance.class, isMultiple = true)
	private List<ILCDCompliance> complianceDeclarations = new ArrayList<ILCDCompliance>();

	/**
	 * Statements on compliance of several data set aspects with compliance
	 * requirements as defined by the referenced compliance system (e.g. an EPD
	 * scheme, handbook of a national or international data network such as the
	 * ILCD, etc.).
	 * 
	 * @Element complianceDeclarations
	 * @ContentModel (compliance+, other?)
	 */
	public List<ILCDCompliance> getComplianceDeclarations() {
		return complianceDeclarations;
	}

	/**
	 * Staff or entity, that documented the generated data set, entering the
	 * information into the database; plus administrative information linked to
	 * the data entry activity.
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
	 *               permanentDataSetURI?)), referenceToOwnershipOfDataSet?,
	 *               other?)
	 */
	@SubContext(contextClass = ILCDPublication.class)
	public ILCDPublication publication;
}
