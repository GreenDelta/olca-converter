package org.openlca.olcatdb.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * The supported XML process formats of the converter.The data formats are
 * identified by the root element and name-space:
 * <ul>
 * <li>EcoSpold1("http://www.EcoInvent.org/EcoSpold01:ecoSpold")</li>
 * <li>EcoSpold2("http://www.EcoInvent.org/EcoSpold02:ecoSpold")</li>
 * <li>ILCD("http://lca.jrc.it/ILCD/Process:processDataSet")</li>
 * </ul>
 * 
 * @author Michael Srocka
 */
public enum XMLProcessFormat {

	/**
	 * The EcoSpold 01 process format:
	 * EcoSpold1("http://www.EcoInvent.org/EcoSpold01:ecoSpold")
	 */
	EcoSpold1("EcoSpold 1", "ecoSpold", "http://www.EcoInvent.org/EcoSpold01"),

	/**
	 * The EcoSpold 01 process format:
	 * EcoSpold1("http://www.EcoInvent.org/EcoSpold01:ecoSpold")
	 */
	EcoSpold1_Sima_Pro("EcoSpold 1 (SimaPro)", "ecoSpold",
			"http://www.EcoInvent.org/EcoSpold01"),

	/**
	 * The EcoSpold 02 process format:
	 * EcoSpold2("http://www.EcoInvent.org/EcoSpold02:ecoSpold")
	 */
	EcoSpold2("EcoSpold 2", "ecoSpold", "http://www.EcoInvent.org/EcoSpold02"),

	/**
	 * The ILCD process format:
	 * ILCD("http://lca.jrc.it/ILCD/Process:processDataSet").
	 */
	ILCD("ILCD", "processDataSet", "http://lca.jrc.it/ILCD/Process"),
	/**
	 * The CSV process format
	 */
	CSV("CSV (SimaPro)", "", "");

	// the format name
	private final String name;

	// the name of the root element
	private final String rootElement;

	// the format's name space
	private final String namespace;

	private XMLProcessFormat(String name, String rootElement, String namespace) {
		this.name = name;
		this.namespace = namespace;
		this.rootElement = rootElement;
	}

	/**
	 * Returns the format name.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Get the format of the given file. XML and ZIP files are supported. If the
	 * given file is a ZIP file the first identified format of an entry is
	 * returned. If the format could not be identified, null is returned.
	 */
	public static XMLProcessFormat fromFile(String path) {
		return fromFile(new File(path));
	}

	/**
	 * Get the format of the given file. XML and ZIP files are supported. If the
	 * given file is a ZIP file the first identified format of an entry is
	 * returned. If the format could not be identified, null is returned.
	 */
	public static XMLProcessFormat fromFile(File file) {

		if (file == null || !file.exists() || !file.isFile()) {
			return null;
		}

		XMLProcessFormat format = null;

		String name = file.getName().toLowerCase();

		if (name.endsWith(".xml") || name.endsWith(".spold")) {

			// parse a single XML file
			SAXBuilder builder = new SAXBuilder();
			try {
				Document doc = builder.build(file);
				format = fromDocument(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (name.endsWith(".zip")) {
			try {
				ZipFile zipFile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				SAXBuilder builder = new SAXBuilder();
				while (entries.hasMoreElements() && format == null) {

					ZipEntry zipEntry = entries.nextElement();
					if (matches(zipEntry)) {
						Document doc = builder.build(zipFile
								.getInputStream(zipEntry));
						format = fromDocument(doc);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (name.endsWith(".csv")) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				if(line.startsWith("{SimaPro")) {
					format = XMLProcessFormat.CSV;					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return format;
	}

	/**
	 * Returns true if the given entry matches the following schema:
	 * <ul>
	 * <li>the entry is no directory</li>
	 * <li>the entry name ends with '.xml' or '.XML'</li>
	 * <li>the entry name does not contain the words 'flows', 'flowproperties',
	 * 'unitgroups', 'contacts', 'sources', 'external_docs'. (These are the
	 * sub-folder names for the other data types in ILCD. Disabling the parsing
	 * of these entries is increasing the speed of the format identification ).
	 * </ul>
	 */
	private static boolean matches(ZipEntry zipEntry) {

		String name = zipEntry.getName().toLowerCase();
		return !zipEntry.isDirectory()
				&& (name.endsWith(".xml") || name.endsWith(".spold"))
				&& !name.contains("flows") && !name.contains("flowproperties")
				&& !name.contains("unitgroups") && !name.contains("contacts")
				&& !name.contains("sources") && !name.contains("external_docs");
	}

	/**
	 * Get the format of the given JDOM document. If the format could not be
	 * identified, null is returned.
	 */
	private static XMLProcessFormat fromDocument(Document doc) {

		XMLProcessFormat format = null;

		Element element = doc.getRootElement();
		if (element != null) {

			String rootName = element.getName();
			String namespace = element.getNamespaceURI();

			for (XMLProcessFormat f : values()) {
				if (f.rootElement.equals(rootName)
						&& f.namespace.equals(namespace)) {
					format = f;
					break;
				}
			}

		}
		return format;
	}

}
