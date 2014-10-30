package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * The stored attributes of an EcoSpold 01 compartment / elementary flow
 * categories.
 * 
 * @author Michael Srocka
 * 
 */
public class ES1CompartmentRec {

	/**
	 * The compartment ID.
	 */
	private int id = -1;

	/**
	 * The name of the compartment / category.
	 */
	private String compartment;

	/**
	 * The name of the sub-compartment / sub-category.
	 */
	private String subCompartment;

	/**
	 * The compartment ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The compartment ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The name of the compartment / category.
	 */
	public String getCompartment() {
		return compartment;
	}

	/**
	 * The name of the compartment / category.
	 */
	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}

	/**
	 * The name of the sub-compartment / sub-category.
	 */
	public String getSubCompartment() {
		return subCompartment;
	}

	/**
	 * The name of the sub-compartment / sub-category.
	 */
	public void setSubCompartment(String subCompartment) {
		this.subCompartment = subCompartment;
	}

	/**
	 * Returns the EcoSpold 01 compartment / elementary flow category record for
	 * the given ID from the database, or <code>null</code> if no such record
	 * exists.
	 */
	public static ES1CompartmentRec forID(int id) {
		ES1CompartmentRec rec = null;
		String query = "SELECT * FROM ES1_COMPARTMENTS WHERE id=" + id;
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = new ES1CompartmentRec();
				rec.id = id;
				rec.compartment = rs.getString("compartment");
				rec.subCompartment = rs.getString("subCompartment");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * Returns the EcoSpold 01 compartment / elementary flow category record for
	 * the given compartment and sub-compartment names from the database, or
	 * <code>null</code> if no such record exists.
	 */
	public static ES1CompartmentRec forNames(String compartment,
			String subCompartment) {
		ES1CompartmentRec rec = null;
		String query = "SELECT id FROM ES1_COMPARTMENTS WHERE compartment='"
				+ compartment + "' AND subCompartment='" + subCompartment + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = new ES1CompartmentRec();
				rec.id = rs.getInt("id");
				rec.compartment = compartment;
				rec.subCompartment = subCompartment;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rec;
	}
}
