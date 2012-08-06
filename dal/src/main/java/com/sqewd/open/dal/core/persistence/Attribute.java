/**
 * 
 */
package com.sqewd.open.dal.core.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks fields to be persisted for an Entity. Fields should be
 * exposed via getters/setters (method name format getXxxxx()/setXxxxx().
 * 
 * @author subhagho
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Attribute {
	/**
	 * Column name this entity attribute maps to.
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * Is this attribute a key/key part.
	 * 
	 * @return
	 */
	boolean keyattribute() default false;

	/**
	 * Custom data handler for the field.
	 * 
	 * @return
	 */
	String handler() default "";

	/**
	 * Specify the column data size. Only applicable for String(s).
	 * 
	 * @return
	 */
	int size() default 0;

	/**
	 * Column is auto-incremented.
	 * 
	 * @return
	 */
	boolean autoincr() default false;
}
