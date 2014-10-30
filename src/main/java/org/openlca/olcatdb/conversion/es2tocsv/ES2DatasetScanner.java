package org.openlca.olcatdb.conversion.es2tocsv;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.conversion.AbstractConversionImpl;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2ElementaryExchange;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.XmlContextParser;

public class ES2DatasetScanner extends AbstractConversionImpl {
	private List<String> langs = new ArrayList<String>();
	private List<String> units = new ArrayList<String>();
	private Map<String, String[]> comparments = new HashMap<String, String[]>();
	private Set<String> geos = new HashSet<String>();
	private ScanMode mode;

	public ES2DatasetScanner(ScanMode mode, File sourceFile) {
		this.mode = mode;
		setSourceFile(sourceFile);
		super.monitorProgressText = "Scan";
	}

	public Map<String, String[]> getComparments() {
		return comparments;
	}

	public List<String> getLangs() {
		Collections.sort(langs);
		return langs;
	}

	public Set<String> getGeos() {
		return geos;
	}

	public List<String> getUnits() {
		Collections.sort(units);
		return units;
	}

	public void setMode(ScanMode mode) {
		this.mode = mode;
	}

	@Override
	public void run() {
		XmlContextParser parser = new XmlContextParser();
		while (this.hasNext()) {
			InputStream is = this.next();
			if (is != null) {
				try {
					ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class,
							is);
					is.close();
					if (ecoSpold != null) {
						for (ES2Dataset dataset : ecoSpold.getDatasets()) {

							switch (mode) {
							case COMPARTMENTS:
								scanCompartments(dataset);
								break;

							case GEO:
								scanGeos(dataset);
								break;

							case LANG:
								scanLangs(dataset);
								break;

							case UNITS:
								scanUnits(dataset);
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		flush();
	}

	@Override
	public ResourceFolder getResult() {
		return null;
	}

	private List<Object> getAllContextObjects(Object object)
			throws IllegalArgumentException, IllegalAccessException {
		List<Object> contextObjects = new ArrayList<Object>();

		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getType().getSuperclass() != null
					&& field.getType().getSuperclass()
							.equals(ContextObject.class)) {
				contextObjects.add(field.get(object));
			} else if (field.getType() != null
					&& field.getType().equals(List.class)) {
				List<Object> list = (List<Object>) field.get(object);
				for (Object o : list) {
					if (o.getClass().getSuperclass()
							.equals(ContextObject.class)) {
						contextObjects.add(o);
					}
				}
			}
		}

		return contextObjects;
	}

	private void scanLangs(Object object) throws IllegalArgumentException,
			IllegalAccessException {
		if (object != null) {

			// get all contextObjects
			List<Object> contextObjects = getAllContextObjects(object);

			// scan all fields
			for (Field field : object.getClass().getDeclaredFields()) {
				if (field.getType() != null
						&& field.getType().equals(List.class)) {
					field.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<Object> list = (List<Object>) field.get(object);
					if (list.size() > 0) {
						if (list.get(0).getClass().equals(LangString.class)) {
							for (Object o : list) {
								LangString langString = LangString.class
										.cast(o);
								if (langString.getLangCode() != null
										&& !langs.contains(langString
												.getLangCode()))
									langs.add(langString.getLangCode());
							}
						}
					}
				}
			}

			// scan all objects
			for (Object o : contextObjects) {
				scanLangs(o);
			}

		}

	}

	private void scanUnits(Object object) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		if (object != null) {

			// get all contextObjects
			List<Object> contextObjects = getAllContextObjects(object);

			// scan all fields
			for (Method method : object.getClass().getMethods()) {
				if (method.getName().equals("getUnitNames")) {
					List<LangString> unitNames = (List<LangString>) method
							.invoke(object);
					String unit = ConversionUtils.getLangEntry(unitNames);
					if (unit != null && !units.contains(unit))
						units.add(unit);
				}
			}

			// scan all objects
			for (Object o : contextObjects) {
				scanUnits(o);
			}

		}

	}

	private void scanGeos(ES2Dataset dataset) throws IllegalArgumentException,
			IllegalAccessException {
		String geo = null;
		if (dataset.geography != null) {
			geo = ConversionUtils.getLangEntry(dataset.geography
					.getShortNames());
		}

		if (geo != null)
			geos.add(geo);

	}

	private void scanCompartments(ES2Dataset dataset) {
		for (ES2ElementaryExchange exchange : dataset.getElementaryExchanges()) {

			String compartment = ConversionUtils.getLangEntry(exchange
					.getCompartment());
			String subcompartment = ConversionUtils.getLangEntry(exchange
					.getSubCompartment());
			String key = compartment + subcompartment;
			String elem[] = new String[2];
			elem[0] = compartment;
			elem[1] = subcompartment;
			comparments.put(key, elem);
		}

	}

	public enum ScanMode {
		LANG, COMPARTMENTS, GEO, UNITS
	}

}
