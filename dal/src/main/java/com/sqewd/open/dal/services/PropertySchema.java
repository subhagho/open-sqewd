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
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.core.persistence.model.EntityModelHelper;

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
	public void setName(final String name) {
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
	public void setType(final String type) {
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
	public void setJsonname(final String jsonname) {
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
	public void setDbcolumn(final String dbcolumn) {
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
	public void setDbsize(final String dbsize) {
		this.dbsize = dbsize;
	}

	public static PropertySchema load(final Class<?> type, final String field)
			throws Exception {
		AttributeDef attr = EntityModelHelper.get().getEntityDef(type)
				.getAttribute(field);
		if (attr == null)
			return null;
		PropertySchema def = new PropertySchema();
		def.dbcolumn = attr.getName();
		def.name = attr.getField().getName();
		def.type = attr.getField().getType().getCanonicalName();
		if (attr.getField().isAnnotationPresent(JsonProperty.class)) {
			JsonProperty xattr = attr.getField().getAnnotation(
					JsonProperty.class);
			def.jsonname = xattr.value();
		}
		if (attr.getSize() > 0) {
			def.dbsize = "" + attr.getSize();
		} else {
			def.dbsize = "-";
		}
		if (attr.getField().getType().isEnum()) {
			def.enumeration = getEnumValues(attr.getField().getType());
		}
		return def;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T extends Enum> String[] getEnumValues(final Class<?> type)
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
	public void setEnumeration(final String[] enumeration) {
		this.enumeration = enumeration;
	}
}