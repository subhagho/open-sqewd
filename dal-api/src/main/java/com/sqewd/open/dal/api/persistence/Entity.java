/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 * Is this Entity type a view?
	 * 
	 * Note: Views require the custom select query to be specified and no CRUD
	 * operations will be allowed.
	 * 
	 * @return
	 */
	boolean isview() default false;

	/**
	 * Virtual Joins are entities which are joined post select to create a view.
	 * 
	 * @return
	 */
	boolean isjoin() default false;

	/**
	 * The Select Query which will be used to create the entity. This is only
	 * applicable to Entity types which are defined as views.
	 * 
	 * @return
	 */
	String query() default "";
}
