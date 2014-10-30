package org.openlca.olcatdb.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.openlca.olcatdb.templates.TemplateLoader;
import org.openlca.olcatdb.templates.TemplateType;

/**
 * The XML writer for the respective format context objects.
 * 
 * @author Michael Srocka
 * @author Georg Kšster
 * 
 */
public class XmlOutputter {

	// the default character encoding of this outputter
	private static final String ENCODING = "utf-8";

	private final static ExecutorService executor = new ThreadPoolExecutor(0,
			8, 2L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
			new ThreadPoolExecutor.CallerRunsPolicy());
	/**
	 * Writes the context object to the given file using the specified template.
	 * 
	 * @param context
	 *            the context to be written
	 * @param templateType
	 *            the template of the output
	 * @param file
	 *            the file to be written
	 */
	public void output(Object context, TemplateType templateType, File file) {
		output(context, templateType, file, false);
	}
	
	/**
	 * Writes the context object to the given file using the specified template.
	 * 
	 * @param context
	 *            the context to be written
	 * @param templateType
	 *            the template of the output
	 * @param file
	 *            the file to be written
	 * @param otherThread
	 *            indicates whether to run this operation in an own thread (if
	 *            <code>true</code>, a new thread is created for the output,
	 *            otherwise not)
	 */
	public void output(Object context, TemplateType templateType, File file,
			boolean otherThread) {

		if (context == null || templateType == null || file == null) {
			throw new IllegalArgumentException(
					"The context object, template type, "
							+ "and target file cannot be NULL.");
		}

		FileWorker worker = new FileWorker(context, templateType, file);
		if (!otherThread) {
			worker.run();
		} else {
			executor.execute(worker);
		}		
	}

	/**
	 * Writes the context object to the given writer using the specified
	 * template.
	 * 
	 * @param context
	 *            the context to be written
	 * @param templateType
	 *            the template of the output
	 * @param writer
	 *            the writer for the output
	 */
	public void output(Object context, TemplateType templateType,
			Writer writer) {
		output(context, templateType, writer, false);
	}
	
	/**
	 * Writes the context object to the given writer using the specified
	 * template.
	 * 
	 * @param context
	 *            the context to be written
	 * @param templateType
	 *            the template of the output
	 * @param writer
	 *            the writer for the output
	 * @param otherThread
	 *            indicates whether to run this operation in an own thread (if
	 *            <code>true</code>, a new thread is created for the output,
	 *            otherwise not)
	 */
	public void output(Object context, TemplateType templateType,
			Writer writer, boolean otherThread) {

		if (context == null || templateType == null || writer == null) {
			throw new IllegalArgumentException(
					"The context object, template type, "
							+ "and writer cannot be NULL.");
		}

		WriterWorker worker = new WriterWorker(context, templateType, writer);
		if (!otherThread) {
			worker.run();
		} else {
			executor.execute(worker);
		}
	}

	/**
	 * A worker for the file output.
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class FileWorker implements Runnable {

		private Object context;

		private TemplateType templateType;

		private File file;

		public FileWorker(Object context, TemplateType templateType, File file) {
			super();
			this.context = context;
			this.templateType = templateType;
			this.file = file;
		}

		@Override
		public void run() {
			Template template = TemplateLoader.getInstance().getTemplate(
					templateType);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put(templateType.getContextName(), context);
			try {

				EventCartridge cartridge = new EventCartridge();
				cartridge.addEventHandler(new EscapeXmlReference());
				cartridge.attachToContext(velocityContext);
				Writer writer = new OutputStreamWriter(new FileOutputStream(
						file), ENCODING);
				template.merge(velocityContext, writer);
				writer.flush();
				writer.close();
				XmlFormatter formatter = new XmlFormatter();
				formatter.format(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A worker for the writer output.
	 * 
	 * @author ms
	 * 
	 */
	private class WriterWorker implements Runnable {

		private Object context;

		private TemplateType templateType;

		private Writer writer;

		public WriterWorker(Object context, TemplateType templateType,
				Writer writer) {
			this.context = context;
			this.templateType = templateType;
			this.writer = writer;
		}

		@Override
		public void run() {
			Template template = TemplateLoader.getInstance().getTemplate(
					templateType);
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put(templateType.getContextName(), context);
			try {

				EventCartridge cartridge = new EventCartridge();
				cartridge.addEventHandler(new EscapeXmlReference());
				cartridge.attachToContext(velocityContext);

				// write the context to a cache
				StringWriter cacheWriter = new StringWriter();
				template.merge(velocityContext, cacheWriter);
				cacheWriter.flush();
				cacheWriter.close();
				StringReader cacheReader = new StringReader(cacheWriter
						.toString());

				// write the formatted output from the cache
				XmlFormatter formatter = new XmlFormatter();
				formatter.format(cacheReader, writer);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
