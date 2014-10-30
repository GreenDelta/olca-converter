package org.openlca.olcatdb.examples;

import java.util.ArrayList;
import java.util.Collections;

import org.openlca.olcatdb.datatypes.LangString;

public class MultiLangStrings {

	public static void main(String[] args) {
		
		// concatenation of two string lists
		ArrayList<LangString> list1 = new ArrayList<LangString>();
		list1.add(new LangString("de", "prozess"));
		list1.add(new LangString("en", "process"));
		
		ArrayList<LangString> list2 = new ArrayList<LangString>();
		list2.add(new LangString("en", "electricity"));
	
		LangString.concat(list1, list2, " // ");
		
		System.out.println(list2);
		
	}
	
}
