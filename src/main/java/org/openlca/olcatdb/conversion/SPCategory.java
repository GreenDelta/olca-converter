package org.openlca.olcatdb.conversion;

import java.util.HashMap;
import java.util.Map;

public class SPCategory implements Comparable<SPCategory> {

	private String category;
	private String subCategory;
	private String processType;

	public static String PROCESS_TYPE_MATERIAL = "material";
	public static String PROCESS_TYPE_ENERGY = "energy";
	public static String PROCESS_TYPE_TRANSPORT = "transport";
	public static String PROCESS_TYPE_USE = "use";
	public static String PROCESS_TYPE_WASTE_TREATMENT = "waste treatment";
	public static String PROCESS_TYPE_PROCESSING = "processing";

	private static Map<String, SPCategory> categories = new HashMap<String, SPCategory>();

	private static String getType(String category) {
		String type = PROCESS_TYPE_MATERIAL;
		if ("Energy carriers and technologies".equals(category)) {
			type = PROCESS_TYPE_ENERGY;
		} else if ("Transport services".equals(category)) {
			type = PROCESS_TYPE_TRANSPORT;
		}
		return type;
	}

	public static SPCategory getCategory(String category, String subCategory,
			String type) {
		if (category == null) {
			category = "Others";
		}
		if (subCategory == null) {
			subCategory = "Others";
		}
		if (type == null) {
			type = getType(category);
		}

		String key = category + subCategory + type;
		SPCategory spCategory = categories.get(key);
		if (spCategory == null) {
			spCategory = new SPCategory(category, subCategory, type);
			categories.put(key, spCategory);
		}
		return spCategory;
	}

	private SPCategory(String category, String subCategory, String type) {
		this.category = category;
		this.subCategory = subCategory;
		this.processType = type;
	}

	public String getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public String getProcessType() {
		return processType;
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof SPCategory) {
			SPCategory cat = (SPCategory) obj;
			String key1 = getCategory() + getSubCategory() + getProcessType();
			String key2 = cat.getCategory() + cat.getSubCategory()
					+ cat.getProcessType();
			eq = key1.equals(key2);
		}
		return eq;
	}

	@Override
	public int compareTo(SPCategory o) {
		return category.compareTo(o.category);
	}

}
