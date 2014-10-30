package org.openlca.olcatdb.templates;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.ecospold1.ES1EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.masterdata.ES2ClassificationSystemList;
import org.openlca.olcatdb.ecospold2.masterdata.ES2UnitList;

/**
 * The supported template types.
 * 
 * @author Michael Srocka
 * 
 *         (last change 19/12/2009)
 */
public enum TemplateType {

	/**
	 * Template for an EcoSpold 01 process data set.
	 */
	EcoSpold01("EcoSpold1.vtl", "ecoSpold", ES1EcoSpold.class),

	/**
	 * Template for an EcoSpold 02 activity data set.
	 */
	EcoSpold02("EcoSpold2.vtl", "ecoSpold", ES2EcoSpold.class),

	/**
	 * Template for an EcoSpold 02 master-data unit list.
	 */
	ES2Units("ES2Units.vtl", "unitList", ES2UnitList.class),

	/**
	 * Template for an EcoSpold 02 master data file with valid sources.
	 */
	ES2SourceList("ES2Sources.vtl", "sources",
			org.openlca.olcatdb.ecospold2.masterdata.ES2SourceList.class),

	/**
	 * Template for an EcoSpold 02 master data file with valid activity names.
	 */
	ES2ActivityNameList("ES2ActivityNames.vtl", "activities",
			org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityNameList.class),

	/**
	 * Template for an EcoSpold 02 master data file with valid activity
	 * descriptors.
	 */
	ES2ActivityList("ES2ActivityList.vtl", "activities",
			org.openlca.olcatdb.ecospold2.masterdata.ES2ActivityList.class),

	ES2Classifications("ES2Classifications.vtl", "systems",
			ES2ClassificationSystemList.class),

	ES2ElemFlowList("ES2ElementaryExchanges.vtl", "exchanges",
			org.openlca.olcatdb.ecospold2.masterdata.ES2ElemFlowList.class),

	ES2ProductFlowList("IntermediateExchanges.vtl", "exchanges",
			org.openlca.olcatdb.ecospold2.masterdata.ES2ProductFlowList.class),

	/**
	 * Template for an EcoSpold 02 master data file with valid geographies.
	 */
	ES2GeographyList("ES2Geographies.vtl", "geographies",
			org.openlca.olcatdb.ecospold2.masterdata.ES2GeographyList.class),

	/**
	 * Template for an EcoSpold 02 master data file with valid geographies.
	 */
	ES2PersonList("ES2Persons.vtl", "persons",
			org.openlca.olcatdb.ecospold2.masterdata.ES2PersonList.class),

	/**
	 * Template for the EcoSpold 02 Taglist.
	 */
	ES2TagList("ES2Tags.vtl", "tagList",
			org.openlca.olcatdb.ecospold2.masterdata.ES2TagList.class),

	/**
	 * Template for the EcoSpold 02 CompartmentsList.
	 */
	ES2CompartmentList("ES2Compartments.vtl", "compartmentList",
			org.openlca.olcatdb.ecospold2.masterdata.ES2CompartmentList.class),

	/**
	 * Template for the EcoSpold 02 uncertainty type.
	 */
	ES2Uncertainty("ES2Uncertainty.vtl", "uncertainty",
			org.openlca.olcatdb.ecospold2.ES2Uncertainty.class),

	/**
	 * Template for an ILCD process data set.
	 */
	ILCDProcess("ILCDProcess.vtl", "process",
			org.openlca.olcatdb.ilcd.ILCDProcess.class),

	/**
	 * Template for an ILCD flow data set.
	 */
	ILCDFlow("ILCDFlow.vtl", "flow", org.openlca.olcatdb.ilcd.ILCDFlow.class),

	/**
	 * Template for an ILCD flow property
	 */
	ILCDFlowProperty("ILCDFlowProperty.vtl", "flowProperty",
			org.openlca.olcatdb.ilcd.ILCDFlowProperty.class),

	/**
	 * Template for an ILCD unit group data set.
	 */
	ILCDUnitGroup("ILCDUnitGroup.vtl", "unitGroup",
			org.openlca.olcatdb.ilcd.ILCDUnitGroup.class),

	/**
	 * Template for an ILCD source data set.
	 */
	ILCDSource("ILCDSource.vtl", "source",
			org.openlca.olcatdb.ilcd.ILCDSource.class),

	/**
	 * Template for an ILCD contact data set.
	 */
	ILCDContact("ILCDContact.vtl", "contact",
			org.openlca.olcatdb.ilcd.ILCDContact.class),

	/**
	 * Template for ILCD data set reference type.
	 */
	ILCDDataSetReference("ILCDDataSetRef.vtl", "refElem refVal",
			DataSetReference.class),

	/**
	 * Template for the conversion index.
	 */
	FileIndex("FileIndex.vtl", "index",
			org.openlca.olcatdb.conversion.FileIndex.class);

	/** The template file name. */
	private final String templateName;

	/** The name of the (root) context object used IN the template. */
	private final String contextName;

	/** The type of the allowed (root) context object of the template. */
	private final Class<?> contextType;

	/**
	 * Creates a new template type.
	 * 
	 * @param templateName
	 *            The template file name.
	 * @param contextName
	 *            The name of the (root) context object used IN the template.
	 * @param contextType
	 *            The type of the allowed (root) context object of the template.
	 */
	private TemplateType(String templateName, String contextName,
			Class<?> contextType) {
		this.templateName = templateName;
		this.contextName = contextName;
		this.contextType = contextType;
	}

	/**
	 * Get the template file name.
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Get the name of the (root) context object used IN the template.
	 */
	public String getContextName() {
		return contextName;
	}

	/**
	 * Get the type of the allowed (root) context object of the template.
	 */
	public Class<?> getContextType() {
		return contextType;
	}
}