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
 * @filename AbstractPersistedEntity.java
 * @created Aug 22, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class AbstractPersistedEntity extends AbstractEntity {
	public static final String _TX_TIMESTAMP_COLUMN_ = "TX_TIMESTAMP";

	@Attribute(name = _TX_TIMESTAMP_COLUMN_)
	@JsonProperty(value = "tx-timestamp")
	protected Date timestamp;

	/**
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
