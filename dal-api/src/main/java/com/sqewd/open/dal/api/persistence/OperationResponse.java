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
 * @filename OperationResponse.java
 * @created Aug 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates the
 * 
 * @author subhagho
 * 
 */
public class OperationResponse {
	@JsonProperty(value = "operation")
	private EnumPersistenceOperation operation;

	@JsonProperty(value = "entity")
	private String entity;

	@JsonProperty(value = "key")
	private String key = "NONE";

	/**
	 * @return the operation
	 */
	public EnumPersistenceOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(EnumPersistenceOperation operation) {
		this.operation = operation;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@JsonIgnore
	public String getHashKey() {
		return operation.name() + ":" + entity + ":" + key;
	}
}
