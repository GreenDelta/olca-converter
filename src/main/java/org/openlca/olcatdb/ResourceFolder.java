package org.openlca.olcatdb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Abstract class for a format folder. Every format needs a specific folder
 * structure to organize the XML-, XSL-, and XSD-files. See the respective
 * implementations in the format packages.
 * 
 * @author Michael Srocka
 */
public abstract class ResourceFolder {

	/**
	 * The root directory of this folder. This field is intended to be accessed
	 * in sub-classes.
	 */
	protected File rootDir;

	/**
	 * Creates a new resource folder.
	 * 
	 * 
	 * @param rootDir
	 *            The root directory of this folder. This directory is created
	 *            if it not yet exists.
	 */
	public ResourceFolder(File rootDir) {
		if (rootDir == null) {
			throw new IllegalArgumentException(
					"The root directory cannot be NULL.");
		}
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		if (!rootDir.isDirectory()) {
			throw new IllegalArgumentException(
					"The given root directory is NOT a directory.");
		}
		this.rootDir = rootDir;
	}

	/**
	 * Get the optional HTML file of this folder describing the content.
	 */
	public File getIndexFile() {
		return new File(rootDir, "index.html");
	}

	/**
	 * Get the optional HTML log file.
	 */
	public File getLogFile() {
		return new File(rootDir, "log.html");
	}

	/**
	 * Get the optional HTML file with the logs written in a validation process.
	 */
	public File getValidationLog() {
		return new File(rootDir, "validation.html");
	}

	/**
	 * Returns the root directory of the folder.
	 */
	public File getRootDir() {
		return rootDir;
	}

	/**
	 * Creates the content of a format specific folder. The content of a folder
	 * are for example sub-directories, style-sheets, schemas, and XML files.
	 */
	public abstract void createContent();

	/**
	 * Returns the files (not the directories) of the root and all sub-folders.
	 */
	public List<File> listFiles() {
		List<File> files = new ArrayList<File>();
		if (rootDir != null && rootDir.exists() && rootDir.isDirectory()) {
			Queue<File> queue = new LinkedList<File>();
			queue.add(rootDir);
			while (!queue.isEmpty()) {
				for (File f : queue.poll().listFiles()) {
					if (f.isDirectory()) {
						queue.add(f);
					} else {
						files.add(f);
					}
				}
			}
		}
		return files;
	}

	/**
	 * A helper function which extracts resources from the given sub-package
	 * into the target folder. The sub-package is searched relative to the
	 * package of the instances class.
	 * 
	 */
	protected void extract(String subPackage, File targetFolder) {

		try {
			// create the target folder if necessary
			if (!targetFolder.exists())
				targetFolder.mkdir();

			// get the file index of the package to be extracted
			InputStream idxIS = this.getClass().getResourceAsStream(
					subPackage + "/index.xml");
			SAXBuilder builder = new SAXBuilder();
			Document index = builder.build(idxIS);

			// extract the files
			for (Object o : index.getRootElement().getChildren("file")) {

				Element fileElement = (Element) o;
				String fileName = fileElement.getTextNormalize();
				File targetFile = new File(targetFolder, fileName);

				if (!targetFile.exists()) {
					// read the file
					BufferedInputStream in = new BufferedInputStream(this
							.getClass().getResourceAsStream(
									subPackage + "/" + fileName));

					// extract the file into the target folder
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(targetFile));
					int b = -1;
					while ((b = in.read()) != -1) {
						out.write(b);
					}
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
