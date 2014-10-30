package org.openlca.olcatdb.ecospold2.masterdata;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class MDReaderTest {
	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException, IOException {

		File file = new File("/Users/imo/Desktop/MasterData.zip");

		ES2MDDataSet md = new ES2MDDataSet();
		md.read(file);
		System.err.println(md);
		// md.classifications = new ES2ClassificationSystemList();
		// md.classifications.contextId = "bla";
		//
		// Object object = md.getMasterData(ES2ClassificationSystemList.class);
		// ES2ClassificationSystemList classificationSystemList =
		// ES2ClassificationSystemList.class
		// .cast(object);
		// System.out.println(classificationSystemList);

	}

}
