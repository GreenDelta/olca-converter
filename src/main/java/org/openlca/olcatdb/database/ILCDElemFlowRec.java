package org.openlca.olcatdb.database;

import java.sql.ResultSet;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.Time;
import org.openlca.olcatdb.ilcd.ILCDCompliance;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDFlowDescription;
import org.openlca.olcatdb.ilcd.ILCDFlowEntry;
import org.openlca.olcatdb.ilcd.ILCDFlowPropertyValue;
import org.openlca.olcatdb.ilcd.ILCDPublication;

/**
 * The stored attributes of an ILCD elementary flow.
 * 
 * 
 * @author Michael Srocka
 * 
 */
public class ILCDElemFlowRec {

	/**
	 * The UUID of the flow.
	 */
	private String id;

	/**
	 * The ID of the category / compartment.
	 */
	private String compartmentId;

	/**
	 * The name of the flow.
	 */
	private String name;

	/**
	 * The CAS number of the flow.
	 */
	private String cas;

	/**
	 * The sum-formula of the flow.
	 */
	private String formula;

	/**
	 * The ID of the reference flow property.
	 */
	private String propertyId;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(String compartmentId) {
		this.compartmentId = compartmentId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cas
	 */
	public String getCas() {
		return cas;
	}

	/**
	 * @param cas
	 *            the cas to set
	 */
	public void setCas(String cas) {
		this.cas = cas;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId
	 *            the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Creates a standard ILCD flow data set for this record.
	 */
	public ILCDFlow toFlow() {
		ILCDFlow flow = new ILCDFlow();
		flow.type = "Elementary flow";

		// flow information
		ILCDFlowDescription description = new ILCDFlowDescription();
		flow.description = description;
		description.casNumber = this.cas;
		description.sumFormula = this.formula;
		description.uuid = this.id;
		description.getName().add(new LangString(this.name));

		// the flow category
		ILCDCompartmentRec rec = ILCDCompartmentRec.forID(this.compartmentId);
		if (rec != null) {
			flow.description.getElemFlowCategorizations().add(
					rec.toCategorization());
		}

		flow.referenceFlowProperty = 0;
		flow.type = "Elementary flow";

		// compliance
		ILCDCompliance compliance = new ILCDCompliance();
		flow.getComplianceDeclarations().add(compliance);
		DataSetReference complianceRef = new DataSetReference();
		compliance.complianceSystem = complianceRef;
		complianceRef.setRefObjectId("88d4f8d9-60f9-43d1-9ea3-329c10d7d727");
		complianceRef
				.setUri("../sources/ILCD_Compliance_88d4f8d9-60f9-43d1-9ea3-329c10d7d727.xml");
		complianceRef.setType(ILCDDataSetType.Source.toString());
		complianceRef.getDescription().add(
				new LangString("ILCD Data Network compliance"));
		compliance.overallCompliance = "Fully compliant";

		// data entry
		ILCDFlowEntry entry = new ILCDFlowEntry();
		flow.entry = entry;
		entry.timestamp = Time.now();
		DataSetReference formatRef = new DataSetReference();
		entry.getDataSetFormats().add(formatRef);
		formatRef.setRefObjectId("a97a0155-0234-4b87-b4ce-a45da52f2a40");
		formatRef.setType(ILCDDataSetType.Source.toString());
		formatRef
				.setUri("../sources/ILCD_Format_a97a0155-0234-4b87-b4ce-a45da52f2a40.xml");
		formatRef.setVersion("01.00.000");
		formatRef.getDescription().add(new LangString("ILCD format"));

		DataSetReference entryRef = new DataSetReference();
		entry.getPersonsOrEntities().add(entryRef);
		entryRef.setRefObjectId("d0d5f8bb-9311-49d1-9e30-2f20a6977f4f");
		entryRef.setType(ILCDDataSetType.Contact.toString());
		entryRef.setUri("../contacts/d0d5f8bb-9311-49d1-9e30-2f20a6977f4fl");
		entryRef.setVersion("01.00.000");
		entryRef.getDescription().add(new LangString("EPLCA working group"));

		// publication and ownership
		ILCDPublication publication = new ILCDPublication();
		flow.publication = publication;
		publication.dataSetVersion = "02.00.000";
		publication.permanentDataSetURI = "http://lca.jrc.ec.europa.eu/lcainfohub/datasets/flows/"
				+ this.id + "_" + publication.dataSetVersion + ".xml";
		DataSetReference ownerRef = new DataSetReference();
		publication.ownership = ownerRef;
		ownerRef.setRefObjectId("d0d5f8bb-9311-49d1-9e30-2f20a6977f4f");
		ownerRef.setType(ILCDDataSetType.Contact.toString());
		ownerRef.setUri("../contacts/d0d5f8bb-9311-49d1-9e30-2f20a6977f4f.xml");
		ownerRef.setVersion("01.00.001");
		ownerRef.getDescription().add(new LangString("EPLCA working group"));

		// the reference flow property
		ILCDFlowPropertyValue propVal = new ILCDFlowPropertyValue();
		propVal.id = 0;
		propVal.meanValue = 1.0;
		ILCDPropertyRec propertyRec = ILCDPropertyRec.forID(this.propertyId);
		if (propertyRec != null) {
			propVal.flowPropertyDataSet = propertyRec.toReference();
		}
		flow.getFlowPropertyValues().add(propVal);

		return flow;
	}

	/**
	 * Creates a data set reference for this record.
	 */
	public DataSetReference toReference() {
		DataSetReference ref = new DataSetReference();
		ref.setName(this.name);
		ref.setRefObjectId(this.id);
		ref.setType(ILCDDataSetType.Flow.toString());
		ref.setUri("../flows/" + this.id + ".xml");
		ref.setVersion("02.00.000");
		ref.getDescription().add(new LangString(this.name));
		return ref;
	}

	/**
	 * Returns the ILCD elementary flow record for the given ID from the
	 * database or <code>null</code> if there is no record stored for this ID.
	 */
	public static ILCDElemFlowRec forID(String flowId) {
		ILCDElemFlowRec rec = null;

		String query = "SELECT * FROM ILCD_ELEM_FLOWS WHERE id='" + flowId
				+ "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = new ILCDElemFlowRec();
				rec.cas = rs.getString("cas");
				rec.compartmentId = rs.getString("compartmentId");
				rec.formula = rs.getString("formula");
				rec.id = flowId;
				rec.name = rs.getString("name");
				rec.propertyId = rs.getString("propertyId");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rec;
	}

}
