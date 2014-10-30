package org.openlca.olcatdb.database;

import java.sql.ResultSet;

/**
 * Contains the assignments between the elementary flows.
 * 
 * @author Michael Srocka
 */
public class ElemFlowMap {

	private static String isProxyWhere = "";

	public static void setIsProxy(boolean isProxy) {
		if (isProxy) {
			isProxyWhere = "";
		} else {
			isProxyWhere = " isProxy=false AND";
		}
	}

	/**
	 * Returns a map entry of an EcoSpold 01 flow with the given attributes to
	 * an ILCD flow, or <code>null</code> if there is no assignment for such a
	 * flow stored in the database.
	 */
	public static Entry es1ToILCD(String name, String compartment,
			String subCompartment, String unit) {
		Entry entry = null;

		// 1) try to find the EcoSpold flow
		ES1ElemFlowRec rec = ES1ElemFlowRec.forAtts(name, compartment,
				subCompartment, unit);
		if (rec != null) {
			try {

				// 2) try to find the map entry
				String query = "SELECT ilcdFlowId, factor FROM FLOW_MAP_ES1_TO_ILCD WHERE"
						+ isProxyWhere + " es1FlowId=" + rec.getId();

				ResultSet rs = Database.getInstance().query(query);
				if (rs.next()) {
					entry = new Entry();
					entry.factor = rs.getDouble("factor");
					entry.flowId = rs.getString("ilcdFlowId");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return entry;
	}

	public static Entry ilcdToES1(String ilcdFlowId) {
		Entry entry = null;

		String query = "SELECT es1FlowId, factor FROM FLOW_MAP_ILCD_TO_ES1 WHERE"
				+ isProxyWhere + " ilcdFlowId='" + ilcdFlowId + "'";

		try {

			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				entry = new Entry();
				entry.flowId = Integer.toString(rs.getInt("es1FlowId"));
				entry.factor = rs.getDouble("factor");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return entry;
	}

	/**
	 * Returns the map entry of the ILCD flow for the given EcoSpold 02
	 * elementary flow ID, or <code>null</code> if there is no mapping for this
	 * flow in the database
	 */
	public static Entry es2ToILCD(String e2Id) {
		Entry entry = null;
		String query = "SELECT ilcdFlowId, factor FROM FLOW_MAP_ES2_TO_ILCD WHERE"
				+ isProxyWhere + " es2FlowId='" + e2Id + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				entry = new Entry();
				entry.flowId = rs.getString("ilcdFlowId");
				entry.factor = rs.getDouble("factor");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entry;
	}

	/**
	 * Returns the ID of the EcoSpold 02 elementary exchange for the given ILCD
	 * elementary flow ID, or <code>null</code> if there is no mapping for this
	 * flow in the database
	 */
	public static String ilcdToES2(String ilcdId) {
		String es2Id = null;
		String query = "SELECT es2FlowId FROM FLOW_MAP_ILCD_TO_ES2 WHERE"
				+ isProxyWhere + " ilcdFlowId='" + ilcdId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				es2Id = rs.getString("es2FlowId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return es2Id;
	}

	// private static void checkProxy() {
	// System.out.println(ConversionPanel.getInstance().isProxy());
	//
	// if (ConversionPanel.getInstance().isProxy()) {
	// isProxy = "";
	// } else {
	// isProxy = " isProxy=false AND";
	// }
	// }

	/**
	 * The entry of a elementary flow mapping.
	 */
	public static class Entry {

		/**
		 * The ID of the elementary flow in the target format.
		 */
		private String flowId;

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property of the mapped elementary flows. The factor should be applied
		 * in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		private double factor;

		/**
		 * The ID of the elementary flow in the target format.
		 */
		public String getFlowId() {
			return flowId;
		}

		/**
		 * The ID of the elementary flow in the target format.
		 */
		public void setFlowId(String flowId) {
			this.flowId = flowId;
		}

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property of the mapped elementary flows. The factor should be applied
		 * in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		public double getFactor() {
			return factor;
		}

		/**
		 * The conversion factor for converting an amount given in the source
		 * unit / flow property into an amount of the target unit / flow
		 * property of the mapped elementary flows. The factor should be applied
		 * in the following form:
		 * <code>targetAmount = sourceAmount * factor</code>.
		 */
		public void setFactor(double factor) {
			this.factor = factor;
		}
	}
}
