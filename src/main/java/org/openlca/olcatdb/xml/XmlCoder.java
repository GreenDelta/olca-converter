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
				.replaceAll("\"", "&&quot;").replaceAll("ä", "&#228;")
				.replaceAll("ö", "&#246;").replaceAll("ü", "&#252;")
				.replaceAll("Ä", "&#196;").replaceAll("Ö", "&#214;")
				.replaceAll("Ü", "&#220;").replaceAll("ß", "&#223;");
	}

}
