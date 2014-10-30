package org.openlca.olcatdb.conversion;

import java.io.File;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openlca.olcatdb.ResourceFolder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Validation of a resource folder.
 * 
 * @author Michael Srocka
 * 
 */
public class Validation implements Task {

	/**
	 * The folder to be validated.
	 */
	private ResourceFolder folder;

	/**
	 * An optional monitor for the validation progress.
	 */
	private Monitor monitor;

	/**
	 * Indicates the the process was canceled.
	 */
	private boolean canceled = false;

	/**
	 * Validates the XML files in the resource folder against their respective
	 * schema.
	 * 
	 * @param The
	 *            resource folder to be validated.
	 */
	public Validation(ResourceFolder folder) {
		this.folder = folder;
	}

	/**
	 * Set the optional monitor of the validation progress.
	 */
	@Override
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {

		List<File> files = folder.listFiles();
		if (monitor != null) {
			monitor.begin(files.size());
		}

		// set up the logger
		Logger logger = Logger.getLogger("org.openlca.olcatdb.conversion");
		FileHandler handler = null;
		try {
			handler = new FileHandler(folder.getValidationLog()
					.getAbsolutePath());
			handler.setFormatter(new LogHtmlFormatter());
			logger.addHandler(handler);
		} catch (Exception e) {
			logger.severe("Cannot add file handler " + e.getMessage());
		}

		DocumentBuilder builder = docBuilder();
		if (builder == null) {
			logger.severe("Cannot set up document builder.");
		} else {

			ValidationHandler errorHandler = new ValidationHandler();
			builder.setErrorHandler(errorHandler);
			errorHandler.logger = logger;

			// validate the XML files
			int worked = 0;
			for (File file : files) {

				if (canceled) {
					break;
				}

				errorHandler.file = file;

				if (monitor != null) {
					monitor.progress("Validate " + file.getName(), ++worked);
				}

				if (file.isFile()
						&& (file.getName().toLowerCase().endsWith(".xml") || file
								.getName().toLowerCase().endsWith(".spold"))) {
					// parse the XML
					logger.info("Validate file " + file.getName());
					try {
						builder.parse(file);
					} catch (Exception e) {
						logger.severe("Cannot parse file " + file.getName()
								+ ": " + e.getMessage());
					}
				}
			}
		}

		// remove the file handler
		if (handler != null) {
			handler.flush();
			handler.close();
			logger.removeHandler(handler);
		}

		if (monitor != null) {
			monitor.finished(this);
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}

	@Override
	public ResourceFolder getResult() {
		return folder;
	}

	/**
	 * Creates the document builder which is used for validating XML files.
	 */
	private DocumentBuilder docBuilder() {
		DocumentBuilder docBuilder = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(true);
			factory.setNamespaceAware(true);
			factory.setAttribute(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			docBuilder = factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docBuilder;
	}

	/**
	 * A handler for Validation events.
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class ValidationHandler implements ErrorHandler {

		/**
		 * The current file to be validated.
		 */
		private File file;

		/**
		 * The current logger for catching validation events.
		 */
		private Logger logger;

		@Override
		public void error(SAXParseException ex) throws SAXException {
			if (file != null && logger != null) {
				logger.severe("Validation of file " + file.getName() + ": "
						+ ex.getMessage());
			}
		}

		@Override
		public void fatalError(SAXParseException ex) throws SAXException {
			if (file != null && logger != null) {
				logger.severe("Validation of file " + file.getName() + ": "
						+ ex.getMessage());
			}
		}

		@Override
		public void warning(SAXParseException ex) throws SAXException {
			if (file != null && logger != null) {
				logger.warning("Validation of file " + file.getName() + ": "
						+ ex.getMessage());
			}
		}
	}

}
