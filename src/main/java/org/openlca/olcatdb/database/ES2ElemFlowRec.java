package org.openlca.olcatdb.database;

import java.sql.ResultSet;
import java.util.UUID;

import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;

/**
 * The stored attributes of an EcoSpold 02 elementary exchange.
 * 
 * @author Michael Srocka
 * 
 */
public class ES2ElemFlowRec {

	private String id;

	private String cas;

	private String formula;

	private String name;

	private String unitId;

	private String compartmentId;

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
	 * @return the unitId
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId
	 *            the unitId to set
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the compartmentId
	 */
	public String getCompartmentId() {
		return compartmentId;
	}

	/**
	 * @param compartmentId
	 *            the compartmentId to set
	 */
	public void setCompartmentId(String compartmentId) {
		this.compartmentId = compartmentId;
	}

	/**
	 * Returns the EcoSpold 02 elementary flow record for the given ID from the
	 * database, or <code>null</code> if there is no record stored for this ID.
	 */
	public static ES2ElemFlowRec forID(String id) {
		ES2ElemFlowRec rec = null;
		String query = "SELECT * FROM ES2_ELEM_FLOWS WHERE id='" + id + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if (rs.next()) {
				rec = new ES2ElemFlowRec();
				rec.cas = rs.getString("cas");
				rec.compartmentId = rs.getString("compartmentId");
				rec.formula = rs.getString("formula");
				rec.id = id;
				rec.name = rs.getString("name");
				rec.unitId = rs.getString("unitId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

	/**
	 * Creates an EcoSpold 02 elementary exchange from the flow record ready to
	 * be used in an activity data set (but without amount fields, of course).
	 */
	public ES2ElementaryExchange toExchange() {
		ES2ElementaryExchange eExchange = new ES2ElementaryExchange();

		// the general exchange attributes
		eExchange.casNumber = this.cas;
		eExchange.elementaryExchangeId = this.id;
		eExchange.id = UUID.randomUUID().toString(); // generated
		eExchange.unitId = this.unitId;
		eExchange.isCalculatedAmount = false;
		eExchange.getName().add(new LangString(this.name));

		// the unit
		ES2UnitRec rec = ES2UnitRec.forID(this.unitId);
		if (rec != null) {
			eExchange.getUnitNames().add(new LangString(rec.getName()));
		}

		// the compartment
		eExchange.compartmentId = this.compartmentId;
		ES2CompartmentRec compRec = ES2CompartmentRec.forID(this.compartmentId);
		if (rec != null) {
			eExchange.getCompartment().add(
					new LangString(compRec.getCompartment()));
			if (compRec.getSubCompartment() != null) {
				eExchange.getSubCompartment().add(
						new LangString(compRec.getSubCompartment()));
			}
		}

		return eExchange;
	}

}
