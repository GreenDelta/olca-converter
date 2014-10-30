package org.openlca.olcatdb.conversion.es2tocsv;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.beanutils.PropertyUtils;
import org.openlca.olcatdb.ecospold2.ES2Dataset;
import org.openlca.olcatdb.ecospold2.ES2EcoSpold;
import org.openlca.olcatdb.ecospold2.ES2Uncertainty;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.XmlContextParser;

/**
 * 
 * @author Imo Graf
 * 
 */
public class DataSetMerger {

	void mergeChildAndParentDataset(ES2Dataset childDataset,
			Map<String, ActivityEntry> activityIndex, File sourceXml,
			ZipFile sourceZip, Logger logger) {
		try {
			ActivityEntry entry = activityIndex
					.get(childDataset.description.parentActivityId);
			if (entry == null)
				logger.severe("For the child data set \""
						+ ConversionUtils.getLangEntry(childDataset.description
								.getName())
						+ "\" is the parent data set missing.");

			// TODO same EcoSpold child and normal data set
			ES2Dataset parentDataset = getParentDataset(entry.fileName,
					entry.id, sourceXml, sourceZip);

			// TODO multiple childs

			merge(childDataset, parentDataset);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

	}

	private ES2Dataset getParentDataset(String fileName, String parentId,
			File sourceXml, ZipFile sourceZip) throws IOException {
		ES2Dataset dataset = null;
		XmlContextParser parser = new XmlContextParser();
		if (sourceXml != null) {
			InputStream stream = new FileInputStream(sourceXml);
			ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class, stream);
			if (ecoSpold != null) {

				for (ES2Dataset d : ecoSpold.getDatasets()) {
					if (d.description.id.equals(parentId))
						dataset = d;
				}

			}

		} else if (sourceZip != null) {
			ZipFile zf = sourceZip;
			ZipEntry entry = zf.getEntry(fileName);

			InputStream stream = sourceZip.getInputStream(entry);

			ES2EcoSpold ecoSpold = parser.getContext(ES2EcoSpold.class, stream);
			if (ecoSpold != null) {

				for (ES2Dataset d : ecoSpold.getDatasets()) {
					if (d.description.id.equals(parentId))
						dataset = d;
				}

			}

		}

		return dataset;
	}

	/**
	 * 
	 * @param child
	 * @param parent
	 * @throws NoSuchMethodException
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void merge(Object child, Object parent)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		if (child == null)
			throw new IllegalArgumentException(
					"Object 'child' can not be null!");
		if (parent != null) {

			PropertyDescriptor[] propertyDescriptors = PropertyUtils
					.getPropertyDescriptors(child);
			for (PropertyDescriptor pd : propertyDescriptors) {
				if (pd.getPropertyType().equals(List.class)) {

					List childList = (List) PropertyUtils.getSimpleProperty(
							child, pd.getName());

					List parentList = (List) PropertyUtils.getSimpleProperty(
							parent, pd.getName());

					if (parentList.size() > 0)
						compareList(childList, parentList);
				}
			}

			Field[] fields = child.getClass().getDeclaredFields();
			// merge groups
			Set<Class<?>> groupSet = new HashSet<Class<?>>();
			for (Field f : fields) {
				ContextField cf = getContextField(f);
				if (cf != null && !cf.classGroup().equals(Object.class))
					groupSet.add(cf.classGroup());
			}

			for (Class<?> c : groupSet) {
				List<Field> groupFields = new ArrayList<Field>();
				for (Field f : fields) {
					ContextField cf = getContextField(f);
					if (cf != null && cf.classGroup().equals(c))
						groupFields.add(f);
				}
				if (groupFields.size() > 0)
					mergeGroup(groupFields, child, parent);
			}

			// merge all other fields
			fields = child.getClass().getFields();
			for (Field field : fields) {

				Object c = field.get(child);
				Object p = field.get(parent);

				// For subclasses
				if (field.getType().getSuperclass() != null
						&& field.getType().getSuperclass()
								.equals(ContextObject.class)) {

					if (c == null && p != null)
						field.set(child, p);
					else if (c != null && p != null)
						merge(c, p);

					// for attributes
				} else if (field.getType() != null
						&& field.getType().equals(ES2Uncertainty.class)) {
					mergeUncertainty(c, p);
				} else {

					ContextField cf = getContextField(field);

					if (cf.classGroup().equals(Object.class)) {

						// search isOverwrittenbychild
						boolean isOverwrittenByChild = isOverwrittenByChild(cf,
								child);

						if (cf.isMaster()) {
							if (!isOverwrittenByChild && p != null
									&& p.getClass().equals(String.class)
									&& !String.class.cast(p).equals(""))
								field.set(child, p);

						} else if (c == null && p != null) {
							field.set(child, p);
						}
					}

				}
			}

		}
	}

	/**
	 * 
	 * @param groupFields
	 * @param child
	 * @param parent
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws java.lang.reflect.InvocationTargetException
	 */
	private void mergeGroup(List<Field> groupFields, Object child, Object parent)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Field masterField = null;
		for (Field f : groupFields) {
			ContextField cf = getContextField(f);
			if (cf.isMaster())
				masterField = f;
		}

		if (masterField == null)
			throw new IllegalArgumentException("Master field is null.");

		boolean isOverwritten = isOverwrittenByChild(
				getContextField(masterField), child);
		Object mC = masterField.get(child);
		Object mP = masterField.get(parent);
		boolean isParentMasterNull = mP == null;
		boolean isMasterEqual = false;
		if (mC != null && mP != null && mC.equals(mP))
			isMasterEqual = true;

		if (!isOverwritten && !isParentMasterNull) {

			for (Field f : groupFields) {
				f.setAccessible(true);
				Object c = f.get(child);
				Object p = f.get(parent);
				if (isMasterEqual) {
					if (f.getType().equals(List.class)) {
						if (((List) p).size() > 0)
							compareList((List) c, (List) p);
					} else {
						if (p != null)
							f.set(child, p);
					}
				} else {
					f.set(child, p);
				}
			}

		}
	}

	/**
	 * 
	 * @param child
	 * @param parent
	 * @throws NoSuchMethodException
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void compareList(List child, List parent)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List temp = new ArrayList();

		for (Object p : parent) {
			boolean add = true;

			// TODO check this
			for (int i = 0; i < child.size() && add; i++) {
				if (child.get(i).equals(p)) {
					if (p.getClass().getSuperclass()
							.equals(ContextObject.class))
						merge(child.get(i), p);
					add = false;
				}
			}

			if (add)
				temp.add(p);
		}

		child.addAll(temp);
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	private ContextField getContextField(Field field) {
		ContextField cf = null;
		for (Annotation annotation : field.getAnnotations()) {
			if (annotation instanceof ContextField) {
				cf = (ContextField) annotation;
			}
		}
		return cf;
	}

	/**
	 * 
	 * @param cf
	 * @param child
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private boolean isOverwrittenByChild(ContextField cf, Object child)
			throws IllegalArgumentException, IllegalAccessException {

		boolean result = false;
		if (cf != null && !cf.overwrittenByChild().equals("")) {
			for (Field f : child.getClass().getFields()) {
				if (f.getName().equals(cf.overwrittenByChild())) {
					result = Boolean.class.cast(f.get(child));
				}
			}
		}
		return result;
	}

	private void mergeUncertainty(Object child, Object parent) {
		// TODO
	}

}
