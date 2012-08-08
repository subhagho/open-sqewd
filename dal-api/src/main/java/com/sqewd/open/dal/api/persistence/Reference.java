/**
 * 
 */
package com.sqewd.open.dal.api.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks referenced entities and association attribute.
 * 
 * @author subhagho
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {
	/**
	 * Target entity the attribute points to.
	 * 
	 * @return
	 */
	String target();

	/**
	 * Target entity attribute this field refers to.
	 * 
	 * @return
	 */
	String attribute();

	/**
	 * Specify the association with the target entity.
	 * 
	 * @return
	 */
	EnumRefereceType association() default EnumRefereceType.One2One;

	/**
	 * Lazy Load referenced entity.
	 * 
	 * @return
	 */
	boolean lazyload() default false;
}
