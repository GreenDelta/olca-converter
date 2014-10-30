package org.openlca.olcatdb.parsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface Context {

	/**
	 * The name of the context element (without namespace prefix).
	 */
	String name();

	/**
	 * The optional number of fields.
	 */
	int fieldCount() default 0;

	/**
	 * The optional name of the parent element (without namespace prefix, for
	 * the XML parser this field is required).
	 */
	String parentName() default "";
	
	boolean parentNonStrict() default false;

}
