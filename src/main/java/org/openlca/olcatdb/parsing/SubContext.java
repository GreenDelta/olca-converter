package org.openlca.olcatdb.parsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface SubContext {

	/**
	 * Indicates whether the sub-context can occur several times. If so, the
	 * respective type in the context class must be an initialized instance of
	 * {@link java.util.Collection}.
	 */
	boolean isMultiple() default false;

	/**
	 * The class of the sub-context. The respective class must be annotated with
	 * {@link org.openlca.olcatdb.parsing.Context}.
	 */
	Class<?> contextClass();

}
