package org.openlca.olcatdb.parsing;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openlca.olcatdb.datatypes.DataSetReference;
import org.openlca.olcatdb.datatypes.ExtendedUrl;
import org.openlca.olcatdb.datatypes.LangString;
import org.openlca.olcatdb.datatypes.NamedString;
import org.openlca.olcatdb.datatypes.TextAndImage;
import org.openlca.olcatdb.parsing.ContextField.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlContextParser {

	private SAXParser parser;

	private Class<?> contextRootClass;

	public XmlContextParser() {
		try {
			// create the SAXParser
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> T getContext(Class<T> contextRootClass, InputStream is) {
		// ASSERT: Context annotation present
		if (contextRootClass == null
				|| !contextRootClass.isAnnotationPresent(Context.class))
			throw new IllegalArgumentException(
					"The context root class must not be null "
							+ "and must be annotated with @Context");
		this.contextRootClass = contextRootClass;

		T t = null;
		try {
			XmlEventHandler<T> xmlEventHandler = new XmlEventHandler<T>();
			parser.parse(is, xmlEventHandler);
			t = xmlEventHandler.rootContextObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private class XmlEventHandler<T> extends DefaultHandler {

		/**
		 * The context object stack. On the top is the object of the current
		 * context. If a sub-context is created the respective object is set on
		 * the top of this stack. Equally a context object is removed from this
		 * stack if the respective context element is closed.
		 */
		private Stack<Object> contextStack = new Stack<Object>();

		/**
		 * This stack is synchronously managed with the {$link contextStack}. On
		 * the top of this stack is the declared field list of the current
		 * context object.
		 */
		private Stack<Field[]> contextFieldStack = new Stack<Field[]>();

		/**
		 * Stack with the element names. If an element starts it's name is put
		 * on this stack, if the element ends it's name is removed from this
		 * stack.
		 */
		private Stack<String> eNames = new Stack<String>();

		/**
		 * A cache for a currently created field.
		 */
		private FieldElementTray fieldElementTray = new FieldElementTray();

		/**
		 * A cache for a currently created data set reference.
		 */
		private DataSetReferenceTray dataSetReferenceTray = new DataSetReferenceTray();

		/**
		 * The cached characters between the start and end of an element.
		 */
		private StringBuffer characters;

		private TextAndImageTray textAndImageTray = new TextAndImageTray();

		private T rootContextObject = null;

		@Override
		public void startElement(String uri, String localName, String qualName,
				Attributes attributes) throws SAXException {

			// get the element name
			String eName = eName(localName, qualName);
			// get the parent element name
			String parentEName = eNames.isEmpty() ? "" : eNames.peek();
			// push the new element name on the top of the name stack
			eNames.push(eName);

			// initialize the character cache
			characters = new StringBuffer(100);

			if (contextStack.isEmpty()) {

				// create the root context if the context stack is
				// empty and the element name matches the root context's
				// element name
				Context rootContext = contextRootClass
						.getAnnotation(Context.class);
				if (eName.equals(rootContext.name())
						&& parentEName.equals(rootContext.parentName())) {
					try {
						T context = (T) contextRootClass.newInstance();
						Field[] fields = context.getClass().getDeclaredFields();
						setAttributeValues(context, fields, eName, parentEName,
								attributes);

						rootContextObject = context;
						contextStack.push(context);
						contextFieldStack.push(fields);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {

				// check if there is a data set reference open
				if (!dataSetReferenceTray.isEmpty()) {
					dataSetReferenceTray.setCurrentLangCode(attributes
							.getValue("xml:lang"));
					if (eName.equals("subReference")) {
						dataSetReferenceTray.setSubRefOpen(true);
					} else if (eName.equals("shortDescription")) {
						dataSetReferenceTray.setDescrOpen(true);
					}
				}

				// check if there is a text and image type open and
				// prepare an entry if the element matches the
				// respective entry type
				if (!textAndImageTray.isEmpty()) {
					if (eName.equals("text")) {
						String index = attributes.getValue("index");
						String langCode = attributes.getValue("xml:lang");
						textAndImageTray.prepareTextEntry(index, langCode);
					} else if (eName.equals("imageUrl")) {
						String index = attributes.getValue("index");
						textAndImageTray.prepareImageEntry(index);
					} else if (eName.equals("variable")) {
						String _name = attributes.getValue("name");
						textAndImageTray.prepareVariableEntry(_name);
					}
				}

				// set the context attributes, if they fit to the current
				// context object
				Object contextObject = contextStack.peek();
				Field[] fields = contextFieldStack.peek();
				setAttributeValues(contextObject, fields, eName, parentEName,
						attributes);

				// check fields for sub-contexts end element fields
				for (Field f : fields) {

					// test for element field
					if (f.isAnnotationPresent(ContextField.class)) {

						ContextField contextField = f
								.getAnnotation(ContextField.class);
						if (contextField.name().equals(eName)
								&& (contextField.parentName().equals(
										parentEName) || contextField
										.parentNonStrict())
								&& !contextField.isAttribute()) {

							if (contextField.type() != Type.DataSetReference
									&& contextField.type() != Type.TextAndImage) {

								// cache the field for the characters
								fieldElementTray.put(f,
										attributes.getValue("xml:lang"));

							} else if (contextField.type() == Type.TextAndImage) {

								// create a text and image type and put it on
								// the tray
								TextAndImage textAndImage = new TextAndImage();
								textAndImageTray
										.put(contextField, textAndImage);

								// try to set the field value with the created
								// text and image type
								try {
									f.setAccessible(true);
									f.set(contextObject, textAndImage);
								} catch (Exception e) {
									e.printStackTrace();
								}

							} else if (contextField.type() == Type.DataSetReference) {
								// create a new (ILCD) data set reference type
								DataSetReference dataSetReference = new DataSetReference();
								dataSetReference.setRefObjectId(attributes
										.getValue("refObjectId"));
								dataSetReference.setType(attributes
										.getValue("type"));
								dataSetReference.setUri(attributes
										.getValue("uri"));
								dataSetReference.setVersion(attributes
										.getValue("version"));

								// try to set the field value with the created
								// data set reference
								try {
									f.setAccessible(true);
									if (!contextField.isMultiple()) {
										f.set(contextObject, dataSetReference);
									} else {
										Object collectionObject = f
												.get(contextObject);
										if (collectionObject != null
												&& collectionObject instanceof Collection) {
											((Collection) collectionObject)
													.add(dataSetReference);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								this.dataSetReferenceTray.put(dataSetReference,
										contextField);

							}
						}

					} else if (f.isAnnotationPresent(SubContext.class)) {

						// test for sub-context
						SubContext subContext = f
								.getAnnotation(SubContext.class);
						Class<?> clazz = subContext.contextClass();
						Context context = clazz.getAnnotation(Context.class);

						if (context.name().equals(eName)
								&& (context.parentName().equals(parentEName) || context
										.parentNonStrict())) {

							// a sub-context is found
							try {

								// create a new context and set the attribute
								// values if they fit
								Object subContextObject = clazz.newInstance();

								Field[] subFields = subContextObject.getClass()
										.getDeclaredFields();

								Field[] superFields = subContextObject
										.getClass().getSuperclass()
										.getDeclaredFields();

								int length = subFields.length
										+ superFields.length;

								Field[] subContextFields = new Field[length];

								for (int i = 0; i < length;) {
									for (int j = 0; j < subFields.length; j++, i++) {
										subContextFields[i] = subFields[j];
									}

									for (int j = 0; j < superFields.length; j++, i++) {
										subContextFields[i] = superFields[j];
									}
								}

								setAttributeValues(subContextObject,
										subContextFields, eName, parentEName,
										attributes);

								contextStack.push(subContextObject);
								contextFieldStack.push(subContextFields);

								// check if the sub-context gets the
								// element's text content
								for (Field subContextField : subContextFields) {
									if (subContextField
											.isAnnotationPresent(ContextField.class)) {
										ContextField contextField = subContextField
												.getAnnotation(ContextField.class);
										if (contextField.name().equals(eName)
												&& (contextField.parentName()
														.equals(parentEName) || contextField
														.parentNonStrict())
												&& !contextField.isAttribute()) {
											// cache the field for the
											// characters
											fieldElementTray
													.put(subContextField,
															attributes
																	.getValue("xml:lang"));
										}
									}
								}

								// add the sub-context to the parent context
								f.setAccessible(true);
								if (!subContext.isMultiple()) {
									f.set(contextObject, subContextObject);
								} else {
									Object collectionObject = f
											.get(contextObject);
									if (collectionObject != null
											&& collectionObject instanceof Collection) {
										((Collection) collectionObject)
												.add(subContextObject);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} // for

			}// else

		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// cache the characters
			if (characters != null) {
				characters.append(new String(ch, start, length));
			}
		}

		@Override
		public void endElement(String uri, String localName, String qualName)
				throws SAXException {

			// get the element value
			String value = null;
			if (characters != null) {
				value = characters.toString();
				characters = null;
			}

			// set a possible field value
			if (!fieldElementTray.isEmpty() && value != null) {
				Object contextObject = contextStack.peek();
				if (contextObject != null) {
					Field field = fieldElementTray.getField();
					// set the value
					ContextField contextField = field
							.getAnnotation(ContextField.class);
					if (field.getDeclaringClass().equals(
							contextObject.getClass())) {
						setFieldValue(contextObject, field, contextField,
								value, fieldElementTray.getLangCode());
					}
				}
				// clear the field cache
				fieldElementTray.clear();
			}

			// set possible data set reference descriptions or sub-references
			if (!dataSetReferenceTray.isEmpty() && value != null
					&& value.length() > 0) {
				if (dataSetReferenceTray.isDescrOpen()) {
					LangString descr = null;
					if (dataSetReferenceTray.getCurrentLangCode() != null) {
						descr = new LangString(
								dataSetReferenceTray.getCurrentLangCode(),
								value);
					} else {
						descr = new LangString(value);
					}
					DataSetReference dRef = dataSetReferenceTray
							.getDataSetReference();
					dRef.getDescription().add(descr);
				} else if (dataSetReferenceTray.isSubRefOpen()) {
					DataSetReference dRef = dataSetReferenceTray
							.getDataSetReference();
					dRef.getSubReferences().add(value);
				}
			}

			// check if there is a prepared entry on the text and image
			// tray and set the value to this entry if this is the case
			if (!textAndImageTray.isEmpty()
					&& textAndImageTray.hasPreparedEntry() && value != null) {
				textAndImageTray.finishEntry(value);
			}

			String eName = eName(localName, qualName);

			// check if there is a context to close
			if (!contextStack.isEmpty()) {
				Object o = contextStack.peek();
				if (o != null) {
					Context context = o.getClass().getAnnotation(Context.class);
					if (context.name().equals(eName)) {
						// remove the context and its fields from the cache
						contextStack.pop();
						contextFieldStack.pop();
					}
				}
			}
			// check if there is a data set reference to clear, or close
			// sub-elements
			if (!dataSetReferenceTray.isEmpty()) {
				ContextField contextField = dataSetReferenceTray
						.getContextField();
				if (contextField.name().equals(eName)) {
					dataSetReferenceTray.clear();
				} else {
					dataSetReferenceTray.setCurrentLangCode(null);
					dataSetReferenceTray.setDescrOpen(false);
					dataSetReferenceTray.setSubRefOpen(false);
				}
			}

			// check if the text and image tray should be cleared
			if (!textAndImageTray.isEmpty()) {
				ContextField contextField = textAndImageTray.getContextField();
				if (contextField != null && contextField.name().equals(eName)) {
					textAndImageTray.clear();
				}
			}

			// remove the element name from the name stack
			eNames.pop();
		}

		/**
		 * Sets the attribute values to the respective fields of the context
		 * object. An attribute value is set to a field if:
		 * <ul>
		 * <li>the field is annotated with a {@link org.openlca.olcatdb.parsing.ContextField} type</li>
		 * <li>the context field type is declared as attribute</li>
		 * <li>the element name of the context field type is equal to the
		 * current element name</li>
		 * <li>the parent element of the context field type is equal to the
		 * current parent element name OR the context field is declared as
		 * "parentNonStrict"</li>
		 * <li>and, the attribute name is equal to the attribute name of the
		 * declared context field type</li>
		 * </ul>
		 * 
		 * @param contextObject
		 *            the context object
		 * @param fields
		 *            the fields of the given context object
		 * @param elementName
		 *            the current element name
		 * @param parentElementName
		 *            the current parent element
		 * @param attributes
		 *            the list of attributes of the current element
		 */
		private void setAttributeValues(Object contextObject, Field[] fields,
				String elementName, String parentElementName,
				Attributes attributes) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(ContextField.class)) {
					ContextField contextField = field
							.getAnnotation(ContextField.class);
					if (contextField.isAttribute()
							&& contextField.name().equals(elementName)
							&& (contextField.parentName().equals(
									parentElementName) || contextField
									.parentNonStrict())) {
						String v = attributes.getValue(contextField
								.attributeName());
						if (v != null) {
							setFieldValue(contextObject, field, contextField,
									v, null);
						}
					}
				}
			}
		}

		private void setFieldValue(Object contextObject, Field field,
				ContextField contextField, String stringValue, String langCode) {
			try {
				field.setAccessible(true);

				Object value = null;
				String string = stringValue.trim();

				// transform the field value
				switch (contextField.type()) {
				case Text:
					value = new String(string);
					break;
				case MultiLangText:
					value = langCode == null ? new LangString(string)
							: new LangString(langCode, string);
					break;
				case Boolean:
					boolean b = string.equalsIgnoreCase("true")
							|| string.equals("1");
					value = b;
					break;
				case Double:
					value = Double.parseDouble(string);
					break;
				case Integer:
					value = Integer.parseInt(string);
				default:
					break;
				}

				// set the field value
				if (!contextField.isMultiple()) {
					field.set(contextObject, value);
				} else {
					// add the value to the respective collection
					Object collectionObject = field.get(contextObject);
					if (collectionObject != null
							&& collectionObject instanceof Collection) {
						((Collection) collectionObject).add(value);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Makes a pure element name from the respective SAX handler arguments.
		 * 
		 * @param localName
		 *            the local name (without prefix) of the SAX handler's event
		 *            (is <code>null</code> when Namespace processing is
		 *            performed).
		 * @param qName
		 *            the qualified name of the SAX hanlder's event (is
		 *            <code>null</code> if no qualified names is available).
		 * @return the element name without prefix, or an empty string if no
		 *         element name is available.
		 */
		private String eName(String localName, String qName) {
			String eName = "";
			if (localName != null && localName.length() > 0) {
				eName = localName;
			} else if (qName != null && qName.length() > 0) {
				int pos = qName.indexOf(':');
				if (pos != -1) {
					eName = qName.substring(pos + 1);
				} else {
					eName = qName;
				}
			}
			return eName;
		}

	}

	/**
	 * Container for element fields in the parsing process. It is used to assign
	 * a possible sequence of characters of an XML element to the respective
	 * Field of the current context object.
	 */
	private class FieldElementTray {

		/**
		 * The optional language code (xml:lang-attribute of the element field.)
		 * of the field (field type is MultiLangString)
		 */
		private String langCode;

		/**
		 * The respective field declaration in the context object.
		 */
		private Field field;

		/**
		 * Puts the given field and language code on this tray.
		 */
		public void put(Field field, String langCode) {
			this.field = field;
			this.langCode = langCode;
		}

		/**
		 * Get the current field from this tray.
		 * 
		 * @return The current field from this tray, or <code>null</code> if
		 *         this tray is empty.
		 */
		public Field getField() {
			return field;
		}

		/**
		 * Get the current language code of the field on this tray.
		 * 
		 * @return The current language code of the field on this tray, or
		 *         <code>null</code> if this tray is empty or no language code
		 *         is declared.
		 */
		public String getLangCode() {
			return langCode;
		}

		/**
		 * Returns <code>true</code> if there is a field declaration on this
		 * tray, otherwise <code>false</code>.
		 */
		public boolean isEmpty() {
			return field == null;
		}

		/**
		 * Removes the current field declaration from this tray.
		 */
		public void clear() {
			field = null;
			langCode = null;
		}

	}

	private class DataSetReferenceTray {

		private DataSetReference dataSetReference;

		private ContextField contextField;

		private String currentLangCode;

		private boolean subRefOpen = false;

		private boolean descrOpen = false;

		public void put(DataSetReference dataSetReference,
				ContextField contextField) {
			this.dataSetReference = dataSetReference;
			this.contextField = contextField;
		}

		public boolean isEmpty() {
			return dataSetReference == null;
		}

		public String getCurrentLangCode() {
			return currentLangCode;
		}

		public void setCurrentLangCode(String currentLangCode) {
			this.currentLangCode = currentLangCode;
		}

		public boolean isSubRefOpen() {
			return subRefOpen;
		}

		public void setSubRefOpen(boolean subRefOpen) {
			this.subRefOpen = subRefOpen;
		}

		public boolean isDescrOpen() {
			return descrOpen;
		}

		public void setDescrOpen(boolean descrOpen) {
			this.descrOpen = descrOpen;
		}

		public DataSetReference getDataSetReference() {
			return dataSetReference;
		}

		public ContextField getContextField() {
			return contextField;
		}

		public void clear() {
			dataSetReference = null;
			contextField = null;
			currentLangCode = null;
			subRefOpen = false;
			descrOpen = false;
		}

	}

	private class TextAndImageTray {

		/**
		 * The context field of the current text and image type on this tray.
		 */
		private ContextField contextField;

		/**
		 * The current text and image type on the tray. If this type is
		 * <code>null</code> the tray is empty.
		 */
		private TextAndImage textAndImage;

		/**
		 * A prepared text entry of the current text and image type.
		 */
		private LangString text;

		/**
		 * A prepared variable of the current text and image type.
		 */
		private NamedString variable;

		/**
		 * A prepared image URL of the current text and image type.
		 */
		private ExtendedUrl imageUrl;

		/**
		 * Puts a new text and image type on the tray.
		 */
		public void put(ContextField contextField, TextAndImage textAndImage) {
			this.contextField = contextField;
			this.textAndImage = textAndImage;
		}

		/**
		 * Prepares a text entry.
		 * 
		 * @param index
		 *            the index of the text entry
		 * @param langCode
		 *            the language code of the text entry
		 */
		public void prepareTextEntry(String index, String langCode) {
			LangString langString = new LangString();
			langString.setLangCode(langCode);
			if (index != null) {
				try {
					langString.setIndex(Integer.parseInt(index));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.text = langString;
		}

		/**
		 * Prepares an entry for a variable
		 * 
		 * @param name
		 *            the variable name
		 */
		public void prepareVariableEntry(String name) {
			NamedString var = new NamedString();
			var.setName(name);
			this.variable = var;
		}

		/**
		 * Prepares an entry for an image URL.
		 * 
		 * @param index
		 *            the index of the URL in the text and image type.
		 */
		public void prepareImageEntry(String index) {
			ExtendedUrl url = new ExtendedUrl();
			if (index != null) {
				try {
					url.setIndex(Integer.parseInt(index));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.imageUrl = url;
		}

		/**
		 * Finishes the prepared entry on the tray, adds it to the text and
		 * image type on the try, and closes the entry.
		 */
		public void finishEntry(String value) {
			if (textAndImage != null) {
				if (text != null) {
					text.setValue(value);
					textAndImage.getText().add(text);
					text = null;
				} else if (variable != null) {
					variable.setValue(value);
					textAndImage.getVariables().add(variable);
					variable = null;
				} else if (imageUrl != null) {
					imageUrl.setUrl(value);
					textAndImage.getImages().add(imageUrl);
					imageUrl = null;
				}
			}
		}

		/**
		 * Get the context field of the current text and image type on this
		 * tray.
		 */
		public ContextField getContextField() {
			return contextField;
		}

		/**
		 * Returns true if there is a text and image type on the tray with a
		 * prepared entry for a text, variable, or image URL.
		 */
		public boolean hasPreparedEntry() {
			return textAndImage != null
					&& (text != null || variable != null || imageUrl != null);
		}

		/**
		 * Returns true if there is no element on the tray.
		 */
		public boolean isEmpty() {
			return textAndImage == null;
		}

		/**
		 * Clears the tray.
		 */
		public void clear() {
			this.contextField = null;
			this.textAndImage = null;
			this.imageUrl = null;
			this.text = null;
			this.variable = null;
		}

	}

}
