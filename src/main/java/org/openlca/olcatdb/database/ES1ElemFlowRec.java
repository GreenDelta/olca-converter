package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * The stored attributes of an EcoSpold 01 elementary exchange.
 * 
 * @author Michael Srocka
 * 
 */
public class ES1ElemFlowRec {

	private int id;

	private String name;

	private String cas;

	private String formula;

	private String unit;

	private int compartmentId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(int compartmentId) {
		this.compartmentId = compartmentId;
	}

	/**
	 * Returns the stored EcoSpold 01 record for the given attributes, or
	 * <code>null</code> if there is no flow stored for these attributes.
	 */
	public static ES1ElemFlowRec forAtts(String name, String compartment,
			String subCompartment, String unit) {

		ES1ElemFlowRec rec = null;

		try {
			// select the compartment
			String query = "SELECT id FROM ES1_COMPARTMENTS WHERE "
					+ "compartment='" + compartment + "' AND subCompartment='"
					+ subCompartment + "'";
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				int compartmentId = rs.getInt("id");
				
				query = "SELECT id, cas, formula FROM ES1_ELEM_FLOWS WHERE " +
						"name='" +  name + "' AND unit='" + unit + "' AND compartmentId=" +compartmentId;
				rs = Database.getInstance().query(query);
				if (rs.next()) {
					rec = new ES1ElemFlowRec();
					rec.id = rs.getInt("id");
					rec.cas = rs.getString("cas");
					rec.formula = rs.getString("formula");
					rec.compartmentId = compartmentId;
					rec.name = name;
					rec.unit = unit;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rec;
	}

	/**
	 * Returns the elementary flow record for the given ID, or <code>null</code>
	 * if no such record is stored in the database. Note, that the id must be an
	 * integer (as string).
	 */
	public static ES1ElemFlowRec forID(String id) {
		ES1ElemFlowRec rec = null;

		String query = "SELECT id, name, cas, formula, unit, compartmentId "
				+ "FROM ES1_ELEM_FLOWS WHERE id=" + id;

		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = new ES1ElemFlowRec();
				rec.id = rs.getInt("id");
				rec.name = rs.getString("name");
				rec.cas = rs.getString("cas");
				rec.formula = rs.getString("formula");
				rec.unit = rs.getString("unit");
				rec.compartmentId = rs.getInt("compartmentId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rec;
	}

}
