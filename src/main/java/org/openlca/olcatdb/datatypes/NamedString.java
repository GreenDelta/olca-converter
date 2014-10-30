package org.openlca.olcatdb.datatypes;

public class NamedString {

	private String value;
	
	private String name;

	public NamedString() {
	}
		
	public NamedString(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
