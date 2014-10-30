package org.openlca.olcatdb.ilcd;

/**
 * The ILCD data set types.
 * 
 */
public enum ILCDDataSetType {

	Source("source data set"),

	Process("process data set"),

	Flow("flow data set"),

	FlowProperty("flow property data set"),

	UnitGroup("unit group data set"),

	Contact("contact data set"),

	LCIAMethod("LCIA method data set"),

	Other("other external file");

	private final String ilcdName;

	private ILCDDataSetType(String ilcdName) {
		this.ilcdName = ilcdName;
	}

	@Override
	public String toString() {
		return ilcdName;
	}

	/**
	 * Tries to get the ILCD data set type for the given type name. See the ILCD
	 * documentation for the type names. If the is no type with the given name,
	 * <code>null</code> is returned. <code>null</code> is allowed for 'type'.
	 */
	public static ILCDDataSetType forName(String type) {
		if (type != null)
			for (ILCDDataSetType t : ILCDDataSetType.values())
				if (t.ilcdName.equalsIgnoreCase(type))
					return t;
		return null;
	}

}
