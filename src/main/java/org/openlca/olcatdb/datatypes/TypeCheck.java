package org.openlca.olcatdb.datatypes;

/**
 * A helper class for checking data types of the different
 * formats (e.g. the field length)
 * 
 * @author Michael Srocka
 *
 */
public class TypeCheck {

	public static String checkLength(String val, int length) {
		String nVal = val;
		if(nVal != null && nVal.length() > length) {
			nVal = nVal.substring(0, length -1) + "#";
		}
		return nVal;
	}
	
	public static LangString checkLength(LangString val, int length) {
		LangString lString = val;
		if(val != null && val.getValue() != null && val.getValue().length() > length) {
			String nVal = checkLength(val.getValue(), length);
			lString = new LangString(val.getLangCode(), nVal);
		}		
		return lString;
	}
	
	public static void main(String[] args) {
		System.out.println(checkLength(new LangString("Test al looo"), 8));
	}
	
}
