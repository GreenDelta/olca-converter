package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * The stored attributes of a EcoSpold 02 geography.
 * 
 * @author Michael Srocka
 * 
 */
public class ES2GeographyRec {

	private String id;

	private String shortName;

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

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Returns the EcoSpold 02 geography record for the given ID from the
	 * database, or <code>null</code> if no such record exists.
	 */
	public static ES2GeographyRec forID(String id) {
		ES2GeographyRec rec = null;
		String query = "SELECT * FROM ES2_GEOGRAPHIES WHERE id='" + id + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				rec = new ES2GeographyRec();
				rec.id = id;
				rec.shortName = rs.getString("shortName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}
	
	/**
	 * Returns the EcoSpold 02 geography record for the given short name (geo-code) from the
	 * database, or <code>null</code> if no such record exists.
	 */
	public static ES2GeographyRec forShortName(String shortName) {
		ES2GeographyRec rec = null;
		String query = "SELECT * FROM ES2_GEOGRAPHIES WHERE shortName='" + shortName + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				rec = new ES2GeographyRec();
				rec.id = rs.getString("id");
				rec.shortName = shortName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

}
