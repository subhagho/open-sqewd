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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author subhagho
 * 
 */
public class StructEntityReflect {
	public String Key;
	public String Class;
	public String Entity;
	public HashMap<String, StructAttributeReflect> FieldMaps = new HashMap<String, StructAttributeReflect>();
	public HashMap<String, StructAttributeReflect> ColumnMaps = new HashMap<String, StructAttributeReflect>();
	public List<StructAttributeReflect> Attributes = new ArrayList<StructAttributeReflect>();
	public boolean IsView = false;
	public String Query;

	public void add(StructAttributeReflect attr) {
		if (FieldMaps.containsKey(attr.Field.getName())) {
			FieldMaps.remove(attr.Field.getName());
		}
		FieldMaps.put(attr.Field.getName(), attr);
		if (ColumnMaps.containsKey(attr.Column)) {
			ColumnMaps.remove(attr.Column);
		}
		ColumnMaps.put(attr.Column, attr);
		Attributes.add(attr);
	}

	public StructAttributeReflect get(String name) {
		if (FieldMaps.containsKey(name)) {
			return FieldMaps.get(name);
		}
		if (ColumnMaps.containsKey(name)) {
			return ColumnMaps.get(name);
		}
		return null;
	}
}
