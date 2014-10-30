package org.openlca.olcatdb.parsing;

import java.lang.reflect.Field;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * 
 */
public abstract class ContextObject {

	/**
	 * Returns the value of the field with the given name. This is required for
	 * the queries of the velocity template engine regarding public fields.
	 */
	public final Object get(String contextField) {
		Object v = null;
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.getName().equals(contextField)) {
				try {
					field.setAccessible(true);
					v = field.get(this);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return v;
	}

	public final void makeValid() {
		for (Field field : this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (field.isAnnotationPresent(ContextField.class)) {

					ContextField context = field
							.getAnnotation(ContextField.class);
					Object content = field.get(this);

					if (context.isRequired() && content == null) {
						// set default values
						switch (context.type()) {
						case Boolean:
							content = false;
							break;
						case DataSetReference:
							DataSetReference ref = new DataSetReference();
							ref.setName("generated reference");
							ref
									.setRefObjectId("00000000-0000-0000-0000-000000000000");
							ref.setType("no type");
							ref.setVersion("0");
							content = ref;
							break;
						case Double:
							content = 0.0d;
							break;
						case Integer:
							content = 0;
							break;
						case MultiLangText:
							content = new LangString("-no value-");
							break;
						case Text:
							content = "-no value-";
							break;
						default:
							break;
						}
						field.set(this, content);
					}

					if (content != null && context.length() > 0) {

						if (context.type() == Type.Text
								&& content instanceof String) {
							String str = (String) content;
							if (str.length() > context.length()) {
								str = str.substring(0, context.length() - 1)
										.concat("#");
								field.set(this, str);
							}
						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
