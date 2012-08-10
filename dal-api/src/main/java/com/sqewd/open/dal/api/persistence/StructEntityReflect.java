/**
 * 
 */
package com.sqewd.open.dal.api.persistence;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

/**
 * @author subhagho
 * 
 */
public class StructEntityReflect {
	public String Classname;
	public String Entity;
	public HashMap<String, StructAttributeReflect> Attributes = new HashMap<String, StructAttributeReflect>();
	public List<Field> Fields = null;

	public void add(StructAttributeReflect attr) {
		if (Attributes.containsKey(attr.Field.getName())) {
			Attributes.remove(attr.Field.getName());
		}
		Attributes.put(attr.Field.getName(), attr);
		if (Attributes.containsKey(attr.Column)) {
			Attributes.remove(attr.Column);
		}
		Attributes.put(attr.Column, attr);
	}

	public StructAttributeReflect get(String name) {
		if (Attributes.containsKey(name)) {
			return Attributes.get(name);
		}
		return null;
	}
}
