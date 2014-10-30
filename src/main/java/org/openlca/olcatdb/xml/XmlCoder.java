package org.openlca.olcatdb.xml;

import java.lang.reflect.Field;
import java.util.Collection;

import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.SubContext;
import org.openlca.olcatdb.parsing.ContextField.Type;

public class XmlCoder {

	public void encode(Object contextObject) {

		for (Field field : contextObject.getClass().getDeclaredFields()) {

			if (field.isAnnotationPresent(ContextField.class)) {
				ContextField contextDecl = field
						.getAnnotation(ContextField.class);
				if (contextDecl.type() == ContextField.Type.Text) {
					field.setAccessible(true);
					try {
						Object o = field.get(contextObject);
						if (o != null && o instanceof String) {							
							String s = (String) o;							
							s = encodeStr(s);
							field.set(contextObject, s);
						}
						// TODO: collections
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// TODO: multi-lang strings
			} else if (field.isAnnotationPresent(SubContext.class)) {
				try {
					field.setAccessible(true);
					Object o = field.get(contextObject);
					if (o != null) {
						if (o instanceof Collection) {
							Collection<?> c = (Collection<?>) o;
							for (Object oo : c) {
								encode(oo);
							}
						} else {
							encode(o);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String encodeStr(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("'", "&apos;").replaceAll(
				"<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\"", "&&quot;").replaceAll("�", "&#228;")
				.replaceAll("�", "&#246;").replaceAll("�", "&#252;")
				.replaceAll("�", "&#196;").replaceAll("�", "&#214;")
				.replaceAll("�", "&#220;").replaceAll("�", "&#223;");
	}

}
