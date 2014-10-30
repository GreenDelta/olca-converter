package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * The stored attributes of an EcoSpold 02 unit.
 * 
 * @author Michael Srocka
 * 
 */
public class ES2UnitRec {

	private String id;

	private String name;

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
	 * Returns the stored unit record with the given ID from the database, or
	 * <code>null</code> if no such record is stored.
	 */
	public static ES2UnitRec forID(String id) {
		ES2UnitRec rec = null;
		String query = "SELECT * FROM ES2_UNITS WHERE id='"+id+"'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				rec = new ES2UnitRec();
				rec.id = rs.getString("id");
				rec.name = rs.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

}
