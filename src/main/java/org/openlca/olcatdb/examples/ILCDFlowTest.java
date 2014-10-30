package org.openlca.olcatdb.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.parsing.XmlContextParser;
import org.openlca.olcatdb.xml.XmlFormatter;
import org.openlca.olcatdb.xml.XmlOutputter;

public class ILCDFlowTest {

	public static void main(String[] args) {

		File flowDir = new File(
				"C:\\Projekte\\openLCA\\converter\\ELCD\\ILCD\\flows");

		List<String> elementaryCats = new ArrayList<String>();

		try {
			XmlContextParser parser = new XmlContextParser();

			for (File f : flowDir.listFiles()) {
				if (f.getName().endsWith(".xml")) {
					ILCDFlow flow = parser.getContext(ILCDFlow.class,
							new FileInputStream(f));
					if (flow != null && flow.type != null) {

						if (!flow.type.equals("Elementary flow")) {

							String path = flow.description
									.getClassifications().get(0)
									.getPath("/");
							
							if(!elementaryCats.contains(path)) {
								elementaryCats.add(path);
							}
						}

					}
				}
			}
			
			for(String cat : elementaryCats) {
				System.out.println(cat);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
