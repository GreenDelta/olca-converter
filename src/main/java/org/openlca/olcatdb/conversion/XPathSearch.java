package org.openlca.olcatdb.conversion;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathSearch implements Runnable {

	private File xmlFile;

	private ZipFile zipFile;

	private XPathExpression expression;

	private QName resultType;

	private HashMap<String, String> result;

	private String filter;

	/**
	 * Initializes a new XPath search.
	 * 
	 * @param file
	 *            An XML or ZIP file. If the file has another format or does not
	 *            exist an {@link IllegalArgumentException} is thrown.
	 * @param query
	 *            The XPath query. If the query is not syntactically correct an
	 *            {@link IllegalArgumentException} is thrown.
	 */
	public XPathSearch(File file, String query) {

		// check if the file is correct
		if (file == null) {
			throw new IllegalArgumentException("The given file is NULL");
		}
		String fileName = file.getName();
		if (!(isXml(fileName) || isZip(fileName))) {
			throw new IllegalArgumentException("Unsupported file format.");
		}

		// check if the XPath is correct
		if (query == null || query.length() == 0) {
			throw new IllegalArgumentException("There is no XPath query given.");
		}
		String xQuery = query;
		if (query.contains("#")) {
			try {
				int pos = query.indexOf("#");
				xQuery = query.substring(pos + 1);
				filter = query.substring(0, pos).trim();
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"The XPath expression is not valid.");
			}
		}

		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		try {
			expression = xpath.compile(xQuery);
			resultType = resultType(xQuery);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"The XPath expression is not valid.");
		}

		// set the file variables
		if (isXml(fileName)) {
			xmlFile = file;
		} else {
			try {
				zipFile = new ZipFile(file);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"The given file is not a valid ZIP file");
			}
		}

		// initialize the result map
		result = new HashMap<String, String>();

	}

	private boolean isXml(String fileName) {
		return fileName.endsWith(".xml") || fileName.endsWith(".XML");
	}

	private boolean isZip(String fileName) {
		return fileName.endsWith(".zip") || fileName.endsWith(".ZIP");
	}

	/**
	 * Runs the search.
	 */
	@Override
	public void run() {

		result.clear();

		if (xmlFile != null) {
			singleSearch();
		} else if (zipFile != null) {
			bulkSearch();
		}

	}

	private void singleSearch() {

		try {
			// initialize the document builder
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// query the document
			Document doc = builder.parse(xmlFile);
			Object queryRes = expression.evaluate(doc, resultType);

			// create the result
			if (queryRes instanceof NodeList) {
				NodeList nodes = (NodeList) queryRes;
				for (int i = 0; i < nodes.getLength(); i++) {
					String key = nodes.item(i).getNodeName() + "_"
							+ Integer.toString(i);
					result.put(key, nodes.item(i).getNodeValue());
				}
			} else if (queryRes != null) {
				result.put("result", queryRes.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void bulkSearch() {

		try {
			// initialize the document builder
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();

			Enumeration<? extends ZipEntry> en = zipFile.entries();
			while (en.hasMoreElements()) {
				ZipEntry zipEntry = en.nextElement();
				String name = zipEntry.getName();
				boolean matchEntry = filter == null ? true : name
						.contains(filter);
				if (!zipEntry.isDirectory() && matchEntry
						&& (name.endsWith(".xml") || name.endsWith(".XML"))) {

					Document doc = builder.parse(zipFile
							.getInputStream(zipEntry));
					Object queryRes = expression.evaluate(doc, resultType);
					if (queryRes instanceof NodeList) {
						NodeList nodes = (NodeList) queryRes;
						String resultStr = "";
						if (nodes.getLength() > 1)
							resultStr += "[";

						for (int i = 0; i < nodes.getLength(); i++) {
							if (nodes.item(i).getNodeValue() != null) {
								resultStr += nodes.item(i).getNodeValue();
							}
							if (i < (nodes.getLength() - 1)) {
								resultStr += ", ";
							}
						}
						if (nodes.getLength() > 1)
							resultStr += "]";

						result.put(name, resultStr);
					} else if (queryRes != null) {
						result.put(name, queryRes.toString());
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the result of the search.
	 */
	public HashMap<String, String> getResult() {
		return result;
	}

	private QName resultType(String query) {

		List<String> numberFuns = new ArrayList<String>();
		Collections.addAll(numberFuns, "sum(", "count(", "last(", "position(",
				"string-length(", "ceiling(", "floor(", "number(", "round(");

		List<String> boolFuns = new ArrayList<String>();
		Collections.addAll(boolFuns, "contains(", "starts-with(", "boolean(",
				"false(", "lang(", "not(", "true(");

		List<String> stringFuns = new ArrayList<String>();
		Collections.addAll(stringFuns, "concat(", "local-name(", "name(",
				"namespace-uri(", "normalize-space(", "string(", "substring(",
				"substring-after(", "substring-before(", "translate(",
				"format-number(", "generate-id(");

		if (match(query.trim(), numberFuns)) {
			return XPathConstants.NUMBER;
		} else if (match(query.trim(), boolFuns)) {
			return XPathConstants.BOOLEAN;
		} else if (match(query.trim(), stringFuns)) {
			return XPathConstants.STRING;
		}

		return XPathConstants.NODESET;

	}

	private boolean match(String query, List<String> functions) {
		for (String fn : functions) {
			if (query.startsWith(fn)) {
				return true;
			}
		}
		return false;
	}

}
