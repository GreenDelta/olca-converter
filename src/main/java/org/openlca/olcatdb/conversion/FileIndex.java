package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.templates.TemplateLoader;
import org.openlca.olcatdb.templates.TemplateType;

/**
 * A file index (e.g. the result of a conversion).
 * 
 * @author Michael Srocka
 * 
 */
public class FileIndex {

	/**
	 * The title of this index.
	 */
	private String title;

	/**
	 * A path to the optional log file of this index.
	 */
	private String log;

	/**
	 * The list of data sets in this index.
	 */
	private List<DataSetReference> dataSets = new ArrayList<DataSetReference>();

	/**
	 * The list of data sets in this index.
	 */
	public List<DataSetReference> getDataSets() {
		return dataSets;
	}

	/**
	 * The title of this index.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The title of this index.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * A special data set containing the log of this index.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * A special data set containing the log of this index.
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * Adds a data set to this index.
	 */
	public void add(DataSetReference ref) {
		getDataSets().add(ref);
	}

	/**
	 * Writes this index to an HTML file using a velocity template.
	 */
	public void toFile(File file) throws IOException {

		Collections.sort(dataSets, DataSetReference.comparator());

		// prepare / fill the template
		Template template = TemplateLoader.getInstance().getTemplate(
				TemplateType.FileIndex);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(TemplateType.FileIndex.getContextName(), this);

		// write the file
		Writer writer = new FileWriter(file);
		template.merge(velocityContext, writer);
		writer.flush();
		writer.close();
	}
}
