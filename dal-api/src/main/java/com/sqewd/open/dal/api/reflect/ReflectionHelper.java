/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename ReflectionHelper.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Entity;

/**
 * Utility class for Reflection functions.
 * 
 * @author subhagho
 * 
 */
public class ReflectionHelper {

	/**
	 * Get all the declared fields, including fields declared in the
	 * superclass(s).
	 * 
	 * @param type
	 * @return
	 */
	public static List<Field> getFields(final Class<?> type) {
		List<Field> fields = new ArrayList<Field>();
		getFields(type, fields);
		return fields;
	}

	/**
	 * Check if the type represents an Entity type. Basically check if "@Entity"
	 * annotation is present.
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isEntityType(final Class<?> type) {
		if (type.isAnnotationPresent(Entity.class))
			return true;
		return false;
	}

	/**
	 * Check if the specified field is a List of type AbstractEntity.
	 * 
	 * @param fd
	 * @return
	 */
	public static Class<?> isEntityListType(final Field fd) {
		if (fd.getType().equals(List.class))
			if (!fd.getType().isPrimitive()) {
				Class<?>[] infs = fd.getType().getInterfaces();
				if (infs != null && infs.length > 0) {
					for (Class<?> it : infs) {
						if (it.equals(List.class)) {
							Type type = fd.getGenericType();
							if (type instanceof ParameterizedType) {
								ParameterizedType pt = (ParameterizedType) type;
								for (Type t : pt.getActualTypeArguments()) {
									if (t instanceof Class<?>) {
										Class<?> cls = (Class<?>) t;
										if (hasSuperClass(cls,
												AbstractEntity.class))
											return cls;
									}
								}
							}
						}
					}
				}
			}
		return null;
	}

	/**
	 * Check if the specified class is inherited from the passed superclass.
	 * 
	 * @param type
	 *            - Derived class to check for.
	 * @param superc
	 *            - Super Class.
	 * @return
	 */
	public static boolean hasSuperClass(final Class<?> type,
			final Class<?> superc) {
		if (type.equals(type))
			return true;
		Class<?> sc = type.getSuperclass();
		if (!sc.equals(Object.class)) {
			if (sc.equals(superc))
				return true;
			else
				return hasSuperClass(sc, superc);
		}
		return false;
	}

	private static void getFields(final Class<?> type, final List<Field> array) {
		// Check and add the superclass fields first.
		Class<?> superc = type.getSuperclass();
		if (!(superc instanceof Object)) {
			getFields(superc, array);
		}
		Field[] fields = type.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field fd : fields) {
				array.add(fd);
			}
		}
	}
}
