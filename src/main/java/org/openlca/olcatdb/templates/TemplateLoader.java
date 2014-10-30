package org.openlca.olcatdb.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

/**
 * Initializes and loads the Velocity templates.
 * 
 * @author Michael Srocka
 * @author Georg Kšster
 * 
 */
public class TemplateLoader {

	private static volatile TemplateLoader loader;
	private static Object loaderLock = new Object();
	private static String templateFolder = "templates";
	private volatile VelocityEngine engine;

	private TemplateLoader() {
		initializeTemplatesAndEngine();
	}

	/**
	 * Get the singleton instance of the template loader.
	 */
	public static TemplateLoader getInstance() {
		if (loader == null) {
			synchronized (loaderLock) {
				if (loader == null) {
					loader = new TemplateLoader();
				}
			}
		}
		return loader;
	}

	/**
	 * Set the folder where the templates should be loaded from. If there is an
	 * instance of the template loader already created it is re-initialized with
	 * the new folder.
	 */
	public static void setTemplateFolder(String folder) {
		if (!folder.equals(templateFolder)) {
			templateFolder = folder;
			if (loader != null) {
				loader = new TemplateLoader();
			}
		}
	}

	private void initializeTemplatesAndEngine() {
		File templateDir = initTemplateFolder();
		extractTemplates(templateDir);
		initEngine();
	}

	private File initTemplateFolder() {
		File templateDir = new File(templateFolder);
		if (!templateDir.exists()) {
			templateDir.mkdir();
		}
		return templateDir;
	}

	private void extractTemplates(File templateDir) {
		for (TemplateType templateType : TemplateType.values()) {
			String templateName = templateType.getTemplateName();
			File templateFile = new File(templateDir, templateName);
			if (!templateFile.exists()) {
				extractTemplate(templateName, templateFile);
			}
		}
	}

	private void extractTemplate(String templateName, File templateFile) {
		try {
			tryExtractTemplate(templateName, templateFile);
		} catch (Exception e) {
			String message = String.format(
					"Cannot extract template %s to folder %s.", templateName,
					templateFile);
			throw new RuntimeException(message, e);
		}
	}

	private void tryExtractTemplate(String templateName, File templateFile)
			throws Exception {
		InputStream is = TemplateLoader.class.getResourceAsStream(templateName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		BufferedWriter writer = new BufferedWriter(new FileWriter(templateFile));
		String line = null;
		while ((line = reader.readLine()) != null) {
			writer.write(line);
			writer.newLine();
		}
		writer.flush();
		writer.close();
		reader.close();
	}

	private void initEngine() {
		synchronized (this) {
			if (engine != null) {
				return;
			}
			engine = new VelocityEngine();
			engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
					templateFolder);
			try {
				Properties props = new Properties();
				props.put("file.resource.loader.cache", "true");
				props.put("resource.loader", "file");
				engine.init(props);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the Velocity template for the given type.
	 */
	public Template getTemplate(TemplateType type) {
		Template t = null;
		try {
			t = engine.getTemplate(type.getTemplateName());
		} catch (Exception e) {
			String message = String.format("Cannot load template %s.",
					type.getTemplateName());
			throw new RuntimeException(message, e);
		}
		return t;
	}

}
