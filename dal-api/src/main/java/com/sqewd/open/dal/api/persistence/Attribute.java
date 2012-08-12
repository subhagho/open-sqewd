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
