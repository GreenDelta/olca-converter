package org.openlca.olcatdb.ecospold2.masterdata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.openlca.olcatdb.parsing.XmlContextParser;

/**
 * 
 * @author Imo Graf
 * 
 */
public class ES2MDDataSet {

	public ES2ActivityList activityIndex;
	public ES2ActivityNameList activityNames;
	public ES2ClassificationSystemList classifications;
	public ES2CompanyList companies;
	public ES2CompartmentList compartments;
	public ES2Context context;
	public ES2ElemFlowList elementaryExchanges;
	public ES2ProductFlowList intermediateExchanges;
	public ES2GeographyList geographies;
	public ES2LanguageList languages;
	public ES2MacroEconomicScenarioList macroEconomicScenarios;
	public ES2ParameterList parameters;
	public ES2PersonList persons;
	public ES2PropertyList properties;
	public ES2SourceList sources;
	public ES2SystemModelList systemModels;
	public ES2TagList tags;
	public ES2UnitList units;

	private void set(Object object) throws IllegalArgumentException,
			IllegalAccessException {
		
		Field[] fields = this.getClass().getFields();

		for (Field field : fields) {
			if (field.getType().equals(object.getClass())) {
				field.set(this, object);
			}
		}
	}

	/**
	 * Read the master date and fill the lists.
	 * 
	 * @param zipFile
	 * @throws java.io.IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void read(File file) throws IOException, IllegalArgumentException,
			IllegalAccessException {
		if (file != null) {
			ZipFile zipFile = new ZipFile(file);

			XmlContextParser parser = new XmlContextParser();
			ZipFile zf = zipFile;
			Enumeration<?> entries = zf.entries();

			Map<String, Class<?>> map = new HashMap<String, Class<?>>();
			map.put("ActivityIndex.xml", ES2ActivityList.class);
			map.put("ActivityNames.xml", ES2ActivityNameList.class);
			map.put("Classifications.xml", ES2ClassificationSystemList.class);
			map.put("Companies.xml", ES2CompanyList.class);
			map.put("Compartments.xml", ES2CompartmentList.class);
			map.put("Context.xml", ES2Context.class);
			map.put("ElementaryExchanges.xml", ES2ElemFlowList.class);
			map.put("IntermediateExchanges.xml", ES2ProductFlowList.class);
			map.put("Geographies.xml", ES2GeographyList.class);
			map.put("Languages.xml", ES2LanguageList.class);
			map.put("MacroEconomicScenarios.xml",
					ES2MacroEconomicScenarioList.class);
			map.put("Parameters.xml", ES2ParameterList.class);
			map.put("Persons.xml", ES2PersonList.class);
			map.put("Properties.xml", ES2PropertyList.class);
			map.put("Sources.xml", ES2SourceList.class);
			map.put("SystemModels.xml", ES2SystemModelList.class);
			map.put("Tags.xml", ES2TagList.class);
			map.put("Units.xml", ES2UnitList.class);

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				File entryFile = new File(entry.getName());

				if (map.containsKey(entryFile.getName())) {
					InputStream stream = zipFile.getInputStream(entry);
					Class<?> mdClass = map.get(entryFile.getName());
					Object object = parser.getContext(mdClass, stream);

					if (object != null)
						set(object);
				}
			}
		}
	}

	public <T> T getMasterData(Class<T> mdClass)
			throws IllegalArgumentException, IllegalAccessException {
		T result = null;
		Field[] fields = this.getClass().getFields();

		for (Field field : fields) {
			if (field.getType().equals(mdClass)) {
				result = mdClass.cast(field.get(this));
			}
		}

		return result;
	}
}
