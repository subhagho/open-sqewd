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
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.EnumEntityState;

/**
 * Abstract class, to be inherited by all entities that are persisted.
 * 
 * @author subhagho
 * 
 */
public abstract class AbstractEntity {
	public static final String _TX_TIMESTAMP_COLUMN_ = "TX_TIMESTAMP";
	protected EnumEntityState state = EnumEntityState.Loaded;

	@Attribute(name = _TX_TIMESTAMP_COLUMN_)
	@XmlElement(name = "tx-timestamp")
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

	/**
	 * Indicates that the value of an attribute of this entity has been updated.
	 * Method sets the state of the entity.
	 * 
	 * Should be invoked by all setters.
	 */
	protected void updated() {
		if (state == EnumEntityState.New)
			return;
		if (state == EnumEntityState.Loaded)
			state = EnumEntityState.Updated;
		if (state == EnumEntityState.Deleted)
			return;
	}
	
	/**
	 * @return the state
	 */
	public EnumEntityState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(EnumEntityState state) {
		this.state = state;
	}
}
