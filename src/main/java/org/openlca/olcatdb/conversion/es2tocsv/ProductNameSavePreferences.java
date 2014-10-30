package org.openlca.olcatdb.conversion.es2tocsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductNameSavePreferences implements Serializable {

	private static final long serialVersionUID = 3689796993442819313L;
	private static File database = new File("database/ProductNamePrefSave.ser");
	private boolean chkBoxFlowName = true;
	private boolean chkBoxGeo = true;
	private boolean chkBoxActivityName = true;
	private boolean chkBoxSystemModelName = true;
	private boolean chkBoxActivityType = true;
	int sortFlowName = 1;
	int sortGeo = 2;
	int sortActivityName = 3;
	int sortSystemModelName = 4;
	int sortAcitvityType = 5;

	public boolean isChkBoxFlowName() {
		return chkBoxFlowName;
	}

	public void setChkBoxFlowName(boolean chkBoxflowName) {
		this.chkBoxFlowName = chkBoxflowName;
	}

	public boolean isChkBoxGeo() {
		return chkBoxGeo;
	}

	public void setChkBoxGeo(boolean chkBoxGeo) {
		this.chkBoxGeo = chkBoxGeo;
	}

	public boolean isChkBoxActivityName() {
		return chkBoxActivityName;
	}

	public void setChkBoxActivityName(boolean chkBoxActivityName) {
		this.chkBoxActivityName = chkBoxActivityName;
	}

	public boolean isChkBoxSystemModelName() {
		return chkBoxSystemModelName;
	}

	public void setChkBoxSystemModelName(boolean chkBoxSystemModelName) {
		this.chkBoxSystemModelName = chkBoxSystemModelName;
	}

	public boolean isChkBoxActivityType() {
		return chkBoxActivityType;
	}

	public void setChkBoxActivityType(boolean chkBoxActivityType) {
		this.chkBoxActivityType = chkBoxActivityType;
	}

	public int getSortFlowName() {
		return sortFlowName;
	}

	public void setSortFlowName(int orderFlowName) {
		this.sortFlowName = orderFlowName;
	}

	public int getSortGeo() {
		return sortGeo;
	}

	public void setSortGeo(int orderGeo) {
		this.sortGeo = orderGeo;
	}

	public int getSortActivityName() {
		return sortActivityName;
	}

	public void setSortActivityName(int orderActivityName) {
		this.sortActivityName = orderActivityName;
	}

	public int getSortSystemModelName() {
		return sortSystemModelName;
	}

	public void setSortSystemModelName(int orderSystemModelName) {
		this.sortSystemModelName = orderSystemModelName;
	}

	public int getSortAcitvityType() {
		return sortAcitvityType;
	}

	public void setSortAcitvityType(int orderAcitvityType) {
		this.sortAcitvityType = orderAcitvityType;
	}

	public String getSortedName(String flowName, String geoShortName,
			String ActivityName, String systemModelName, String ActivityType) {
		String name = "";
		List<String> sortList = new ArrayList<String>();
		if (chkBoxFlowName)
			sortList.add(sortFlowName + flowName);
		if (chkBoxGeo)
			sortList.add(sortGeo + geoShortName);
		if (chkBoxActivityName)
			sortList.add(sortActivityName + ActivityName);
		if (chkBoxSystemModelName)
			sortList.add(sortSystemModelName + systemModelName);
		if (chkBoxActivityType)
			sortList.add(sortAcitvityType + ActivityType);

		Collections.sort(sortList);

		for (String s : sortList) {
			if (name.equals("")) {
				name = s.substring(1);
			} else {
				name += " " + s.substring(1);
			}
		}
		return name;
	}

	public static ProductNameSavePreferences readDatabase() {
		ProductNameSavePreferences save = null;
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					database));
			save = (ProductNameSavePreferences) is.readObject();
			is.close();
		} catch (Exception e) {
			save = new ProductNameSavePreferences();
			writeDatabase(save);
		}
		return save;
	}

	public static boolean writeDatabase(ProductNameSavePreferences save) {
		try {
			FileOutputStream fs = new FileOutputStream(database);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(save);
			os.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
