package org.openlca.olcatdb.conversion;

public final class Util {

	private Util(){
	}

	public static boolean nullOrEmpty(String s) {
		if(s == null)
			return true;
		return s.trim().isEmpty();
	}

	public static boolean notEmpty(String s) {
		if(s == null)
			return false;
		return !s.trim().isEmpty();
	}

}
