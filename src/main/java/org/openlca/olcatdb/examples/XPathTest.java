package org.openlca.olcatdb.examples;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathTest {

	public static void main(String[] args) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();

		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		XPathExpression expr = xpath.compile("//flowProperty[@dataSetInternalID>0]/@dataSetInternalID");

		File file = new File("C:\\Projekte\\openLCA"
				+ "\\converter\\ELCD\\ILCD\\flows");

		int c = 0;

		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		for (File f : file.listFiles()) {
			Document doc = builder.parse(f);
			NodeList nodes = (NodeList) expr.evaluate(doc,
					XPathConstants.NODESET);
			String result = "";
			if(nodes.getLength() > 1)
				result += "[";
			
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeValue() != null) {
					result += nodes.item(i).getNodeValue();
				}
				if (i < (nodes.getLength() - 1)) {
					result += ", ";
				} 
			}
			if(nodes.getLength() > 1)
				result += "]";
			
			if(result.length() > 0) {
				resultMap.put(f.getName(), result);
				System.out.println(f.getName() + "; " + result);
			}

//			if (c > 10)
//				break;
			c++;
		}

	}

}
