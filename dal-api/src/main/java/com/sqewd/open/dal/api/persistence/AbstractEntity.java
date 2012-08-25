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

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sqewd.open.dal.api.persistence.EnumEntityState;

/**
 * Abstract class, to be inherited by all entities that are persisted.
 * 
 * @author subhagho
 * 
 */
public abstract class AbstractEntity {
	@JsonProperty(value = "record-state")
	protected EnumEntityState state = EnumEntityState.Loaded;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("\n");
		try {
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(this.getClass());
			buff.append("[ENTITY:").append(enref.Entity).append("(")
					.append(enref.Class).append(")");
			for (StructAttributeReflect attr : enref.Attributes) {
				buff.append("\n\t[")
						.append(attr.Column)
						.append(":")
						.append(PropertyUtils.getSimpleProperty(this,
								attr.Field.getName())).append("]");
			}
			buff.append("\n]");
		} catch (Exception e) {
			buff.append(e.getLocalizedMessage());
		}
		return buff.toString();
	}

}
