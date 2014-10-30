package org.openlca.olcatdb.datatypes;

public enum ProcessType {

	UnitProcess("Unit process, single operation", 1),
	
	UnitProcessBlackBox("Unit process, black box", -1),
	
	LCIResult("LCI result", 2),
	
	PartlyTerminatedSystem("Partly terminated system", -1),
	
	AvoidedProductSystem ("Avoided product system", -1),
	
	MultiOutputProcess ("Unit process, black box", 5);
	
	private final String ilcdName;

	private final int ecoSpoldCode;

	private ProcessType(String ilcdName, int ecoSpoldCode) {
		this.ilcdName = ilcdName;
		this.ecoSpoldCode = ecoSpoldCode;
	}

	public int getEcoSpoldCode() {
		return ecoSpoldCode != -1 ? ecoSpoldCode : 1;
	}

	public String getILCDName() {
		return ilcdName;
	}

	public static ProcessType fromILCDName(String ilcdName) {
		ProcessType type = null;
		for (ProcessType t : ProcessType.values()) {
			if (t.ilcdName.equals(ilcdName)) {
				type = t;
				break;
			}
		}
		return type == null ? UnitProcessBlackBox : type;
	}

	public static ProcessType fromEcoSpoldCode(int code) {
		ProcessType type = null;
		for (ProcessType t : ProcessType.values()) {
			if (t.ecoSpoldCode == code) {
				type = t;
				break;
			}
		}
		return type == null ? UnitProcess : type;
	}
	
}
