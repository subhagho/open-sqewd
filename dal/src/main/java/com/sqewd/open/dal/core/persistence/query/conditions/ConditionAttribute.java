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
 * @filename ConditionAttribute.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;

/**
 * Represents an attribute (column/field) in a condition node.
 * 
 * @author subhagho
 * 
 */
public class ConditionAttribute extends ConditionElement {
	protected SchemaObjectAttribute attribute;

	public ConditionAttribute() {
	}

	public ConditionAttribute(final SchemaObjectAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the attribute
	 */
	public SchemaObjectAttribute getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute
	 *            the attribute to set
	 */
	public void setAttribute(final SchemaObjectAttribute attribute) {
		this.attribute = attribute;
	}

}
