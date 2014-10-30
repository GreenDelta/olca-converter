package org.openlca.olcatdb.conversion;

import java.io.File;

/**
 * In a data set conversion a source XML or ZIP file is transformed into a
 * number of data sets.
 * 
 * @author Michael Srocka
 */
public interface Conversion extends Task {

	/**
	 * The source XML or ZIP file should be set, of course, before running the
	 * conversion.
	 */
	void setSourceFile(File sourceFile);

	/**
	 * The target directory where the created files are stared.
	 */
	void setTargetDir(File targetDir);
}
