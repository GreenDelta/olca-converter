package org.openlca.olcatdb.datatypes;

public enum UncertaintyType {

	Undefined("undefined", 0),
	
	LogNormal("log-normal", 1),
	
	Normal("normal", 2),
	
	Triangular("triangular", 3),
	
	Uniform("uniform", 4);
	
	private final String ilcdName;
	
	private final int ecoSpoldCode;
	
	private UncertaintyType(String ilcdName, int ecoSpoldCode) {
		this.ilcdName = ilcdName;
		this.ecoSpoldCode = ecoSpoldCode;
	}
	
	public int getEcoSpoldCode() {
		return ecoSpoldCode;
	}
	
	public String getILCDName() {
		return ilcdName;
	}
	
	public static UncertaintyType fromILCDName (String ilcdName) {		
		UncertaintyType type = null;
		for(UncertaintyType t : UncertaintyType.values()) {
			if(t.ilcdName.equals(ilcdName)) {
				type = t; 
				break;
			}
		}
		return type == null ? Undefined : type;
	}
	
	public static UncertaintyType fromEcoSpoldCode (int code) {
		UncertaintyType type = null;
		for(UncertaintyType t : UncertaintyType.values()) {
			if(t.ecoSpoldCode == code) {
				type = t; 
				break;
			}
		}
		return type == null ? Undefined : type;
	}
	
}
