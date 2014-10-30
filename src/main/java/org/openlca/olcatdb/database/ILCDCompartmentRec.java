package org.openlca.olcatdb.database;

import java.sql.ResultSet;

import org.openlca.olcatdb.ilcd.ILCDElemFlowCategorization;
import org.openlca.olcatdb.ilcd.ILCDElemFlowCategory;

/**
 * 
 * The stored attributes of an ILCD elementary flow category.
 * 
 * @author Michael Srocka
 * 
 */
public class ILCDCompartmentRec {

	/**
	 * The ID of the category.
	 */
	private String id;

	/**
	 * The level 0 entry.
	 */
	private String level0;

	/**
	 * The level 1 entry.
	 */
	private String level1;

	/**
	 * The level 2 entry;
	 */
	private String level2;

	/**
	 * The identifier of the parent compartment.
	 */
	private String parentId;

	/**
	 * The ID of the category.
	 */
	public String getId() {
		return id;
	}

	/**
	 * The ID of the category.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The level 0 entry.
	 */
	public String getLevel0() {
		return level0;
	}

	/**
	 * The level 0 entry.
	 */
	public void setLevel0(String level0) {
		this.level0 = level0;
	}

	/**
	 * The level 1 entry.
	 */
	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	/**
	 * The level 1 entry.
	 */
	public String getLevel1() {
		return level1;
	}

	/**
	 * The level 2 entry.
	 */
	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	/**
	 * The level 2 entry.
	 */
	public String getLevel2() {
		return level2;
	}

	/**
	 * The identifier of the parent compartment.
	 */
	public String getParentId() {
		return parentId;
	}
	
	/**
	 * The identifier of the parent compartment.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * Creates the ILCD elementary flow categorization element for this record. The
	 * returned object can be added to a ILCD flow data set.
	 */
	public ILCDElemFlowCategorization toCategorization() {
		ILCDElemFlowCategorization categorization = new ILCDElemFlowCategorization();
		
		// level 0
		ILCDElemFlowCategory cat = new ILCDElemFlowCategory();
		cat.level = 0;
		cat.name = this.level0;
		categorization.getCategories().add(cat);
		
		// level 1
		if(level1 != null) {
			cat = new ILCDElemFlowCategory();
			cat.level = 1;
			cat.name = this.level1;
			categorization.getCategories().add(cat);
		}
		
		// level 2
		if(level2 != null) {
			cat = new ILCDElemFlowCategory();
			cat.level = 2;
			cat.name = this.level2;
			categorization.getCategories().add(cat);
		}
		
		return categorization;
	}
	
	/**
	 * Returns the record of the elementary flow category with the given ID
	 * stored in the database, or <code>null</code> if there is no such record.
	 */
	public static ILCDCompartmentRec forID(String catId) {
		ILCDCompartmentRec catRec = null;
		String query = "SELECT * FROM ILCD_COMPARTMENTS WHERE id='" + catId + "'";
		try {
			ResultSet rs = Database.getInstance().query(query);
			if(rs.next()) {
				catRec = new ILCDCompartmentRec();
				catRec.id = catId;
				catRec.level0 = rs.getString("level0");
				catRec.level1 = rs.getString("level1");
				catRec.level2 = rs.getString("level2");
				catRec.parentId = rs.getString("parentId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return catRec;
	}

}
