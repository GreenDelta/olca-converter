package org.openlca.olcatdb.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.Writer;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlFormatter {

	private SAXBuilder builder = new SAXBuilder();

	private XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

	public void format(File file) {
		try {
			Document doc = builder.build(file);
			FileOutputStream fos = new FileOutputStream(file);
			outputter.output(doc, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void format(Reader reader, Writer writer) {
		try {
			Document doc = builder.build(reader);			
			outputter.output(doc, writer);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
