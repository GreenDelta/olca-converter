package org.openlca.olcatdb.parsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ContextField {

	/**
	 * The supported list of field types.
	 */
	enum Type {
		Text, MultiLangText, Double, Boolean, Integer, DataSetReference, TextAndImage
	};

	/**
	 * Indicates whether the field is an attribute.
	 */
	boolean isAttribute() default false;

	/**
	 * The element name of this field. If the field is an XML attribute, the
	 * element which contains the field declaration is given here (without
	 * namespace prefix).
	 */
	String name();

	/**
	 * The optional name of the parent element (without namespace prefix, for
	 * the XML parser this field is required).
	 */
	String parentName() default "";

	/**
	 * If the field is an attribute, the attribute name must be given.
	 */
	String attributeName() default "";

	/**
	 * The type of this field.
	 */
	Type type() default Type.Text;

	/**
	 * Indicates whether the field can occur several times. If so, the field
	 * type in the context class must be an initialized instance of
	 * {@link java.util.Collection}.
	 */
	boolean isMultiple() default false;

	/**
	 * Indicates that the parent element should not be checked in the parsing
	 * process.
	 */
	boolean parentNonStrict() default false;

	/**
	 * The allowed field length > 0. -1 means that there is no limitation.
	 */
	int length() default -1;

	/**
	 * Indicates whether the field is required or not. The default value is
	 * <code>false</code>.
	 */
	boolean isRequired() default false;

	/**
	 * 
	 * @return
	 */
	Class<?> classGroup() default Object.class;

	// TODO
	/**
	 * Used for the merging data sets
	 */
	boolean isMaster() default false;

	/**
	 * 
	 * @return
	 */
	String overwrittenByChild() default "";

}
