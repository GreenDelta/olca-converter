package org.openlca.olcatdb.datatypes;

public enum SourceType {

	Undefined("Undefined", 0),

	Article("Article in periodical", 1),

	ChapterInAnthology("Chapter in anthology", 2),

	Monograph("Monograph", 3),

	Measurement("Direct measurement", 4),

	OralCommunication("Oral communication", 5),

	WrittenCommunication("Personal written communication", 6),

	Questionnaire("Questionnaire", 7),

	SoftwareDatabase("Software or database", -1),

	Other("Other unpublished and grey literature", -1);

	private final String ilcdName;

	private final int ecoSpoldCode;

	private SourceType(String ilcdName, int ecoSpoldCode) {
		this.ilcdName = ilcdName;
		this.ecoSpoldCode = ecoSpoldCode;
	}

	public int getEcoSpoldCode() {
		return ecoSpoldCode != -1 ? ecoSpoldCode : 0;
	}

	public String getILCDName() {
		return ilcdName;
	}

	public static SourceType fromILCDName(String ilcdName) {
		SourceType type = null;
		for (SourceType t : SourceType.values()) {
			if (t.ilcdName.equals(ilcdName)) {
				type = t;
				break;
			}
		}
		return type == null ? Undefined : type;
	}

	public static SourceType fromEcoSpoldCode(int code) {
		SourceType type = null;
		for (SourceType t : SourceType.values()) {
			if (t.ecoSpoldCode == code) {
				type = t;
				break;
			}
		}
		return type == null ? Undefined : type;
	}

}
