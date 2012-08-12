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
