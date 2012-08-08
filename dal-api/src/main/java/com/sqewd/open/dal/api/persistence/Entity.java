/**
 * 
 */
package com.sqewd.open.dal.api.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marking a POJO as a persisted entity.
 * 
 * @author subhagho
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	/**
	 * Table/ColumnGroup/RecordSet name this entity is mapped to.
	 * 
	 * @return
	 */
	String recordset();

	/**
	 * Custom data persister class(name) to use for this entity. If no explicit
	 * persister is specified the DataManager will search for the persister.
	 * 
	 * @return
	 */
	String persister() default "";

	/**
	 * Is the entity cacheable?
	 * 
	 * @return
	 */
	boolean cached() default false;

	/**
	 * Time-to-Live for cached entities.
	 * 
	 * @return
	 */
	long TTL() default -1;

	/**
	 * Cache this entity.
	 * 
	 * @return
	 */
	boolean cache() default false;
}
