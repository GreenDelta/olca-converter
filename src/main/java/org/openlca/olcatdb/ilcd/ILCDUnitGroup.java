package org.openlca.olcatdb.ilcd;

import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * @Element unitGroupDataSet
 * @ContentModel (unitGroupInformation, modellingAndValidation?,
 *               administrativeInformation?, units?, other?)
 */
@Context(name = "unitGroupDataSet")
public class ILCDUnitGroup extends ContextObject {

	/**
	 * 
	 * @Element dataSetInformation
	 * @ContentModel (UUID, name*, classificationInformation?, generalComment*,
	 *               other?)
	 */
	@SubContext(contextClass = ILCDUnitGroupDescription.class)
	public ILCDUnitGroupDescription description;

	/**
	 * This section identifies the quantitative reference of this data set, i.e.
	 * the "reference unit" in which the data set is expressed. It is the basis
	 * for the conversion to other units in the data set (e.g. for mass- related
	 * units "kg" as basis for conversion to and among "g", "ounces",
	 * "short tons", etc.).
	 * 
	 * @Element quantitativeReference
	 * @ContentModel (referenceToReferenceUnit, other?)
	 */
	@ContextField(name = "referenceToReferenceUnit", parentName = "quantitativeReference", type = Type.Integer)
	public Integer referenceUnit;

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

	/**
	 * @Element unit
	 * @ContentModel (name, meanValue, generalComment*, other?)
	 */
	@SubContext(contextClass = ILCDUnit.class, isMultiple = true)
	private List<ILCDUnit> units = new ArrayList<ILCDUnit>();

	/**
	 * @Element unit
	 * @ContentModel (name, meanValue, generalComment*, other?)
	 */
	public List<ILCDUnit> getUnits() {
		return units;
	}
	
	/**
	 * Get the reference unit of this unit group.
	 */
	public ILCDUnit getRefUnit() {
		ILCDUnit refUnit = null;		
		if(referenceUnit != null) {			
			for(ILCDUnit unit : this.units) {				
				if(referenceUnit == unit.dataSetInternalID) {
					refUnit = unit;
					break;
				}				
			}			
		}		
		return refUnit;
	}

}
