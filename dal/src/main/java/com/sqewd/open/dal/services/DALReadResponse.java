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
 * @filename DALReadResponse.java
 * @created Aug 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.services;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class DALReadResponse {
	@JsonProperty(value = "schema")
	private EntitySchema schema = null;

	@JsonProperty(value = "data")
	private Object data = null;

	/**
	 * @return the schema
	 */
	public EntitySchema getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(EntitySchema schema) {
		this.schema = schema;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

}
