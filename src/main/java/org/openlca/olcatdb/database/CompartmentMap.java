package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * The managed mappings between the elementary flow categories / compartments of
 * the different formats.
 * 
 * @author Michael Srocka
 * 
 */
public class CompartmentMap {

	/**
	 * Returns the ID of the ILCD elementary flow category for the given
	 * EcoSpold 01 compartment ID, or <code>null</code> if there is no mapping
	 * for this compartment in the database.
	 */
	public static ILCDCompartmentRec es1ToILCD(int eCompId) {
		ILCDCompartmentRec rec = null;
		String query = "SELECT ilcdCompartmentId FROM COMPARTMENT_MAP_ES1_TO_ILCD "
				+ "WHERE es1CompartmentId =" + eCompId;
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = ILCDCompartmentRec.forID(rs.getString("ilcdCompartmentId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}
	
	/**
	 * Returns the ID of the ILCD elementary flow category for the given
	 * EcoSpold 02 compartment ID, or <code>null</code> if there is no mapping
	 * for this compartment in the database.
	 */
	public static String es2ToILCD(String eCompId) {
		String iCatId = null;
		String query = "SELECT ilcdCompartmentId FROM COMPARTMENT_MAP_ES2_TO_ILCD "
				+ "WHERE es2CompartmentId ='" + eCompId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				iCatId = rs.getString("ilcdCompartmentId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iCatId;
	}

	/**
	 * Returns the ID of the EcoSpold 02 compartment for the given ILCD
	 * elementary flow category ID, or <code>null</code> if there is no mapping
	 * for this category in the database.
	 */
	public static String ilcdToES2(String ilcdCatId) {
		String compId = null;
		String query = "SELECT es2CompartmentId FROM COMPARTMENT_MAP_ILCD_TO_ES2 " +
				"WHERE ilcdCompartmentId='" +ilcdCatId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				compId = rs.getString("es2CompartmentId");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return compId;
	}
	
	/**
	 * Returns the ID of the EcoSpold 01 compartment for the given ILCD
	 * elementary flow category ID, or <code>-1</code> if there is no mapping
	 * for this category in the database.
	 */
	public static int ilcdToES1(String ilcdCatId) {
		int compId = -1;
		String query = "SELECT es1CompartmentId FROM COMPARTMENT_MAP_ILCD_TO_ES1 " +
				"WHERE ilcdCompartmentId='" +ilcdCatId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				compId = rs.getInt("es1CompartmentId");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return compId;
	}

}
