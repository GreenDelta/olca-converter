package org.openlca.olcatdb.conversion;

import java.io.File;

import org.openlca.olcatdb.database.ILCDPropertyRec;
import org.openlca.olcatdb.database.UnitMap;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.ilcd.ILCDCompliance;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDFlowDescription;
import org.openlca.olcatdb.ilcd.ILCDFlowEntry;
import org.openlca.olcatdb.ilcd.ILCDPublication;
import org.openlca.olcatdb.ilcd.resources.ILCDFolder;
import org.openlca.olcatdb.templates.TemplateType;
import org.openlca.olcatdb.xml.XmlOutputter;

/**
 * A helper class for creating ILCD output.
 * 
 * @author Michael Strocka
 * 
 */
class ILCDHelper {

	/**
	 * Creates a new reference to a flow data set. New created flow data sets
	 * can be identified by its version number which is 01.00.000 whereas
	 * assigned ILCD elementary flows get a version number of 02.00.000.
	 * 
	 * @param uuid
	 *            the UUID of the flow
	 * @param name
	 *            the flow name
	 * @return a data set reference
	 */
	public static DataSetReference newFlowRef(String uuid, String name) {

		DataSetReference flowRef = new DataSetReference();
		flowRef.setRefObjectId(uuid);
		flowRef.setType(ILCDDataSetType.Flow.toString());
		flowRef.setUri("../flows/" + uuid + ".xml");
		flowRef.setVersion("01.00.000");
		flowRef.getDescription().add(new LangString(name));
		flowRef.setName(name);

		return flowRef;
	}

	/**
	 * Creates a the frame for an ILCD flow with the given attributes.
	 */
	public static ILCDFlow makeFlow(String uuid, String name, String cas,
			String formula, String type, UnitMap.Entry unitEntry) {

		ILCDFlow flow = new ILCDFlow();

		// flow description
		ILCDFlowDescription descr = new ILCDFlowDescription();
		flow.description = descr;
		descr.uuid = uuid;
		descr.getName().add(new LangString(name));
		descr.casNumber = cas;
		descr.sumFormula = formula;

		// flow type
		flow.type = type;

		// compliance declarations
		ILCDCompliance compliance = new ILCDCompliance();
		flow.getComplianceDeclarations().add(compliance);
		compliance.complianceSystem = ReferenceFactory.ILCD_COMPLIANCE
				.createReference();
		compliance.overallCompliance = "Not defined";

		// data entry
		ILCDFlowEntry entry = new ILCDFlowEntry();
		flow.entry = entry;
		entry.timestamp = Time.now();
		entry.getDataSetFormats().add(
				ReferenceFactory.ILCD_FORMAT.createReference());
		entry.getDataSetFormats().add(
				ReferenceFactory.ECOSPOLD_FORMAT.createReference());

		// publication
		ILCDPublication pub = new ILCDPublication();
		flow.publication = pub;
		pub.dataSetVersion = "01.00.000";

		// the flow property reference / value
		if (unitEntry != null) {
			flow.referenceFlowProperty = 0;
			ILCDPropertyRec propertyRec = ILCDPropertyRec.forID(unitEntry
					.getId());
			if (propertyRec != null) {
				flow.getFlowPropertyValues().add(propertyRec.toValue());
			}
		}

		return flow;
	}

	/**
	 * Writes the given flow to the given folder and adds a reference to the
	 * created files.
	 */
	public static void writeFlow(ILCDFolder folder, DataSetReference flowRef,
			ILCDFlow flow, FileIndex createdFiles) {

		File f = folder.file(flowRef);
		new XmlOutputter().output(flow, TemplateType.ILCDFlow, f, false);

		// a reference for the created files
		DataSetReference cRef = flowRef.copy();
		cRef.setUri(f.toURI().toString());
		createdFiles.add(cRef);
	}

}
