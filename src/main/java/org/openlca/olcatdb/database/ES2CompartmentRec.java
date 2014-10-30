package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * 
 * The stored attributes of an EcoSpold 2 compartment in the database.
 * 
 * @author Michael Srocka
 * 
 */
public class ES2CompartmentRec {

	/**
	 * The compartment ID.
	 */
	private String id;

	/**
	 * The name of the compartment.
	 */
	private String compartment;

	/**
	 * The name of the sub-compartment
	 */
	private String subCompartment;

	/**
	 * The compartment ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The compartment ID.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The name of the compartment.
	 */
	public String getCompartment() {
		return compartment;
	}

	/**
	 * The name of the compartment.
	 */
	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}

	/**
	 * The name of the sub-compartment
	 */
	public String getSubCompartment() {
		return subCompartment;
	}

	/**
	 * The name of the sub-compartment
	 */
	public void setSubCompartment(String subCompartment) {
		this.subCompartment = subCompartment;
	}

	/**
	 * Returns the EcoSpold 02 compartment record for the given ID from the
	 * database, or <code>null</code> if no such record exists.
	 */
	public static ES2CompartmentRec forID(String id) {
		ES2CompartmentRec rec = null;
		String query = "SELECT * FROM ES2_COMPARTMENTS WHERE id='" + id + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				rec = new ES2CompartmentRec();
				rec.compartment = rs.getString("compartment");
				rec.id = id;
				rec.subCompartment = rs.getString("subCompartment");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return rec;
	}

}
