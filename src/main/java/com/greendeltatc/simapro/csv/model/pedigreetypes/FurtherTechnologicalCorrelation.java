package com.greendeltatc.simapro.csv.model.pedigreetypes;

public enum FurtherTechnologicalCorrelation {
	/**
	 * na
	 */
	NA("na", "Unspecified", 1),

	/**
	 * one
	 */
	ONE("1", "Data from enterprises, processes and materials under study", 1),

	/**
	 * two
	 */
	TWO(
			"2",
			"Data from processes and materials under study (i.e. identical technology) but from different enterprises", 1),

	/**
	 * three
	 */
	THREE("3",
			"Data from processes and materials under study but from different technology", 1.2),
	/**
	 * four
	 */
	FOUR("4", "Data on related processes or materials", 1.5),
	/**
	 * five
	 */
	FIVE("5",
			"Data on related processes on laboratory scale or from different technology",2);

	private String key;
	private String value;
	private double indicator;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	public double getIndicator() {
		return indicator;
	}

	private FurtherTechnologicalCorrelation(String key, String value, double indicator) {
		this.key = key;
		this.value = value;
		this.indicator = indicator;
	}
}
