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
 * @filename PropertySchema.java
 * @created Aug 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.services;

/**
 * @author subhagho
 * 
 * TODO: <comment>
 * 
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;

/**
 * @author subhagho
 * 
 */
@JsonRootName(value = "Property")
public class PropertySchema {
	@JsonProperty(value = "Name")
	private String name;
	@JsonProperty(value = "JavaType")
	private String type;
	@JsonProperty(value = "JsonName")
	private String jsonname;
	@JsonProperty(value = "DbColumn")
	private String dbcolumn;
	@JsonProperty(value = "Size")
	private String dbsize;
	@JsonProperty(value = "Enumeration")
	private String[] enumeration;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the jsonname
	 */
	public String getJsonname() {
		return jsonname;
	}

	/**
	 * @param jsonname
	 *            the jsonname to set
	 */
	public void setJsonname(String jsonname) {
		this.jsonname = jsonname;
	}

	/**
	 * @return the dbcolumn
	 */
	public String getDbcolumn() {
		return dbcolumn;
	}

	/**
	 * @param dbcolumn
	 *            the dbcolumn to set
	 */
	public void setDbcolumn(String dbcolumn) {
		this.dbcolumn = dbcolumn;
	}

	/**
	 * @return the dbsize
	 */
	public String getDbsize() {
		return dbsize;
	}

	/**
	 * @param dbsize
	 *            the dbsize to set
	 */
	public void setDbsize(String dbsize) {
		this.dbsize = dbsize;
	}

	public static PropertySchema load(Class<?> type, String field)
			throws Exception {
		StructAttributeReflect attr = ReflectionUtils.get().getAttribute(type,
				field);
		if (attr == null)
			return null;
		PropertySchema def = new PropertySchema();
		def.dbcolumn = attr.Column;
		def.name = attr.Field.getName();
		def.type = attr.Field.getType().getCanonicalName();
		if (attr.Field.isAnnotationPresent(JsonProperty.class)) {
			JsonProperty xattr = attr.Field.getAnnotation(JsonProperty.class);
			def.jsonname = xattr.value();
		}
		if (attr.Size > 0) {
			def.dbsize = "" + attr.Size;
		} else {
			def.dbsize = "-";
		}
		if (attr.Field.getType().isEnum()) {
			def.enumeration = getEnumValues(attr.Field.getType());
		}
		return def;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T extends Enum> String[] getEnumValues(Class<?> type)
			throws Exception {
		T[] enums = (T[]) type.getEnumConstants();
		if (enums != null && enums.length > 0) {
			String[] values = new String[enums.length];
			for (int ii = 0; ii < enums.length; ii++) {
				values[ii] = enums[ii].name();
			}
			return values;
		}
		return null;
	}

	/**
	 * @return the enumeration
	 */
	public String[] getEnumeration() {
		return enumeration;
	}

	/**
	 * @param enumeration
	 *            the enumeration to set
	 */
	public void setEnumeration(String[] enumeration) {
		this.enumeration = enumeration;
	}
}