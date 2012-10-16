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
 * @filename ConditionValue.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

import com.sqewd.open.dal.api.reflect.SchemaObjectDatatype;

/**
 * Represents a value element for a condition node.
 * 
 * @author subhagho
 * 
 */
public class ConditionValue implements Condition {
	protected Object value;

	protected SchemaObjectDatatype<?> type = null;

	protected Condition parent;

	public ConditionValue() {
	}

	public ConditionValue(final Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#getParent
	 * ()
	 */
	public Condition getParent() {
		return parent;
	}

	/**
	 * @return the type
	 */
	public SchemaObjectDatatype<?> getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#prettyPrint
	 * (int)
	 */
	public String prettyPrint(final int offset) {
		return toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#setParent
	 * (com.sqewd.open.dal.core.persistence.query.conditions.Condition)
	 */
	public void setParent(final Condition parent) {
		this.parent = parent;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final SchemaObjectDatatype<?> type) {
		this.type = type;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (value != null)
			return value.toString();
		else
			return "<NULL>";
	}

}
