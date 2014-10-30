package org.openlca.olcatdb.examples;

import java.util.GregorianCalendar;

import org.openlca.olcatdb.conversion.XMLProcessFormat;

public class FormatIdentification {

	public static void main(String[] args) {
		
		String file = "C:/Projekte/openLCA/data/ELCD/ELCD.zip";
		file = "C:/Projekte/openLCA/data/ELCD/ILCD/processes/0a1b40db-5645-4db8-a887-eb09300b7b74.xml";
		file = "C:/Dokumente und Einstellungen/ms/Eigene Dateien/EcoSpoldExample_HeatOil.02.xml";
		file = "C:/Projekte/openLCA/data/ecoinvent2.0/ecoinventData/cumulativeResultsLCIA.zip";
		long before = new GregorianCalendar().getTime().getTime();
		System.out.println(XMLProcessFormat.fromFile(file));
		long after = new GregorianCalendar().getTime().getTime();
		System.out.println("in " + (after-before) + " ms");
		
	}
	
}
