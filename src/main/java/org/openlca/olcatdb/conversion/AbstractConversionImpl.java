package org.openlca.olcatdb.conversion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.ilcd.ILCDContact;
import org.openlca.olcatdb.ilcd.ILCDDataSetType;
import org.openlca.olcatdb.ilcd.ILCDFlow;
import org.openlca.olcatdb.ilcd.ILCDFlowProperty;
import org.openlca.olcatdb.ilcd.ILCDSource;
import org.openlca.olcatdb.ilcd.ILCDUnitGroup;
import org.openlca.olcatdb.parsing.XmlContextParser;

/**
 * A package local helper class with recurring functions intended to be
 * sub-classed.
 * 
 * @author Michael Srocka
 */
public abstract class AbstractConversionImpl implements Conversion,
		Iterator<InputStream> {

	public String monitorProgressText = "Convert";

	/**
	 * The logger for errors and exceptions.
	 */
	protected Logger logger = Logger
			.getLogger("org.openlca.olcatdb.conversion");

	/**
	 * The index with the created files.
	 */
	protected FileIndex createdFiles = new FileIndex();

	/**
	 * The source XML file to be converted. This file is <code>null</code> if
	 * the source file is a ZIP file.
	 */
	protected File sourceXml = null;

	/**
	 * The next XML file to be opened and parsed. The source XML is used for
	 * referencing other resources, where this file is used for the iterator
	 * process.
	 */
	private File nextFile = null;

	/**
	 * The source ZIP file to be converted. This file is <code>null</code> if
	 * the source file is a XML file.
	 */
	protected ZipFile sourceZip = null;

	/**
	 * The enumeration of ZIP entries available if the source file is a ZIP
	 * package.
	 */
	private Enumeration<? extends ZipEntry> zipEntries = null;

	/**
	 * The next entry of a ZIP package to be opened.
	 */
	private ZipEntry nextEntry = null;

	/**
	 * Caches created objects like sources, contacts, and flows.
	 */
	private HashMap<String, Object> objectCache = new HashMap<String, Object>();

	/**
	 * The log management.
	 */
	private Logs logs;

	/**
	 * Time measurement of the conversion: the conversion starts with the
	 * creation of the target folder and ends with flush - function.
	 */
	private Timer timer;

	/**
	 * An optional log file handler which can be initialized by sub-classes.
	 */
	private FileHandler logFile;

	/**
	 * The target directory where the output folder is created.
	 */
	protected File targetDir;

	/**
	 * An optional monitor for the observation of the conversion progress.
	 */
	protected Monitor monitor;

	private int worked = 0;

	/**
	 * Indicates that the conversion process was canceled.
	 */
	protected boolean canceled = false;

	/**
	 * Returns the current file name
	 */

	public String getCurrentFileName() {
		String fileName = null;
		if (nextFile != null)
			fileName = nextFile.getName();
		else if (nextEntry != null)
			fileName = nextEntry.getName();

		return fileName;
	}

	/**
	 * Sets the source file to be converted. Allowed parameters are XML and ZIP
	 * files.
	 */
	@Override
	public void setSourceFile(File sourceFile) {
		// the source file dispatch depending on the file extension
		if (sourceFile != null) {

			String name = sourceFile.getName().toLowerCase();

			if (name.endsWith(".xml") || name.endsWith(".spold")) {
				sourceXml = sourceFile;
				nextFile = sourceFile;
			} else if (name.endsWith(".zip")) {

				try {
					sourceZip = new ZipFile(sourceFile);
					zipEntries = sourceZip.entries();
					pointNextEntry();
				} catch (Exception e) {
					logger.severe("Cannot open the source ZIP: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}

	/**
	 * Indicates whether there is an un-handled stream of a data set in the
	 * source format available.
	 */
	@Override
	public boolean hasNext() {
		return !canceled && (nextFile != null || nextEntry != null);
	}

	@Override
	public void cancel() {
		canceled = true;
	}

	/**
	 * Returns the next input stream of a data set in the respective source
	 * format. If something went wrong, the respective error logs are written
	 * and <code>null</code> is returned.
	 */
	@Override
	public InputStream next() {

		InputStream is = null;
		String next = "";

		if (nextFile == null && nextEntry == null) {

			logger.severe("No source XML file or ZIP entry could be loaded.");

		} else if (nextFile != null) {

			// get the stream from a single XML file
			next = nextFile.getName();
			try {
				is = new FileInputStream(nextFile);
			} catch (Exception e) {
				logger.severe("Cannot load XML file: " + e.getMessage());
				e.printStackTrace();
			} finally {
				// there is nothing to load next
				nextFile = null;
			}
		} else {

			// get the stream of the next ZIP entry
			try {
				next = nextEntry.getName();
				is = sourceZip.getInputStream(nextEntry);
			} catch (Exception e) {
				logger.severe("Cannot load ZIP entry: " + e.getMessage());
				e.printStackTrace();
			} finally {
				pointNextEntry();
			}
		}

		if (monitor != null && is != null) {
			monitor.progress(monitorProgressText + " " + next, ++worked);
		}

		return is;
	}

	/**
	 * As this function is optional (see the iterator doc) and there is no
	 * reason why a data set should be removed on this stage, a call of this
	 * function has no effect.
	 */
	@Override
	public void remove() {
	}

	/**
	 * This method is intended to be used in sub-classes
	 * (super.createFolder(.)).
	 */
	protected void createFolder(File targetDir) {

		// start the clock
		timer = new Timer();
		timer.start();

		// add the log handler
		logs = new Logs();
		logger.addHandler(logs);

		// initialize the monitor
		if (monitor != null) {
			monitor.begin(estimateEffort());
			if (sourceXml != null) {
				monitor.progress("Setup conversion", 0);
			}
		}
	}

	/**
	 * Initializes the log file of the resource folder.
	 */
	protected void logFile(ResourceFolder folder) {
		try {
			File file = folder.getLogFile();
			this.logFile = new FileHandler(file.getAbsolutePath());
			logFile.setFormatter(new LogHtmlFormatter());
			logger.addHandler(logFile);
		} catch (Exception e) {
			logger.severe("Cannot create log file: " + e.getMessage());
		}
	}

	/**
	 * This predicate returns true if the given entry name indicates an XML file
	 * to be converted. This method can be sub-classed in order to apply further
	 * filtering on ZIP entries (e.g. get only the process data sets of an ILCD
	 * package).
	 */
	protected boolean matchEntry(String entryName) {
		return entryName != null
				&& (entryName.toLowerCase().endsWith(".xml") || entryName
						.toLowerCase().endsWith(".spold"));
	}

	/**
	 * Tries to load the referenced object of the given type.
	 */
	protected Object fetch(DataSetReference ref) {

		if (ref == null) {
			logger.severe("Fetch on NULL reference.");
			return null;
		}

		// check if there is an URI given
		String uri = ref.getUri();
		if (uri == null) {
			logger.severe("No URI in data set reference.");
			return null;
		}

		// check if there is a cached object for the URI
		Object o = objectCache.get(uri);
		if (o != null) {
			return o;
		}

		// get the ILCD data set type of the reference
		String refTypeStr = ref.getType();
		if (refTypeStr == null) {
			logger.severe("There is no data set "
					+ "type given in reference to file " + uri);
			return null;
		}

		ILCDDataSetType refType = ILCDDataSetType.forName(refTypeStr);
		if (refType == null) {
			logger.severe("There is no data set type for name '" + refTypeStr
					+ "'.");
			return null;
		}

		// get the context class
		Class<?> contextClass = null;
		String folder = null; // for the search in the ILCD package
		switch (refType) {
		case Contact:
			contextClass = ILCDContact.class;
			folder = "contacts";
			break;
		case Flow:
			contextClass = ILCDFlow.class;
			folder = "flows";
			break;
		case Source:
			contextClass = ILCDSource.class;
			folder = "sources";
			break;
		case FlowProperty:
			contextClass = ILCDFlowProperty.class;
			folder = "flowproperties";
			break;
		case UnitGroup:
			contextClass = ILCDUnitGroup.class;
			folder = "unitgroups";
			break;
		default:
			break;
		// TODO: add other types...
		}

		if (contextClass == null) {
			logger.severe("No context call available for type '" + refTypeStr
					+ "'");
			return null;
		}

		// get the ref-object ID
		String refId = ref.getRefObjectId();
		if (refId == null) {
			logger.severe("No reference object ID given in reference to file '"
					+ uri + "'");
			return null;
		}

		Object context = null;

		if (sourceXml != null) {

			// get the file relative to the source file

			// check if the referenced file exists
			File file = new File(sourceXml.getParentFile(), uri);
			if (!file.exists() || !file.isFile()) {
				logger.severe("The referenced file not exists: "
						+ file.getAbsolutePath());
				return null;
			}

			// try to parse the referenced file
			try {
				FileInputStream fis = new FileInputStream(file);
				XmlContextParser parser = new XmlContextParser();
				context = parser.getContext(contextClass, fis);
				objectCache.put(uri, context);
			} catch (Exception e) {
				logger.severe("Cannot parse the referenced file: "
						+ file.getAbsolutePath());
			}

		} else if (sourceZip != null) {

			// get the entry from the ZIP file

			ZipEntry entry = null;
			Enumeration<? extends ZipEntry> entries = sourceZip.entries();
			while (entries.hasMoreElements() && entry == null) {
				ZipEntry zipEntry = entries.nextElement();
				String name = zipEntry.getName();
				if (!zipEntry.isDirectory()
						&& (name.endsWith(".xml") || name.endsWith(".XML"))
						&& name.contains(folder) && name.contains(refId)) {
					entry = zipEntry;
				}
			}

			if (entry == null) {
				logger.severe("Cannot find Zip entry for reference " + uri);
				return null;
			} else {

				try {
					XmlContextParser parser = new XmlContextParser();
					InputStream is = sourceZip.getInputStream(entry);
					context = parser.getContext(contextClass, is);
					objectCache.put(uri, context);
				} catch (Exception e) {
					logger.severe("Cannot parse the referenced Zip entry "
							+ uri);
				}
			}
		}

		if (context == null) {
			logger.severe("Cannot load object for reference: " + ref);
		}

		return context;
	}

	/**
	 * Writes the log and index file and frees resources. This function should
	 * be called as last method in the run() function of the respective
	 * conversion implementation.
	 */
	protected void flush() {

		ResourceFolder folder = getResult();
		if (folder != null) {

			if (logs != null) {
				logger.removeHandler(logs);
				logs.flush();
				logs.close();
				if (logs.count > 0 && folder.getLogFile() != null) {
					createdFiles.setLog(folder.getLogFile().toURI().toString());
				}
			}

			// create the index title
			if (createdFiles != null) {
				String title = Integer.toString(createdFiles.getDataSets()
						.size()) + " files created";
				if (timer != null) {
					timer.stop();
					title += " (" + timer.toString() + ")";
				}
				createdFiles.setTitle(title);
			}

			// write the file index
			File indexFile = folder.getIndexFile();
			if (indexFile != null && createdFiles != null) {
				try {
					createdFiles.toFile(indexFile);
				} catch (Exception e) {
					logger.severe("Cannot create index file: " + e.getMessage());
				}
			}
		}

		// remove the log management
		if (logFile != null) {
			logger.removeHandler(logFile);
			logFile.flush();
			logFile.close();
		}

		objectCache.clear();
		if (sourceZip != null) {
			try {
				sourceZip.close();
			} catch (Exception e) {
				logger.severe("Cannot close ZIP file: " + e.getMessage());
				e.printStackTrace();
			}
			sourceZip = null;
		}

		if (monitor != null) {
			monitor.finished(this);
		}
	}

	/**
	 * Estimate the conversion effort, i.e. the number of XML files to be
	 * converted.
	 */
	private int estimateEffort() {
		int size = 0;
		if (sourceXml != null) {
			size = 2; // 50% to setup the directory
		} else if (sourceZip != null) {
			try {
				Enumeration<? extends ZipEntry> entries = sourceZip.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if (matchEntry(entry.getName())) {
						size++;
					}
				}
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
		}
		return size;
	}

	/**
	 * Selects the next ZIP entry to be opened if there is any.
	 */
	private void pointNextEntry() {
		nextEntry = null;
		if (zipEntries != null) {

			while (nextEntry == null && zipEntries.hasMoreElements()) {

				ZipEntry entry = zipEntries.nextElement();
				if (!entry.isDirectory()) {
					String entryName = entry.getName();
					if (matchEntry(entryName)) {
						nextEntry = entry;
					}
				}
			}
		}
	}

	/**
	 * A handler class for the log-management.
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class Logs extends Handler {

		private int count = 0;

		// boolean withErrors = false;

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {
		}

		@Override
		public void publish(LogRecord record) {
			count++;
		}
	}

	/**
	 * A clock.
	 * 
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class Timer {

		private long atStart;

		private long atStop;

		public void start() {
			atStart = new GregorianCalendar().getTimeInMillis();
		}

		public void stop() {
			atStop = new GregorianCalendar().getTimeInMillis();
		}

		public String toString() {
			long sec = (atStop - atStart) / 1000;
			return Long.toString(sec) + " seconds";
		}

	}

}
