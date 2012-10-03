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
 * @filename SchemaObjectAttribute.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

/**
 * Base type for defining attributes for Schema Objects.
 * 
 * @author subhagho
 * 
 */
public abstract class SchemaObjectAttribute {
	protected SchemaObject parent;

	protected String name;

	protected SchemaObjectDatatype<?> type;

	protected SchemaObjectAttribute(final String name,
			final SchemaObjectDatatype<?> type, final SchemaObject parent) {
		this.name = name;
		this.type = type;
		this.parent = parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public SchemaObjectDatatype<?> getType() {
		return type;
	}

}
