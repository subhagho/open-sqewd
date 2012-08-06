/**
 * 
 */
package com.sqewd.open.dal.core.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.core.persistence.CustomFieldDataHandler;

/**
 * @author subhagho
 * 
 */
public class ReflectionUtils {
	private HashMap<String, HashMap<String, AttributeReflection>> metacache = new HashMap<String, HashMap<String, AttributeReflection>>();
	private HashMap<String, List<Field>> fieldscache = new HashMap<String, List<Field>>();
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
	public AttributeReflection getAttribute(Class<?> type, String column)
			throws Exception {
		HashMap<String, AttributeReflection> map = getEntityMetadata(type);
		if (map.containsKey(column)) {
			return map.get(column);
		}
		return null;
	}

	/**
	 * Get/Load the type metadata.
	 * 
	 * @param type
	 *            - Class type
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, AttributeReflection> getEntityMetadata(Class<?> type)
			throws Exception {
		if (!metacache.containsKey(type.getName())) {
			synchronized (metacache) {
				if (!type.isAnnotationPresent(Entity.class))
					throw new Exception("Class [" + type.getCanonicalName()
							+ "] does not implement Entity annotation.");

				Entity eann = type.getAnnotation(Entity.class);
				HashMap<String, AttributeReflection> map = new HashMap<String, AttributeReflection>();

				List<Field> fields = getFields(type);
				if (fields != null && fields.size() > 0) {
					for (Field fd : fields) {
						if (!fd.isAnnotationPresent(Attribute.class))
							continue;
						Attribute attr = (Attribute) fd
								.getAnnotation(Attribute.class);
						AttributeReflection ar = new AttributeReflection();
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
							ar.Reference = new ReferenceReflection();
							ar.Reference.Class = ref.target();
							ar.Reference.Field = ref.attribute();
							ar.Reference.Type = ref.association();
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
						map.put(ar.Column, ar);
						map.put(ar.Field.getName(), ar);
					}
				}
				metacache.put(type.getName(), map);
				typecahce.put(eann.recordset(), type);
			}
		}
		return metacache.get(type.getName());
	}

	public Class<?> getType(String table) {
		if (typecahce.containsKey(table)) {
			return typecahce.get(table);
		}
		return null;
	}

	public List<Field> getFields(Class<?> type) {
		if (!fieldscache.containsKey(type.getCanonicalName())) {
			synchronized (fieldscache) {
				List<Field> array = new ArrayList<Field>();
				getFields(type, array);
				fieldscache.put(type.getCanonicalName(), array);
			}
		}

		return fieldscache.get(type.getCanonicalName());
	}

	private void getFields(Class<?> type, List<Field> array) {
		if (type.equals(Object.class))
			return;
		Field[] fields = type.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
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
