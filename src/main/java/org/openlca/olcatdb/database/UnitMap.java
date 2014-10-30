package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * A conversion map between units and flow properties of the different formats.
 * 
 * @author Michael Srocka
 * 
 */
public class UnitMap {

	/**
	 * Returns the conversion entry to an EcoSpold 02 unit from an ILCD flow
	 * property.
	 */
	public static Entry ildcToES2(String ilcdPropertyId) {
		Entry entry = null;
		String query = "SELECT es2UnitId, factor FROM UNIT_MAP_ILCD_TO_ES2 WHERE ilcdPropertyId='"
				+ ilcdPropertyId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				entry = new Entry();
				entry.factor = rs.getDouble("factor");
				entry.id = rs.getString("es2UnitId");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return entry;
	}

	/**
	 * Returns the conversion entry for an EcoSpold 01 unit (name) to be
	 * converted to an ILCD flow property.
	 * 
	 */
	public static Entry es1ToILCD(String unit) {
		Entry entry = null;
		String query = "SELECT ilcdPropertyId, factor FROM "
				+ "UNIT_MAP_ES1_TO_ILCD WHERE es1Unit='" + unit + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				entry = new Entry();
				entry.factor = rs.getDouble("factor");
				entry.id = rs.getString("ilcdPropertyId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return entry;
	}
	
	/**
	 * Returns the conversion entry for an EcoSpold 02 unit (ID) to be
	 * converted to an ILCD flow property.
	 * 
	 */
	public static Entry es2ToILCD(String unitId) {
		Entry entry = null;
		String query = "SELECT ilcdPropertyId, factor FROM "
				+ "UNIT_MAP_ES2_TO_ILCD WHERE es2UnitId='" + unitId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				entry = new Entry();
				entry.factor = rs.getDouble("factor");
				entry.id = rs.getString("ilcdPropertyId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return entry;
	}

	/**
	 * The entry of a unit - unit / unit - property mapping.
	 */
	public static class Entry {

		/**
		 * The ID of the target unit or flow property.
		 */
		private String id;

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property. The factor should be applied in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		private double factor;

		/**
		 * The ID of the target unit or flow property.
		 */
		public String getId() {
			return id;
		}

		/**
		 * The ID of the target unit or flow property.
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property. The factor should be applied in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		public double getFactor() {
			return factor;
		}

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property. The factor should be applied in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		public void setFactor(double factor) {
			this.factor = factor;
		}
	}

}
