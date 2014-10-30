package org.openlca.olcatdb.conversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SPMappingFile {

	private List<SPCategory> categories = new ArrayList<SPCategory>();

	public void add(SPCategory category) {
		if (!categories.contains(category)) {
			categories.add(category);
		}
	}

	private void write(BufferedWriter writer, String toWrite)
			throws IOException {
		writer.write(toWrite);
		writer.newLine();
	}

	public void writeToFile(String fileName) throws IOException {
		Collections.sort(categories);

		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		write(writer, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		write(writer, "<root>");
		write(writer, "  <categories>");

		String lastCategory = null;
		for (SPCategory category : categories) {
			if (!category.getCategory().equals(lastCategory)) {
				if (lastCategory != null) {
					write(writer, "    </category>");
				}
				write(writer,
						"    <category name=\"" + category.getCategory()
								+ "\" type=\"process\" internalName=\""
								+ category.getCategory() + "\">");
				lastCategory = category.getCategory();
			}
			write(writer,
					"      <subCategory name=\"" + category.getSubCategory()
							+ "\" processType=\"" + category.getProcessType()
							+ "\"/>");
		}
		if (categories.size() > 0) {
			write(writer, "    </category>");
		}

		write(writer, "  </categories>");
		write(writer, "  <processes/>");
		write(writer, "  <mappings>");
		write(writer, "    <categories/>");
		write(writer, "    <units/>");
		write(writer, "    <quantities/>");
		write(writer, "    <substances/>");
		write(writer, "    <processes/>");
		write(writer, "    <sources/>");
		write(writer, "    <dummyProcesses/>");
		write(writer, "    <locations/>");
		write(writer, "  </mappings>");
		write(writer, "</root>");

		writer.flush();
		writer.close();
	}
}
