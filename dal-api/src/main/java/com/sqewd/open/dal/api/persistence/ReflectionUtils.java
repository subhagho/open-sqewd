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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.api.persistence.CustomFieldDataHandler;

/**
 * @author subhagho
 * 
 */
public class ReflectionUtils {
	private HashMap<String, StructEntityReflect> metacache = new HashMap<String, StructEntityReflect>();
	private HashMap<String, Class<?>> typecahce = new HashMap<String, Class<?>>();

	/**
	 * Get the Getter/Setter method name for the field.
	 * 
	 * @param prefix
	 *            - "get"/"set"
	 * @param field
	 *            - Field name
	 * @return
	 */
	public String getMethodName(String prefix, String field) {
		return prefix + field.toUpperCase().charAt(0) + field.substring(1);
	}

	/**
	 * Get the reflection definition of the class column.
	 * 
	 * @param type
	 *            - Class type
	 * @param column
	 *            - Column name : Column name refers to the annotated name and
	 *            not the field name.
	 * @return
	 * @throws Exception
	 */
	public StructAttributeReflect getAttribute(Class<?> type, String column)
			throws Exception {
		StructEntityReflect map = getEntityMetadata(type);
		return map.get(column);
	}

	/**
	 * Get the type metadata.
	 * 
	 * @param type
	 *            - Class type
	 * @return
	 * @throws Exception
	 */
	public StructEntityReflect getEntityMetadata(Class<?> type)
			throws Exception {
		if (!metacache.containsKey(type.getName())) {
			load(type);
		}
		return metacache.get(type.getName());
	}

	/**
	 * Get the Entity metadata based on the Entity name.
	 * 
	 * @param type
	 *            - Entity Name
	 * @return
	 * @throws Exception
	 */
	public StructEntityReflect getEntityMetadata(String type) throws Exception {
		return metacache.get(type);
	}

	/**
	 * Get all the registered entity metadata.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<StructEntityReflect> getAllMetadata() throws Exception {
		List<StructEntityReflect> ens = new ArrayList<StructEntityReflect>();

		for (String key : metacache.keySet()) {
			StructEntityReflect enref = metacache.get(key);
			if (!ens.contains(enref)) {
				ens.add(enref);
			}
		}
		return ens;
	}

	public void load(Class<?> type) throws Exception {
		synchronized (metacache) {
			if (!metacache.containsKey(type.getName())) {
				if (!type.isAnnotationPresent(Entity.class))
					throw new Exception("Class [" + type.getCanonicalName()
							+ "] does not implement Entity annotation.");

				Entity eann = type.getAnnotation(Entity.class);

				StructEntityReflect entity = new StructEntityReflect();
				entity.Key = type.getName();
				entity.Class = type.getCanonicalName();
				entity.Entity = eann.recordset();
				entity.IsView = eann.isview();
				entity.IsJoin = eann.isjoin();
				entity.Query = eann.query();
				if (entity.IsJoin) {
					entity.IsView = true;
					if (!type.isAnnotationPresent(EntityJoin.class))
						throw new Exception(
								"Class ["
										+ type.getCanonicalName()
										+ "] is specified as a vitual join, but missing Join annotation ["
										+ EntityJoin.class.getCanonicalName()
										+ "]");
					EntityJoin join = type.getAnnotation(EntityJoin.class);
					StructJoinedEntityView jev = new StructJoinedEntityView();
					String[] entities = join.entities().split(",");
					jev.Entities = new String[entities.length];
					for (int ii = 0; ii < jev.Entities.length; ii++) {
						jev.Entities[ii] = entities[ii].trim();
					}

					jev.Join = join.join();

					entity.Join = jev;
				}
				List<Field> fields = new ArrayList<Field>();
				getFields(type, fields);
				if (fields != null && fields.size() > 0) {
					for (Field fd : fields) {
						if (!fd.isAnnotationPresent(Attribute.class))
							continue;
						Attribute attr = (Attribute) fd
								.getAnnotation(Attribute.class);
						StructAttributeReflect ar = new StructAttributeReflect();
						ar.Field = fd;
						ar.Column = attr.name();
						ar.IsKeyColumn = attr.keyattribute();
						ar.Size = attr.size();
						ar.AutoIncrement = attr.autoincr();

						String mname = getMethodName("get", fd.getName());
						ar.Getter = type.getMethod(mname);
						mname = getMethodName("set", fd.getName());
						ar.Setter = type.getMethod(mname, fd.getType());

						if (fd.isAnnotationPresent(Reference.class)) {
							Reference ref = (Reference) fd
									.getAnnotation(Reference.class);
							ar.Reference = new StructReferenceReflection();
							ar.Reference.Class = ref.target();
							ar.Reference.Field = ref.attribute();
							ar.Reference.Type = ref.association();
							ar.Reference.CascadeUpdate = ref.cascade();
						}
						if (attr.handler() != null && !attr.handler().isEmpty()) {
							String handler = attr.handler();
							Class<?> cls = Class.forName(handler);
							Object hobj = cls.newInstance();
							if (hobj instanceof CustomFieldDataHandler) {
								ar.Convertor = (CustomFieldDataHandler) hobj;
							} else {
								throw new Exception(
										"["
												+ type.getCanonicalName()
												+ "]"
												+ "Invalid Attribute : Convertor class ["
												+ cls.getCanonicalName()
												+ "] doesnot implement ["
												+ CustomFieldDataHandler.class
														.getCanonicalName()
												+ "]");
							}
						}
						entity.add(ar);
					}
				}
				metacache.put(entity.Key, entity);
				metacache.put(entity.Entity, entity);
				typecahce.put(eann.recordset(), type);
			}
		}
	}

	public Class<?> getType(String table) {
		if (typecahce.containsKey(table)) {
			return typecahce.get(table);
		}
		return null;
	}

	private void getFields(Class<?> type, List<Field> array) {
		if (type.equals(Object.class)) {
			// TODO : Need to investigate why there are null fields.
			// Temporary Quickfix
			List<Integer> toremove = new ArrayList<Integer>();
			for (int ii = 0; ii < array.size(); ii++) {
				Field fd = array.get(ii);
				if (fd == null) {
					toremove.add(ii);
				}
			}
			if (toremove.size() > 0) {
				for (int ii : toremove) {
					array.remove(ii);
				}
			}
			return;
		}
		Field[] fields = type.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field != null && !Modifier.isStatic(field.getModifiers()))
					array.add(field);
			}
		}
		Class<?> suptype = type.getSuperclass();
		getFields(suptype, array);
	}

	private static ReflectionUtils _instance = new ReflectionUtils();

	/**
	 * Get a handle to the Reflections Utility class.
	 * 
	 * @return
	 */
	public static ReflectionUtils get() {
		return _instance;
	}
}
