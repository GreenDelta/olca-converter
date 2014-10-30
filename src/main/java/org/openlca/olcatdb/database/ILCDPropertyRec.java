package org.openlca.olcatdb.database;

import java.sql.ResultSet;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDFlowPropertyValue;

/**
 * 
 * The stored attributes of an ILCD flow property in the database.
 * 
 * @author Michael Srocka
 * 
 */
public class ILCDPropertyRec {

	/**
	 * The UUID of the property.
	 */
	private String id;

	/**
	 * The name of the property.
	 */
	private String name;

	/**
	 * The reference unit group of this property.
	 */
	private String unitGroupId;

	/**
	 * The UUID of the property.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The UUID of the property.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The name of the property.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The name of the property.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the unit group ID.
	 */
	public String getUnitGroupId() {
		return unitGroupId;
	}

	/**
	 * Set the unit group ID.
	 */
	public void setUnitGroupId(String unitGroupId) {
		this.unitGroupId = unitGroupId;
	}

	/**
	 * Returns the stored flow property record in the database for the given ID.
	 */
	public static ILCDPropertyRec forID(String propId) {
		ILCDPropertyRec rec = null;
		String query = "SELECT * FROM ILCD_FLOW_PROPERTIES WHERE id='" + propId
				+ "'";
		try {
			ResultSet set = Database.getInstance().query(query);
			if (set.next()) {
				rec = new ILCDPropertyRec();
				rec.id = set.getString("id");
				rec.name = set.getString("name");
				rec.unitGroupId = set.getString("unitGroupId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * Creates a data set reference for this flow property record.
	 */
	public DataSetReference toReference() {
		DataSetReference ref = new DataSetReference();
		ref.setName(this.name);
		ref.setRefObjectId(this.id);
		ref.setType(ILCDDataSetType.FlowProperty.toString());
		ref.setUri("../flowproperties/" + this.id + ".xml");
		ref.setVersion("02.00.000");
		ref.getDescription().add(new LangString(this.name));
		return ref;
	}
	
	/**
	 * Creates an ILCD flow property for this record value ready
	 * to be used in ILCD flow data sets. 
	 */
	public ILCDFlowPropertyValue toValue() {
		ILCDFlowPropertyValue propValue = new ILCDFlowPropertyValue();
		propValue.flowPropertyDataSet = this.toReference();
		propValue.id = 0;
		propValue.meanValue = 1d;
		return propValue;
	}
	

	/**
	 * Returns the reference unit of the flow property with the given ID, or
	 * <code>null</code> if there is no such flow property stored in the
	 * database.
	 */
	public static String unit(String id) {
		String unit = null;
		String query = "SELECT u.refUnit AS unit FROM ILCD_FLOW_PROPERTIES p "
				+ "INNER JOIN ILCD_UNIT_GROUPS u ON p.unitGroupId = u.id "
				+ "WHERE p.id='" + id + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				unit = rs.getString("unit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}

	// /**
	// * Returns all stored ILCD flow properties from the database.
	// */
	// public static List<ILCDPropertyRec> getAll() {
	//
	// List<ILCDPropertyRec> records = new ArrayList<ILCDPropertyRec>();
	// String query = "SELECT id, name, unit FROM ILCD_PROPERTIES";
	// try {
	// ResultSet set = Database.getInstance().query(query);
	// while(set.next()) {
	// ILCDPropertyRec rec = new ILCDPropertyRec();
	// rec.id = set.getString("id");
	// rec.name = set.getString("name");
	// rec.unit = set.getString("unit");
	// records.add(rec);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return records;
	// }

}
