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
